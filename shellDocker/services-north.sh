#!/bin/bash
# Author: wuhongliang
# update-rc.d: 99 99
# /etc/rc.local: /etc/init.d/services.sh start
# Reproduced with express authorization from its contributors
#-e 　若指令传回值不等于0，则立即退出shell。
#-x 　执行指令后，会先显示该指令及所下的参数
#-u 　变量为空会报错
#注意中文 - 与英文 - 区别
# :set ff?
# :set ff=unix

CMD_HEAD="nohup java -jar "
#注意第一个大括号前后有空格
LOG_NULL=' > /dev/null 2>&1'
SERVICE="uwb"
RETVAL=0
SERVICE_HOME=""
NAME=services.sh
SCRIPTNAME=/etc/init.d/${NAME}
CONF_HOME=/home/project/uwb/config
SLEEP=5s
SRV_SLEEP=30s

WEBSOCKET=websocket
PRODUCER=producer
SERVICES=($WEBSOCKET $PRODUCER)
USAGE="Usage: $SCRIPTNAME {startOne|stopOne|startParam|start|stop|restart|restartOne}"

SRV_NAME=$2
#根据命令类型初始化参数
case "$1" in    
	startParam)
       PARAM=$3
	   XMS=$4
	   XMX=$5
	   VER=$6
       ;; 
     *)
       XMS=$3
	   XMX=$4
	   VER=$5
       ;;
esac


if [ -z "${SERVICE_HOME}" ] 
then
	#SERVICE_HOME=`pwd`
	SERVICE_HOME=/home/project/uwb
fi

if [ -n "${XMS}" ] 
then
  XMS_VALUE="-Xms${XMS}m "
else
  XMS=64
  XMS_VALUE="-Xms${XMS}m "
fi

if [ -n "${XMX}" ] 
then
   XMX_VALUE="-Xms${XMX}m "
   XMX_OLD=${XMX}
else
  XMX=128
  XMX_OLD=${XMX}
  XMX_VALUE="-Xmx${XMX}m "
fi

if [ -z "${VER}" ] 
then
  VER=1.0.0
fi

#外部配置文件路径
# CONF_PATH=${CONF_HOME}/${SRV_NAME}/application.properties

help(){
    cat <<- EOF
	Desc: how to start or stop uwb services
	Usage: ${USAGE}
	Author: wuhongliang
	License: zhilutec	
--------------all uwb services name---------------------------
     | ${RESOURCES} |
     | ${FASTDFS} |
     | ${PRODUCER} |
     | ${CENTER} |   
     | ${STATUS_SAVE} |
     | ${WEBSOCKET} |
     | ${STORAGE} |
	 for(i=0;i<${#SERVICES[@]};i++) do echo | ${SERVICES[i]} |; done
--------------------------------------------------------------
    start  start all services
           ${SCRIPTNAME} start
    stop   stop all services
           ${SCRIPTNAME} start
	restart restart all services
            ${SCRIPTNAME} restart
    startOne   start one of the services
               ${SCRIPTNAME} startOne serviceName
               eg:
                 ${SCRIPTNAME} startOne resources
    stopOne   stop one of the services
              ${SCRIPTNAME} stopOne serviceName
              eg:
                 ${SCRIPTNAME} stopOne resources
	restartOne   restart one of the services
                 ${SCRIPTNAME} restartOne serviceName
    startParam   start one of the services,with params or config file
                 ${SCRIPTNAME} startParam serviceName params
                 eg:
                    ${SCRIPTNAME} startParam fastdfs --fastdfs.tracker_servers=192.168.10.232:22122
                    ${SCRIPTNAME} startParam fastdfs -Dspring.config.location=E:\svn\src\Java\uwb\zl_fastdfs\target\application.properties
EOF
    exit 0
}	  

# print services 
getServices(){
    # SRVS=`jps -l|grep $1`	
	SRVS=`ps -ef|grep uwb|grep -v grep|awk -F ' ' '{print $2}'`
	if [ ! -n "${SRVS}" ] 
	then
	  echo "no Services"
	else
	  printf "%s\n" $SRVS
	fi
}

#del nohup.out
delNohupOut(){
  NOHUP_FILE=`ls -l|grep nohup.out`
  if [ -n "$NOHUP_FILE" ]
  then
     rm nohup.out
  else
     echo "nohup.out file not existed!"
  fi 
}

d_pause(){
  while true
  do
    pause_time=1h
    echo "pausing the srcipt ${pause_time}...."
    sleep $pause_time
  done
}

startAll(){	  
	for(( i=0;i<${#SERVICES[@]};i++)) do
	   SRV_NAME=${SERVICES[i]}	   
	   startService
	done	
    getServices $SERVICE
	delNohupOut
}

stopAll(){
    # PIDS=`jps -l|grep ${SERVICE}|awk -F " " '{print $1}'`
		PIDS=`ps -ef|grep ${SERVICE}|grep -v grep|awk -F ' ' '{print $2}'`
	if [ -n "${PIDS}" ];then
      kill -9 ${PIDS}
	else
	  echo "Services were stopped!"
	fi
}

startService(){
    CONF_PATH=${CONF_HOME}/${SRV_NAME}/application.properties
	
	if [ "${SRV_NAME}" == "${FASTDFS}" ];then
	  if [ ${XMX} -lt 256 ];then
	   XMX=128
	   XMX_VALUE="-Xmx${XMX}m "
	 fi	 
    else
	   XMX_VALUE="-Xmx${XMX_OLD}m "	 	 
	fi

	if [ -f "${SERVICE_HOME}/zl_${SRV_NAME}-${VER}-uwb-SNAPSHOT.jar" ];then
	 if [ ! -f $CONF_PATH ]; then
        echo "Starting '${SRV_NAME}' by default config file ..."
		cmd="${CMD_HEAD}${XMS_VALUE}${XMX_VALUE}${SERVICE_HOME}/zl_${SRV_NAME}-${VER}-uwb-SNAPSHOT.jar${LOG_NULL} &"
		echo $cmd
        bash -c "${cmd}"
     elif [ -f $CONF_PATH ]; then
        echo "Starting '${SRV_NAME}' with config file ${CONF_PATH} ..."	
	    cmd="${CMD_HEAD}${XMS_VALUE}${XMX_VALUE}-Dspring.config.location=${CONF_PATH} ${SERVICE_HOME}/zl_${SRV_NAME}-${VER}-${SERVICE}-SNAPSHOT.jar${LOG_NULL} &"
		echo $cmd
	    bash -c "${cmd}"
     fi
	 sleep ${SRV_SLEEP}	
    else
      echo "server '$SRV_NAME' not existed!"	     
	fi
	
}

startOne(){  
	startService  
	getServices zl_${SRV_NAME}-${VER}-${SERVICE}-SNAPSHOT.jar
	delNohupOut
}

#启动时修改一个或多个配置文件参数
startParam(){  
	echo "${CMD_HEAD}${XMS_VALUE}${XMX_VALUE}${SERVICE_HOME}/zl_${SRV_NAME}-${VER}-${SERVICE}-SNAPSHOT.jar ${PARAM}${LOG_NULL} &"
    # ${CMD_HEAD}${XMS_VALUE}${XMX_VALUE}${SERVICE_HOME}/zl_${SRV_NAME}-${VER}-${SERVICE}-SNAPSHOT.jar ${PARAM}${LOG_NULL} &
		PID=`ps -ef|grep zl_${SRV_NAME}-${VER}-${SERVICE}-SNAPSHOT.jar|grep -v grep|awk -F ' ' '{print $2}'`
	getServices zl_${SRV_NAME}-${VER}-${SERVICE}-SNAPSHOT.jar	
	delNohupOut
}

stopOne(){   
	# kill -9 `jps -l|grep zl_${SRV_NAME}-${VER}-${SERVICE}-SNAPSHOT.jar|awk -F " " '{print $1}'`	
	PID=`jps -l|grep zl_${SRV_NAME}-${VER}-${SERVICE}-SNAPSHOT.jar|awk -F " " '{print $1}'`
	if [ -n "${PID}" ];then
      kill -9 ${PID}
	else
	  echo "Service ${SRV_NAME} is stopped!"
	fi	
}

case "$1" in
    help)
        help
        ;;
    start)
	    #等10s，等其它服务启动
        sleep ${SLEEP}
        echo "Starting all services..."
        startAll
		echo "waiting for services starting..."
		sleep 10
	    echo "done."
		d_pause
        ;;
    stop)
        echo "Stopping all services..."
        stopAll
	    echo "done."
        ;;
	restart)
	   echo "Restarting all services..."
	   echo "Stopping all services..."
	   stopAll
	   echo "Starting all services..."
	   startAll
	   echo "waiting for services starting..."
	   sleep 10
	   echo "done."
	   ;;
    startOne)
        echo "Starting $SRV_NAME..."
        startOne
		sleep ${SLEEP}
	    echo "done."
        ;;
	startParam)
        echo "Starting $SRV_NAME..."
        startParam
		sleep ${SLEEP}
	    echo "done."
        ;;
    stopOne)
        echo "Stopping $SRV_NAME..."
        stopOne
	    echo "done."
        ;;
	restartOne)
	    echo "Restarting $SRV_NAME..."
		echo "Stopping $SRV_NAME..."
        stopOne 
	    echo "Starting $SRV_NAME..."
	    startOne	
		sleep ${SLEEP}
        echo "done."		
	    ;;
     *)
        echo ${USAGE} >&2
        # exit 3
        ;;
esac


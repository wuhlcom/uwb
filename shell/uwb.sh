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
# nohup java -jar -Xms64m -Xmx256m /home/project/uwb/zl_resources-1.0.0-uwb-SNAPSHOT.jar >/dev/null 2>&1 & 
# nohup java -jar -Xms64m -Xmx256m /home/project/uwb/zl_fastdfs-1.0.0-uwb-SNAPSHOT.jar >/dev/null 2>&1 &  
# nohup java -jar -Xms64m -Xmx256m /home/project/uwb/zl_producer-1.0.0-uwb-SNAPSHOT.jar >/dev/null 2>&1 &
# nohup java -jar -Xms64m -Xmx256m /home/project/uwb/zl_center-1.0.0-uwb-SNAPSHOT.jar >/dev/null 2>&1 &
# nohup java -jar -Xms64m -Xmx256m /home/project/uwb/zl_status-1.0.0-uwb-SNAPSHOT.jar >/dev/null 2>&1 &
# specify the config
#java -jar -Xms64m -Xmx256m zl_fastdfs-1.0.0-uwb-SNAPSHOT.jar -Dspring.config.location=E:\svn\src\Java\uwb\zl_fastdfs\target\application.properties
#nohup java -jar -Xms64m -Xmx256m zl_producer-1.0.0-uwb-SNAPSHOT.jar -Dspring.config.location=/home/project/uwb/config/producer/application.propertie > /dev/null 2>&1 &
#java -jar -Xms64m -Xmx256mzl_fastdfs-1.0.0-uwb-SNAPSHOT.jar --fastdfs.tracker_servers=192.168.10.196:22122
#set -e 当命令以非零状态退出时，则退出shell
#set -u 当变量为空时报错

CMD_HEAD="nohup java -jar "
#注意第一个大括号前后有空格
LOG_NULL=' > /dev/null 2>&1'
PROJECT_NAME="uwb"
RETVAL=0
SERVICE_HOME=""
NAME=uwb.sh
SCRIPTNAME=/home/project/uwb/shell/${NAME}
CONF_HOME=/home/project/uwb/config
COMMON_SLEEP=5s
SRV_SLEEP=10s
SRV_PRIFIX1=spring-cloud-
SRV_PRIFIX2=uwb-modules

EUREKA="spring-cloud-eureka"
GATEWAY="spring-cloud-gateway"
RESOURCES="uwb-modules-resources"
FILE="uwb-modules-file"
ACCESS="uwb-modules-access"
CENTER="uwb-modules-center"
STATUS="uwb-modules-status"
WEBSOCKET="uwb-modules-websocket"
STORAGE="uwb-modules-storage"
SERVICES=($EUREKA $GATEWAY $RESOURCES $FILE $ACCESS $CENTER  $STATUS $WEBSOCKET $STORAGE)

USAGE="Usage: $SCRIPTNAME {startOne|stopOne|startParam|start|stop|restart|restartOne|status}"
#接收命令行参数
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
  XMX=256
  XMX_OLD=${XMX}
  XMX_VALUE="-Xmx${XMX}m "
fi

if [ -z "${VER}" ] 
then
  VER="1.0.0-SNAPSHOT"
fi

#外部配置文件路径
# CONF_PATH=${CONF_HOME}/${SRV_NAME}/application.properties

help(){
    cat <<- EOF
	Desc: how to start or stop ${PROJECT_NAME} services
	Usage: ${USAGE}
	Author: wuhongliang
	License: zhilutec	
--------------all ${PROJECT_NAME} services name---------------------------
     | ${EUREKA} |
     | ${GATEWAY} |
     | ${RESOURCES} |
     | ${FILE} |
     | ${ACCESS} |
     | ${CENTER} |   
     | ${STATUS} |
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
# 可处理一个参数 
getServices(){
    if  [ x"$1" = x ]
	then
      SRV_FILTER="${SRV_PRIFIX1}.*|${SRV_PRIFIX2}.*"
	else
	  SRV_FILTER=$1
	fi	
	SRVS=`ps -ef|grep -E "${SRV_FILTER}"|grep -v grep|awk -F ' ' '{print $2,$12,$13}'`
	if [ ! -n "${SRVS}" ] 
	then
	  echo "Can't get filter ${SRV_FILTER} Services!"
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
     echo "clean ${PROJECT_NAME} nohup.out file,but not existed!"
  fi 
}

d_pause(){
  while true
  do
    pause_time=24h
    echo "pausing the ${PROJECT_NAME} project srcipt ${pause_time}...."
    sleep $pause_time
  done
}

startAll(){	  
	for(( i=0;i<${#SERVICES[@]};i++)) do
	   SRV_NAME=${SERVICES[i]}	   
	   startService
	done	
    getServices
	delNohupOut
}

stopAll(){
	PIDS=`ps -ef|grep -E "${SRV_PRIFIX1}.*|${SRV_PRIFIX2}.*"|grep -v grep|awk -F ' ' '{print $2}'`
	echo "stop service id:${PIDS}"
	if [ -n "${PIDS}" ];then
      kill -9 ${PIDS}
	else
	  echo "Services were stopped!"
	fi
}

startService(){
   if [ "${SRV_NAME}" == "${EUREKA}" ]||[ "${SRV_NAME}" == "${GATEWAY}" ];then
	CONF_PATH=${CONF_HOME}/${SRV_NAME}/application.yml
   else
    CONF_PATH=${CONF_HOME}/${SRV_NAME}/application.properties
   fi
	
	if [ "${SRV_NAME}" == "${FILE}" ];then
	  if [ ${XMX} -lt 256 ];then
	   XMX=128
	   XMX_VALUE="-Xmx${XMX}m "
	 fi	 
    else
	   XMX_VALUE="-Xmx${XMX_OLD}m "	 	 
	fi

	if [ -f "${SERVICE_HOME}/${SRV_NAME}-${VER}.jar" ];then
	 if [ ! -f $CONF_PATH ]; then
        echo "Starting '${SRV_NAME}' by default config file ..."
		cmd="${CMD_HEAD}${XMS_VALUE}${XMX_VALUE}${SERVICE_HOME}/${SRV_NAME}-${VER}.jar${LOG_NULL} &"
		echo $cmd
        bash -c "${cmd}"
     elif [ -f $CONF_PATH ]; then
        echo "Starting '${SRV_NAME}' with config file ${CONF_PATH} ..."	
	    cmd="${CMD_HEAD}${XMS_VALUE}${XMX_VALUE}-Dspring.config.location=${CONF_PATH} ${SERVICE_HOME}/${SRV_NAME}-${VER}.jar${LOG_NULL} &"
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
	getServices ${SRV_NAME}-${VER}.jar
	delNohupOut
}

#启动时修改一个或多个配置文件参数
startParam(){  
	echo "${CMD_HEAD}${XMS_VALUE}${XMX_VALUE}${SERVICE_HOME}/${SRV_NAME}-${VER}.jar ${PARAM}${LOG_NULL} &"
    ${CMD_HEAD}${XMS_VALUE}${XMX_VALUE}${SERVICE_HOME}/${SRV_NAME}-${VER}.jar ${PARAM}${LOG_NULL} &
	getServices ${SRV_NAME}-${VER}.jar
	delNohupOut
}

stopOne(){  
	PID=`ps -ef|grep "${SRV_NAME}-${VER}.jar"|grep -v grep|awk -F ' ' '{print $2}'`
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
	    #等待系统其它服务启动
        sleep ${COMMON_SLEEP}
        echo "Starting all services..."
        startAll
		echo "waiting for services starting..."
		sleep ${COMMON_SLEEP}
	    echo "done."
		#d_pause
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
	   sleep ${COMMON_SLEEP}
	   echo "done."
	   ;;
    startOne)
        echo "Starting $SRV_NAME..."
        startOne
		sleep ${COMMON_SLEEP}
	    echo "done."
        ;;
	startParam)
        echo "Starting $SRV_NAME..."
        startParam
		sleep ${COMMON_SLEEP}
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
		sleep ${COMMON_SLEEP}
        echo "done."		
	    ;;
	status)
		echo "get server status $SRV_NAME..."
        getServices $SRV_NAME  
        echo "done."		
	    ;;
     *)
        echo ${USAGE} >&2
        # exit 3
        ;;
esac


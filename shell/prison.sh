#!/bin/bash
# Author: wuhongliang
# update-rc.d: 99 99
# /etc/rc.local: /etc/init.d/services.sh start
# Reproduced with express authorization from its contributors
#-e 　若指令传回值不等于0，则立即退出shell。
#-x 　执行指令后，会先显示该指令及所下的参数
#注意中文 - 与英文 - 区别
# :set ff?
# :set ff=unix
# nohup java -jar -Xms64m -Xmx256m zl_permission-1.0.0-prison-SNAPSHOT.jar > /dev/null 2>&1 &
# nohup java -jar -Xms64m -Xmx256m zl_resources-1.0.0-prison-SNAPSHOT.jar > /dev/null 2>&1 &
# nohup java -jar -Xms64m -Xmx256m zl_producer-1.0.0-prison-SNAPSHOT.jar > /dev/null 2>&1 &
# nohup java -jar -Xms64m -Xmx256m zl_consumer-1.0.0-prison-SNAPSHOT.jar > /dev/null 2>&1 &
# nohup java -jar -Xms64m -Xmx256m zl_websocket-1.0.0-prison-SNAPSHOT.jar > /dev/null 2>&1 &

set -e
#set -u
#等10s，等其它服务启动
sleep 5
SCRIPTNAME=/etc/init.d/prison.sh
XMS_V=64
XMX_V=256
CMD_HEAD="nohup java -jar "
XMS_VALUE="-Xms${XMS_V}m "
XMX_VALUE="-Xmx${XMX_V}m "
#注意第一个大括号前后有空格
LOG_NULL=' > /dev/null 2>&1'
SERVICE="prison"
RETVAL=0
SERVICE_HOME=""
NAME=services.sh
SCRIPTNAME=/etc/init.d/${NAME}
SRV_NAME=$2
XMS=$3
XMX=$4
VER=$5
SLEEP=5

if [ -z "${SERVICE_HOME}" ] 
then
	#SERVICE_HOME=`pwd`
	SERVICE_HOME=/home/project/prison
fi

if [ -n "${XMS}" ] 
	then
		XMS_VALUE="-Xms${XMS}m "
	fi

	if [ -n "${XMX}" ] 
	then
	   XMX_VALUE="-Xms${XMX}m "
	fi
	
	if [ -z "${VER}" ] 
	then
	   VER=1.0.0
fi

# print services 
getServices(){
    SRVS=`jps -l|grep $1`	
	if [ ! -n "${SRVS}" ] 
	then
	  echo "no Services"
	else
	  printf "%s\n" $SRVS
	fi
}

#del nohup.out
delNohupOut(){
  nohupRs=`ls -l|grep nohup.out`
  if [ -n "nohupRs" ]
  then
     rm nohup.out
  fi  
}


startAll(){	   
	echo  "Starting zl_permission ..."
	echo "${CMD_HEAD}${XMS_VALUE}${XMX_VALUE}${SERVICE_HOME}/zl_permission-${VER}-prison-SNAPSHOT.jar${LOG_NULL} &"
	${CMD_HEAD}${XMS_VALUE}${XMX_VALUE}${SERVICE_HOME}/zl_permission-${VER}-prison-SNAPSHOT.jar${LOG_NULL} &
	sleep ${SLEEP}	
	echo  "Starting zl_resources ..."
	${CMD_HEAD}${XMS_VALUE}${XMX_VALUE}${SERVICE_HOME}/zl_resources-${VER}-prison-SNAPSHOT.jar${LOG_NULL} &
	sleep ${SLEEP}
	echo  "Starting zl_producer ..."
	${CMD_HEAD}${XMS_VALUE}${XMX_VALUE}${SERVICE_HOME}/zl_producer-${VER}-prison-SNAPSHOT.jar${LOG_NULL} &
	sleep ${SLEEP}		
	echo  "Starting zl_consumer ..."
	${CMD_HEAD}${XMS_VALUE}${XMX_VALUE}${SERVICE_HOME}/zl_consumer-${VER}-prison-SNAPSHOT.jar${LOG_NULL} &
	sleep ${SLEEP}	
	echo  "Starting zl_websocket ..."
	${CMD_HEAD}${XMS_VALUE}${XMX_VALUE}${SERVICE_HOME}/zl_websocket-${VER}-prison-SNAPSHOT.jar${LOG_NULL} &
	sleep ${SLEEP}	
    getServices $SERVICE
	RETVAL=$?
	delNohupOut
	return $RETVAL
}

stopAll(){
    kill -9 `jps -l|grep ${SERVICE}|awk -F " " '{print $1}'`
	getServices $SERVICE	
	RETVAL=$?
	return $RETVAL
}


startOne(){  
	echo "${CMD_HEAD}${XMS_VALUE}${XMX_VALUE}${SERVICE_HOME}/zl_${SRV_NAME}-${VER}-uwb-SNAPSHOT.jar${LOG_NULL} &"
    ${CMD_HEAD}${XMS_VALUE}${XMX_VALUE}${SERVICE_HOME}/zl_${SRV_NAME}-${VER}-uwb-SNAPSHOT.jar${LOG_NULL} &
	getServices $SRV_NAME	
	RETVAL=$?
	delNohupOut
	return $RETVAL
}

stopOne(){   
	kill -9 `jps -l|grep ${SRV_NAME}|awk -F " " '{print $1}'`
	getServices $SRV_NAME	
	RETVAL=$?
	return $RETVAL
}


case "$1" in
    start)
        echo  "Starting all services..."
        startAll
        ;;
    stop)
        echo  "Stopping all services..."
        stopAll
        ;;
    startOne)
        echo "Starting $SRV_NAME..."
        startOne
        ;;
    stopOne)
        echo  "Stopping $SRV_NAME..."
        stopOne 	
        ;;
     *)
        echo "Usage: $SCRIPTNAME {startOne|stopOne|start|stop}" >&2
        exit 3
        ;;
esac

exit 0

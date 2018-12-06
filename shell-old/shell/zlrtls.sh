#!/bin/bash
# Author: wuhongliang
# update-rc.d: 51 52 
#description:zookeeper  
#processname:zookeeper  
#注意中文 - 与英文 - 区别
# :set ff?
# :set ff=unix
set -e
#set -u
DESC="zlrtls daemon"
NAME=zlrtls.sh
DAEMON_SCRIPT=./ZhiLuRTLS.sh
DAEMON_HOME=/home/lyz/ZhiLuRTLS_52000
DAEMON=${DAEMON_HOME}/${DAEMON_SCRIPT}
SCRIPTNAME=/etc/init.d/$NAME

#check file exist
if [ ! -f $DAEMON ]; then
  echo "file $DAEMON does not exist!"
  exit 2
fi

#check file exec
#test -x $DAEMON || exit 2
d_start(){
 cd ${DAEMON_HOME}
 DIR=`pwd`
 echo ${DIR}
 $DAEMON_SCRIPT
}

#ps -ef|grep ZhiLuRTLS|grep -v grep|awk -F ' ' '{print $2}'
d_status(){
 RTL_PID=`ps -ef|grep ZhiLuRTLS|grep -v grep|awk -F ' ' '{print $2}'`
 if [ -n "${RTL_PID}" ]
 then
    echo "rtls pid is ${RTL_PID}"
    echo "ZhiluRTLS started"
 else
    echo "ZhiluRTLs not started!"
 fi
}

d_stop(){
 RTL_PID=`ps -ef|grep ZhiLuRTLS|grep -v grep|awk -F ' ' '{print $2}'`
 if [ -n "${RTL_PID}" ]
 then
    kill -9 ${RTL_PID}  
 else
    echo "ZhiluRTLs not started!"
 fi
}

case $1 in  
    start) 
       echo "zlrtls starting..."
       d_start      
       sleep 1
       echo "zlrtls started"
         ;;  
    stop) 
        echo "zlrtls stopping..."
        d_stop 
	 echo "zlrtls stoped..."
         ;;  
    status) 
       d_status
       ;;
    restart) 
       echo "zlrtls restarting..."
       d_stop
       d_start   
       echo "zlrtls restarted"
        ;;  
    *)
        echo "require {start|stop|restart|status}"
		exit 1
    ;;  
esac  

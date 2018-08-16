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
DESC="zookeeper daemon"
NAME=zookeepser.sh
#CONF=/etc/zookeeper/zookeeper.cfg
BASE_HOME=/home/project/uwb
CONF=${BASE_HOME}/config/zookeeper/zookeeper.cfg
if [ -n "$2" ]; then
  CONF=$2
fi
DAEMON=/opt/zookeeper-3.4.10/bin/zkServer.sh
SCRIPTNAME=/etc/init.d/$NAME
SCRIPTNAME=${BASE_HOME}/shell/$NAME
#check file exist
if [ ! -f $DAEMON ]; then
  echo "file $DAEMON does not exist!"
  exit 2
fi
#check file exec
test -x $DAEMON || exit 2

d_start(){
  if [ -n "${CONF}" ]; then
    $DAEMON start $CONF  
  else
    $DAEMON start  
  fi
}

d_stop(){
  #kill -9 `jps -l|grep org.apache.zookeeper.server.quorum.QuorumPeerMain|awk -F " " '{print $1}'` 
  if [ -n "${CONF}" ]; then
    $DAEMON stop $CONF  
  else
    $DAEMON stop  
  fi
}

d_status(){
   if [ -n "${CONF}" ]; then
    $DAEMON status $CONF  
  else
    $DAEMON status  
  fi
}

d_restart(){
   if [ -n "${CONF}" ]; then
    $DAEMON restart $CONF  
  else
    $DAEMON restart  
  fi
}


case $1 in  
    start) 
	     echo "zookeeper starting..."
         #su root $DAEMON start
		 d_start
		 sleep 3
		 echo "zookeeper started"
         ;;  
    stop) 
	     echo "zookeeper stopping..."      
		 d_stop
		 echo "zookeeper stoped..."
         ;;  
    status) 
	    echo "zookeeper status:"
        # su root $DAEMON status
		d_status
        ;;  
    restart) 
	   echo "zookeeper restarting..."
       # su root $DAEMON restart
	   d_restart
	   echo "zookeeper restarted"
        ;;  
    *)
        echo "require start|stop|status|restart"
		exit 1
    ;;  
esac  

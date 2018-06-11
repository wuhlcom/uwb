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
DAEMON=/opt/zookeeper-3.4.10/bin/zkServer.sh
SCRIPTNAME=/etc/init.d/$NAME

#check file exist
if [ ! -f $DAEMON ]; then
  echo "file $DAEMON does not exist!"
  exit 2
fi

#check file exec
test -x $DAEMON || exit 2

case $1 in  
    start) 
	     echo "zookeeper starting..."
         su root $DAEMON start
		 sleep 3
		 echo "zookeeper started"
         ;;  
    stop) 
	     echo "zookeeper stopping..."
         su root $DAEMON stop
		 echo "zookeeper stoped..."
         ;;  
    status) 
	    echo "zookeeper status:"
        su root $DAEMON status
        ;;  
    restart) 
	   echo "zookeeper restarting..."
       su root $DAEMON restart
	   echo "zookeeper restarted"
        ;;  
    *)
        echo "require start|stop|status|restart"
		exit 1
    ;;  
esac  

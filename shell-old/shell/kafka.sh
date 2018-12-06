#!/bin/bash
# Author: wuhongliang
# must start after zookeeper
# update-rc.d:  99 99 
# description: Kafka
# /etc/rc.local: /etc/init.d/kafka.sh start#
# Description: Starts and stops the Kafka server
# :set ff?
# :set ff=unix
set -e
#set -u
KAFKA_HOME=/opt/kafka/kafka_2.11
BASE_HOME=/home/project/uwb
KAFKA_SCRIPT=$KAFKA_HOME/bin/kafka-server-start.sh
#CONF=/etc/kafka/server.properties
CONF=${BASE_HOME}/config/kafka/server.properties
ZOO=

# check file exists
if [ ! -f $KAFKA_SCRIPT ]; then
  echo "file $KAFKA_SCRIPT does not exist!"
  exit 2
fi


# check zookeeper exists
checkZoo(){
	i=0
	while [ -z "$ZOO" ] 
	do
	   echo "get zookeeper status..."
	   ZOO=`jps -l|grep zookeeper` 
	   echo "$ZOO"
	   if [ "${i}" -gt 10 ]
	   then
		 echo "get zookeeper timeout"
		 exit 2
	   fi
	   sleep 1;
	   i=i+1;
	done
}


d_start(){
    checkZoo
    #bash -c "nohup ${KAFKA_SCRIPT} ${CONF} > /dev/null 2>&1 &"
	${KAFKA_SCRIPT} -daemon ${CONF}
	sleep 5
}


d_stop(){
  `jps -l|grep kafka.Kafka|awk '{print $1}'|xargs kill -9`
}

case "$1" in
  start)
    echo -n "Starting Kafka:"
	d_start
    echo " done."
    exit 0
    ;;
  stop)
    echo -n "Stopping Kafka: "
	d_stop
    echo " done."
    exit 0
    ;;
  status)
    c_pid=`ps -ef | grep kafka.Kafka |awk '{print $2}'`
    if [ "$c_pid" = "" ] ; then
      echo "Stopped"
      exit 3
    else
      echo "Running $c_pid"
      exit 0
    fi
    ;;
  restart)
    d_stop
    d_start
    ;;
  *)
    echo "Usage: kafka {start|stop|hardstop|status|restart}"
    exit 1
    ;;
esac

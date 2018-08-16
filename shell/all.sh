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
SHELL_HOME=/home/project/uwb
SCRIPT_NAME=${SHELL_HOME}/shell/all.sh

d_start(){

 ${SHELL_HOME}/zookeeper.sh start
 ${SHELL_HOME}/kafka.sh start
 ${SHELL_HOME}/redis.sh start
 ${SHELL_HOME}/nginx.sh start
 #${SHELL_HOME}/fdfs_trackerd start
 #${SHELL_HOME}/fdfs_storaged start
 sleep 20s
 ${SHELL_HOME}/services.sh start 
 # ${SHELL_HOME}/alrtls.sh start 
}

case "$1" in
  start)
    echo -n "Starting all:"
	d_start
    echo " done."
    ;;  
  *)
    echo "Usage: ${SCRIPT_NAME} {start}"
    exit 1
    ;;
esac

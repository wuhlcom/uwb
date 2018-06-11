#!/bin/bash
# Author: wuhongliang
# update-rc.d: 50 50  
# description: Start and Stop redis   
#注意中文 - 与英文 - 区别
# :set ff?
# :set ff=unix
set -e
set -u
REDISPORT=6379
REDIS_HOME=/usr/local/bin
EXEC=${REDIS_HOME}/redis-server
CLIEXEC=${REDIS_HOME}/redis-cli
PIDFILE=/var/run/redis_${REDISPORT}.pid
CONF=/etc/redis/redis.conf
PASSWD=`awk '/^requirepass */{print $2}' /etc/redis/redis.conf`
PID=`ps -ef|grep -v grep |grep redis-server|awk '{print $2}'`

case "$1" in
    start)
        if [ -z "$PID" ]
        then
		        echo "Starting Redis server..."
                $EXEC $CONF &         
        else
                echo "redis $PID exists, process is already running or crashed"
        fi
        ;;
    stop)
        if [ -z "$PID" ]
        then
                echo "redis pid does not exist, process is not running"
        else                     
                while [ -n "$PID" ]
                do
				    sudo kill -9 $PID   
				    PID=`ps -ef|grep -v grep |grep redis-server|awk '{print $2}'`
                    echo "Waiting for Redis to shutdown ..."
                    sleep 1
                done
                echo "Redis stopped"
        fi
        ;;
    restart)
        "$0" stop
        sleep 3
        "$0" start
        ;;
    *)
        echo "Please use start or stop or restart as first argument"
        ;;
esac

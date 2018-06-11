#!/bin/bash
# chkconfig: 2345 10 90  
# description: Start and Stop redis   
#注意中文 - 与英文 - 区别
# :set ff?
# :set ff=unix
set -e
REDISPORT=6379
REDIS_HOME=/usr/local/bin
EXEC=${REDIS_HOME}/redis-server
CLIEXEC=${REDIS_HOME}/redis-cli
PIDFILE=/var/run/redis_${REDISPORT}.pid
CONF=/etc/redis/redis.conf
PASSWD=`awk '/^requirepass */{print $2}' /etc/redis/redis.conf`

case "$1" in
    start)
        if [ -f $PIDFILE ]
        then
                echo "$PIDFILE exists, process is already running or crashed"
        else
                echo "Starting Redis server..."
                $EXEC $CONF &
        fi
        ;;
    stop)
        if [ ! -f $PIDFILE ]
        then
                echo "$PIDFILE does not exist, process is not running"
        else
                PID=$(cat $PIDFILE)
                echo "Stopping ..."
                if [ ! -n $PASSWD ]; then
 		  echo "Redis Password IS NULL"
                  $CLIEXEC -p  $REDISPORT shutdown
	        else
		  echo "Redis Password IS NOT NULL"
                  $CLIEXEC -a $PASSWD -p $REDISPORT shutdown
	        fi
    
                while [ -x /proc/${PID} ]
                do
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

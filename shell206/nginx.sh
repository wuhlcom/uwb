#!/bin/bash
# Author: wuhongliang
# update-rc.d: 55 55
# Reproduced with express authorization from its contributors
#-e 　若指令传回值不等于0，则立即退出shell。
#-x 　执行指令后，会先显示该指令及所下的参数
#注意中文 - 与英文 - 区别
# :set ff?
# :set ff=unix
set -e
#set -u
PATH=/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin
DESC="nginx daemon"
NAME=nginx
DAEMON=/usr/local/nginx/sbin/${NAME}
NGINX_CONF=/usr/local/nginx/conf/${NAME}.conf
if [ -n "$2" ]; then
  NGINX_CONF=$2
fi
SCRIPTNAME=/etc/init.d/${NAME}.sh
# If the daemon file is not found, terminate the script.
# 检查文件是否存在
test -x $DAEMON || exit 1
#命令行,不要使用引号
d_start() {
        ${DAEMON} -c ${NGINX_CONF}||echo  " already running"
}

d_stop() {
	${DAEMON} -s quit||echo  " not running"
}

d_reload() {
        ${DAEMON} -s reload|| echo " could not reload"
}

case "$1" in
    start)
        echo  "Starting $DESC: $NAME"
        d_start
        echo "."
        ;;
    stop)
        echo  "Stopping $DESC: $NAME"
        d_stop
        echo "."
        ;;
    reload)
        echo "Reloading $DESC configuration..."
        d_reload
        echo "reloaded."
        ;;
    restart)
        echo  "Restarting $DESC: $NAME"
        d_stop
        # Sleep for two seconds before starting again, this should give the
        # Nginx daemon some time to perform a graceful stop.
        sleep 2
        d_start
        echo "."
        ;;
     *)
        echo "Usage: $SCRIPTNAME {start|stop|restart|reload}" >&2
        exit 3
        ;;
esac

exit 0


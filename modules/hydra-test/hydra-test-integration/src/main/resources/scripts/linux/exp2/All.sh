#!/bin/bash
JPS_PATTER="StartService"
source ./env.sh
function running(){
	  JAVA_PIDS=`jps | grep $JPS_PATTER | awk '{print $1}'`
	echo $JAVA_PIDS are exist
		if [ "$JAVA_PIDS" == "" ]; then
	    	return 1;
		else
			return 0;
		fi
}
function start_server() {
	echo 暂不支持全部启动 请分别启动
	exit 1
}

function stop_server() {
    while running;
    do
        JAVA_PIDS=`jps | grep $JPS_PATTER | awk '{print $1}'`
        pid=$(echo $JAVA_PIDS | awk '{print $1}')
        echo $pid will be killed
        kill -9 $pid
	     sleep 3;
    done
	echo "Stop all services successfully."
}
function help() {
    echo "Usage: startup.sh {start|stop}" >&2
    echo "       start:             start the server"
    echo "       stop:              stop the server"
}

command=$1
shift 1
case $command in
    start)
        start_server $@;
        ;;
    stop)
        stop_server $@;
        ;;
    *)
        help;
        exit 1;
        ;;
esac


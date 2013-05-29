#!/bin/bash
MAINCLASSNAME="com.jd.bdp.hydra.benchmark.exp2.StartServiceC2"
LOGNAME="$MAINCLASSNAME".log

source ./env.sh
TAIL_FILE="$LOG_DIR/$LOGNAME"
java $SERVER_ARGS $MAINCLASSNAME > $TAIL_FILE &
#!/bin/bash
MAINCLASSNAME="com.jd.bdp.hydra.benchmark.exp2.Trigger"
LOGNAME=Trigger2.log

if [ -z "$BASE_DIR" ] ; then
  PRG="$0"
  # need this for relative symlinks
  while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
      PRG="$link"
    else
      PRG="`dirname "$PRG"`/$link"
    fi
  done
  BASE_DIR=`dirname "$PRG"`/..
  BASE_DIR=`cd "$BASE_DIR" && pwd`
fi

source $BASE_DIR/bin/env.sh
TAIL_FILE="$BASE_DIR/log/$LOGNAME"

java $SERVER_ARGS $MAINCLASSNAME > $TAIL_FILE &

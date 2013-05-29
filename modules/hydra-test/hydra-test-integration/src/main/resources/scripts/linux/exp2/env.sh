#!/bin/bash
#Config your java home
#JAVA_HOME=/opt/jdk/

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
  BASE_DIR=`dirname "$PRG"`/../..
  BASE_DIR=`cd "$BASE_DIR" && pwd`
fi
CONF_DIR=$BASE_DIR/conf
LIB_DIR=$BASE_DIR/lib
BIN_DIR=$BASE_DIR/bin
LOG_DIR=$BASE_DIR/log
if [ ! -d $LOGS_DIR ]; then
    mkdir $LOGS_DIR
fi

export BASE_DIR="$BASE_DIR"
export CONF_DIR="$CONF_DIR"
export LIB_DIR="$LIB_DIR"
export BIN_DIR="$BIN_DIR"
export LOG_DIR="$LOG_DIR"


if [ -z "$JAVA_HOME" ]; then
  export JAVA=`which java`
else
  export JAVA="$JAVA_HOME/bin/java"
fi

export CLASSPATH=$CLASSPATH:$CONF_DIR:$(ls $LIB_DIR/*.jar | tr '\n' :)

#Server jvm args
SERVER_JVM_ARGS="-Xmx512m -Xms512m -server -cp $CLASSPATH "

if [ -z "$SERVER_ARGS" ]; then
  export SERVER_ARGS="$SERVER_JVM_ARGS"
fi

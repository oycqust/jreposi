#!/bin/bash

DIR_NAME=`dirname $0`
cd $DIR_NAME
source setEnv.sh

SHELLNAME=`basename $0`
printLog "$SHELLNAME" "start to run ${APP_NAME}."

MAIN_CLASS_NAME=com.utstar.IntegralActivityApplication


if [ `$JPS -ml|grep $MAIN_CLASS_NAME | wc -l` -gt 0 ];
then
	printLog "$SHELLNAME" "the ${APP_NAME} has been running. exit!"
	exit 0
fi

cd ..
APP_HOME=`pwd`
export APP_HOME
APP_CLASSPATH="$APP_HOME"/lib/*

[ -d ${APP_HOME}/log/gc ] || mkdir -p ${APP_HOME}/log/gc
if [ $? -ne 0 ]; then
	printLog "$SHELLNAME" "create gc log dir : ${APP_HOME}/log/gc failed! exit!"
	exit 1  
fi

printLog "$SHELLNAME" "run ${APP_NAME} main program."
printLog "$SHELLNAME" "${APP_NAME} used memory size: ${JAVAMEM}"
CURRENT_DATE=`date '+%Y%m%d-%H.%M.%S'`
GC_OPTS="-Xms"${JAVAMEM}" -Xmx"${JAVAMEM}" -d64 -server -XX:+AggressiveOpts -XX:MaxDirectMemorySize=128M -XX:+UseG1GC -XX:MaxGCPauseMillis=400 -XX:G1ReservePercent=15 -XX:InitiatingHeapOccupancyPercent=30 -XX:ParallelGCThreads=16 -XX:ConcGCThreads=4 -XX:+UnlockExperimentalVMOptions -XX:+UnlockDiagnosticVMOptions -XX:G1NewSizePercent=20 -XX:+G1SummarizeConcMark -XX:G1LogLevel=finest -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -XX:+PrintAdaptiveSizePolicy -Xloggc:${APP_HOME}/logs/gc/gc-$CURRENT_DATE.txt -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=10M -XX:+HeapDumpOnOutOfMemoryError"

$JAVA -server $GC_OPTS -classpath "$APP_CLASSPATH" -Dintegral.home=$APP_HOME -DAPP=ACP -Djava.net.preferIPv4Stack=true -Dspring.config.location=${APP_HOME}/config/applicaition.properties -Dspring.pid.fail-on-write-error=true -Dspring.pid.file=${APP_HOME}/acp.pid -Dlogging.config=${APP_HOME}/config/logback.xml $MAIN_CLASS_NAME > /dev/null 2>&1 &

printLog "$SHELLNAME" "the ${APP_NAME} is running."
printLog "$SHELLNAME" "please refer to ${APP_HOME}/log/acp.log"
printLog "$SHELLNAME" "the pid file is ${APP_HOME}/acp.pid"

if [ `ps -ef | grep "monitor_acp.py" | grep -v grep | wc -l` -eq "0" ]; then
    printLog "$SHELLNAME" "run monitor_acp.py script."
    cd ./bin
   # nohup python monitor_acp.py &
    cd ..
fi

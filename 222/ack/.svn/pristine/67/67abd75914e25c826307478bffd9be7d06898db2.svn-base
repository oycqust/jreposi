#!/bin/bash

DIR_NAME=`dirname $0`
cd $DIR_NAME
source setEnv.sh

SHELLNAME=`basename $0`
APP_HOME=`cd ..;pwd`
MAIN_CLASS_NAME=com.utstar.IntegralActivityApplication

printLog "$SHELLNAME" "It will stop ${APP_NAME}."

if [ `$JPS -ml|grep $MAIN_CLASS_NAME | wc -l` -ge 1 ]; then
	if [ -f ${APP_HOME}/acp.pid ]; then
		COMMAND="kill -9 `cat ${APP_HOME}/acp.pid`"
		COMMAND=$COMMAND" "`ps -ef | grep "monitor_acp.py" | grep -v grep | awk '{ print $2 }' 2>/dev/null`
		printLog "$SHELLNAME" "the kill command is $COMMAND"
		$COMMAND
		printLog "$SHELLNAME" "sleep 2 secconds...."
        	sleep 2
		if [ `$JPS -ml|grep $MAIN_CLASS_NAME | wc -l` -eq 0 ]; then
			printLog "$SHELLNAME:" "the ${APP_NAME} has stopped."
			rm -rf  ${APP_HOME}/acp.pid
			exit 0
		fi

	fi
	
	PID=`$JPS -ml|grep $MAIN_CLASS_NAME | awk '{ print $1 }'`
	if [ "$PID" ]; then
		COMMAND="kill -9 $PID"
		COMMAND=$COMMAND" "`ps -ef | grep "monitor_acp.py" | grep -v grep | awk '{ print $2 }' 2>/dev/null`
		printLog "$SHELLNAME" "the kill command is $COMMAND]"
		$COMMAND
		printLog "$SHELLNAME" "sleep 2 secconds...."
                sleep 2
		if [ `$JPS -ml|grep $MAIN_CLASS_NAME | wc -l` -eq 0 ]; then
                        printLog "$SHELLNAME:" "the ${APP_NAME} has stopped."
			rm -rf  ${APP_HOME}/acp.pid
                        exit 0
                fi
	fi

else
	printLog "$SHELLNAME" "the ${APP_NAME} not run."
fi

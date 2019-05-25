#!/bin/bash
JAVA_HOME=/opt/spark/app/jdk1.8.0_51
export JAVA_HOME
JAVA="$JAVA_HOME"/bin/java
JPS="$JAVA_HOME"/bin/jps

#set app name
APP_NAME=ACP
export APP_NAME

#set MALLOC_ARENA_MAX
export MALLOC_ARENA_MAX=4

#resolve too many open files
#ulimit -n 32678

#no limit
#ulimit -c unlimited

#set jvm memory size
JAVAMEM=1024M


#public function - printLog
#function printLog: print normal logs
#input: $1 - shell name
#	$2 - general commetns
function printLog
{
	echo ""`date +"%Y-%m-%d %H:%M:%S"`" - [$1]: $2"
}


#public function - printError
#function printLog: if recived error result, print logs
#input: $1 - shell name
#	$2 - error comments
function printError
{
	if [ $? -ne 0 ]; then
		echo ""`date +"%Y-%m-%d %H:%M:%S"`" - [$1]: $2"
	fi	
}

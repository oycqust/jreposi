#!/usr/bin/python

import urllib2
import time
import logging
import os
import subprocess
import sys

url = "http://localhost:8097/acp/heartBeat"
headers = {'Content-Type': 'application/json'}
home = os.getenv("APP_HOME")
failMaxNum = 5
def start():
    os.chdir(home+"/bin")
    logging.info("start to run restart activity. curr dir: %s" % (os.getcwd()))
    subprocess.Popen('nohup ./start.sh &', shell='true')
    sys.exit()

def postHeartBeat():
    request = urllib2.Request(url=url, headers=headers)
    try:
        resp = urllib2.urlopen(request, timeout=10)
        status = resp.getcode()
        msg = resp.read()
        if status != 200 and msg != "PONG":
            logging.error("postHeartBeat the status: %d" % (status))
            logging.error("postHeartBeat the msg: %s" % (msg))
            return -1
        else :
            return 0
    except Exception, e:
        logging.error("postHeartBeat generate exception: %s" % (e))
        return -1

time.sleep(30)
exceptionNum = 0
while True :
    if postHeartBeat() == 0 :
        exceptionNum = 0
    else :
        exceptionNum +=1
        logging.info("postHeartBeat exception, exceptionNum: %d" % (exceptionNum))

    if exceptionNum > failMaxNum :
        logging.info(
            "monitor activity run excepted, so restart activity. exceptCount: %s" % (exceptionNum))
        start()
    time.sleep(10)


package com.utstar.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;

/**
 * Created by UTSC0928 on 2017/10/16.
 */
public class TomcatPort {

    public static String getIpAddressAndPort() throws MalformedObjectNameException, NullPointerException,
            UnknownHostException {
        MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"),
                Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
        String host = InetAddress.getLocalHost().getHostAddress();
        String port = objectNames.iterator().next().getKeyProperty("port");
        String ipadd = host + ":" + port;
        return ipadd;
    }

    public static int getTomcatPort() throws MalformedObjectNameException, NullPointerException {
        MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"),
                Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
        String port = objectNames.iterator().next().getKeyProperty("port");

        return Integer.parseInt(port);
    }

    public static String getIp() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    public static String getHostName(){
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            LoggerFactory.getLogger(TomcatPort.class).error("get hostname from local failed ! ! ! !");
            return null;
        }
    }
}

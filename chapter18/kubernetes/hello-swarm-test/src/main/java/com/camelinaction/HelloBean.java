package com.camelinaction;

import javax.inject.Singleton;

import org.apache.camel.util.InetAddressUtil;

/**
 * A bean that returns the hostname
 */
@Singleton
public class HelloBean {

    public String sayHello() throws Exception {
        return "Swarm says hello from "
                + InetAddressUtil.getLocalHostName();
    }
}

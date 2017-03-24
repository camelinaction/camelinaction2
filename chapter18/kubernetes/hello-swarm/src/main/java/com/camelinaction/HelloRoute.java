package com.camelinaction;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.camel.Endpoint;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.Uri;

/**
 * Camel routes that uses undertow to expose a HTTP service
 */
@Singleton
public class HelloRoute extends RouteBuilder {

    @Inject @Uri("undertow:http://0.0.0.0:8080/")
    private Endpoint undertow;

    @Inject
    private HelloBean hello;

    @Override
    public void configure() throws Exception {
        from(undertow).bean(hello);
    }
}

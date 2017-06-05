package com.camelinaction;

import java.net.URL;

import io.fabric8.kubernetes.client.KubernetesClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.arquillian.cube.kubernetes.annotations.Named;
import org.arquillian.cube.kubernetes.annotations.PortForward;
import org.arquillian.cube.kubernetes.impl.requirement.RequiresKubernetes;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * JUnit test using Arquillian to perform an unit test by running
 * this application as a container in a temporary created namespace
 * and then call the service and assert the response is what we expect
 */
@RunWith(Arquillian.class)
@RequiresKubernetes
public class HelloSwarmKT {

    // the client is not used in this test but it can be handy to have
    @ArquillianResource
    KubernetesClient client;

    // inject the url to this service
    // and enable port forwarding so we can call the service externally
    // from this unit test which runs on your host operating system
    // and then call the running pod inside the kubernetes cluster which runs wildfly-swarm
    @Named("helloswarm-kubernetes")
    @PortForward
    @ArquillianResource
    URL url;

    @Test
    public void testCallService() throws Exception {
        assertNotNull(url);

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().get().url(url).build();

        Response response = okHttpClient.newCall(request).execute();
        assertNotNull(response);
        assertEquals(200, response.code());

        String body = response.body().string();

        System.out.println(">>>> " + body);

        assertTrue(body.contains("Swarm says hello from"));
    }

}

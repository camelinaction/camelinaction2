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
 * JUnit test using Arquillian to perform an integration test by running
 * this application as a container in a temporary created namespace
 * and then call the service and assert the response is what we expect
 */
@RunWith(Arquillian.class)
@RequiresKubernetes
public class HelloSwarmKT {

    @ArquillianResource
    KubernetesClient client;

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

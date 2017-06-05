package com.camelinaction;

import io.fabric8.kubernetes.client.KubernetesClient;
import org.arquillian.cube.kubernetes.impl.requirement.RequiresKubernetes;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.fabric8.kubernetes.assertions.Assertions.assertThat;

/**
 * JUnit test using Arquillian to perform an unit test by running
 * this application as a container in a temporary created namespace
 * and then check if the pod can deploy and startup and be ready for a stable period of time.
 */
@RunWith(Arquillian.class)
@RequiresKubernetes
public class PodStartedKT {

    @ArquillianResource
    KubernetesClient client;

    @Test
    public void testPodStarted() throws Exception {
        // assert that the deployment works and that the pod is ready
        // for a stable period of time (10 sec).
        assertThat(client).deployments().pods().isPodReadyForPeriod();
    }

}

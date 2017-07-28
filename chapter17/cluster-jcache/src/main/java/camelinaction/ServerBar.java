package camelinaction;

import org.apache.camel.component.jcache.JCacheComponent;
import org.apache.camel.main.Main;
import org.infinispan.jcache.remote.JCachingProvider;

public class ServerBar {

    private Main main;

    public static void main(String[] args) throws Exception {
        ServerBar bar = new ServerBar();
        bar.boot();
    }

    public void boot() throws Exception {
        main = new Main();

        // create jcache component and configure it
        JCacheComponent jcache = new JCacheComponent();
        // use infinispan
        jcache.setCachingProvider(JCachingProvider.class.getName());
        // load infinispan client (hotrod) configuration from the classpath
        jcache.setConfigurationUri("hotrod-client.properties");

        // register the component to Camel with the name jcache
        main.bind("jcache", jcache);

        // route which uses get/put operations
        main.addRouteBuilder(new CounterRoute("Bar", 8889));
        main.run();
    }

}

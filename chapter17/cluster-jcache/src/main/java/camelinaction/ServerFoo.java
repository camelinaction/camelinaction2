package camelinaction;

import org.apache.camel.component.jcache.JCacheComponent;
import org.apache.camel.main.Main;
import org.infinispan.jcache.remote.JCachingProvider;

public class ServerFoo {

    private Main main;

    public static void main(String[] args) throws Exception {
        ServerFoo foo = new ServerFoo();
        foo.boot();
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

        main.addRouteBuilder(new CounterRoute("Foo", 8888));
        main.run();
    }

}

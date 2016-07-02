package camelinaction;

import org.apache.camel.spring.boot.FatJarRouter;
import org.apache.camel.spring.boot.FatWarInitializer;

public class MySpringBootRouterWarInitializer extends FatWarInitializer {

    @Override
    protected Class<? extends FatJarRouter> routerClass() {
        return MySpringBootRouter.class;
    }

}

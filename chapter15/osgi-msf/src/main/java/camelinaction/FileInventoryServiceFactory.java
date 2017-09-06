package camelinaction;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.camel.CamelContext;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileInventoryServiceFactory implements ManagedServiceFactory {
    
    private static final Logger LOG = LoggerFactory.getLogger(FileInventoryServiceFactory.class);

    private CamelContext camelContext;
    private BundleContext bundleContext;
    private Map<String, FileInventoryRoute> routes = new HashMap<String, FileInventoryRoute>();
    private ServiceRegistration registration;
    
    @Override
    public String getName() {
        return "FileInventoryRouteCamelServiceFactory";
    }

    @SuppressWarnings("unchecked")
    public void init() {
        Dictionary properties = new Properties();
        properties.put( Constants.SERVICE_PID, "camelinaction.fileinventoryroutefactory");
        registration = bundleContext.registerService(ManagedServiceFactory.class.getName(), this, properties);
        
        LOG.info("FileInventoryRouteCamelServiceFactory ready to accept " +
        "new config with PID=camelinaction.fileinventoryroutefactory-xxx");
    }
    
    @Override
    public void updated(String pid, Dictionary<String, ?> properties) throws ConfigurationException {
        String path = (String) properties.get("path");

        LOG.info("Updating route for PID=" + pid + " with new path=" + path);
        
        // need to remove old route before updating
        deleted(pid);

        // now we create a new route with update path
        FileInventoryRoute newRoute = new FileInventoryRoute();
        newRoute.setInputPath(path);
        newRoute.setRouteId("file-" + pid);
        
        // finally we add the route
        try {
            camelContext.addRoutes(newRoute);
        } catch (Exception e) {
            LOG.error("Failed to add route", e);
        }
        routes.put(pid, newRoute);
    }

    @Override
    public void deleted(String pid) {
        LOG.info("Deleting route with PID=" + pid);
        
        try {
            FileInventoryRoute route = routes.get(pid);
            if (route != null) {
                camelContext.stopRoute(route.getRouteId());
                camelContext.removeRoute(route.getRouteId());
                routes.remove(pid);
            }
        } catch (Exception e) {
            LOG.error("Failed to remove route", e);
        }
    }       
   
    public BundleContext getBundleContext() {
        return bundleContext;
    }

    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    public CamelContext getCamelContext() {
        return camelContext;
    }

    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }
}

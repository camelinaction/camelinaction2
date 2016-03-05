package camelinaction;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.util.security.Constraint;

/**
 * Setup Jetty security to use basic authentication for all the paths.
 * <p/>
 * In a real use-case you would use more stronger security or integrate to a LDAP or some other use-store.
 * But for this little example we load the users from a file.
 */
public class JettySecurity {

    public static ConstraintSecurityHandler createSecurityHandler() {
        Constraint constraint = new Constraint("BASIC", "customer");
        constraint.setAuthenticate(true);

        ConstraintMapping mapping = new ConstraintMapping();
        mapping.setConstraint(constraint);
        mapping.setPathSpec("/*");

        ConstraintSecurityHandler handler = new ConstraintSecurityHandler();
        handler.addConstraintMapping(mapping);
        handler.setAuthenticator(new BasicAuthenticator());
        handler.setLoginService(new HashLoginService("RiderAutoParts", "etc/rest-users.properties"));

        return handler;
    }

}

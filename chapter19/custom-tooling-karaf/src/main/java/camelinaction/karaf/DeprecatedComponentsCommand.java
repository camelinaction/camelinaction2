package camelinaction.karaf;

import java.util.List;

import camelinaction.old.DeprecatedValidator;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;

/**
 * A Karaf command that checks if any Camel application is using deprecated Camel components
 */
@Command(scope = "oldstuff", name = "deprecated-components",
         description = "Lists all deprecated components in use.")
@Service
public class DeprecatedComponentsCommand implements Action {

    @Override
    public Object execute() throws Exception {
        // create the validator to use which is from the chapter19-custom-tooling module
        DeprecatedValidator validator = new DeprecatedValidator();
        // find all the deprecated component names that are in use
        List<String> names = validator.findDeprecatedComponents();

        if (names.isEmpty()) {
            System.out.println("No deprecated Camel components found.");
        } else {
            System.out.println("Deprecated Camel components found:");
            for (String name : names) {
                System.out.println("\t" + name);
            }
        }

        return null;
    }
}

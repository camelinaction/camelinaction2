package camelinaction.karaf;

import java.util.List;

import camelinaction.DeprecatedValidator;
import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;

@Command(scope = "custom", name = "deprecated-components", description = "Lists all deprecated components in use.")
public class DeprecatedComponentsCommand extends OsgiCommandSupport {

    @Override
    protected Object doExecute() throws Exception {
        DeprecatedValidator validator = new DeprecatedValidator();
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

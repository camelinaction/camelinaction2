package camelinaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.management.ObjectName;

import org.apache.camel.catalog.CamelCatalog;
import org.apache.camel.catalog.DefaultCamelCatalog;
import org.apache.camel.util.JsonSchemaHelper;

import static camelinaction.CamelContextController.findCamelContexts;
import static camelinaction.CamelContextController.findComponentNames;

/**
 * A validator to check for deprecated usage of Camel components and options
 * in any of the running Camel application in the JVM.
 */
public class DeprecatedValidator {

    private CamelCatalog catalog;

    public DeprecatedValidator() {
        // create the catalog, and turn on caching
        catalog = new DefaultCamelCatalog(true);
    }

    /**
     * Checks the running JVM for all Camel applications running, and returns a list of Camel components
     * in use, that has been deprecated.
     *
     * @return list of deprecated component names
     * @throws Exception is thrown if error happened
     */
    public List<String> findDeprecatedComponents() throws Exception {
        List<String> answer = new ArrayList<>();

        // find all Camel applications running
        Set<ObjectName> camels = findCamelContexts();
        for (ObjectName on : camels) {
            // find all the component names that the camel application uses
            List<String> names = findComponentNames(on);

            // is any of these component deprecated
            for (String name : names) {
                if (isDeprecatedComponent(name)) {
                    answer.add(name);
                }
            }
        }

        return answer;
    }

    /**
     * Is the component deprecated?
     */
    private boolean isDeprecatedComponent(String name) {
        // load the JSon schema for the component
        String json = catalog.componentJSonSchema(name);

        // parse the JSon into a row structure
        // "component" = the group we want to read, which is the component details
        // false = do not parse as properties
        List<Map<String, String>> rows = JsonSchemaHelper.parseJsonSchema("component", json, false);
        for (Map<String, String> row : rows) {
            // find the deprecated row and check if its true
            if (row.get("deprecated") != null) {
                return "true".equals(row.get("deprecated"));
            }
        }

        return false;
    }

}

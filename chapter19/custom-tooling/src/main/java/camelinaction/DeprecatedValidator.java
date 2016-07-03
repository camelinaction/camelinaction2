package camelinaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

        List<ObjectName> camels = findCamelContexts();
        for (ObjectName on : camels) {
            List<String> names = findComponentNames(on);

            // is the component deprecated
            for (String name : names) {
                if (isDeprecatedComponent(name)) {
                    answer.add(name);
                }
            }
        }

        return answer;
    }

    private boolean isDeprecatedComponent(String name) {
        String json = catalog.componentJSonSchema(name);

        List<Map<String, String>> rows = JsonSchemaHelper.parseJsonSchema("component", json, false);
        for (Map<String, String> row : rows) {
            if (row.get("deprecated") != null) {
                return "true".equals(row.get("deprecated"));
            }
        }

        return false;
    }

}

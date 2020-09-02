package camelinaction.internal;

import org.apache.camel.CamelContext;
import org.apache.camel.support.component.ApiMethodPropertiesHelper;

import camelinaction.HiWorldConfiguration;

/**
 * Singleton {@link ApiMethodPropertiesHelper} for HiWorld component.
 */
public final class HiWorldPropertiesHelper extends ApiMethodPropertiesHelper<HiWorldConfiguration> {

    private static HiWorldPropertiesHelper helper;

    private HiWorldPropertiesHelper(CamelContext camelContext) {
        super(camelContext, HiWorldConfiguration.class, HiWorldConstants.PROPERTY_PREFIX);
    }

    public static synchronized HiWorldPropertiesHelper getHelper(CamelContext camelContext) {
        if (helper == null) {
            helper = new HiWorldPropertiesHelper(camelContext);
        }
        return helper;
    }
}

package camelinaction.internal;

import org.apache.camel.util.component.ApiMethodPropertiesHelper;

import camelinaction.HiWorldConfiguration;

/**
 * Singleton {@link ApiMethodPropertiesHelper} for HiWorld component.
 */
public final class HiWorldPropertiesHelper extends ApiMethodPropertiesHelper<HiWorldConfiguration> {

    private static HiWorldPropertiesHelper helper;

    private HiWorldPropertiesHelper() {
        super(HiWorldConfiguration.class, HiWorldConstants.PROPERTY_PREFIX);
    }

    public static synchronized HiWorldPropertiesHelper getHelper() {
        if (helper == null) {
            helper = new HiWorldPropertiesHelper();
        }
        return helper;
    }
}

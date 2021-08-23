package org.swisspush.kobuka.client.base;

import java.util.Map;
import java.util.function.Function;

/**
 * Base class for custom builders. Do not use directly.
 */
public class BaseAdminClientConfigBuilder<T extends BaseAdminClientConfigBuilder<T>>
        extends AbstractAdminClientConfigBuilder<T> implements ClientBuilderFunctions<T> {

    public void copyFrom(BaseAdminClientConfigBuilder<?> parent) {
        configs.putAll(parent.configs);
    }

    public void copyFrom(BaseCommonClientConfigBuilder<?> parent) {
        configs.putAll(parent.configs);
    }

    @Override
    public Map<String, Object> build() {
        return configs;
    }

    public <R> R transform(Function<BaseAdminClientConfigBuilder<?>, R> fn) {
        return fn.apply(this);
    }
}

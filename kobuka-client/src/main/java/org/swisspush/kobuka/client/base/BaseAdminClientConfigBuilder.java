package org.swisspush.kobuka.client.base;

import java.util.Map;
import java.util.function.Function;

/**
 * Base class for custom builders. Do not use directly.
 */
public class BaseAdminClientConfigBuilder<T extends BaseAdminClientConfigBuilder<T>>
        extends AbstractAdminClientConfigBuilder<T> implements ClientBuilderFunctions<T> {

    public static <T extends BaseAdminClientConfigBuilder<T>> BaseAdminClientConfigBuilder<T> create() {
        return new BaseAdminClientConfigBuilder<>();
    }

    public static <T extends BaseAdminClientConfigBuilder<T>> BaseAdminClientConfigBuilder<T> create(BaseAdminClientConfigBuilder<?> parent) {
        BaseAdminClientConfigBuilder<T> result = new BaseAdminClientConfigBuilder<>();
        result.configs.putAll(parent.configs);
        return result;
    }

    public static <T extends BaseAdminClientConfigBuilder<T>> BaseAdminClientConfigBuilder<T> create(BaseCommonClientConfigBuilder<?> parent) {
        BaseAdminClientConfigBuilder<T> result = new BaseAdminClientConfigBuilder<>();
        result.configs.putAll(parent.configs);
        return result;
    }

    @Override
    public Map<String, Object> build() {
        return configs;
    }

    public <R> R transform(Function<BaseAdminClientConfigBuilder<?>, R> fn) {
        return fn.apply(this);
    }
}

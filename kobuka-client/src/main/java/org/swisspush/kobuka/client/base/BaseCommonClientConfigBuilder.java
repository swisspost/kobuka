package org.swisspush.kobuka.client.base;

import java.util.Map;
import java.util.function.Function;

/**
 * Base class for custom builders. Do not use directly.
 */
public class BaseCommonClientConfigBuilder<T extends BaseCommonClientConfigBuilder<T>>
        extends AbstractCommonClientConfigBuilder<T>
        implements ClientBuilderFunctions<T> {

    public static BaseCommonClientConfigBuilder<?> create() {
        return new BaseCommonClientConfigBuilder<>();
    }

    @Override
    public Map<String, Object> build() {
        return configs;
    }

    public <R> R transform(Function<BaseCommonClientConfigBuilder<?>, R> fn) {
        return fn.apply(this);
    }
}

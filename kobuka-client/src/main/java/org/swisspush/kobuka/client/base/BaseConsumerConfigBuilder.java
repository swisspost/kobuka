package org.swisspush.kobuka.client.base;

import java.util.Map;
import java.util.function.Function;

/**
 * Base class for custom builders. Do not use directly.
 */
public class BaseConsumerConfigBuilder<T extends BaseConsumerConfigBuilder<T> & ClientBuilderFunctions<T>>
        extends AbstractConsumerConfigBuilder<T> implements ClientBuilderFunctions<T> {

    public void copyFrom(BaseConsumerConfigBuilder<?> parent) {
        configs.putAll(parent.configs);
    }

    public void copyFrom(BaseCommonClientConfigBuilder<?> parent) {
        configs.putAll(parent.configs);
    }

    @Override
    public Map<String, Object> build() {
        return configs;
    }

    public <R> R transform(Function<BaseConsumerConfigBuilder<?>, R> fn) {
        return fn.apply(this);
    }
}

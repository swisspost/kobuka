package org.swisspush.kobuka.client.base;

import java.util.Map;
import java.util.function.Function;

/**
 * Base class for custom builders. Do not use directly.
 */
public class BaseConsumerConfigBuilder<T extends BaseConsumerConfigBuilder<T> & ClientBuilderFunctions<T>>
        extends AbstractConsumerConfigBuilder<T> implements ClientBuilderFunctions<T> {

    public static <T extends BaseConsumerConfigBuilder<T>> BaseConsumerConfigBuilder<T> create() {
        return new BaseConsumerConfigBuilder<T>();
    }

    public static <T extends BaseConsumerConfigBuilder<T>> BaseConsumerConfigBuilder<T> create(BaseConsumerConfigBuilder<?> parent) {
        BaseConsumerConfigBuilder<T> result = new BaseConsumerConfigBuilder<>();
        result.configs.putAll(parent.configs);
        return result;
    }

    public static <T extends BaseConsumerConfigBuilder<T>> BaseConsumerConfigBuilder<T> create(BaseCommonClientConfigBuilder<?> parent) {
        BaseConsumerConfigBuilder<T> result = new BaseConsumerConfigBuilder<>();
        result.configs.putAll(parent.configs);
        return result;
    }

    @Override
    public Map<String, Object> get() {
        return configs;
    }

    public <R> R transform(Function<BaseConsumerConfigBuilder<?>, R> fn) {
        return fn.apply(this);
    }
}

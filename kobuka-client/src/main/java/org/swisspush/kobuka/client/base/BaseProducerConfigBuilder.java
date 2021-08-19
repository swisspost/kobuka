package org.swisspush.kobuka.client.base;

import java.util.Map;
import java.util.function.Function;

/**
 * Base class for custom builders. Do not use directly.
 */
public class BaseProducerConfigBuilder<T extends BaseProducerConfigBuilder<T> & ClientBuilderFunctions<T>>
        extends AbstractProducerConfigBuilder<T> implements ClientBuilderFunctions<T> {
    public static <T extends BaseProducerConfigBuilder<T>> BaseProducerConfigBuilder<T> create() {
        return new BaseProducerConfigBuilder<T>();
    }

    public static <T extends BaseProducerConfigBuilder<T>> BaseProducerConfigBuilder<T> create(BaseProducerConfigBuilder<?> parent) {
        BaseProducerConfigBuilder<T> result = new BaseProducerConfigBuilder<>();
        result.configs.putAll(parent.configs);
        return result;
    }

    public static <T extends BaseProducerConfigBuilder<T>> BaseProducerConfigBuilder<T> create(BaseCommonClientConfigBuilder<?> parent) {
        BaseProducerConfigBuilder<T> result = new BaseProducerConfigBuilder<>();
        result.configs.putAll(parent.configs);
        return result;
    }

    @Override
    public Map<String, Object> get() {
        return configs;
    }

    public <R> R transform(Function<BaseProducerConfigBuilder<?>, R> fn) {
        return fn.apply(this);
    }
}

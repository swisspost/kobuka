package org.swisspush.kobuka.client.base;

import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Map;
import java.util.function.Function;

/**
 * Base class for custom builders. Do not use directly.
 */
public class BaseProducerConfigBuilder<T extends BaseProducerConfigBuilder<T> & BuilderFunctions<T>>
        extends AbstractProducerConfigBuilder<T> implements BuilderFunctions<T> {
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

    public Map<String, Object> build() {
        return configs;
    }

    public <R> R map(Function<BaseProducerConfigBuilder<?>, R> fn) {
        return fn.apply(this);
    }
}

package org.swisspush.kobuka.client.base;

import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Map;
import java.util.function.Function;

/**
 * Base class for custom builders. Do not use directly.
 */
public class BaseConsumerConfigBuilder<T extends BaseConsumerConfigBuilder<T>> extends AbstractConsumerConfigBuilder<T> {

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

    public Map<String, Object> build() {
        return configs;
    }

    public <R> R build(Function<Map<String, Object> , R> fn) {
        return fn.apply(build());
    }

    public <R> R map(Function<BaseConsumerConfigBuilder<?>, R> fn) {
        return fn.apply(this);
    }
}

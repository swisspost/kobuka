package org.swisspush.kobuka.client;

import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.function.Function;

public class BaseConsumerConfigBuilder<T extends BaseConsumerConfigBuilder<T>> extends AbstractConsumerConfigBuilder<T> {
    public ConsumerConfig build() {
        return new ConsumerConfig(configs);
    }

    public <R> R map(Function<BaseConsumerConfigBuilder<?>, R> fn) {
        return fn.apply(this);
    }

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
}

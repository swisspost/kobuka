package org.swisspush.kobuka.client;

import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.function.Function;

public class BaseProducerConfigBuilder<T extends BaseProducerConfigBuilder<T>> extends AbstractProducerConfigBuilder<T> {
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

    public ProducerConfig build() {
        return new ProducerConfig(configs);
    }

    public <R> R build(Function<ProducerConfig, R> fn) {
        return fn.apply(build());
    }

    public <R> R map(Function<BaseProducerConfigBuilder<?>, R> fn) {
        return fn.apply(this);
    }
}

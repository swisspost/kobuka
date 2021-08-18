package org.swisspush.kobuka.client;

import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Map;
import java.util.function.Function;

public class BaseProducerConfigBuilder<T extends BaseProducerConfigBuilder<T>> extends AbstractProducerConfigBuilder<T> {
    public ProducerConfig build() {
        return new ProducerConfig(configs);
    }

    public <R> R map(Function<Map<String, Object>, R> fn) {
        return fn.apply(configs);
    }
}

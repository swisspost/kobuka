package org.swisspush.kobuka.spring.base;

import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;

public class BaseDefaultKafkaConsumerFactoryBuilder<T extends BaseDefaultKafkaConsumerFactoryBuilder<T, K, V>, K, V>
        implements SpringBuilderFunctions<T, DefaultKafkaConsumerFactory<K, V>> {

    final DefaultKafkaConsumerFactory<K, V> factory;

    public BaseDefaultKafkaConsumerFactoryBuilder(Map<String, Object> config) {
        factory = new DefaultKafkaConsumerFactory<>(config);
    }

    public T keyDeserializer(Deserializer<K> deserializer) {
        factory.setKeyDeserializer(deserializer);
        return self();
    }

    public T valueDeserializer(Deserializer<V> deserializer) {
        factory.setValueDeserializer(deserializer);
        return self();
    }

    @Override
    public DefaultKafkaConsumerFactory<K, V> get() {
        return factory;
    }

}

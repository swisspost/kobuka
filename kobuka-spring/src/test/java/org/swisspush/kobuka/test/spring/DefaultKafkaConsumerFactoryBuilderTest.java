package org.swisspush.kobuka.test.spring;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.swisspush.kobuka.client.ConsumerConfigBuilder;
import org.swisspush.kobuka.test.spring.base.BaseDefaultKafkaConsumerFactoryBuilder;

import java.util.Map;
import java.util.function.Supplier;

public class DefaultKafkaConsumerFactoryBuilderTest {

    @Test
    public void testSimpleBuilder() {
        DefaultKafkaConsumerFactory<String, String> result =
                new ConsumerConfigBuilder()
                        .bootstrapServers("localhost:9092,otherhost:9092")
                        .build(DefaultKafkaConsumerFactoryBuilder<String, String>::new)
                        .get();
    }

    @Test
    public void testCustomBuilder() {
        DefaultKafkaConsumerFactory<String, String> factory =
                new ConsumerConfigBuilder()
                        .bootstrapServers("localhost:9092,otherhost:9092")
                        .build(CustomDefaultKafkaConsumerFactoryBuilder<String, String>::new)
                        .withDefaultSerialization()
                        .get();
    }

    interface SerializationTraits<T extends SerializationTraits<T, K, V>, K, V>
            extends Supplier<DefaultKafkaConsumerFactory<K, V>> {

        @SuppressWarnings("unchecked")
        default T withDefaultSerialization() {
            get().setKeyDeserializer((Deserializer<K>) new StringDeserializer());
            get().setValueDeserializer((Deserializer<V>) new StringDeserializer());
            return (T) this;
        }
    }

    static class CustomDefaultKafkaConsumerFactoryBuilder<K, V>
            extends BaseDefaultKafkaConsumerFactoryBuilder<CustomDefaultKafkaConsumerFactoryBuilder<K, V>, K, V>
            implements SerializationTraits<CustomDefaultKafkaConsumerFactoryBuilder<K, V>, K, V> {

        public CustomDefaultKafkaConsumerFactoryBuilder(Map<String, Object> config) {
            super(config);
        }
    }

}
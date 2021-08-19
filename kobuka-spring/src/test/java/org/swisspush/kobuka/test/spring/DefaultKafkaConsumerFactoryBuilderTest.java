package org.swisspush.kobuka.test.spring;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.swisspush.kobuka.client.ConsumerConfigBuilder;
import org.swisspush.kobuka.spring.DefaultKafkaConsumerFactoryBuilder;
import org.swisspush.kobuka.spring.base.BaseDefaultKafkaConsumerFactoryBuilder;
import org.swisspush.kobuka.spring.base.SpringBuilderFunctions;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultKafkaConsumerFactoryBuilderTest {

    @Test
    public void testSimpleBuilder() {

        DefaultKafkaConsumerFactory<String, String> factory =
                new ConsumerConfigBuilder()
                        .bootstrapServers("localhost:9092,otherhost:9092")
                        .map(DefaultKafkaConsumerFactoryBuilder<String, String>::new)
                        .keyDeserializer(new StringDeserializer())
                        .get();

        assertEquals("localhost:9092,otherhost:9092",
                factory.getConfigurationProperties().get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
    }


    @Test
    public void testApply() {

        Deserializer<String> des = new StringDeserializer();
        DefaultKafkaConsumerFactory<String, String> result =
                new ConsumerConfigBuilder()
                        .bootstrapServers("localhost:9092,otherhost:9092")
                        .map(DefaultKafkaConsumerFactoryBuilder<String, String>::new)
                        .apply(factory -> factory.setKeyDeserializer(des))
                        .get();

        assertEquals(des, result.getKeyDeserializer());
    }

    @Test
    public void testCustomBuilder() {
        DefaultKafkaConsumerFactory<String, String> factory =
                new ConsumerConfigBuilder()
                        .bootstrapServers("localhost:9092,otherhost:9092")
                        .map(CustomDefaultKafkaConsumerFactoryBuilder<String, String>::new)
                        .withDefaultSerialization()
                        .get();

        assert (factory.getKeyDeserializer() instanceof StringDeserializer);
    }

    interface SerializationTraits<T extends SerializationTraits<T, K, V>, K, V>
            extends SpringBuilderFunctions<T, DefaultKafkaConsumerFactory<K, V>> {

        @SuppressWarnings("unchecked")
        default T withDefaultSerialization() {
            get().setKeyDeserializer((Deserializer<K>) new StringDeserializer());
            get().setValueDeserializer((Deserializer<V>) new StringDeserializer());
            return self();
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
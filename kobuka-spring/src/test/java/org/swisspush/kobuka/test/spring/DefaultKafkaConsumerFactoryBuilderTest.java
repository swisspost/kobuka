package org.swisspush.kobuka.test.spring;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.swisspush.kobuka.client.ConsumerConfigBuilder;
import org.swisspush.kobuka.spring.ConcurrentKafkaListenerContainerFactoryBuilder;
import org.swisspush.kobuka.spring.DefaultKafkaConsumerFactoryBuilder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.kafka.listener.ContainerProperties.AckMode.BATCH;

public class DefaultKafkaConsumerFactoryBuilderTest {

    @Test
    public void testSimpleBuilder() {

        DefaultKafkaConsumerFactory<String, String> factory =
                new ConsumerConfigBuilder()
                        .bootstrapServers("localhost:9092,otherhost:9092")
                        .build(DefaultKafkaConsumerFactoryBuilder<String, String>::new)
                        .keyDeserializer(new StringDeserializer())
                        .build();

        assertEquals("localhost:9092,otherhost:9092",
                factory.getConfigurationProperties().get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
    }

    @Test
    public void testGeneratedBuilder() {
        new ConsumerConfigBuilder()
                .bootstrapServers("localhost:9092,otherhost:9092")
                .build(DefaultKafkaConsumerFactoryBuilder::new)
                .build(ConcurrentKafkaListenerContainerFactoryBuilder::new)
                .ackDiscarded(true)
                .withContainerProperties(p -> p
                        .ackCount(2)
                        .ackMode(BATCH))
                .build();
    }

    @Test
    public void testDefaultSerialization() {
        new ConsumerConfigBuilder()
                .bootstrapServers("localhost:9092,otherhost:9092")
                .build(CustomDefaultKafkaConsumerFactoryBuilder::new)
                .withDefaultSerialization()
                .build();
    }

    static class CustomDefaultKafkaConsumerFactoryBuilder<K, V> extends DefaultKafkaConsumerFactoryBuilder<K, V> {
        public CustomDefaultKafkaConsumerFactoryBuilder(Map<String, Object> configs) {
            super(configs);
        }

        @SuppressWarnings("unchecked")
        public CustomDefaultKafkaConsumerFactoryBuilder<K, V> withDefaultSerialization() {
            return (CustomDefaultKafkaConsumerFactoryBuilder<K, V>) this
                    .keyDeserializer((Deserializer<K>) new StringDeserializer())
                    .valueDeserializer((Deserializer<V>) new StringDeserializer());
        }
    }
}
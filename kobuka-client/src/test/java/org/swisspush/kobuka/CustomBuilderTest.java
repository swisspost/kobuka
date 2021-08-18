package org.swisspush.kobuka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.BytesSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.swisspush.kobuka.client.BaseProducerConfigBuilder;
import org.swisspush.kobuka.client.ProducerConfigBuilder;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomBuilderTest {

    /**
     * Configuration traits about optimization. Written by Kafka experts in the company.
     */
    @SuppressWarnings("unchecked")
    interface OptimizationExtensions<T extends OptimizationExtensions<T>> extends ProducerConfigBuilder<T> {

        default T withBetterThroughput() {
            acks("0");
            maxInFlightRequestsPerConnection(1000);
            return (T)this;
        }

        default T withBetterDurability() {
            acks("all");
            maxInFlightRequestsPerConnection(1);
            return (T)this;
        }
    }

    /**
     * Configuration traits about serialization. Written by Kafka experts in the company.
     */
    @SuppressWarnings("unchecked")
    interface SerializationExtensions<T extends ProducerConfigBuilder<T>>  extends ProducerConfigBuilder<T> {

        default T withDefaultSerialization() {
            keySerializer(StringSerializer.class);
            valueSerializer(StringSerializer.class);
            return (T)this;
        }

        default T withBinarySerialization() {
            keySerializer(StringSerializer.class);
            valueSerializer(BytesSerializer.class);
            return (T)this;
        }

    }

    /**
     * Our custom configuration builder with the two traits above. Typically in the application template.
     */
    static class CustomProducerConfigBuilder extends BaseProducerConfigBuilder<CustomProducerConfigBuilder> implements
            OptimizationExtensions<CustomProducerConfigBuilder>,
            SerializationExtensions<CustomProducerConfigBuilder>{}


    @Test
    public void testSimpleProducerConfig() {

        // This is what the developers will write

        ProducerConfig config = new CustomProducerConfigBuilder()
                .bootstrapServers("localhost:9092") // from
                .withBetterThroughput()
                .withDefaultSerialization()
                .build();

        assertEquals(config.getList(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG), Collections.singletonList("localhost:9092"));
        assertEquals(config.getClass(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG), StringSerializer.class);
        assertEquals(config.getClass(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG), StringSerializer.class);
        assertEquals(config.getString(ProducerConfig.ACKS_CONFIG), "0");
        assertEquals(config.getInt(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION), 1000);
    }
}
package org.swisspush.kobuka.test.client;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.BytesSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.swisspush.kobuka.client.BaseProducerConfigBuilder;
import org.swisspush.kobuka.client.ProducerConfigBuilder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This illustrates how users can add "traits" to the builder mechanism.
 * Traits are typically written by the Kafka experts in the company to add
 * methods initializing multiple properties in a consistent way.
 * Note: It is actually the "extension methods for the poor".
 */
public class CustomBuilderTest {

    /**
     * Configuration traits about optimization.
     */
    @SuppressWarnings("unchecked")
    interface OptimizationTraits<T extends OptimizationTraits<T>> extends ProducerConfigBuilder<T> {

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
     * Configuration traits about serialization.
     */
    @SuppressWarnings("unchecked")
    interface SerializationTraits<T extends ProducerConfigBuilder<T>>  extends ProducerConfigBuilder<T> {

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
     * Our custom configuration builder bundling the two traits above. Typically in the application template.
     */
    static class CustomProducerConfigBuilder extends BaseProducerConfigBuilder<CustomProducerConfigBuilder> implements
            OptimizationTraits<CustomProducerConfigBuilder>,
            SerializationTraits<CustomProducerConfigBuilder> {}


    @Test
    public void testSimpleProducerConfig() {

        // Developers just use the fluent API with all additional methods available.

        ProducerConfig config = new CustomProducerConfigBuilder()
                .bootstrapServers("localhost:9092")
                .withDefaultSerialization()
                .batchSize(2)
                .withBetterThroughput()
                .build();

        assertEquals(config.getList(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG), Collections.singletonList("localhost:9092"));
        assertEquals(config.getClass(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG), StringSerializer.class);
        assertEquals(config.getClass(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG), StringSerializer.class);
        assertEquals(config.getString(ProducerConfig.ACKS_CONFIG), "0");
        assertEquals(config.getInt(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION), 1000);
    }
}
package org.swisspush.kobuka.test.client;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.BytesSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.swisspush.kobuka.client.base.BaseProducerConfigBuilder;
import org.swisspush.kobuka.client.base.ProducerConfigFields;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This illustrates how users can add "traits" to the builder mechanism.
 * Traits are typically written by the Kafka experts in the company to add
 * methods initializing multiple properties in a consistent way.
 * Note: It is actually the "extension methods for the poor".
 */
public class CustomBuilderTest {

    @Test
    public void testSimpleProducerConfig() {

        // Developers just use the fluent API with all additional methods available (withXXX).

        ProducerConfig config = new CustomProducerConfigBuilder()
                .bootstrapServers("localhost:9092")
                .withDefaultSerialization()
                .batchSize(2)
                .withBetterThroughput()
                .build(ProducerConfig::new);

        assertEquals(config.getList(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG), Collections.singletonList("localhost:9092"));
        assertEquals(config.getClass(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG), StringSerializer.class);
        assertEquals(config.getClass(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG), StringSerializer.class);
        assertEquals(config.getString(ProducerConfig.ACKS_CONFIG), "0");
        assertEquals(config.getInt(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION), 1000);
    }

    /**
     * Configuration traits about optimization.
     */
    interface OptimizationTraits<T extends OptimizationTraits<T>> extends ProducerConfigFields<T> {

        default T withBetterThroughput() {
            acks("0");
            maxInFlightRequestsPerConnection(1000);
            return self();
        }

        default T withBetterDurability() {
            acks("all");
            maxInFlightRequestsPerConnection(1);
            return self();
        }
    }

    /**
     * Configuration traits about serialization.
     */
    interface SerializationTraits<T extends SerializationTraits<T>> extends ProducerConfigFields<T> {

        default T withDefaultSerialization() {
            keySerializer(StringSerializer.class);
            valueSerializer(StringSerializer.class);
            return self();
        }

        default T withBinarySerialization() {
            keySerializer(StringSerializer.class);
            valueSerializer(BytesSerializer.class);
            return self();
        }

    }

    /**
     * The custom configuration builder includes the two traits above.
     * Typically provided in the application template where users can adapt it.
     */
    static class CustomProducerConfigBuilder extends BaseProducerConfigBuilder<CustomProducerConfigBuilder> implements
            OptimizationTraits<CustomProducerConfigBuilder>,
            SerializationTraits<CustomProducerConfigBuilder> {
    }
}
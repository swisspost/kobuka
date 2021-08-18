package org.swisspush.kobuka.test.client;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.types.Password;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.swisspush.kobuka.client.CommonClientConfigBuilder;
import org.swisspush.kobuka.client.ConsumerConfigBuilder;
import org.swisspush.kobuka.client.ProducerConfigBuilder;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ConfigBuilderTest {

    @Test
    public void testMinimalConfig() {

        KafkaConsumer<String, String> consumer =
                new ConsumerConfigBuilder()
                        .bootstrapServers("localhost:9092,otherhost:9092")
                        .keyDeserializer(StringDeserializer.class)
                        .valueDeserializer(StringDeserializer.class)
                        .groupId("hello")
                        .build(KafkaConsumer::new);

        assertEquals("hello", consumer.groupMetadata().groupId());
    }

    /**
     * This illustrates how to reuse parts of configuration.
     */
    @Test
    public void testCopy() {

        ConsumerConfigBuilder original =
                new ConsumerConfigBuilder()
                        .bootstrapServers("localhost:9092,otherhost:9092")
                        .keyDeserializer(StringDeserializer.class)
                        .valueDeserializer(StringDeserializer.class);

        ConsumerConfig config =
                original
                        .map(ConsumerConfigBuilder::create)
                        .autoCommitIntervalMs(1234)
                        .build(ConsumerConfig::new);

        assertEquals(Arrays.asList("localhost:9092", "otherhost:9092"),
                config.getList(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
        assertEquals(1234, config.getInt(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG));
        assertNotEquals(1234, original.build(ConsumerConfig::new).getInt(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG));
    }

    /**
     * This illustrates how to reuse common configuration properties.
     */
    @Test
    public void testInheritance() {

        CommonClientConfigBuilder commonConfigBuilder =
                new CommonClientConfigBuilder()
                        .bootstrapServers("localhost:9092,otherhost:9092");

        ConsumerConfig consumerConfig =
                commonConfigBuilder
                        .map(ConsumerConfigBuilder::create)
                        .keyDeserializer(StringDeserializer.class)
                        .valueDeserializer(StringDeserializer.class)
                        .build(ConsumerConfig::new);

        ProducerConfig producerConfig =
                commonConfigBuilder
                        .map(ProducerConfigBuilder::create)
                        .keySerializer(StringDeserializer.class)
                        .valueSerializer(StringDeserializer.class)
                        .build(ProducerConfig::new);

        assertEquals(Arrays.asList("localhost:9092", "otherhost:9092"),
                consumerConfig.getList(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
        assertEquals(Arrays.asList("localhost:9092", "otherhost:9092"),
                producerConfig.getList(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
    }

    /**
     * List and Password types have also pure-string setters.
     */
    @Test
    public void testStringOverload() {

        ConsumerConfig conf1 =
                new ConsumerConfigBuilder()
                        .bootstrapServers("localhost:9092,otherhost:9092")
                        .sslKeyPassword(new Password("secret"))
                        .keyDeserializer(StringDeserializer.class)
                        .valueDeserializer(StringDeserializer.class)
                        .build(ConsumerConfig::new);

        ConsumerConfig conf2 =
                new ConsumerConfigBuilder()
                        .bootstrapServers(Arrays.asList("localhost:9092", "otherhost:9092"))
                        .sslKeyPassword("secret")
                        .keyDeserializer(StringDeserializer.class)
                        .valueDeserializer(StringDeserializer.class)
                        .build(ConsumerConfig::new);

        assertEquals(
                conf1.getList(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG),
                conf2.getList(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
    }

    @Test
    public void testCustomProperty() {

        Map<String, Object> config =
                new ConsumerConfigBuilder()
                        .bootstrapServers("localhost:9092,otherhost:9092")
                        .keyDeserializer(StringDeserializer.class)
                        .valueDeserializer(StringDeserializer.class)
                        .addProperty("custom", "prop")
                        .get();

        assertEquals("prop", config.get("custom"));
    }
}

package org.swisspush.kobuka.spring;

import org.swisspush.kobuka.spring.base.BaseDefaultKafkaConsumerFactoryBuilder;

import java.util.Map;

public class DefaultKafkaConsumerFactoryBuilder<K, V>
        extends BaseDefaultKafkaConsumerFactoryBuilder<DefaultKafkaConsumerFactoryBuilder<K, V>, K, V> {

    public DefaultKafkaConsumerFactoryBuilder(Map<String, Object> configs) {
        super(configs);
    }

    public static <K,V> DefaultKafkaConsumerFactoryBuilder<K, V> create(Map<String, Object> configs) {
        return new DefaultKafkaConsumerFactoryBuilder<K, V>(configs);
    }
}

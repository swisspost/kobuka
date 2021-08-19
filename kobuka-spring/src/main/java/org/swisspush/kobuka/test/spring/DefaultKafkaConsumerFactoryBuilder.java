package org.swisspush.kobuka.test.spring;

import org.swisspush.kobuka.test.spring.base.BaseDefaultKafkaConsumerFactoryBuilder;

import java.util.Map;

public class DefaultKafkaConsumerFactoryBuilder<K, V>
        extends BaseDefaultKafkaConsumerFactoryBuilder<DefaultKafkaConsumerFactoryBuilder<K, V>, K, V> {

    public DefaultKafkaConsumerFactoryBuilder(Map<String, Object> config) {
        super(config);
    }
}

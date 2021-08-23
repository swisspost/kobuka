package org.swisspush.kobuka.spring;

import java.util.Map;

public class DefaultKafkaProducerFactoryBuilder<K, V>
        extends AbstractDefaultKafkaProducerFactoryBuilder<K, V> {

    public DefaultKafkaProducerFactoryBuilder(Map<String, Object> configs) {
        configs(configs);
    }

}

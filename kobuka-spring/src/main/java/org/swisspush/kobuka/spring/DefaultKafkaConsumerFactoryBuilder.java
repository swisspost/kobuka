package org.swisspush.kobuka.spring;

import java.util.Map;

public class DefaultKafkaConsumerFactoryBuilder<K, V>
        extends AbstractDefaultKafkaConsumerFactoryBuilder<K, V> {

    public DefaultKafkaConsumerFactoryBuilder(Map<String, Object> configs) {
        configs(configs);
    }

}

package org.swisspush.kobuka.spring;

import org.springframework.kafka.core.ConsumerFactory;
import org.swisspush.kobuka.spring.internal.BuiltConcurrentKafkaListenerContainerFactoryBuilder;

public class ConcurrentKafkaListenerContainerFactoryBuilder<K, V>
        extends BuiltConcurrentKafkaListenerContainerFactoryBuilder<K, V> {

    public ConcurrentKafkaListenerContainerFactoryBuilder(ConsumerFactory<K, V> consumerFactory) {
        consumerFactory(consumerFactory);
    }

    public static <K, V> ConcurrentKafkaListenerContainerFactoryBuilder<K, V> create(ConsumerFactory<K, V> consumerFactory) {
        return new ConcurrentKafkaListenerContainerFactoryBuilder<>(consumerFactory);
    }
}

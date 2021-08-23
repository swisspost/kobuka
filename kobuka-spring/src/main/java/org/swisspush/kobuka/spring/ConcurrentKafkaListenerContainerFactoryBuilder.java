package org.swisspush.kobuka.spring;

import org.springframework.kafka.core.ConsumerFactory;

public class ConcurrentKafkaListenerContainerFactoryBuilder<K, V> extends AbstractConcurrentKafkaListenerContainerFactoryBuilder<K, V> {

  public ConcurrentKafkaListenerContainerFactoryBuilder(ConsumerFactory<? super K, ? super V> consumerFactory) {
    consumerFactory(consumerFactory);
  }

}

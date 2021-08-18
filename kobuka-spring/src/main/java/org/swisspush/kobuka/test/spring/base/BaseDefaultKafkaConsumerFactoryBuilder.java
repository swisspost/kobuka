package org.swisspush.kobuka.test.spring.base;

import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class BaseDefaultKafkaConsumerFactoryBuilder<T
        extends BaseDefaultKafkaConsumerFactoryBuilder<T,K,V>, K,V> implements Supplier<DefaultKafkaConsumerFactory<K,V> > {

    final DefaultKafkaConsumerFactory<K,V> factory;

    public BaseDefaultKafkaConsumerFactoryBuilder(Map<String, Object> config) {
        factory = new DefaultKafkaConsumerFactory<K,V>(config);
    }

    @SuppressWarnings("unchecked")
    public T keyDeserializer(Deserializer<K> deserializer) {
        factory.setKeyDeserializer(deserializer);
        return (T)this;
    }

    @SuppressWarnings("unchecked")
    public T valueDeserializer(Deserializer<V> deserializer) {
        factory.setValueDeserializer(deserializer);
        return (T)this;
    }

    @SuppressWarnings("unchecked")
    public T apply(Consumer<DefaultKafkaConsumerFactory<K,V>> consumer) {
        consumer.accept(factory);
        return (T)this;
    }

    public DefaultKafkaConsumerFactory<K,V> get() {
        return factory;
    }

    public <R> R build(Function<DefaultKafkaConsumerFactory<K,V>, R> fn) {
        return fn.apply(factory);
    }

}

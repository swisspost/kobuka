package org.swisspush.kobuka.spring.internal;

import net.karneim.pojobuilder.GeneratePojoBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.ProducerPostProcessor;

import java.util.List;
import java.util.Map;

public class DefaultKafkaProducerFactoryProvider {
    @GeneratePojoBuilder(
            withBuilderInterface = Builder.class,
            withSetterNamePattern = "*",
            excludeProperties = {"applicationContext", "beanName", "applicationEventPublisher"},
            withGenerationGap = true,
            intoPackage = "org.swisspush.kobuka.spring"
    )
    public static <K, V> DefaultKafkaProducerFactory<K, V> create(Map<String, Object> configs,
                                                                  List<ProducerFactory.Listener<K, V>> withListeners,
                                                                  List<ProducerPostProcessor<K, V>> withPostProcessors)
    {
        DefaultKafkaProducerFactory<K, V> result = new DefaultKafkaProducerFactory<>(configs);
        if(withListeners != null) {
            withListeners.forEach(result::addListener);
        }
        if(withPostProcessors != null) {
            withPostProcessors.forEach(result::addPostProcessor);
        }
        return result;
    }
}
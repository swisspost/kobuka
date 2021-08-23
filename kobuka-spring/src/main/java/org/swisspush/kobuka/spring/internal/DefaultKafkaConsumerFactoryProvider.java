package org.swisspush.kobuka.spring.internal;

import net.karneim.pojobuilder.GeneratePojoBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.ConsumerPostProcessor;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.List;
import java.util.Map;

public class DefaultKafkaConsumerFactoryProvider {
    @GeneratePojoBuilder(
            withBuilderInterface = Builder.class,
            withSetterNamePattern = "*",
            excludeProperties = {"applicationContext", "beanName", "applicationEventPublisher"},
            withGenerationGap = true,
            intoPackage = "org.swisspush.kobuka.spring"
    )
    public static <K, V> DefaultKafkaConsumerFactory<K, V>
    createDefaultKafkaConsumerFactory(Map<String, Object> configs,
                                      List<ConsumerFactory.Listener<K, V>> withListeners,
                                      List<ConsumerPostProcessor<K, V>> withPostProcessors) {
        DefaultKafkaConsumerFactory<K, V> result = new DefaultKafkaConsumerFactory<>(configs);
        if(withListeners != null) {
            withListeners.forEach(result::addListener);
        }
        if(withPostProcessors != null) {
            withPostProcessors.forEach(result::addPostProcessor);
        }
        return result;
    }
}
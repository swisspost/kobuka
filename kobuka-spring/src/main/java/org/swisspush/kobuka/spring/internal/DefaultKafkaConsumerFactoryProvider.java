package org.swisspush.kobuka.spring.internal;

import net.karneim.pojobuilder.GeneratePojoBuilder;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;

public class DefaultKafkaConsumerFactoryProvider {
    @GeneratePojoBuilder(
            withBuilderInterface = Builder.class,
            withSetterNamePattern = "*",
            excludeProperties = {"applicationContext", "beanName", "applicationEventPublisher"},
            withGenerationGap = true,
            intoPackage = "org.swisspush.kobuka.spring"
    )
    public static <K, V> DefaultKafkaConsumerFactory<K, V> createDefaultKafkaConsumerFactory(Map<String, Object> configs) {
        return new DefaultKafkaConsumerFactory<K, V>(configs);
    }
}
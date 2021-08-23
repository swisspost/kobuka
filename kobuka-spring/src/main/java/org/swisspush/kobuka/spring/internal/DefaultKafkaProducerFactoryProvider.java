package org.swisspush.kobuka.spring.internal;

import net.karneim.pojobuilder.GeneratePojoBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;

import java.util.Map;

public class DefaultKafkaProducerFactoryProvider {
    @GeneratePojoBuilder(
            withBuilderInterface = Builder.class,
            withSetterNamePattern = "*",
            excludeProperties = {"applicationContext", "beanName", "applicationEventPublisher"},
            withGenerationGap = true,
            intoPackage = "org.swisspush.kobuka.spring"
    )
    public static <K, V> DefaultKafkaProducerFactory<K, V> create(Map<String, Object> configs) {
        return new DefaultKafkaProducerFactory<K, V>(configs);
    }
}
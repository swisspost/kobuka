package org.swisspush.kobuka.spring.internal;

import net.karneim.pojobuilder.GeneratePojoBuilder;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.ContainerProperties;

public class ContainerPropertiesProvider {

    @GeneratePojoBuilder(
            withBuilderInterface = Builder.class,
            withSetterNamePattern = "*",
            intoPackage = "org.swisspush.kobuka.spring"
    )
    public static ContainerProperties create(ConcurrentKafkaListenerContainerFactory<?, ?> factory) {
        return factory.getContainerProperties();
    }
}
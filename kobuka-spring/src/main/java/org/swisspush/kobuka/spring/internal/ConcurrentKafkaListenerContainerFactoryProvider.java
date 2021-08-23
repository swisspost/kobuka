package org.swisspush.kobuka.spring.internal;

import net.karneim.pojobuilder.GeneratePojoBuilder;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.swisspush.kobuka.spring.ContainerPropertiesBuilder;

import java.util.function.UnaryOperator;

public class ConcurrentKafkaListenerContainerFactoryProvider {

    @GeneratePojoBuilder(
            withBuilderInterface = Builder.class,
            withSetterNamePattern = "*",
            excludeProperties = {"applicationContext", "beanName", "applicationEventPublisher"},
            withGenerationGap = true,
            intoPackage = "org.swisspush.kobuka.spring"
    )
    public static <K, V> ConcurrentKafkaListenerContainerFactory<K, V>
    createConcurrentKafkaListenerContainerFactory(UnaryOperator<ContainerPropertiesBuilder> withContainerProperties) {
        ConcurrentKafkaListenerContainerFactory<K, V> result = new ConcurrentKafkaListenerContainerFactory<>();
        if (withContainerProperties != null) {
            ContainerPropertiesBuilder b = new ContainerPropertiesBuilder().factory(result);
            withContainerProperties.apply(b);
            b.build();
        }
        return result;
    }

}



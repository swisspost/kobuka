package org.swisspush.kobuka.spring.internal;

import net.karneim.pojobuilder.GeneratePojoBuilder;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.function.Supplier;

@GeneratePojoBuilder(
        withBaseclass = BuiltConcurrentKafkaListenerContainerFactory.BuilderMethods.class,
        withBuilderInterface = Builder.class,
        withSetterNamePattern = "*",
        withGenerationGap = true
)
class BuiltConcurrentKafkaListenerContainerFactory<K, V>
        extends ConcurrentKafkaListenerContainerFactory<K, V> {

    abstract static class BuilderMethods<T> implements Builder<T> {

        @SuppressWarnings("unchecked")
        public <K, V> ConcurrentKafkaListenerContainerFactory<K, V> get() {
            return (ConcurrentKafkaListenerContainerFactory<K, V>) build();
        }

        public <K, V> AbstractBuiltConcurrentKafkaListenerContainerFactoryBuilder<K, V> with() {
            return (AbstractBuiltConcurrentKafkaListenerContainerFactoryBuilder<K, V>)this;
        }
    }
}



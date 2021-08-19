package org.swisspush.kobuka.spring.base;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface SpringBuilderFunctions<T, R> extends Supplier<R> {

    default T apply(Consumer<R> consumer) {
        consumer.accept(get());
        return self();
    }

    @SuppressWarnings("unchecked")
    default T self() {
        return (T) this;
    }
}

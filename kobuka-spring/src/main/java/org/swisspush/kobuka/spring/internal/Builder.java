package org.swisspush.kobuka.spring.internal;

import java.util.function.Function;
import java.util.function.Supplier;

public interface Builder<T> {
    T build();

    default <R> R build(Function<T, R> fn) {
        return fn.apply(build());
    }

    default Supplier<T> asSupplier() {
        return this::build;
    }
}

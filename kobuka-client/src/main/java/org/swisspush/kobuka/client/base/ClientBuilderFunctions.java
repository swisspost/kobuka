package org.swisspush.kobuka.client.base;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

interface ClientBuilderFunctions<T> {
    default <R> R build(Function<Map<String, Object>, R> fn) {
        return fn.apply(build());
    }

    @SuppressWarnings("unchecked")
    default T property(String key, Object value) {
        build().put(key, value);
        return (T) this;
    }

    default Supplier<Map<String, Object>> asSupplier() {
        return this::build;
    }

    Map<String, Object> build();
}

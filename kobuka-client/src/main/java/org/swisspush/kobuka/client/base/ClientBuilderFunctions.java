package org.swisspush.kobuka.client.base;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

interface ClientBuilderFunctions<T> extends Supplier<Map<String, Object>> {
    default <R> R map(Function<Map<String, Object>, R> fn) {
        return fn.apply(get());
    }

    @SuppressWarnings("unchecked")
    default T property(String key, Object value) {
        get().put(key, value);
        return (T) this;
    }

}

package org.swisspush.kobuka.client.base;

import java.util.Map;
import java.util.function.Function;

interface BuilderFunctions<T> {
    default <R> R build(Function<Map<String, Object>, R> fn) {
        return fn.apply(build());
    }

    @SuppressWarnings("unchecked")
    default T addProperty(String key, Object value) {
        build().put(key, value);
        return (T) this;
    }

    Map<String, Object> build();
}

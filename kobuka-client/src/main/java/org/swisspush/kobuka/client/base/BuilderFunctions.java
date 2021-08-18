package org.swisspush.kobuka.client.base;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

interface BuilderFunctions<T> extends Supplier<Map<String, Object>> {
    default <R> R build(Function<Map<String, Object>, R> fn) {
        return fn.apply(get());
    }

    @SuppressWarnings("unchecked")
    default T addProperty(String key, Object value) {
        get().put(key, value);
        return (T) this;
    }

}

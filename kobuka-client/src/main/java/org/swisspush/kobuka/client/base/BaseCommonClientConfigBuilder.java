package org.swisspush.kobuka.client.base;

import java.util.function.Function;

/**
 * Base class for custom builders. Do not use directly.
 */
public class BaseCommonClientConfigBuilder<T extends BaseCommonClientConfigBuilder<T>> extends AbstractCommonClientConfigBuilder<T> {

    public static BaseCommonClientConfigBuilder<?> create() {
        return new BaseCommonClientConfigBuilder<>();
    }

    public <R> R map(Function<BaseCommonClientConfigBuilder<?>, R> fn) {
        return fn.apply(this);
    }
}

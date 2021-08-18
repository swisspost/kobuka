package org.swisspush.kobuka.client;

import java.util.function.Function;

public class BaseCommonClientConfigBuilder<T extends BaseCommonClientConfigBuilder<T>> extends AbstractCommonClientConfigBuilder<T> {

    public <R> R map(Function<BaseCommonClientConfigBuilder<?>, R> fn) {
        return fn.apply(this);
    }

    public static BaseCommonClientConfigBuilder<?> create() {
        return new BaseCommonClientConfigBuilder<>();
    }
}

package org.swisspush.kobuka.client.base;

import java.util.Map;
import java.util.function.Function;

/**
 * Base class for custom builders. Do not use directly.
 */
public class BaseStreamsConfigBuilder<T extends BaseStreamsConfigBuilder<T> & ClientBuilderFunctions<T>>
        extends AbstractStreamsConfigBuilder<T> implements ClientBuilderFunctions<T> {

    public void copyFrom(BaseStreamsConfigBuilder<?> parent) {
        configs.putAll(parent.configs);
    }

    public void copyFrom(BaseCommonClientConfigBuilder<?> parent) {
        configs.putAll(parent.configs);
    }

    @Override
    public Map<String, Object> build() {
        return configs;
    }

    public <R> R transform(Function<BaseStreamsConfigBuilder<?>, R> fn) {
        return fn.apply(this);
    }
}

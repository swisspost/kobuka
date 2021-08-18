package org.swisspush.kobuka.client.base;

import org.apache.kafka.clients.admin.AdminClientConfig;

import java.util.Map;
import java.util.function.Function;

/**
 * Base class for custom builders. Do not use directly.
 */
public class BaseAdminClientConfigBuilder<T extends BaseAdminClientConfigBuilder<T>> extends AbstractAdminClientConfigBuilder<T> {

    public static <T extends BaseProducerConfigBuilder<T>> BaseProducerConfigBuilder<T> create() {
        return new BaseProducerConfigBuilder<>();
    }

    public static <T extends BaseProducerConfigBuilder<T>> BaseProducerConfigBuilder<T> create(BaseAdminClientConfigBuilder<?> parent) {
        BaseProducerConfigBuilder<T> result = new BaseProducerConfigBuilder<>();
        result.configs.putAll(parent.configs);
        return result;
    }

    public static <T extends BaseProducerConfigBuilder<T>> BaseProducerConfigBuilder<T> create(BaseCommonClientConfigBuilder<?> parent) {
        BaseProducerConfigBuilder<T> result = new BaseProducerConfigBuilder<>();
        result.configs.putAll(parent.configs);
        return result;
    }

    public Map<String, Object> build() {
        return configs;
    }

    public <R> R build(Function<Map<String, Object> , R> fn) {
        return fn.apply(build());
    }
    public <R> R map(Function<BaseAdminClientConfigBuilder<?>, R> fn) {
        return fn.apply(this);
    }
}

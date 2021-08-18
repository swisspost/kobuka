package org.swisspush.kobuka.client;

import org.apache.kafka.clients.admin.AdminClientConfig;

import java.util.function.Function;

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

    public AdminClientConfig build() {
        return new AdminClientConfig(configs);
    }

    public <R> R build(Function<AdminClientConfig, R> fn) {
        return fn.apply(build());
    }

    public <R> R map(Function<BaseAdminClientConfigBuilder<?>, R> fn) {
        return fn.apply(this);
    }
}

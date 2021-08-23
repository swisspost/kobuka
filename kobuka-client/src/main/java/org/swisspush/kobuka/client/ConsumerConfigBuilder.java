package org.swisspush.kobuka.client;

import org.swisspush.kobuka.client.base.BaseCommonClientConfigBuilder;
import org.swisspush.kobuka.client.base.BaseConsumerConfigBuilder;

public class ConsumerConfigBuilder extends BaseConsumerConfigBuilder<ConsumerConfigBuilder> {

    public ConsumerConfigBuilder() {
    }

    public ConsumerConfigBuilder(BaseConsumerConfigBuilder<?> parent) {
        copyFrom(parent);
    }

    public ConsumerConfigBuilder(BaseCommonClientConfigBuilder<?> parent) {
        copyFrom(parent);
    }
}

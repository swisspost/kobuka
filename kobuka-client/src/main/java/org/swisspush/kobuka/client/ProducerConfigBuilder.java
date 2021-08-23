package org.swisspush.kobuka.client;

import org.swisspush.kobuka.client.base.BaseCommonClientConfigBuilder;
import org.swisspush.kobuka.client.base.BaseConsumerConfigBuilder;
import org.swisspush.kobuka.client.base.BaseProducerConfigBuilder;

public class ProducerConfigBuilder extends BaseProducerConfigBuilder<ProducerConfigBuilder> {

    public ProducerConfigBuilder() {
    }

    public ProducerConfigBuilder(BaseProducerConfigBuilder<?> parent) {
        copyFrom(parent);
    }

    public ProducerConfigBuilder(BaseCommonClientConfigBuilder<?> parent) {
        copyFrom(parent);
    }
}

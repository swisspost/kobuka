package org.swisspush.kobuka.client;

import org.swisspush.kobuka.client.base.BaseCommonClientConfigBuilder;
import org.swisspush.kobuka.client.base.BaseStreamsConfigBuilder;

public class StreamsConfigBuilder extends BaseStreamsConfigBuilder<StreamsConfigBuilder> {

    public StreamsConfigBuilder() {
    }

    public StreamsConfigBuilder(BaseStreamsConfigBuilder<?> parent) {
        copyFrom(parent);
    }

    public StreamsConfigBuilder(BaseCommonClientConfigBuilder<?> parent) {
        copyFrom(parent);
    }
}

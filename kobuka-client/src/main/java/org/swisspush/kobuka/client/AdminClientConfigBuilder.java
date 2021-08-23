package org.swisspush.kobuka.client;

import org.swisspush.kobuka.client.base.BaseAdminClientConfigBuilder;
import org.swisspush.kobuka.client.base.BaseCommonClientConfigBuilder;

public class AdminClientConfigBuilder extends BaseAdminClientConfigBuilder<AdminClientConfigBuilder> {

    public AdminClientConfigBuilder() {
    }

    public AdminClientConfigBuilder(BaseAdminClientConfigBuilder<?> parent) {
        copyFrom(parent);
    }

    public AdminClientConfigBuilder(BaseCommonClientConfigBuilder<?> parent) {
        copyFrom(parent);
    }
}

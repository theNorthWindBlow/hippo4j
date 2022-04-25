/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.hippo4j.starter.config;

import cn.hippo4j.common.model.InstanceInfo;
import cn.hippo4j.common.toolkit.ContentUtil;
import cn.hippo4j.core.toolkit.IdentifyUtil;
import cn.hippo4j.core.toolkit.inet.InetUtils;
import cn.hippo4j.starter.core.DiscoveryClient;
import cn.hippo4j.starter.remote.HttpAgent;
import cn.hutool.core.text.StrBuilder;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;

import java.net.InetAddress;

import static cn.hippo4j.common.constant.Constants.GROUP_KEY_DELIMITER;
import static cn.hippo4j.core.toolkit.IdentifyUtil.CLIENT_IDENTIFICATION_VALUE;
import static cn.hippo4j.starter.toolkit.CloudCommonIdUtil.getDefaultInstanceId;
import static cn.hippo4j.starter.toolkit.CloudCommonIdUtil.getIpApplicationName;

/**
 * Dynamic threadPool discovery config.
 *
 * @author chen.ma
 * @date 2021/8/6 21:35
 */
@AllArgsConstructor
public class DiscoveryConfiguration {

    private final ConfigurableEnvironment environment;

    private final BootstrapProperties properties;

    private final InetUtils hippo4JInetUtils;

    @Bean
    @SneakyThrows
    public InstanceInfo instanceConfig() {
        String namespace = properties.getNamespace();
        String itemId = properties.getItemId();
        String port = environment.getProperty("server.port", "8080");
        String applicationName = environment.getProperty("spring.dynamic.thread-pool.item-id");
        String active = environment.getProperty("spring.profiles.active", "UNKNOWN");

        InstanceInfo instanceInfo = new InstanceInfo();
        String instanceId = getDefaultInstanceId(environment, hippo4JInetUtils);
        instanceId = StrBuilder.create().append(instanceId).append(":").append(CLIENT_IDENTIFICATION_VALUE).toString();

        String contextPath = environment.getProperty("server.servlet.context-path", "");
        instanceInfo.setInstanceId(instanceId)
                .setIpApplicationName(getIpApplicationName(environment, hippo4JInetUtils))
                .setHostName(InetAddress.getLocalHost().getHostAddress())
                .setAppName(applicationName)
                .setPort(port)
                .setClientBasePath(contextPath)
                .setGroupKey(ContentUtil.getGroupKey(itemId, namespace));

        String callBackUrl = new StringBuilder().append(instanceInfo.getHostName()).append(":")
                .append(port).append(instanceInfo.getClientBasePath())
                .toString();
        instanceInfo.setCallBackUrl(callBackUrl);

        String identify = IdentifyUtil.generate(environment, hippo4JInetUtils);
        instanceInfo.setIdentify(identify);
        instanceInfo.setActive(active.toUpperCase());

        return instanceInfo;
    }

    @Bean
    public DiscoveryClient hippo4JDiscoveryClient(HttpAgent httpAgent, InstanceInfo instanceInfo) {
        return new DiscoveryClient(httpAgent, instanceInfo);
    }

}

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
 *
 */

package org.apache.skywalking.oap.server.configuration.apollo;

import com.google.common.base.Strings;
import org.apache.skywalking.oap.server.configuration.api.AbstractConfigurationProvider;
import org.apache.skywalking.oap.server.configuration.api.ConfigWatcherRegister;
import org.apache.skywalking.oap.server.library.module.ModuleStartException;

/**
 * Get configuration from Apollo configuration center.
 */
public class ApolloConfigurationProvider extends AbstractConfigurationProvider {
    private ApolloConfigurationCenterSettings settings;

    @Override
    public String name() {
        return "apollo";
    }

    @Override
    public ConfigCreator newConfigCreator() {
        return new ConfigCreator<ApolloConfigurationCenterSettings>() {
            @Override
            public Class type() {
                return ApolloConfigurationCenterSettings.class;
            }

            @Override
            public void onInitialized(final ApolloConfigurationCenterSettings initialized) {
                settings = initialized;
            }
        };
    }

    @Override
    protected ConfigWatcherRegister initConfigReader() throws ModuleStartException {
        final String apolloCluster = settings.getApolloCluster();

        if (!Strings.isNullOrEmpty(apolloCluster)) {
            System.setProperty("apollo.cluster", apolloCluster);
        }

        final String apolloMeta = settings.getApolloMeta();

        if (!Strings.isNullOrEmpty(apolloMeta)) {
            System.setProperty("apollo.meta", apolloMeta);
        }

        final String appId = settings.getAppId();

        if (!Strings.isNullOrEmpty(appId)) {
            System.setProperty("app.id", appId);
        }

        final String env = settings.getApolloEnv();

        if (!Strings.isNullOrEmpty(env)) {
            System.setProperty("env", env);
        }

        return new ApolloConfigWatcherRegister(settings);
    }
}

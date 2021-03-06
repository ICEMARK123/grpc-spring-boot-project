/*
 * Copyright 2019 The nity.io gRPC Spring Boot Project Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.nity.grpc.sample.grpc;

import io.nity.grpc.client.config.GrpcClientPropertiesMap;
import io.nity.grpc.sample.interceptor.SampleGlobalInterceptor;
import io.nity.grpc.sample.interceptor.SampleInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class GrpcClientConfig {

    @Autowired
    protected GrpcClientPropertiesMap clientPropertiesMap;

    @Bean
    public SampleGlobalInterceptor sampleGlobalInterceptor() {
        return new SampleGlobalInterceptor();
    }

    @Bean
    public SampleInterceptor sampleInterceptor() {
        return new SampleInterceptor();
    }
}

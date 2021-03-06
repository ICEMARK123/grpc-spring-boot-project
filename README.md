gRPC Spring Boot Project
========================================

优雅的集成gRPC到Spring Boot项目。分[grpc-server-spring-boot-starter](grpc-server-spring-boot-starter)、[grpc-client-spring-boot-starter](grpc-client-spring-boot-starter)。

支持四种模式：

- inProcess 进程内模式，只使用在测试和功能演示场景
- simple 明文模式，可使用在内网微服务
- tls TLS模式，服务端、客户端使用证书保证通信安全，可对公网提供服务
- custom 自定义模式，在以上模式不满足要求的情况下，可以对服务端、客户端进行自定义

----------
## 示例

- [InProcess](grpc-spring-boot-samples/grpc-spring-boot-sample-inprocess)

- [Server Simple](grpc-spring-boot-samples/grpc-spring-boot-sample-server-simple)

- [Client Simple](grpc-spring-boot-samples/grpc-spring-boot-sample-client-simple)

- [Server Tls](grpc-spring-boot-samples/grpc-spring-boot-sample-server-tls)

- [Client Tls](grpc-spring-boot-samples/grpc-spring-boot-sample-client-tls)

- [Server Custom](grpc-spring-boot-samples/grpc-spring-boot-sample-server-custom)

- [Client Custom](grpc-spring-boot-samples/grpc-spring-boot-sample-client-custom)


#### 生成Tls测试证书

```
$ sh grpc-spring-boot-samples/tools/create_openssl_key.sh
```

证书默认生成在/tmp/sslcert，可修改脚本自定义

----------
## 使用

### Server

##### Maven
```xml
<dependency>
    <groupId>io.nity.grpc</groupId>
    <artifactId>grpc-server-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

##### Gradle
```gradle
compile 'io.nity.grpc:grpc-server-spring-boot-starter:1.0.0'
```

##### application.properties
```properties
grpc.server.enableReflection=true
grpc.server.model=simple
grpc.server.host=localhost
grpc.server.port=50440
```

##### Java
```java
@GrpcService
public class GreeterGrpcService extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        //grpc service implement code
    }

}
```

#### 如果需要对Server进一步配置
1.可创建GrpcServerBuilderConfigurer bean，在configure里对创建好的serverBuilder进一步配置
```java
@Configuration
public class GrpcCustomConfig {

    @Bean
    public GrpcServerBuilderConfigurer serverBuilderConfigurer() {
        return serverBuilder -> {
            //sample code
            //serverBuilder.maxInboundMessageSize()
        };
    }
}
```

2.使用custom模式，创建ServerBuilder bean，根据需要对serverBuilder进行配置
```java
@Configuration
public class GrpcCustomConfig {
    
    @Bean
    @ConditionalOnProperty(value = "grpc.server.model", havingValue = GrpcServerProperties.SERVER_MODEL_CUSTOM)
    public ServerBuilder getServerBuilder() {
        ServerBuilder<?> serverBuilder;
        
        //create and config serverBuilder
        
        return serverBuilder;
    }

}
```

### Client

##### Maven
```xml
<dependency>
    <groupId>io.nity.grpc</groupId>
    <artifactId>grpc-client-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

##### Gradle
```gradle
compile 'io.nity.grpc:grpc-client-spring-boot-starter:1.0.0'
```

##### application.properties
```properties
grpc.client.default.model=simple
grpc.client.default.host=localhost
grpc.client.default.port=50440
```

##### Java
```java
@RestController
public class GreeterController {

    @GrpcClient("default")
    private GreeterGrpc.GreeterBlockingStub greeterBlockingStub;

    @RequestMapping(value = {"/greet"})
    public String greet() {
        //code...
        response = greeterBlockingStub.sayHello(request);
        //code...
    }

}
```

#### 如果需要对Client进一步配置
1.可创建GrpcChannelBuilderConfigurer bean，在configure里对创建好的channelBuilder进一步配置
```java
@Configuration
public class GrpcClientConfig {

    @Bean
    public GrpcChannelBuilderConfigurer channelBuilderConfigurer() {
        return (channelBuilder, name) -> {
            log.info("configure channelBuilder...");
            //channelBuilder.loadBalancerFactory(something);
            //etc.
        };
    }

}
```

2.可创建GrpcChannelConfigurer bean，在configure里对创建好的channel进一步配置
```java
@Configuration
public class GrpcClientConfig {

    @Bean
    public GrpcChannelConfigurer channelConfigurer() {
        return (channel, name) -> {
            log.info("configure channel...");
            //ClientInterceptors.intercept(channel, interceptors);
            //etc.
        };
    }
}
```

3.可使用custom模式，创建CustomChannelFactory bean，实现ChannelBuilder的创建逻辑
```java
@Configuration
public class GrpcClientConfig {

    @Bean
    public CustomChannelFactory customChannelFactory(final GrpcClientPropertiesMap clientPropertiesMap,
                                                     final GrpcChannelBuilderConfigurer channelBuilderConfigurer,
                                                     final GrpcChannelConfigurer channelConfigurer) {
        return new CustomChannelFactory(clientPropertiesMap, channelBuilderConfigurer, channelConfigurer) {
            @Override
            protected ManagedChannelBuilder newChannelBuilder(final String name, final GrpcClientProperties clientProperties) {
                ManagedChannelBuilder<?> managedChannelBuilder;
                //create and config ChannelBuilder
                return managedChannelBuilder;
            }
        };
    }

}
```

### Server + Client
##### Maven
```xml
<dependency>
    <groupId>io.nity.grpc</groupId>
    <artifactId>grpc-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

##### Gradle
```gradle
compile 'io.nity.grpc:grpc-spring-boot-starter:1.0.0'
```

#### Snapshots仓库
https://oss.sonatype.org/content/repositories/snapshots/

具体的代码请参照各示例模块。

----------
## License
[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html) Copyright (C) Apache Software Foundation

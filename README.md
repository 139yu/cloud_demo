# Spring Cloud实战
当前项目`spring boot`版本`2.0.1`，`spring cloud`版本`Finchley.RC1`
## Eureka
#### 编写Eureka Server    
创建一个`spring boot`项目，引入相关依赖：
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.0.1.RELEASE</version>
</parent>
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
</dependencies>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Finchley.SR2</version>
                <type>pom</type>
                <scope>import</scope>
           </dependency>
        </dependencies>
</dependencyManagement>
```
编写启动类：
```java
@EnableEurekaServer
@SpringBootApplication
public class EurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class,args);
    }
}

```
编写配置文件，因为`yml`文件可读性更高，结构清晰，所以这里使用`yml`类型的配置文件，配置如下：
```yaml
server:
  port: 10086
eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
    service-url:
      defaultZone: http://localhost:10086/eureka
```
配置说明：

- eureka.client.register-with-eureka：表示是否将自己注册到Eureka Server，默认为true。由于当前服务就是Eureka Server，所以这里配置为false
- eureka.client.fetch-registry：表示是否从Eureka Server获取注册信息，默认为true。因为这是一个单点的Eureka Server，不需要同步其他结点的信息，所以配置为false
- eureka.client.service-url.defaultZone：设置与Eureka Server交互的地址，查询服务和注册服务都需要依赖这个地址。默认是`http://localhost/10086/eureka`,当有多个地址时使用`,`分割。

#将微服务注册到Eureka Server上
创建一个`spring boot`项目，添加一下依赖：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```
编写`yml`配置文件：
```yaml
server:
  port: 2008
spring:
    application:
        name: microserver-provider-user
eureka:
  client:
    service-url:
      defaultZone: http://localhost:10086/eureka
  instance:
    prefer-ip-address: true
```
配置说明：

- `spring.application.name`：用于指定注册到Eureka Server上的应用名
- `eureka.instance.prefer-ip-address`：表示是否将自己的IP注册到Eureka Server，不配置默认为false，则表示注册微服务所在操作系统的hostname到Eureka Server
编写启动类：
```java
@SpringBootApplication
@EnableDiscoveryClient
public class SimpleProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(SimpleProviderApplication.class,args);
    }
}
```
也可以用`@EnableEurekaClient`注解代替`@EnableDiscoveryClient`。在`spring cloud`中，微服务发现组件有多种选择，如`Zookeeper`等。`@EnableDiscoveryClient`为各种发现服务组件提供了支持，`@EnableEurekaClient`只表明是一个Eureka的Client。将这些配置好后就可以把服务注册到Eureka Server上了。可访问`http://localhost:10086/eureka/`查看。
### Eureka Server的高可用

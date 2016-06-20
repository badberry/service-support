# service-support

封装好一些微服务的基础功能:接口文档自动生成，统一错误处理，访问日志支持,自动注册

### 使用说明
需要使用基础功能，需要在Spring-Boot的Application上加上ComponentScan注解
```java
@SpringBootApplication
@EnableScheduling
@EnableCaching
@ComponentScan(value = {"cn.cloudtop.strawberry.service","当前项目的命名空间"})
public class XXXServiceApplication {}
```
### 接口文档自动生成
#### 介绍
封装了swagger配置，依赖springfox.swagger:2.2.2
#### 使用说明
要单独使用接口文档自动生成的功能，需要在Spring-Boot的Application上加上ComponentScan注解
```java
@SpringBootApplication
@EnableScheduling
@ComponentScan(value = {"cn.cloudtop.strawberry.service","当前项目的命名空间"})
public class XXXServiceApplication {}
```
在application.properties/application.yaml里面添加:  
```java
info.app.name:#服务名称
info.app.description:#服务说明
info.app.version:#服务版本号，建议简单版本(v1,v2,v3...)
info.app.creator:#服务创建人或者服务维护人，建议用邮箱
```
### 统一错误处理
#### 介绍
提供统一错误处理封装(UnitedErrorController)，包括  
未知错误： Unknown_Error(100999)  
没有找到匹配：API_NOT_EXISTS(100998)  
验证错误: Valid_Error(100901)  
请求方法错误:Method_Not_Allowed(100902)  
发生未知异常错误:None_Process_Exception(100903)  
业务错误：捕获RestException,从中取出errorCode,errorMessage返回错误信息
#### 使用说明
要单独使用统一错误处理的功能，需要在Spring-Boot的Application上加上ComponentScan注解
```java
@SpringBootApplication
@ComponentScan(value = {"cn.cloudtop.strawberry.service.errors","当前项目的命名空间"})
public class XXXServiceApplication {}
```
### 访问日志支持
#### 介绍
实现SpringMvc的拦截器(RequestInfoInterceptor)来实现访问日志的生成,使用AccessLogConfig来实现拦截器的注册.
日志都是Info级别，每次访问都会有两条日志：  
1.请求日志会记录:ip地址，请求方法，请求url,客户端信息，请求参数  
2.请求返回日志会记录:ip地址，请求方法，请求url,发生的时间  
#### 使用说明
要单独使用访问日志的功能，需要在Spring-Boot的Application上加上ComponentScan注解
```java
@SpringBootApplication
@ComponentScan(value = {"cn.cloudtop.strawberry.service.accesses","当前项目的命名空间"})
public class XXXServiceApplication {}
```
### 自动注册
#### 介绍
服务启动后，会开启一个定时任务，每隔60s会往服务注册服务器上注册自己的信息!
#### 使用说明
要单独使用服务注册功能，需要在Spring-Boot的Application上加上ComponentScan，EnableScheduling注解
```java
@SpringBootApplication
@EnableScheduling
@ComponentScan(value = {"cn.cloudtop.strawberry.service.registry","当前项目的命名空间"})
public class XXXServiceApplication {}
```
还需要在application.properties/application.yaml里面添加:
```java
info.app.name:#服务名称
info.app.version:#服务版本号，建议简单版本(v1,v2,v3...)
registry.url://服务注册中心url
```
***还需要添加两个环境变量***:***host***---服务地址,***port***---服务端口号
### 缓存支持
#### 介绍
简单封装Spring缓存,使之支持Reids缓存.使用json序列化反序列化缓存对象.
#### 使用说明
若要单独使用缓存功能，需要在Spring-Boot的Application上加上ComponentScan,EnableCaching注解
```java
@SpringBootApplication
@EnableCaching
@ComponentScan(value = {"cn.cloudtop.strawberry.service.caches","当前项目的命名空间"})
public class XXXServiceApplication {}
```
还需要在application.properties/application.yaml中添加redis的配置:
```properties
spring.redis.host: //redis主机地址
spring.redis.port: //redis监听端口号
spring.redis.password: //redis登录密码
spring.redis.database: //redis库
spring.redis.timeout: //redis连接超时时间
spring.redis.pool.*   //redis连接池配置
```
#### 使用缓存配置介绍
主要用到Cacheable,CacheEvict,CachePut注解，Caching注解用来集合前面几个注解.
##### Cacheable
标记结果缓存，

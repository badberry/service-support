# service-support

封装好一些微服务的基础功能

### 使用说明
需要使用基础功能，需要在Spring-Boot的Application上加上ComponentScan注解
```java
@SpringBootApplication
@EnableScheduling
@ComponentScan(value = {"cn.cloudtop.strawberry.service","当前项目的命名空间"})
public class XXXServiceApplication {}
```

### 接口文档自动生成
1. 介绍  
封装了swagger配置，依赖springfox.swagger:2.2.2
2. 使用说明  
要单独使用接口文档自动生成的功能，需要在Spring-Boot的Application上加上ComponentScan注解
<pre>
```java
@SpringBootApplication
@EnableScheduling
@ComponentScan(value = {"cn.cloudtop.strawberry.service","当前项目的命名空间"})
public class XXXServiceApplication {}
```
</pre>
在application.properties/application.yaml里面添加:  
<pre>
```
service.name:#服务名称
service.desc:#服务说明
service.creator:#服务创建人或者服务维护人，建议用邮箱
service.version:#服务版本号，建议简单版本(v1,v2,v3...)
```
</pre>

### 统一错误处理
1. 介绍  
提供统一错误处理封装(UnitedErrorController)，包括
* 未知错误： Unknown_Error(100999)
* 没有找到匹配：API_NOT_EXISTS(100998)
* 验证错误: Valid_Error(100901)
* 请求方法错误:Method_Not_Allowed(100902)
* 发生未知异常错误:None_Process_Exception(100903)
* 业务错误：捕获RestException,从中取出errorCode,errorMessage返回错误信息.
2. 使用说明  
要单独使用统一错误处理的功能，需要在Spring-Boot的Application上加上ComponentScan注解
```java
@SpringBootApplication
@EnableScheduling
@ComponentScan(value = {"cn.cloudtop.strawberry.service.errors","当前项目的命名空间"})
public class XXXServiceApplication {}
```

### 访问日志支持
1. 介绍  
实现SpringMvc的拦截器(RequestInfoInterceptor)来实现访问日志的生成,使用AccessLogConfig来实现拦截器的注册.
日志都是Info级别，每次访问都会有两条日志：  
1.请求日志会记录:ip地址，请求方法，请求url,客户端信息，请求参数  
2.请求返回日志会记录:ip地址，请求方法，请求url,发生的时间  
2. 使用说明  
要单独使用访问日志的功能，需要在Spring-Boot的Application上加上ComponentScan注解
```java
@SpringBootApplication
@EnableScheduling
@ComponentScan(value = {"cn.cloudtop.strawberry.service.accesses","当前项目的命名空间"})
public class XXXServiceApplication {}
```


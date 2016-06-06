# service-support

封装好一些微服务的基础功能

### 使用说明
需要使用基础功能，需要在Spring-Boot的Application上加上ComponentScan注解
```java
@SpringBootApplication
@EnableScheduling
@ComponentScan(value = {"cn.cloudtop.strawberry.service","当前项目的命名空间"})
public class ZoneServiceApplication {}
```

### 接口文档自动生成
1. 介绍  
封装了swagger配置，依赖springfox.swagger:2.2.2
2. 使用说明  
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
提供统一错误处理封装，包括
* 未知错误： Unknown_Error(100999)
* 没有找到匹配：API_NOT_EXISTS(100998)
* 验证错误: Valid_Error(100901)
* 请求方法错误:Method_Not_Allowed(100902)
* 发生未知异常错误:None_Process_Exception(100903)
* 业务错误：捕获RestException,从中取出errorCode,errorMessage返回错误信息.


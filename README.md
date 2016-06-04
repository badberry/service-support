# service-support

封装好一些微服务的基础功能

### 接口文档自动生成
1. 介绍  
封装了swagger配置，依赖springfox.swagger:2.2.2
2. 使用说明  
在application.properties/application.yaml里面添加:  
>service.name:#服务名称  
>service.desc:#服务说明  
>service.creator:#服务创建人或者服务维护人，建议用邮箱  
>service.version:#服务版本号，建议简单版本(v1,v2,v3...)  

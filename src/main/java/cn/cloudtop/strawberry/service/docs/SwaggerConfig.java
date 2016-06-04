package cn.cloudtop.strawberry.service.docs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by jackie on 16-4-24
 * <p>
 * user swagger to support generate api docs.
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Value("${service.name}")
    private String serviceName;
    @Value("${service.desc}")
    private String description;
    @Value("${service.creator}")
    private String creator;
    @Value("${service.version}")
    private String version;

    @Bean
    public Docket apiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(serviceName)
                .apiInfo(apiInfo())
                .select()
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(serviceName)
                .description(description)
                .contact(creator)
                .version(version)
                .build();
    }
}

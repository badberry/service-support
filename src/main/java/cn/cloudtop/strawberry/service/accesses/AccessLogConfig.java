package cn.cloudtop.strawberry.service.accesses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

/**
 * Created by jackie on 16-6-6
 */
@Configuration
public class AccessLogConfig extends WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessLogConfig.class);

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestInfoInterceptor()).addPathPatterns("/**");
        LOGGER.debug("registry access log interceptor.");
        super.addInterceptors(registry);
    }
}

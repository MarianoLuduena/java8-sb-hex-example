package com.redbee.payments.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    private final TraceSleuthInterceptor traceSleuthInterceptor;

    public AppConfig(final TraceSleuthInterceptor traceSleuthInterceptor) {
        this.traceSleuthInterceptor = traceSleuthInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.traceSleuthInterceptor);
    }

}

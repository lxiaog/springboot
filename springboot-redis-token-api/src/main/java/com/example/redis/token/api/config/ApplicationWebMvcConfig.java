package com.example.redis.token.api.config;

import com.example.redis.token.api.interpretor.ApiIdempotentInterceptor;
import com.example.redis.token.api.interpretor.LogInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * webmvc配置类
 * 该注解@Configuration表示为配置类 springmvc中的web.xml
 * 该注解@EnableWebMvc 表示启动webmvc配置功能
 */
@Configuration
@EnableWebMvc
public class ApplicationWebMvcConfig implements WebMvcConfigurer {

    /**
     * 注解LogInterceptor类到IOC容器中
     */
    @Bean
    public LogInterceptor logInterceptor() {
        return new LogInterceptor();
    }

    @Bean
    public ApiIdempotentInterceptor apiIdempotentInterceptor() {
        return new ApiIdempotentInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册日志拦截器
        registry.addInterceptor(logInterceptor());
        registry.addInterceptor(apiIdempotentInterceptor());
    }
}

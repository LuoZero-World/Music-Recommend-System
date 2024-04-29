package com.llq.config;

import com.llq.interceptor.AuthorizeInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 李林麒
 * @date 2023/10/22 9:23
 * @Description Web配置类
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Resource
    AuthorizeInterceptor authorizeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizeInterceptor)     //添加我们自定义的拦截器，并指明拦截哪些路径
                .addPathPatterns("/**")
                .excludePathPatterns("/api/auth/**", "/api/admin/**");
    }
}

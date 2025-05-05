package com.example.magic03.patterndecorator.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 借助springMVC的扩展点，将自定义的参数解析器注册到springMVC中，那么在处理自定义注解参数的时候，就可以调用自定义的参数解析器，实现自定义的增强功能
 */
@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {

//    @Autowired
//    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @Autowired
    private ApplicationContext applicationContext;


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new TimestampRequestBodyMethodProcessor(applicationContext));
    }
}

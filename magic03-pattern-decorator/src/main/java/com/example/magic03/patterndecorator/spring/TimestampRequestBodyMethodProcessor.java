package com.example.magic03.patterndecorator.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.List;
import java.util.Map;

/**
 * 利用装饰器模式，封装了RequestResponseBodyMethodProcessor，在此基础之上对请求参数（Map类型）进行增强，增加时间戳字段
 */
public class TimestampRequestBodyMethodProcessor implements HandlerMethodArgumentResolver {

    private RequestResponseBodyMethodProcessor processor;

    private ApplicationContext applicationContext;

    public TimestampRequestBodyMethodProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 支持我们增强的注解
     *
     * @param parameter the method parameter to check
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(TimestampRequestBody.class);
    }

    /**
     * 借用RequestResponseBodyMethodProcessor的resolveArgument方法，获取参数，并在参数基础上进行增强
     *
     * @param parameter     the method parameter to resolve. This parameter must
     *                      have previously been passed to {@link #supportsParameter} which must
     *                      have returned {@code true}.
     * @param mavContainer  the ModelAndViewContainer for the current request
     * @param webRequest    the current request
     * @param binderFactory a factory for creating {@link WebDataBinder} instances
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        setupProcessor();
        Object result = processor.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        if (result instanceof Map<?, ?>) {
            ((Map) result).put("timestamp", System.currentTimeMillis());
        }
        return result;
    }

    private void setupProcessor() {
        if (processor != null) {
            return;
        }
        RequestMappingHandlerAdapter adapter = applicationContext.getBean(RequestMappingHandlerAdapter.class);
        List<HandlerMethodArgumentResolver> argumentResolvers = adapter.getArgumentResolvers();
        for (HandlerMethodArgumentResolver argumentResolver : argumentResolvers) {
            if (argumentResolver instanceof RequestResponseBodyMethodProcessor) {
                processor = (RequestResponseBodyMethodProcessor) argumentResolver;
            }
        }
    }
}

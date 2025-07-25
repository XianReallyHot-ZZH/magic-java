package com.example.magic09minispring.web;

import com.alibaba.fastjson2.JSONObject;
import com.example.magic09minispring.BeanPostProcessor;
import com.example.magic09minispring.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 核心类DispatcherServlet，请求分发器
 */
@Component
public class DispatcherServlet extends HttpServlet implements BeanPostProcessor {

    private static final Pattern PATTERN = Pattern.compile("shuaishuaizi\\{(.*?)}");

    private Map<String/*请求的uri*/, WebHandler/*请求对应的处理器*/> handlerMap = new HashMap<>();

    /**
     * 请求处理,根据HttpServletRequest的请求路径找到对应的Controller，并调用其方法处理请求
     *
     * @param req
     * @param res
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        WebHandler handler = findHandler(req);
        if (handler == null) {
            res.setContentType("text/html");
            res.getWriter().write("<h1>404 Not Found</h1>");
            return;
        }
        try {
            Object controllerBean = handler.getControllerBean();
            Object[] args = resolveArgs(req, handler.getMethod());
            Object result = handler.getMethod().invoke(controllerBean, args);
            // 根据ResponseBody注解返回其对应类型的结果
            switch (handler.getResultType()) {
                case HTML -> {
                    res.setContentType("text/html;charset=UTF-8");
                    res.getWriter().write(result.toString());
                }
                case JSON -> {
                    res.setContentType("application/json;charset=UTF-8");
                    res.getWriter().write(JSONObject.toJSONString(result));
                }
                case LOCAL -> {
                    try (InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(((ModelAndView) result).getView())) {
                        String htmlResource = new String(resourceAsStream.readAllBytes());
                        htmlResource = renderTemplate(htmlResource, ((ModelAndView) result).getContext());
                        res.setContentType("text/html;charset=UTF-8");
                        res.getWriter().write(htmlResource);
                    }
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }

    }

    /**
     * 根据上下文，对静态资源html模板进行渲染
     *
     * @param htmlResource
     * @param context
     * @return
     */
    private String renderTemplate(String htmlResource, Map<String, String> context) {
        return PATTERN.matcher(htmlResource).replaceAll(matchResult -> {
            String key = matchResult.group(1);
            return context.getOrDefault(key, "");
        });
    }

    /**
     * 根据方法入参去获取请求uri中的参数
     * @param req
     * @param method
     * @return
     */
    private Object[] resolveArgs(HttpServletRequest req, Method method) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class<?> parameterType = parameter.getType();
            // 获取参数上的PathParam注解，解析出参数名称
            PathParam pathParam = parameter.getAnnotation(PathParam.class);
            String value = pathParam != null ? req.getParameter(pathParam.value()) : req.getParameter(parameter.getName());
            // 参数解析，数据类型转换，这里我们简单处理了，只支持String和Integer
            if (value == null) {
                args[i] = null;
                continue;
            }
            if (String.class.isAssignableFrom(parameterType)) {
                args[i] = value;
            } else if (Integer.class.isAssignableFrom(parameterType)) {
                args[i] = Integer.parseInt(value);
            } else {
                args[i] = null;
            }
        }
        return args;
    }

    /**
     * 通过请求找到处理器
     * 例子：localhost：8080/hello/a，获取uri：/hello/a，根据uri在handlerMap中找到对应的handler
     *
     * @param req
     */
    private WebHandler findHandler(HttpServletRequest req) {
        return handlerMap.get(req.getRequestURI());
    }

    /**
     * 利用spring生命周期接口方法，完成对bean的扫描，扫描出Controller类
     *
     * @param bean
     * @param beanName
     * @return
     */
    @Override
    public Object afterInitializeBean(Object bean, String beanName) {
        if (!bean.getClass().isAnnotationPresent(Controller.class)) {
            return bean;
        }
        // 进行WebHandler封装
        // 1、获取类上的RequestMapping注解的value属性值
        RequestMapping requestMappingAnnotation = bean.getClass().getDeclaredAnnotation(RequestMapping.class);
        String baseUri = requestMappingAnnotation == null ? "" : requestMappingAnnotation.value();
        // 2、获取方法上的RequestMapping注解的value属性值
        Arrays.stream(bean.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> {
                    RequestMapping methodRm = method.getDeclaredAnnotation(RequestMapping.class);
                    // 3、拼装出最终的唯一uri
                    String finalUri = baseUri.concat(methodRm.value());
                    // 4、将最终的uri和WebHandler对象放入map中，如果存在重复的uri，则报错
                    if (handlerMap.put(finalUri, new WebHandler(bean, method)) != null) {
                        throw new RuntimeException("controller的uri定义重复：" + finalUri);
                    }
                });
        return bean;
    }
}

package com.example.magic09minispring.web;

import java.lang.reflect.Method;

/**
 * webHandler,承载着具体controller的具体方法的信息
 */
public class WebHandler {

    private final Object controllerBean;

    private final Method method;

    private final ResultType resultType;

    public WebHandler(Object controllerBean, Method method) {
        this.controllerBean = controllerBean;
        this.method = method;
        this.resultType = resolveResultType(controllerBean,  method);
    }

    private ResultType resolveResultType(Object controllerBean, Method method) {
        if (method.isAnnotationPresent(ResponseBody.class)) {
            return ResultType.JSON;
        }
        if (method.getReturnType() == ModelAndView.class) {
            return ResultType.LOCAL;
        }
        return ResultType.HTML;
    }

    public Object getControllerBean() {
        return controllerBean;
    }

    public Method getMethod() {
        return method;
    }

    public ResultType getResultType() {
        return resultType;
    }

    /**
     * 请求结果返回类型
     */
    enum ResultType {
        // 返回json类型的数据
        JSON,
        // 返回html类型的字符串
        HTML,
        // 在本地查找一个路径，进行解析，解析成html格式的一个字符串
        LOCAL
    }
}

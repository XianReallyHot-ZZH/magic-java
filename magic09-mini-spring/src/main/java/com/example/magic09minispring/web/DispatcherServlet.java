package com.example.magic09minispring.web;

import com.example.magic09minispring.Component;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 核心类DispatcherServlet，请求分发器
 */
@Component
public class DispatcherServlet extends HttpServlet {

    /**
     * 请求处理,根据HttpServletRequest的请求路径找到对应的Controller，并调用其方法处理请求
     * @param req
     * @param res
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html");
        res.getWriter().write("<h1>Hello from Embedded Tomcat!</h1> <br> " + req.getRequestURL().toString());
    }


}

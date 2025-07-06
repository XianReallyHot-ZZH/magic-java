package com.example.magic09minispring.web;

import com.example.magic09minispring.Component;
import com.example.magic09minispring.PostConstruct;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.io.IOException;

/**
 * Tomcat 服务器
 * 这里选择交由spring管理
 */
@Component
public class TomcatServer {

    /**
     * 交由spring管理，并完成Tomcat的启动
     * @throws LifecycleException
     */
    @PostConstruct
    public void start() throws LifecycleException {
        int port = 8080;
        // 创建Tomcat实例
        Tomcat tomcat = new Tomcat();
        // 绑定端口
        tomcat.setPort(port);
        tomcat.getConnector();

        // 创建Context上下文，其实是关联一个文件夹，和静态资源有关，我们这里和静态资源没啥关系，所以这里设置一个空的文件夹
        String contextPath = "";
        String docBase = new File(".").getAbsolutePath();
        Context context = tomcat.addContext(contextPath, docBase);

        // 绑定一个Servlet
        tomcat.addServlet(contextPath, "helloServlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                resp.setContentType("text/html");
                resp.getWriter().write("<h1>Hello from Embedded Tomcat!</h1> <br> " + req.getRequestURL().toString());
            }
        });

        // 将helloServlet绑定到/*（所有的请求）
        context.addServletMappingDecoded("/*", "helloServlet");
        // 启动tomcat服务器
        tomcat.start();
        System.out.println("Tomcat started, port:" +  port);
    }

}

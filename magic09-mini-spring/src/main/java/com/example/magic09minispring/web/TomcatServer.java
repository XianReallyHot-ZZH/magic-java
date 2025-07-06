package com.example.magic09minispring.web;

import com.example.magic09minispring.Autowired;
import com.example.magic09minispring.Component;
import com.example.magic09minispring.PostConstruct;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.File;
import java.util.logging.LogManager;

/**
 * Tomcat 服务器
 * 这里选择交由spring管理
 */
@Component
public class TomcatServer {

    @Autowired
    private DispatcherServlet dispatcherServlet;

    /**
     * 交由spring管理，并完成Tomcat的启动
     *
     * @throws LifecycleException
     */
    @PostConstruct
    public void start() throws LifecycleException {

        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

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
        tomcat.addServlet(contextPath, "dispatcherServlet", dispatcherServlet);

        // 将helloServlet绑定到/*（所有的请求）
        context.addServletMappingDecoded("/*", "dispatcherServlet");
        // 启动tomcat服务器
        tomcat.start();
        System.out.println("Tomcat started, port:" + port);
    }

}

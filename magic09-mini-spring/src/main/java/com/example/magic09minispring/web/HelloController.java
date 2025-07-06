package com.example.magic09minispring.web;

/**
 * controller demo
 */
@Controller
@RequestMapping("/hello")
public class HelloController {

    /**
     *  访问localhost:port/hello/a，会精准的匹配到该方法，其实这就是springmvc的核心能力之一了
     * @return
     */
    @RequestMapping("/a")
    public String hello() {
        return "hello";
    }

}

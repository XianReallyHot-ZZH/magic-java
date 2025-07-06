package com.example.magic09minispring.web;

import com.example.magic09minispring.Component;

/**
 * controller demo
 */
@Component
@Controller
@RequestMapping("/hello")
public class HelloController {

    /**
     *  访问localhost:port/hello/a，会精准的匹配到该方法，其实这就是springmvc的核心能力之一了
     * @return
     */
    @RequestMapping("/a")
    public String hello() {
        return "hello xiao shuai";
    }

    /**
     * 测试带入参的请求
     *
     * @param name
     * @param age
     * @return
     */
    @RequestMapping("/b")
    public String helloB(@PathParam("name") String name, @PathParam("age") Integer age) {
        return String.format("<h1>hello world</h1> <br> name:%s, age:%s", name, age);
    }

//    /**
//     * 测试相同uri的情况
//     * @return
//     */
//    @RequestMapping("/a")
//    public String hello1() {
//        return "hello";
//    }

}

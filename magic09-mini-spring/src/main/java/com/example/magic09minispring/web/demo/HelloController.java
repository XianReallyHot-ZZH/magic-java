package com.example.magic09minispring.web.demo;

import com.example.magic09minispring.Component;
import com.example.magic09minispring.web.*;

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

    /**
     * 测试返回json
     *
     * @param name
     * @param age
     * @return
     */
    @RequestMapping("/json")
    @ResponseBody
    public User json(@PathParam("name") String name, @PathParam("age") Integer age) {
        User user = new User();
        user.setName(name);
        user.setAge(age);
        return user;
    }


    @RequestMapping("/local")
    public ModelAndView local(@PathParam("name") String name, @PathParam("age") Integer age) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("index.html");
        return modelAndView;
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

package com.example.magic09minispring.web;

import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 静态资源对象
 */
public class ModelAndView {

    // 静态资源
    @Setter
    private String view;

    private Map<String, String> context = new HashMap<>();

    public String getView() {
        return view;
    }

    public Map<String, String> getContext() {
        return context;
    }
}

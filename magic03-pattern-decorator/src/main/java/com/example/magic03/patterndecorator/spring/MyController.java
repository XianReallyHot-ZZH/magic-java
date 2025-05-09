package com.example.magic03.patterndecorator.spring;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class MyController {

    @PostMapping("/V1")
    public Map<String, Object> origin(@RequestBody Map<String, Object> json) {
//        json.put("timestamp", System.currentTimeMillis());
        return json;
    }

    @PostMapping("/V2")
    public Map<String, Object> originEnhance(@TimestampRequestBody Map<String, Object> json) {
//        json.put("timestamp", System.currentTimeMillis());
        return json;
    }
}

package com.foxandgrapes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateSession {

    @GetMapping("updateSession")
    public void updateSession() {
        // 更新session的作用
    }
}

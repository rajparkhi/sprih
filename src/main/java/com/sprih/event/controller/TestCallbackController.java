package com.sprih.event.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestCallbackController {

    @PostMapping("/test-callback")
    public void receive(@RequestBody Object body) {
        System.out.println("Callback received: " + body);
    }
}
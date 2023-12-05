package com.icepip.project;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/hello")
    public String home() {
        return "Hello, Spring cloud gateway2!";
    }

}

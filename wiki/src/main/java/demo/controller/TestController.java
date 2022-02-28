package demo.controller;

import demo.domain.Test;
import demo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    @Autowired
    private TestService testService;
    @RequestMapping("/hello")
    public String hello(){
        return "hello world";
    }

    @RequestMapping("/test/list")
    public List<Test> list(){
        return testService.list();
    }
}

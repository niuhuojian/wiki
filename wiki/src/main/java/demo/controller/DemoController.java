package demo.controller;

import demo.domain.Demo;
import demo.domain.Test;
import demo.service.DemoService;
import demo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoController {
    @Autowired
    private DemoService demoService;


    @RequestMapping("/demo/list")
    public List<Demo> list(){
        return demoService.list();
    }
}

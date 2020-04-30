package com.in.sp.controller;

import com.in.sp.bean.Person;
import com.in.sp.service.TestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ship
 * @date 2020-04-27 17:48
 */
@RequestMapping("test")
@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("")
    public void test(){
        Person person = new Person();
        person.setId(1);
        person.setName("张三");
        testService.add(person);
        System.out.println("成功");
    }
}

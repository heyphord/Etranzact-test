package com.gmfs.demo.service;

import org.springframework.stereotype.Service;

@Service
public class DemoService {

    public String reusableServiceMethod(){
        return "Hi, you can reuse me in any controller";
    }
}

package com.pinyougou.manager.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    @RequestMapping("findName")
    public Map name(){
        String name= SecurityContextHolder.getContext()
                .getAuthentication().getName();//security提供的一种上下文的环境.类似于域对象,在一定范围内存值和取值,从上下文中找个用户.再得到用户的名字
        Map map=new HashMap();
        map.put("loginName", name);
        return map ;
    }
}

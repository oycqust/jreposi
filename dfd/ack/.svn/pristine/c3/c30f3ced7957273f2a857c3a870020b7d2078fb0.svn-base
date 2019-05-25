package com.utstar.integral.controller;

import com.utstar.integral.redis.dao.RedisCommonDAO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 *
 * @author UTSC0928
 * @date 2018/5/30
 */
@Controller
@RequestMapping(value = "/demo")
public class DemoController {

    @Resource
    private RedisCommonDAO redisCommonDAO;

    @RequestMapping(value = "/test")
    @ResponseBody
    public String test(){
        System.out.println(redisCommonDAO.keys("*"));
        return "123";
    }
}

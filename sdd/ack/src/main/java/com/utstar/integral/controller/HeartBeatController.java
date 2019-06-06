package com.utstar.integral.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author UTSC0928
 * @date 2018/6/1
 */
@Controller
public class HeartBeatController {

    @ResponseBody
    @RequestMapping(value = "/heartBeat")
    public String heartBeat(){
        return "PONG";
    }
}

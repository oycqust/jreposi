package com.utstar.integral.controller;

import com.utstar.integral.Exception.CoreException;
import com.utstar.integral.Exception.LoginException;
import com.utstar.integral.bean.Constant;
import com.utstar.integral.bean.Result;
import com.utstar.integral.bean.User;
import com.utstar.integral.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * created by UTSC1244 at 2019/5/9
 */

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/login")
    public String toLogin()
    {
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public Result login(String username, String password, HttpSession session)
    {
        Result result = new Result(true);

        User user = loginService.login(username, password);
        if(user == null)
        {
            result.setSuccess(false);
            result.setMessage("用户名或密码错误!");
        }else
        {
            session.setAttribute(Constant.SESSION_USER, user);
        }
        return result;
    }

    @RequestMapping("/loginout")
    @ResponseBody
    public Result loginOut(HttpSession session)
    {
        session.invalidate();
        return new Result(true);
    }
}

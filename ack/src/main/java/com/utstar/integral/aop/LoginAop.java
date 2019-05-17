package com.utstar.integral.aop;

import com.utstar.integral.Exception.LoginException;
import com.utstar.integral.bean.Constant;
import com.utstar.integral.bean.User;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

//描述切面类
//@Aspect
@Component
public class LoginAop {

    //@Pointcut("execution(* com.utstar.integral.controller.LoginController.*(..))")
    public void loginPoint(){}

    //@Before("!loginPoint()")
    public void twiceAsOld1(){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        Object object = session.getAttribute(Constant.SESSION_USER);
        if(object == null || !(object instanceof User)
            || StringUtils.isBlank(((User) object).getUsername()))
        {
            throw new LoginException();
        }

    }
}
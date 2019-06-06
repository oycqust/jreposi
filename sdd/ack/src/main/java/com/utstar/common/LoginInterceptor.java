package com.utstar.common;

import com.utstar.integral.Exception.LoginException;
import com.utstar.integral.bean.Constant;
import com.utstar.integral.bean.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * created by UTSC1244 at 2019/5/10
 * 登录拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o) throws Exception {
        Object object = request.getSession().getAttribute(Constant.SESSION_USER);
        if(object == null || !(object instanceof User)
                || StringUtils.isBlank(((User) object).getUsername()))
        {
            throw new LoginException();
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}

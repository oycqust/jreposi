package com.utstar.integral.service;

import com.utstar.integral.bean.User;

/**
 * created by UTSC1244 at 2019/5/9 0009
 */
public interface LoginService
{
    User login(String username, String password);
}

package com.utstar.integral.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.utstar.integral.Exception.CoreException;
import com.utstar.integral.bean.Constant;
import com.utstar.integral.bean.User;
import com.utstar.integral.service.LoginService;
import com.utstar.integral.utils.Assert;
import com.utstar.integral.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * created by UTSC1244 at 2019/5/9 0009
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService
{
    @Override
    public User login(String username, String password)
    {
        Assert.isUsername(username, "用户名格式错误!");
        Assert.isPassword(password, "密码格式错误!");

        //TODO 密码加密
        String userJson = FileUtils.getFileContendFromClassPath(Constant.USER_FILE_NAME);
        Assert.isNotEmpty(userJson, "server is error!");

        JSONArray jsonArray = JSONArray.parseArray(userJson);

        try
        {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String sourceUsername = jsonObject.getString("username");
                String sourcePwd = jsonObject.getString("password");

                if (username.equals(sourceUsername) && password.equals(sourcePwd))
                {
                    User user = new User();
                    user.setUsername(username);
                    return user;
                }
            }
        }catch (Exception e)
        {
            log.error("login:fail to get user source", e);
            throw new CoreException("server is busy!");
        }
        return null;
    }
}

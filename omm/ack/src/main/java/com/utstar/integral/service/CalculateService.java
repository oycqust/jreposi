package com.utstar.integral.service;

import com.utstar.integral.bean.UserViewLog;

import java.util.List;

/**
 * created by UTSC1244 at 2019/5/17
 */
public interface CalculateService {
    boolean handleUserViewLogs(List<UserViewLog> userViewLogs);
}

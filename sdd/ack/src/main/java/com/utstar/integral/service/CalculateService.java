package com.utstar.integral.service;

import com.utstar.integral.bean.UserViewLog;
import com.utstar.integral.type.ActivityPointDelType;

import java.util.List;
import java.util.Map;

/**
 * created by UTSC1244 at 2019/5/17
 */
public interface CalculateService {
    boolean handleUserViewLogs(List<UserViewLog> userViewLogs);

    void multiIncrAndExpire(Map<String, Long> mvMap, ActivityPointDelType zeroDel);

    void storeUp2Redis(Map<String, Long> upMap, ActivityPointDelType zeroDel, long limit);

    void storeLog2Redis(Map<String, Long> upMap, String actiId);
}

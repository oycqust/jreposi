package com.utstar.integral.bean;

import com.utstar.integral.type.ActivityPointDelType;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * created by UTSC1244 at 2019/6/6 0006
 */
@Data
public class CaculateEntity {
    private ActivityEntity activityEntity;

    private Map<String, Long> mvMap;

    private Map<String, Long> upMap;
}

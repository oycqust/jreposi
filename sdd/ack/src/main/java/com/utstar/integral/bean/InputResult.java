package com.utstar.integral.bean;

import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author UTSC0928
 * @date 2018/6/4
 */
@Data
public class InputResult {

    private List<UserViewLog> userViewLogs = Collections.emptyList();

    /**
     * 只针对于数据来源是文件系统
     */
    private String cdrTime;
}

package com.utstar.config;

import lombok.Data;

import java.util.List;

/**
 *
 * @author UTSC0928
 * @date 2018/5/30
 */
@Data
public class SelfConfigReader {

    private String dataDir;

    private Integer schedulePoolSize;

    public String getDataDir() {
        return dataDir;
    }

    public void setDataDir(String dataDir) {
        this.dataDir = dataDir;
    }

    public Integer getSchedulePoolSize() {
        return schedulePoolSize;
    }

    public void setSchedulePoolSize(Integer schedulePoolSize) {
        this.schedulePoolSize = schedulePoolSize;
    }
}

package com.utstar.integral.bean;

import lombok.Data;

import java.util.Date;

/**
 * 用户观看日志的信息
 * @author UTSC0928
 * @date 2018/6/4
 */
@Data
public class UserViewLog {

    public static final String DIAN_BO = "v";

    public UserViewLog() {
    }

    public UserViewLog(String userid, String type, String mediacode, Long second, String seriesFlag, String parentobject) {
        this.userid = userid;
        this.type = type;
        this.mediacode = mediacode;
        this.second = second;
        this.seriesFlag = seriesFlag;
        this.parentobject = parentobject;
    }

    public UserViewLog(String userid, String type, String mediacode,
                       Long second, String seriesFlag,
                       String parentobject,Date viewStart,
                       Date viewEnd, String sysid) {
        this.userid = userid;
        this.type = type;
        this.mediacode = mediacode;
        this.second = second;
        this.seriesFlag = seriesFlag;
        this.parentobject = parentobject;
        this.viewStart = viewStart;
        this.viewEnd = viewEnd;
        this.sysid = sysid;
    }

    private String userid;

    private String type;

    private String mediacode;

    private Long second;

    private String seriesFlag;

    private String parentobject;

    private Date viewStart;

    private Date viewEnd;

    private String sysid;
}

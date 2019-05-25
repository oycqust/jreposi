package com.utstar.integral.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utstar.integral.type.ActivityPointDelType;
import com.utstar.integral.type.ActivityStatusType;
import com.utstar.integral.type.TransmissionMode;
import com.utstar.integral.type.ActivityDataType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * created by UTSC1244 at 2019/5/10 0010
 */
@Data
@Table(name = "ACP_POINT_CONF_TEST",schema = "BTO_C2")//
@Entity
public class ActivityEntity implements Serializable
{
    @Id
    @Column(name = "confid", unique = true)
    private Long id;

    @Column(name = "clusterid", nullable = true)
    private String clusterId;
    //活动ID
    @Column(name = "activityid", unique = true)
    private String activityId;

    //活动名称
    @Column(name = "activityname")
    private String activityName;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private ActivityStatusType statusType;

    //生效时间
    @Column(name = "validtime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validTime;

    //失效时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "expiretime")
    private Date expireTime;

    //影片来源
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "datatype")
    private ActivityDataType dataType;

    //选择影片来源后选择具体的某一分类
    @Column(name = "mediacode")
    private String mediaCode;

    //m p 影片类型
    @Column(name = "mediatype")
    private String mediaType;

    //观看时长 单位为秒
    @Column(name = "duration")
    private Integer duration;
    //积分
    @Column(name = "point")
    private long point;

    //数据传输方式
    @Column(name = "transmissionmode")
    @Enumerated(EnumType.ORDINAL)
    private TransmissionMode transmissionMode;

    @Column(name = "CREATEDATE")
    private Date createDate;

    @Column(name = "lastupdate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdate;

    @Transient
    private String sysId;

    @Transient
    private String uuid;

    @Transient
    private String createType;

    @Transient
    private String operatePerson;

    @Column(name = "LIMIT")
    private long limit;

    @Column(name = "ZERODEL")
    @Enumerated(EnumType.ORDINAL)
    private ActivityPointDelType zeroDel;

    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date operateTime;
}
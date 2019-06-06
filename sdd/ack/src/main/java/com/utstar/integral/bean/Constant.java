package com.utstar.integral.bean;

import java.util.Locale;

/**
 * created by UTSC1244 at 2019/5/10 0010
 */
public class Constant
{
    /**
     * session 中存放user的key
     */
    public static final String SESSION_USER = "user";
    /**
     * 用户账户存放文件
     */
    public static final String USER_FILE_NAME = "user.json";

    /**
     * 存放活动的key activity_username
     */
    public static final String ACTIVITY_KEY_FORMAT = "pk_activity_%s";
    public static final String IMPORT_EXCEL_FORMAT = "acp_import_%s";
    public static final String FAIL = "操作失败";
    public static final String ACTIVITY_PRIMARY_KEY = "primary_key_activity";

    /**
     * 需要计算积分的mediacode的key 模板
     * activity_${activityid}_${duration}_${point}_${isZeroDel}_${limit}_${sysid}
     */
    public static final String MEDIACODE_FROM_CONFIG_FORMAT = "activity_%s_%s_%s_%s_%s_%s";

    /**
     * 用户积分key up_${activityid}_${userid}_${sysid}
     */
    public static final String USER_POINT_FORMAT = "up_%s_%s_%s";
    /**
     * 媒资观看次数 mv_${activityid}_${mediacode}_${sysid}
     */
    public static final String MOVIE_VIEW_FORMAT = "mv_%s_%s_%s";

    /**
     * 存放某个活动下所有影片观看次数 total_viewcount_${activityid}
     */
    public static final String MEDIA_VIEW_COUNT_FORMAT = "total_viewcount_%s";
    public static final String MEDIA_TYPE_M = "m";
    public static final String MEDIA_TYPE_P = "p";

    public static final String LOG_BASEPATH = "D:/logtest";
    public static final String PK_ACTIVITY = "pk_activity";
    public static final String LOG_KEY ="log_user_point";
    public static final String LOG_SEPERATE ="_";
    public static final String VIEWLOG_LOCK = "acp_viewlog_lock";
    public static final String CACU_LOCK = "acp_cacu_lock_%s_%s";
    public static final String ACTI_ADD_LOCK = "acp_acti_add_lock_%s";

    public static final int MV_QUEUE_SIZE = 1000;
    public static final int UP_QUEUE_SIZE = 1000;
    public static final int LOG_QUEUE_SIZE = 1000;
}

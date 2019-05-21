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
    public static final String ACTIVITY_KEY_FORMAT = "activity_%s";
    public static final String IMPORT_EXCEL_FORMAT = "import_excel_%s";
    public static final String FAIL = "操作失败";
    public static final String ACTIVITY_PRIMARY_KEY = "primary_key_activity";

    /**
     * 需要计算积分的mediacode的key 模板
     * activity_${mediatype}_${activityid}_${duration}_${point}_${isZeroDel}_${limit}}
     */
    public static final String MEDIACODE_FROM_CONFIG_FORMAT = "activity_%s_%s_%s_%s_%s_%s";

    /**
     * 用户积分key up_${activityid}_${userid}
     */
    public static final String USER_POINT_FORMAT = "up_%s_%s";
    /**
     * 媒资观看次数 mv_${activityid}_${mediacode}
     */
    public static final String MOVIE_VIEW_FORMAT = "mv_%s_%s";
    public static final String MEDIA_TYPE_M = "m";
    public static final String MEDIA_TYPE_P = "p";

}

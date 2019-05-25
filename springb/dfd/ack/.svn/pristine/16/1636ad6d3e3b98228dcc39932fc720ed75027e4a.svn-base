package com.utstar.common;

import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author UTSC0928
 * @date 2018/5/31
 */
public class DateTimeUtil {

    public static final String pattern = "yyyyMMddHHmm";

    /**
     * 获取当前时间，格式为yyyyMMddHHmm
     * @return
     */
    public static String getCurrentTime(){
        return new SimpleDateFormat(pattern).format(new Date());
    }


    public static long getTime(String time){
        try {
            Date parseDate = new SimpleDateFormat(pattern).parse(time);
            return parseDate.getTime();
        } catch (ParseException e) {
            LoggerFactory.getLogger(DateTimeUtil.class).error(time+" parse error. ",e);
        }
        return 0;
    }
}

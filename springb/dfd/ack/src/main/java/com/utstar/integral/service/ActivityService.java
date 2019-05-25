package com.utstar.integral.service;

import com.utstar.integral.bean.ActivityEntity;
import com.utstar.integral.bean.TableEntity;

import java.util.List;

/**
 * created by UTSC1244 at 2019/5/10 0010
 */
public interface ActivityService
{
    /**
     * 获取所有活动
     * @param username 用户名
     * @param start 起始位置
     * @param limit 每页记录
     * @return
     */
    TableEntity list(ActivityEntity activityEntity, int start, int limit);

    /**
     * 修改活动
     * @param activityEntity 活动实体类
     * @param username 用户名
     */
    void add(ActivityEntity activityEntity, String username);

    /**
     * 修改
     * @param activityEntity 活动实体类
     * @param username 用户名
     */
    void edit(ActivityEntity activityEntity, String username);

    /**
     * 删除活动
     * @param activityEntity 活动实体
     * @param username 用户名
     */
    void del(ActivityEntity activityEntity, String username);

    void storeMediacode2Redis(List<List<String>> datas, String uuid);

    /**
     * 获取所有没有失效的活动
     * @return 活动List
     */
    List<ActivityEntity> findValidActivities();

    void saveOrSyncMediaCode2Redis(ActivityEntity activityEntity, boolean isSync);

    ActivityEntity selectByActivityId(String activityId);
}

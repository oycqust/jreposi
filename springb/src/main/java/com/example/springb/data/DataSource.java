package com.example.springb.data;

import com.example.springb.Entity.Activity;
import com.example.springb.type.Status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataSource {
    public static List<Activity> data = new ArrayList<>();
    static{
        Activity activity = new Activity();
        activity.setId("11111");
        activity.setStartTime(new Date());
        activity.setStatus(Status.EFFECTIVE);
        Activity activity1 = new Activity();
        activity1.setId("22222");
        activity1.setStartTime(new Date());
        activity1.setStatus(Status.INVALID);
        data.add(activity);
        data.add(activity1);
    }

    public static List<Activity> getData(){
        return data;
    }
}

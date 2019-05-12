package com.example.springb.Entity;

import com.example.springb.type.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Activity {
    private String id;
    private Status status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Status getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }
}

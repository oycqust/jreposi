package com.utstar.integral.bean;

public class Result {


    private boolean success;
    private String message;
    public Result(boolean success){
        this.success = success;
    }
    public static final Result SUCCESS = new Result();
    public Result()
    {
        this.success = true;
        this.message = "操作成功";
    }
    public Result(boolean success,String message){
        this.success = success;
        this.message = message;

    }

    public boolean isSuccess(){
        return success;
    }

    public void setSuccess(boolean success){
        this.success = success;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }
}
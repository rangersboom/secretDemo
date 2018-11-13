package com.sunny.demo.comm;

import java.io.Serializable;

public class Result implements Serializable {
    private boolean isSuccess = false;
    private String code;
    private String msg;
    private Object data;

    public Result() {
    }

    public Result(boolean isSuccess) {
        this.isSuccess = isSuccess;
        if (isSuccess) {
            this.msg = "成功!";
            this.code = "200";
        } else {
            this.msg = "失败!";
            this.code = "500";
        }

    }

    public Result(boolean isSuccess, Object data) {
        this.isSuccess = isSuccess;
        if (isSuccess) {
            this.msg = "成功!";
            this.code = "200";
            this.data = data;
        } else {
            this.msg = "失败!";
            this.code = "500";
        }

    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}

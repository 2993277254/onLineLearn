package com.lsfly.exception;

/**
 * Created by Administrator on 2017/9/12.
 */
public class BaseRuntimeException extends RuntimeException {
    protected RtnCode rtnCode;

    public BaseRuntimeException(String msg) {
        super(msg);
        this.rtnCode = RtnCode.ERROR;
        System.out.println("service异常的原因="+msg);
    }

    public BaseRuntimeException(String msg, RtnCode rtnCode) {
        super(msg);
        this.rtnCode = RtnCode.ERROR;
        this.rtnCode = rtnCode;
    }

    public RtnCode getRtnCode() {
        return this.rtnCode;
    }

    public void setRtnCode(RtnCode rtnCode) {
        this.rtnCode = rtnCode;
    }
}
package com.lsfly.commons;


import com.lsfly.exception.*;

import com.lsfly.exception.SecurityException;
import com.lsfly.sys.Result;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 切片方式统一处理异常返回
 * Created by changxin on 2016/5/9.
 */
@ControllerAdvice
public class MyExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(MyExceptionHandler.class);



    @ExceptionHandler(InvalidParamException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Result handleInvalidError(InvalidParamException ex) {
        Result r = new Result();
        r.setMsg(ex.getMessage());
        r.setRtnCode(ex.getRtnCode().toString());
        return r;
    }

    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Result handleInvalidError(SecurityException ex) {
        Result r = new Result();
        r.setMsg(ex.getMessage());

        if (StringUtils.isBlank(ex.getRtnCode().toString()))
            r.setRtnCode("1");
        else
            r.setRtnCode(ex.getRtnCode().toString());//ex.getRtnCode().toString()
        return r;
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Result handleSericeError(ServiceException ex) {
        Result r = new Result();
        r.setMsg(ex.getMessage());
        r.setRtnCode("1");
        return r;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Result handleUnexpectedServerError(RuntimeException ex) {
        logger.error(ex.getMessage(),ex); //同时记录详细的信息
        Result r = new Result();
        r.setMsg("发生未知错误");
        r.setDevelopMsg(ex.getMessage() == null ? ex.toString() : ex.getMessage());
        r.setRtnCode("1");
        return r;
    }


}
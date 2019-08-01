package com.lsfly.sys;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import springfox.documentation.spring.web.json.Json;

import java.io.IOException;
import java.util.ArrayList;

public class MessageConverter extends FastJsonHttpMessageConverter {
    public MessageConverter() {
    }
    @Override
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        if(obj instanceof Json) {
            super.writeInternal(obj, outputMessage);
        } else if(obj instanceof ArrayList) {
            super.writeInternal(obj, outputMessage);
        } else if(obj instanceof Result) {
            super.writeInternal(obj, outputMessage);
        } else {
            Result r = new Result();
            r.setBizData(obj);
            super.writeInternal(r, outputMessage);
        }
    }
}

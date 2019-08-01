package com.lsfly.commons;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.log4j.Logger;
import com.lsfly.controller.system.BaseController;

import java.util.Properties;

/**
 * mybatis过滤器
 * Created by huoquan on 2017/11/03
 */
@Intercepts(value = {
        @Signature(type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class})})
public class SqlInterceptor extends BaseController implements Interceptor {
    private static final Logger logger = Logger.getLogger(SqlInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try{
            Object target = invocation.getTarget();
            if (target instanceof Executor) {
                final Object[] args = invocation.getArgs();
                //获取原始的ms
                MappedStatement ms = (MappedStatement) args[0];
                String id = ms.getId();
                String commandName = ms.getSqlCommandType().name();
                if(commandName.startsWith("INSERT")||commandName.startsWith("UPDATE")||commandName.startsWith("DELETE")){
                    //
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
    public static void main(String args[]){
        System.out.println("你好\n我不好");
    }
}
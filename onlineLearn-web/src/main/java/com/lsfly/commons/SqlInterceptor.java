package com.lsfly.commons;

import com.lsfly.controller.system.BaseController;
import com.lsfly.util.ToolUtil;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Map;
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
    private final static String IS_UPDATE_VERSION = "isUpdateVersion";  //是否自动更新版本号，仅当=false时候不需要自动更新
    private final static String VERSION = "version";  //版本号字段，自动填充
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
                Object parameterObject = args[1];
                if(commandName.startsWith("INSERT")||commandName.startsWith("UPDATE")||commandName.startsWith("DELETE")){
                    try {
                        //特殊：如果是map类型修改
                        if(parameterObject instanceof Map){
                            parameterObject=((Map)parameterObject).get("record");
                        }
                    } catch (Exception e) {
                        System.out.println("======拦截失败，要手动把version插入！！");
                    }
                    //插入跟更新都要插入version值
                    Class classParameter = (Class) parameterObject.getClass();
                    Boolean isUpdateVersion=true;
                    //获取版本号字段，符合该字符串的都进行赋值
                    Field field=getClassField(classParameter,VERSION);
                    try {
                        Field field_isUpdateVersion=getClassField(classParameter,IS_UPDATE_VERSION);
                        if(field_isUpdateVersion!=null){
                            field_isUpdateVersion.setAccessible(true);
                            Object value = field_isUpdateVersion.get(parameterObject);
                            if(value!=null&&(Boolean)value==false){
                                isUpdateVersion=false; //不需要更新版本号
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("拦截失败，要手动把version插入！！");
                    }
                    if(field!=null){
                        field.setAccessible(true);
                        if(isUpdateVersion){
                            field.set(parameterObject, ToolUtil.getTimeMillis());
                        }
                    }
                }
            }
        }catch(Exception e){
            System.out.println("拦截失败，要手动把version插入！！");
        }
        return invocation.proceed();
    }

    /**
     * 这个方法，是最重要的，关键的实现在这里面
     *
     * @param aClazz
     * @param aFieldName
     * @return
     */
    private static Field getClassField(Class aClazz, String aFieldName) {
        Field[] declaredFields = aClazz.getDeclaredFields();
        for (Field field : declaredFields) {
            // 注意：这里判断的方式，是用字符串的比较。很傻瓜，但能跑。要直接返回Field。我试验中，尝试返回Class，然后用getDeclaredField(String fieldName)，但是，失败了
            if (field.getName().equalsIgnoreCase(aFieldName)) {
                return field;// define in this class
            }
        }
        Class superclass = aClazz.getSuperclass();
        if (superclass !=null) {// 简单的递归一下
            return getClassField(superclass, aFieldName);
        }
        return null;
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
package com.lsfly.commons;


import com.lsfly.controller.system.BaseController;
import com.lsfly.sys.Result;
import com.lsfly.util.ToolUtil;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * Created by huoquan on 2017/10/10.
 */
@Aspect
@Component
@SuppressWarnings("rawtypes")
public class SystemLogAspect extends BaseController {
    /**
     * 日志记录
     */
    private static final Logger LOGGER = Logger.getLogger(SystemLogAspect.class);
    /**
     * Service层切点
     */
    @Pointcut("@annotation(com.lsfly.commons.SystemLog)")
    public void serviceAspect() {
    }

    /**
     * doServiceLog:获取注解参数，记录日志. <br/>
     * @param joinPoint
     */
    @AfterReturning(value="serviceAspect()",returning = "retVal")
    public  void doServiceLog(JoinPoint joinPoint,Object retVal) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String detil="";
        try{
            if(retVal instanceof Result ){
                Result result=(Result)retVal;
                if(result!=null&& ToolUtil.isNotEmpty(result.getRtnCode())
                        &&result.getRtnCode().equals("0")){
                    detil=result.getMsg();  //获取信息
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
        try {
            //日志切片，可记录日志
/*            String moduleName =getServiceMthodModuleName(joinPoint);
            String description =getServiceMthodDescription(joinPoint);
            String ip= ServletUtils.getIpAddr();  //ip地址
            *//*String mac=ServletUtils.getMacAddress(ip); //mac地址，获取速度慢，暂时屏蔽*//*
            String computername=ServletUtils.getHostName(ip);  //电脑名称*/
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * 获取注解中description
     * @param joinPoint
     * @return
     * @throws Exception
     */
    private String getServiceMthodDescription(JoinPoint joinPoint)
            throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for(Method method : methods) {
            if(method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if(clazzs.length == arguments.length) {
                    description = method.getAnnotation(SystemLog.class).description();
                    break;
                }
            }
        }
        return description;
    }

    /**
     * 获取注解中moduleName
     * @param joinPoint
     * @return
     * @throws Exception
     */
    private String getServiceMthodModuleName(JoinPoint joinPoint)
            throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String moduleName = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    moduleName = method.getAnnotation(SystemLog.class).moduleName();
                    break;
                }
            }
        }
        return moduleName;
    }


}

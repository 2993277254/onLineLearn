package com.lsfly.shiro;

import com.lsfly.bas.model.system.SysUser;
import com.lsfly.bas.service.system.ISysUserService;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

@Service
public class UserLogoutFilter extends LogoutFilter {
    @Resource
    private ISysUserService iSysUserService;


    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        //在这里执行退出系统前需要清空的数据

    	Subject subject = getSubject(request, response);


        String redirectUrl = getRedirectUrl(request, response, subject);
        try {
//            SysUser user=(SysUser)subject.getSession(false).getAttribute("ollSysUser");
//            if (user!=null) {
//                user.setOnlineStatus("0");//设置离线
//                iSysUserService.updateByPrimaryKeySelective(user);
//            }
            //System.out.println("==========设置离线成功");
            subject.logout();
            //可以执行退出记录日志
        } catch (SessionException ise) {
           ise.printStackTrace();
        }
        issueRedirect(request, response, redirectUrl);
        //返回false表示不执行后续的过滤器，直接返回跳转到登录页面
        return false;

    }
}

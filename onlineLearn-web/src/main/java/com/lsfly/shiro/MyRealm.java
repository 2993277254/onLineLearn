package com.lsfly.shiro;

import com.lsfly.bas.model.system.SysUser;
import com.lsfly.bas.model.system.SysUserExample;
import com.lsfly.bas.model.system.ext.SysUserList;
import com.lsfly.bas.service.system.ISysUserService;
import com.lsfly.util.ServletUtils;
import com.lsfly.util.ToolUtil;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class MyRealm extends AuthorizingRealm {
    
	@Resource
    private ISysUserService iSysUserService;
    @Resource
    CacheManager cacheManager;

    private Cache<String, AtomicInteger> passwordRetryCache;

    @Autowired
    private SessionDAO sessionDAO;


    /** 
     * 验证当前登录的Subject 
     */  
    @Override  
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        //获取令牌
    	CaptchaUsernamePasswordToken token = (CaptchaUsernamePasswordToken)authcToken;
        token.setHost(ServletUtils.getIpAddr());
        System.out.println("验证当前Subject时获取到token为" + ReflectionToStringBuilder.toString(token, ToStringStyle.MULTI_LINE_STYLE));

        String principal = String.valueOf(token.getPrincipal());
        String password = new String((char[]) token.getCredentials());
        //String usercode = principal;
/*        int count=5;//默认5次允许最大输入密码错误
        try {
            String retryCountStr=PropertiesUtil.getValue("retryCount");
            if(ToolUtil.isNotEmpty(retryCountStr)){
                count= Integer.parseInt(retryCountStr);
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
        //通过数据库进行验证
//        SysUserExample example = new SysUserExample();
//        SysUserExample.Criteria criteria = example.createCriteria();
//        criteria.andUserNameEqualTo(principal);
        SysUserList sysUserList=new SysUserList();
        sysUserList.setUserName(principal);
        List<SysUserList> list=iSysUserService.list2(sysUserList);
        if(list!=null&&list.size()>0){
            SysUserList sysUser=list.get(0);
//            if("1".equals(sysUser.getIsDelete())){
//                //账号被冻结
//                throw new DisabledAccountException("当前账号不存在");
//            }
            if("1".equals(sysUser.getIsDelete())){
                //账号被冻结
                throw new DisabledAccountException("当前账号被禁用");
            }
            sysUserList.setPassWord(ToolUtil.strToMd5(password));
            //criteria.andPassWordEqualTo(ToolUtil.strToMd5(password));
            list=iSysUserService.list2(sysUserList);
            if(list!=null&&list.size()>0){  //账号密码都正确，登陆成功
//                passwordRetryCache.remove(usercode);
                //sysUser extSysuser=iSysUserService.selectByLoginId(usercode);

                setSession("ollSysUser",list.get(0));  //保存session信息
                setSession("ollUserId",list.get(0).getUid());
                list.get(0).setLaterTime(new Date());
                //list.get(0).setOnlineStatus("1");
                iSysUserService.updateByPrimaryKeySelective(list.get(0));
                //apache shiro获取所有在线用户
//                Collection<Session> sessions = sessionDAO.getActiveSessions();
//                for(Session session:sessions){
//                    String loginUsername = String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY));
//
//                    //判断是否同一个用户再同一个浏览器中登录，是就不踢除
//                    if (SecurityUtils.getSubject().getSession().getId().equals(session.getId()))
//                        break;
//                    //获得session中已经登录用户的名字
//                    if(principal.equalsIgnoreCase(loginUsername) ){ //这里的username也就是当前登录的username
//                        sessionDAO.delete(session); //这里就把session清除
//                        break;
//                    }
//                }
                //setSession("id",extSysuser.getId());  //保存session信息
                //setSession("username",extSysuser.getUserName());  //保存session信息
            }else{  //密码错误
/*                 AtomicInteger retryCount = passwordRetryCache.get(usercode);
                if (retryCount == null) {
                    retryCount = new AtomicInteger(0);
                    passwordRetryCache.put(usercode, retryCount);
                }
               if (retryCount.incrementAndGet() > count) {  //输入密码超过5次
                    //冻结账号
                    sysUser.setStatus(0);
                    iSysUserService.updateByPrimaryKey(sysUser);
                    passwordRetryCache.remove(usercode);
                    throw new ExcessiveAttemptsException("当前账号密码连续输入错误超过"+count+"次，账号已冻结，请找管理员处理");
                }*/
                throw new IncorrectCredentialsException("当前账号密码错误");
            }
        }else{
            throw new UnknownAccountException("当前账号不存在");  //用户不存在
        }


        //SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(authentication.getUserCode(), authentication.getUserCode(), getName());
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(principal, password, getName());
        return authenticationInfo;
    }  
    
      
    /** 
     * 授予角色和权限   触发操作
     * 1、代码方式：subject.hasRole(“admin”) 或 subject.isPermitted(“admin”)
	 * 2、注解方式：@RequiresRoles("admin") 或 @RequiresPermissions({"enterprise:user:view"} )
	 * 3、标签方式：[@shiro.hasPermission name = "admin"][/@shiro.hasPermission]
	 * 
	 * 本系统使用权限的概念  不使用角色
     */  
    @Override  
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals){
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
       System.out.println("本系统使用权限的概念  不使用角色");
        return authorizationInfo;
    }  
      
      
    /** 
     * 将一些数据放到ShiroSession中,以便于其它地方使用 
     *   比如Controller,使用时直接用HttpSession.getAttribute(key)就可以取到
     */  
    private void setSession(Object key, Object value){  
        Subject currentUser = SecurityUtils.getSubject();
        if(null != currentUser){  
            Session session = currentUser.getSession();
            System.out.println("Session默认超时时间为[" + session.getTimeout() + "]毫秒");  
            if(null != session){  
                session.setAttribute(key, value);  
            }  
        }  
    }

    @Override
    protected void clearCachedAuthorizationInfo(PrincipalCollection principals) {

        super.clearCachedAuthorizationInfo(principals);
    }

}

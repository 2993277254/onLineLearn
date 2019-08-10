package com.lsfly.controller.system;


import com.lsfly.bas.model.system.SysUser;
import com.lsfly.bas.model.system.SysUserRole;
import com.lsfly.commons.MyDateEditor;
import com.lsfly.util.CommUtil;
import com.lsfly.util.ReadProperties;
import com.lsfly.util.ToolUtil;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BaseController {

//
//    /**
//     * 时间适配器
//     * @param binder
//     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new MyDateEditor());
    }
//
//
//    /**
//     * 返回当前用户的id
//     * 可以获得当前登录者的ID
//     * @return
//     */
    protected String getCurrentUserId() {
        SysUser userDom= CommUtil.getSysUserLoginInfo();
        if(userDom==null){
            return null;
        }
        return userDom.getUid();
    }
//
//    /**
//     * 返回当前用户的loginId
//     * 可以获得当前登录者的账号id
//     * @return
//     */
    protected String getCurrentLoginId() {
        SysUser userDom=CommUtil.getSysUserLoginInfo();
        if(userDom==null){
            return null;
        }
        return userDom.getUserName();
    }
//
//
//    /**
//     * 返回当前用户的Name
//     * 可以获得当前登录者的name
//     * @return
//     */
    protected String getCurrentUserName() {
        SysUser userDom=CommUtil.getSysUserLoginInfo();
        if(userDom==null){
            return null;
        }
        return userDom.getNickName();
    }
//
//
//
//    /**
//     * 返回当前用户的特定用户域
//     *
//     * @return
//     */
    protected SysUser getCurrentUser() {
        //取得当前用户
        return CommUtil.getSysUserLoginInfo();
    }
//
//    /**
//     * 判断是否是超级管理员，如果不是，返回当前userid，如果是，则返回空
//     * @return
//     */
//    protected String getSuperAdminOrUserid(){
//        String superid= ReadProperties.getValue("superUserName");
//        String userid=getCurrentUser().getLoginId();
//        if(ToolUtil.isNotEmpty(superid)&&ToolUtil.isNotEmpty(userid)&superid.equals(userid)){
//            return "";
//        }
//        return  getCurrentUserId();
//    }
//
//    /**
//     * 获取当前用户的系统类型
//     * @return
//     */
//    protected String getCurrentOrganizationId() {
//        SysUserModify userDom=CommUtil.getTAdminLoginInfo();
//        if(userDom==null){
//            return null;
//        }
//        return userDom.getOrganizationId();
//    }
//
    /**
     * 判断是否是超级管理员，如果不是，返回当前roleid，如果是，则返回空
     * @return 返回权限id集合
     */
//    protected List<String> getSuperAdminOrRoleid(){
//        List<String> list=new ArrayList<String>();
//        String superid= ReadProperties.getValue("superUserName");
//        String userid=getCurrentUser().getUserName();
//        if(ToolUtil.isNotEmpty(superid)&& ToolUtil.isNotEmpty(userid)&superid.equals(userid)){
//            return list;
//        }
//        List<SysUserRole> sysUserRoleList=getCurrentUser().getSysUserRoleList();
//        if(sysUserRoleList!=null&&sysUserRoleList.size()>0){
//            for (SysUserRole sysUserRole:sysUserRoleList) {
//                list.add(sysUserRole.getRoleId());
//            }
//        }
//        return  list;
//    }
//
//    /**
//     * 获取当前用户的组织id
//     * @return
//     */
//    protected String getCurrentOrgId() {
//        SysUserModify userDom=CommUtil.getTAdminLoginInfo();
//        if(userDom==null){
//            return null;
//        }
//        if(ToolUtil.isEmpty(getSuperAdminOrUserid())){
//            //超级管理员
//            return "";
//        }else{
//            return userDom.getOrgId();
//        }
//    }



}
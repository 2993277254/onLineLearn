package com.lsfly.bas.model.system.ext;

import com.lsfly.bas.model.system.SysUserRole;
import com.lsfly.sys.PageEntity;
import com.lsfly.bas.model.system.SysUser;
import java.util.Date;
import java.util.List;

/**
 * 用作SysUser的列表查询页面的实体
 */
public class SysUserList extends SysUser{
    //统一封装分页、排序的实体
    private PageEntity page;

    //用户-角色列表
    private List<SysUserRole> sysUserRoleList;

    private String type;//判断进入后台用户管理

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<SysUserRole> getSysUserRoleList() {
        return sysUserRoleList;
    }

    public void setSysUserRoleList(List<SysUserRole> sysUserRoleList) {
        this.sysUserRoleList = sysUserRoleList;
    }

    public PageEntity getPage() {
        return page;
    }

    public void setPage(PageEntity page) {
        this.page = page;
    }

    
}

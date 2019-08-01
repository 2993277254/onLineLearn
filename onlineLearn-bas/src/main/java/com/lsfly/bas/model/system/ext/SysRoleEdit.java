package com.lsfly.bas.model.system.ext;

import com.lsfly.bas.model.system.SysRole;

import java.util.List;

/**
 * 创建SysRole的编辑实体，可增加一些编辑页面需要的实体
 * @date 2018/8/24.
 */
public class SysRoleEdit extends SysRole {
    //权限列表的id集合
    private List<String> ids;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}

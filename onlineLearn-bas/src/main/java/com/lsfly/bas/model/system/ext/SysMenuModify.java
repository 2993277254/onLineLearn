package com.lsfly.bas.model.system.ext;

import com.lsfly.bas.model.system.SysMenu;

/**
 * @author huoquan
 * @date 2018/7/20.
 */
public class SysMenuModify extends SysMenu {

    private String parentName;  //父节点名字

    private Integer sortType;  //排序类型，0 上移 1下移
    private  String roId;

    public String getRoId() {
        return roId;
    }

    public void setRoId(String roId) {
        this.roId = roId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Integer getSortType() {
        return sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }
}

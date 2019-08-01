package com.lsfly.bas.model.system.ext;

import com.lsfly.bas.model.system.SysRoleKey;

import java.util.List;
import java.util.Map;

/**
 * @author huoquan
 * @date 2018/7/20.
 */
public class SysRoleKeyModify extends SysRoleKey {
    private String menuName;//菜单名称
    private String parentId;//菜单父ID
    private String keyName;//按钮名称
    private String keyCode;//按钮code

    private List<Map> menuMap;//权限集合

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }

    public List<Map> getMenuMap() {
        return menuMap;
    }

    public void setMenuMap(List<Map> menuMap) {
        this.menuMap = menuMap;
    }
}

package com.lsfly.bas.service.system;


import com.github.pagehelper.PageInfo;
import com.lsfly.bas.model.system.SysMenu;
import com.lsfly.bas.model.system.SysMenuExample;
import com.lsfly.bas.model.system.ext.SysMenuModify;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;


/**
 *  服务类实现
 */
public interface ISysMenuService{


    SysMenu getByPrimaryKey(String id);

    /**
     * 不带分页
     */
    List<SysMenu> selectByExample(SysMenuExample example);

    /**
     * 带分页
     */
    List<SysMenu> selectByExampleWithRowbounds(SysMenuExample example, RowBounds rowBounds);

    void updateByPrimaryKey(SysMenu model);

    void updateByPrimaryKeySelective(SysMenu model);

    void deleteByPrimaryKey(String id);

    void deleteByPrimaryKeys(List<String> ids);

    void insert(SysMenu model);

    //根据role获取menu
    List<Map<String,Object>> selectMenuByRoId(SysMenuModify sysMenuModify);

    //获取单位列表
    List<SysMenu> list(SysMenu sysmenu);

    //新增/修改菜单
    SysMenu add(SysMenu sysmenu);

    //根据id获取菜单实体
    SysMenuModify info(SysMenu sysMenu);

    int delete(SysMenu sysMenu);

    int sort( List<String> ids,String parentId);

}

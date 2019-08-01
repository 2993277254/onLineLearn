package com.lsfly.bas.service.system;


import com.github.pagehelper.PageInfo;
import com.lsfly.bas.model.system.SysMenu;
import com.lsfly.bas.model.system.SysRole;
import com.lsfly.bas.model.system.SysRoleExample;
import com.lsfly.bas.model.system.ext.SysRoleList;
import com.lsfly.bas.model.system.ext.SysRoleEdit;
import org.apache.ibatis.session.RowBounds;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 *  服务类实现
 */
public interface ISysRoleService{


    SysRole getByPrimaryKey(String id);

    /**
     * 不带分页
     */
    List<SysRole> selectByExample(SysRoleExample example);

    /**
     * 带分页
     */
    List<SysRole> selectByExampleWithRowbounds(SysRoleExample example, RowBounds rowBounds);

    void updateByPrimaryKey(SysRole model);

    void updateByPrimaryKeySelective(SysRole model);

    void deleteByPrimaryKey(String id);

    void deleteByPrimaryKeys(List<String> ids);

    void insert(SysRole model);

    //查询列表分页方法
    PageInfo list(SysRoleList sysRoleList);

    //获取实体方法
    SysRoleEdit getInfo(String id);

    //保存实体方法
    int saveOrEdit(SysRoleEdit sysRoleEdit);

    //删除方法
    int delete(String[] ids);

    List<Map<String, Object>> selectMenu(HttpServletRequest request, SysMenu sysMenu);



}

package com.lsfly.bas.service.system;


import com.github.pagehelper.PageInfo;
import com.lsfly.bas.model.system.SysKey;
import com.lsfly.bas.model.system.SysRoleKey;
import com.lsfly.bas.model.system.SysRoleKeyExample;
import com.lsfly.bas.model.system.ext.SysRoleKeyList;
import com.lsfly.bas.model.system.ext.SysRoleKeyEdit;
import org.apache.ibatis.session.RowBounds;

import java.util.List;


/**
 *  服务类实现
 */
public interface ISysRoleKeyService{


    SysRoleKey getByPrimaryKey(String id);

    /**
     * 不带分页
     */
    List<SysRoleKey> selectByExample(SysRoleKeyExample example);

    /**
     * 带分页
     */
    List<SysRoleKey> selectByExampleWithRowbounds(SysRoleKeyExample example, RowBounds rowBounds);

    void updateByPrimaryKey(SysRoleKey model);

    void updateByPrimaryKeySelective(SysRoleKey model);

    void deleteByPrimaryKey(String id);

    void deleteByPrimaryKeys(List<String> ids);

    void insert(SysRoleKey model);



    //获取实体方法
    SysRoleKeyEdit getInfo(String id);

    //保存实体方法
    int saveOrEdit(SysRoleKeyEdit sysRoleKeyEdit);

    //删除方法
    int delete(String[] ids);

    //根据角色获取按钮权限
    List<SysKey> getAllKeyByRole(List<String> roleList);

}

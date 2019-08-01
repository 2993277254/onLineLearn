package com.lsfly.bas.service.system;


import com.github.pagehelper.PageInfo;
import com.lsfly.bas.model.system.SysUserRole;
import com.lsfly.bas.model.system.SysUserRoleExample;
import com.lsfly.bas.model.system.ext.SysUserRoleList;
import com.lsfly.bas.model.system.ext.SysUserRoleEdit;
import org.apache.ibatis.session.RowBounds;

import java.util.List;


/**
 *  服务类实现
 */
public interface ISysUserRoleService{


    SysUserRole getByPrimaryKey(String id);

    /**
     * 不带分页
     */
    List<SysUserRole> selectByExample(SysUserRoleExample example);

    /**
     * 带分页
     */
    List<SysUserRole> selectByExampleWithRowbounds(SysUserRoleExample example, RowBounds rowBounds);

    void updateByPrimaryKey(SysUserRole model);

    void updateByPrimaryKeySelective(SysUserRole model);

    void deleteByPrimaryKey(String id);

    void deleteByPrimaryKeys(List<String> ids);

    void insert(SysUserRole model);

}

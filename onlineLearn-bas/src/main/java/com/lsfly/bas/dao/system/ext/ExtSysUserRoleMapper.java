package com.lsfly.bas.dao.system.ext;

import com.lsfly.bas.model.system.ext.SysUserRoleList;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 如果需要写sql语句，不要再SysUserRoleMapper写，自己建一个ext文件和ext的xml文件，
 * 比如ExtSysUserRoleMapper.java和ExtSysUserRoleMapper.xml
 */
public interface ExtSysUserRoleMapper {

    List<SysUserRoleList> list(SysUserRoleList sysUserRoleList, RowBounds rowBounds);

}
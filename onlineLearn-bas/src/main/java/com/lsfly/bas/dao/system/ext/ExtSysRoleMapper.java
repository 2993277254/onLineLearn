package com.lsfly.bas.dao.system.ext;

import com.lsfly.bas.model.system.ext.SysRoleList;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 如果需要写sql语句，不要再SysRoleMapper写，自己建一个ext文件和ext的xml文件，
 * 比如ExtSysRoleMapper.java和ExtSysRoleMapper.xml
 */
public interface ExtSysRoleMapper {

    List<SysRoleList> list(SysRoleList sysRoleList, RowBounds rowBounds);

}
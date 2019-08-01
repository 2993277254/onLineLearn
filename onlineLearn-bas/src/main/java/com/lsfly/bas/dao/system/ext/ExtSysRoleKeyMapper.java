package com.lsfly.bas.dao.system.ext;

import com.lsfly.bas.model.system.SysKey;
import com.lsfly.bas.model.system.SysRoleKey;
import com.lsfly.bas.model.system.ext.SysRoleKeyList;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 如果需要写sql语句，不要再SysRoleKeyMapper写，自己建一个ext文件和ext的xml文件，
 * 比如ExtSysRoleKeyMapper.java和ExtSysRoleKeyMapper.xml
 */
public interface ExtSysRoleKeyMapper {

    List<SysKey> getAllKeyByRole(@Param("roleList") List<String> roleList);

    int insertList(@Param("list") List<SysRoleKey> list);

}
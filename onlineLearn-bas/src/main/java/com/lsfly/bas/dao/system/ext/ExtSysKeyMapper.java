package com.lsfly.bas.dao.system.ext;


import com.lsfly.bas.model.system.SysKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 如果需要写sql语句，不要再SysKeyMapper写，自己建一个ext文件和ext的xml文件，
 * 比如ExtSysKeyMapper.java和ExtSysKeyMapper.xml
 */
public interface ExtSysKeyMapper {

    List<SysKey> getAllKeyByRole(@Param("roleList") List<String> roleList);

}
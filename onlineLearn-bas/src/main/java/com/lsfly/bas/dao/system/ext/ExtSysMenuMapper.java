package com.lsfly.bas.dao.system.ext;


import com.lsfly.bas.model.system.ext.SysMenuModify;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 如果需要写sql语句，不要再SysMenuMapper写，自己建一个ext文件和ext的xml文件，
 * 比如ExtSysMenuMapper.java和ExtSysMenuMapper.xml
 */
public interface ExtSysMenuMapper {

    //根据role获取menu
    List<SysMenuModify> selectMenuByRoId(SysMenuModify sysMenuModify );

}
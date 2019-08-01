package com.lsfly.bas.dao.system.ext;


import com.lsfly.bas.model.system.ext.GenTableTreeList;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 如果需要写sql语句，不要再GenTableTreeMapper写，自己建一个ext文件和ext的xml文件，
 * 比如ExtGenTableTreeMapper.java和ExtGenTableTreeMapper.xml
 */
public interface ExtGenTableTreeMapper {

    List<GenTableTreeList> list(GenTableTreeList genTableTreeList, RowBounds rowBounds);

}
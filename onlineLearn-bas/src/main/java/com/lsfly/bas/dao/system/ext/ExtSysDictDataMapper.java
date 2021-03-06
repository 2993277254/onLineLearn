package com.lsfly.bas.dao.system.ext;

import com.lsfly.bas.model.system.ext.SysDictDataList;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 如果需要写sql语句，不要再SysDictDataMapper写，自己建一个ext文件和ext的xml文件，
 * 比如ExtSysDictDataMapper.java和ExtSysDictDataMapper.xml
 */
public interface ExtSysDictDataMapper {

    List<SysDictDataList> list(SysDictDataList sysDictDataList, RowBounds rowBounds);
    List<SysDictDataList> list(SysDictDataList sysDictDataList);
}
package com.lsfly.bas.dao.course.ext;

import com.lsfly.bas.model.course.ext.TDiscussList;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 如果需要写sql语句，不要再TDiscussMapper写，自己建一个ext文件和ext的xml文件，
 * 比如ExtTDiscussMapper.java和ExtTDiscussMapper.xml
 */
public interface ExtTDiscussMapper {

    List<TDiscussList> list(TDiscussList tDiscussList, RowBounds rowBounds);

}
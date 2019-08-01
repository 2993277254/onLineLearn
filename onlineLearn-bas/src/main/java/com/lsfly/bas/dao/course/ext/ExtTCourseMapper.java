package com.lsfly.bas.dao.course.ext;

import com.lsfly.bas.model.course.ext.TCourseList;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 如果需要写sql语句，不要再TCourseMapper写，自己建一个ext文件和ext的xml文件，
 * 比如ExtTCourseMapper.java和ExtTCourseMapper.xml
 */
public interface ExtTCourseMapper {

    List<TCourseList> list(TCourseList tCourseList, RowBounds rowBounds);

}
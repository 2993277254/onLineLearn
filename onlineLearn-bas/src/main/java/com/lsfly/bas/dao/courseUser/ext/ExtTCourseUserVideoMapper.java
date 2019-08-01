package com.lsfly.bas.dao.courseUser.ext;

import com.lsfly.bas.model.courseUser.ext.TCourseUserVideoList;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 如果需要写sql语句，不要再TCourseUserVideoMapper写，自己建一个ext文件和ext的xml文件，
 * 比如ExtTCourseUserVideoMapper.java和ExtTCourseUserVideoMapper.xml
 */
public interface ExtTCourseUserVideoMapper {

    List<TCourseUserVideoList> list(TCourseUserVideoList tCourseUserVideoList, RowBounds rowBounds);

}
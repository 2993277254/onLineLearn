package com.lsfly.bas.dao.courseUser.ext;

import com.lsfly.bas.model.courseUser.ext.TCourseUserList;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 如果需要写sql语句，不要再TCourseUserMapper写，自己建一个ext文件和ext的xml文件，
 * 比如ExtTCourseUserMapper.java和ExtTCourseUserMapper.xml
 */
public interface ExtTCourseUserMapper {

    List<TCourseUserList> list(TCourseUserList tCourseUserList, RowBounds rowBounds);
    List<TCourseUserList> list2(TCourseUserList tCourseUserList, RowBounds rowBounds);

}
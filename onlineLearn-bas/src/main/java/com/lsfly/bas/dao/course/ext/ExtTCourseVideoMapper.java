package com.lsfly.bas.dao.course.ext;

import com.lsfly.bas.model.course.TCourseVideo;
import com.lsfly.bas.model.course.ext.TCourseVideoList;
import com.lsfly.bas.model.video.TVideo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 如果需要写sql语句，不要再TCourseVideoMapper写，自己建一个ext文件和ext的xml文件，
 * 比如ExtTCourseVideoMapper.java和ExtTCourseVideoMapper.xml
 */
public interface ExtTCourseVideoMapper {

    List<TCourseVideoList> list(TCourseVideoList tCourseVideoList, RowBounds rowBounds);
    int insertList(@Param("list") List<TCourseVideo> list);
}
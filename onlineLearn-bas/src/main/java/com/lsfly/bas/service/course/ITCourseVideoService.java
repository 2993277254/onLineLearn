package com.lsfly.bas.service.course;


import com.github.pagehelper.PageInfo;
import com.lsfly.bas.model.course.TCourseVideo;
import com.lsfly.bas.model.course.TCourseVideoExample;
import com.lsfly.bas.model.course.ext.TCourseVideoList;
import com.lsfly.bas.model.course.ext.TCourseVideoEdit;
import org.apache.ibatis.session.RowBounds;

import java.util.List;


/**
 *  服务类实现
 */
public interface ITCourseVideoService{


    TCourseVideo getByPrimaryKey(String id);

    /**
     * 不带分页
     */
    List<TCourseVideo> selectByExample(TCourseVideoExample example);

    /**
     * 带分页
     */
    List<TCourseVideo> selectByExampleWithRowbounds(TCourseVideoExample example, RowBounds rowBounds);

    void updateByPrimaryKey(TCourseVideo model);

    void updateByPrimaryKeySelective(TCourseVideo model);

    void deleteByPrimaryKey(String id);

    void deleteByPrimaryKeys(List<String> ids);

    void insert(TCourseVideo model);

    //查询列表分页方法
    PageInfo list(TCourseVideoList tCourseVideoList);

    //获取实体方法
    TCourseVideoEdit getInfo(String id);

    //保存实体方法
    int saveOrEdit(TCourseVideoEdit tCourseVideoEdit);

    //删除方法
    int delete(String[] ids);

}

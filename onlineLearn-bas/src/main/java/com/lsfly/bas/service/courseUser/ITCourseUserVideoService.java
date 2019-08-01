package com.lsfly.bas.service.courseUser;


import com.github.pagehelper.PageInfo;
import com.lsfly.bas.model.courseUser.TCourseUserVideo;
import com.lsfly.bas.model.courseUser.TCourseUserVideoExample;
import com.lsfly.bas.model.courseUser.ext.TCourseUserVideoList;
import com.lsfly.bas.model.courseUser.ext.TCourseUserVideoEdit;
import org.apache.ibatis.session.RowBounds;

import java.util.List;


/**
 *  服务类实现
 */
public interface ITCourseUserVideoService{


    TCourseUserVideo getByPrimaryKey(String id);

    /**
     * 不带分页
     */
    List<TCourseUserVideo> selectByExample(TCourseUserVideoExample example);

    /**
     * 带分页
     */
    List<TCourseUserVideo> selectByExampleWithRowbounds(TCourseUserVideoExample example, RowBounds rowBounds);

    void updateByPrimaryKey(TCourseUserVideo model);

    void updateByPrimaryKeySelective(TCourseUserVideo model);

    void deleteByPrimaryKey(String id);

    void deleteByPrimaryKeys(List<String> ids);

    void insert(TCourseUserVideo model);

    //查询列表分页方法
    PageInfo list(TCourseUserVideoList tCourseUserVideoList);

    //获取实体方法
    TCourseUserVideoEdit getInfo(String id);

    //保存实体方法
    int saveOrEdit(TCourseUserVideoEdit tCourseUserVideoEdit);

    TCourseUserVideoEdit saveOrEdit2(TCourseUserVideoEdit tCourseUserVideoEdit);

    //删除方法
    int delete(String[] ids);

}

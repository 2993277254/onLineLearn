package com.lsfly.bas.service.course;


import com.github.pagehelper.PageInfo;
import com.lsfly.bas.model.course.TCourse;
import com.lsfly.bas.model.course.TCourseExample;
import com.lsfly.bas.model.course.TCourseWithBLOBs;
import com.lsfly.bas.model.course.ext.TCourseList;
import com.lsfly.bas.model.course.ext.TCourseEdit;
import org.apache.ibatis.session.RowBounds;

import java.util.List;


/**
 *  服务类实现
 */
public interface ITCourseService{


    TCourse getByPrimaryKey(String id);

    /**
     * 不带分页
     */
    List<TCourse> selectByExample(TCourseExample example);

    /**
     * 带分页
     */
    List<TCourse> selectByExampleWithRowbounds(TCourseExample example, RowBounds rowBounds);

    void updateByPrimaryKey(TCourse model);

    void updateByPrimaryKeySelective(TCourseWithBLOBs model);

    void deleteByPrimaryKey(String id);

    void deleteByPrimaryKeys(List<String> ids);

    void insert(TCourseWithBLOBs model);

    //查询列表分页方法
    PageInfo list(TCourseList tCourseList);

    //获取实体方法
    TCourseEdit getInfo(String id, String type);

    //保存实体方法
    int saveOrEdit(TCourseEdit tCourseEdit);

    //删除方法
    int delete(String[] ids);

}

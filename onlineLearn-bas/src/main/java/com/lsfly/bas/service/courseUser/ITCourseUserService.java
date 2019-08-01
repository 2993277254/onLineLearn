package com.lsfly.bas.service.courseUser;


import com.github.pagehelper.PageInfo;

import com.lsfly.bas.model.courseUser.TCourseUser;
import com.lsfly.bas.model.courseUser.TCourseUserExample;
import com.lsfly.bas.model.courseUser.ext.TCourseUserEdit;
import com.lsfly.bas.model.courseUser.ext.TCourseUserList;
import org.apache.ibatis.session.RowBounds;

import java.util.List;


/**
 *  服务类实现
 */
public interface ITCourseUserService{


    TCourseUser getByPrimaryKey(String id);

    /**
     * 不带分页
     */
    List<TCourseUser> selectByExample(TCourseUserExample example);

    /**
     * 带分页
     */
    List<TCourseUser> selectByExampleWithRowbounds(TCourseUserExample example, RowBounds rowBounds);

    void updateByPrimaryKey(TCourseUser model);

    void updateByPrimaryKeySelective(TCourseUser model);

    void deleteByPrimaryKey(String id);

    void deleteByPrimaryKeys(List<String> ids);

    void insert(TCourseUser model);

    //查询列表分页方法
    PageInfo list(TCourseUserList tCourseUserList);

    PageInfo list2(TCourseUserList tCourseUserList);

    //获取实体方法
    TCourseUserEdit getInfo(String id);

    //保存实体方法
    int saveOrEdit(TCourseUserEdit tCourseUserEdit);

    //删除方法
    int delete(String[] ids);

}

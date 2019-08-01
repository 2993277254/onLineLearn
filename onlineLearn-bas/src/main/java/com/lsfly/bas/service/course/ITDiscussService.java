package com.lsfly.bas.service.course;


import com.github.pagehelper.PageInfo;
import com.lsfly.bas.model.course.TDiscuss;
import com.lsfly.bas.model.course.TDiscussExample;
import com.lsfly.bas.model.course.ext.TDiscussList;
import com.lsfly.bas.model.course.ext.TDiscussEdit;
import org.apache.ibatis.session.RowBounds;

import java.util.List;


/**
 *  服务类实现
 */
public interface ITDiscussService{


    TDiscuss getByPrimaryKey(String id);

    /**
     * 不带分页
     */
    List<TDiscuss> selectByExample(TDiscussExample example);

    /**
     * 带分页
     */
    List<TDiscuss> selectByExampleWithRowbounds(TDiscussExample example, RowBounds rowBounds);

    void updateByPrimaryKey(TDiscuss model);

    void updateByPrimaryKeySelective(TDiscuss model);

    void deleteByPrimaryKey(String id);

    void deleteByPrimaryKeys(List<String> ids);

    void insert(TDiscuss model);

    //查询列表分页方法
    PageInfo list(TDiscussList tDiscussList);

    //获取实体方法
    TDiscussEdit getInfo(String id);

    //保存实体方法
    int saveOrEdit(TDiscussEdit tDiscussEdit);

    //删除方法
    int delete(String[] ids);

}

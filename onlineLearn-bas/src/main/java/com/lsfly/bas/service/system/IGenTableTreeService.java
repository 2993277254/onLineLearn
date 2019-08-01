package com.lsfly.bas.service.system;



import com.github.pagehelper.PageInfo;
import com.lsfly.bas.model.system.GenTableTree;
import com.lsfly.bas.model.system.GenTableTreeExample;
import com.lsfly.bas.model.system.ext.GenTableTreeEdit;
import com.lsfly.bas.model.system.ext.GenTableTreeList;
import org.apache.ibatis.session.RowBounds;

import java.util.List;


/**
 *  服务类实现
 */
public interface IGenTableTreeService{


    GenTableTree getByPrimaryKey(String id);

    /**
     * 不带分页
     */
    List<GenTableTree> selectByExample(GenTableTreeExample example);

    /**
     * 带分页
     */
    List<GenTableTree> selectByExampleWithRowbounds(GenTableTreeExample example, RowBounds rowBounds);

    void updateByPrimaryKey(GenTableTree model);

    void updateByPrimaryKeySelective(GenTableTree model);

    void deleteByPrimaryKey(String id);

    void deleteByPrimaryKeys(List<String> ids);

    void insert(GenTableTree model);

    //查询列表分页方法
    PageInfo list(GenTableTreeList genTableTreeList);

    //获取实体方法
    GenTableTreeEdit getInfo(String id);

    //保存实体方法
    int saveOrEdit(GenTableTreeEdit genTableTreeEdit);

    //删除方法
    int delete(String[] ids);

}

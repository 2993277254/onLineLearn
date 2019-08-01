package com.lsfly.bas.service.system;


import com.github.pagehelper.PageInfo;
import com.lsfly.bas.model.system.SysKey;
import com.lsfly.bas.model.system.SysKeyExample;
import org.apache.ibatis.session.RowBounds;

import java.util.List;


/**
 *  服务类实现
 */
public interface ISysKeyService{


    SysKey getByPrimaryKey(String id);

    /**
     * 不带分页
     */
    List<SysKey> selectByExample(SysKeyExample example);

    /**
     * 带分页
     */
    List<SysKey> selectByExampleWithRowbounds(SysKeyExample example, RowBounds rowBounds);

    void updateByPrimaryKey(SysKey model);

    void updateByPrimaryKeySelective(SysKey model);

    void deleteByPrimaryKey(String id);

    void deleteByPrimaryKeys(List<String> ids);

    void insert(SysKey model);

    List<SysKey> searchBtnList(String menuId);

    int saveBthInfo(SysKey syskey);

    int menuBthDel(String[] ids);


    //<editor-fold desc="个性化的接口">

    //</editor-fold>

}

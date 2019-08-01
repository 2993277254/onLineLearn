package com.lsfly.bas.service.video;


import com.github.pagehelper.PageInfo;
import com.lsfly.bas.model.video.TVideo;
import com.lsfly.bas.model.video.TVideoExample;
import com.lsfly.bas.model.video.ext.TVideoList;
import com.lsfly.bas.model.video.ext.TVideoEdit;
import org.apache.ibatis.session.RowBounds;

import java.util.List;


/**
 *  服务类实现
 */
public interface ITVideoService{


    TVideo getByPrimaryKey(String id);

    /**
     * 不带分页
     */
    List<TVideo> selectByExample(TVideoExample example);

    /**
     * 带分页
     */
    List<TVideo> selectByExampleWithRowbounds(TVideoExample example, RowBounds rowBounds);

    void updateByPrimaryKey(TVideo model);

    void updateByPrimaryKeySelective(TVideo model);

    void deleteByPrimaryKey(String id);

    void deleteByPrimaryKeys(List<String> ids);

    void insert(TVideo model);

    //查询列表分页方法
    PageInfo list(TVideoList tVideoList);

    //获取实体方法
    TVideoEdit getInfo(String id);

    //保存实体方法
    int saveOrEdit(TVideoEdit tVideoEdit);

    //删除方法
    int delete(String[] ids);

}

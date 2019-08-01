package com.lsfly.bas.service.courseUser.impl;

import com.github.pagehelper.PageInfo;
import com.lsfly.base.AbstractBaseServiceImpl;
import com.lsfly.exception.ServiceException;
import com.lsfly.sys.PageEntity;
import com.lsfly.bas.dao.courseUser.TCourseUserVideoMapper;
import com.lsfly.bas.dao.courseUser.ext.ExtTCourseUserVideoMapper;
import com.lsfly.bas.model.courseUser.TCourseUserVideo;
import com.lsfly.bas.model.courseUser.TCourseUserVideoExample;
import com.lsfly.bas.model.courseUser.ext.TCourseUserVideoList;
import com.lsfly.bas.model.courseUser.ext.TCourseUserVideoEdit;
import com.lsfly.bas.service.courseUser.ITCourseUserVideoService;
import com.lsfly.util.ToolUtil;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *  服务类实现
 */
@Service
public class TCourseUserVideoServiceImpl extends AbstractBaseServiceImpl implements ITCourseUserVideoService {
    private static final Logger logger = LoggerFactory.getLogger(TCourseUserVideoServiceImpl.class);


    @Autowired
    TCourseUserVideoMapper mapper;

    @Autowired
    ExtTCourseUserVideoMapper extTCourseUserVideoMapper;

    //<editor-fold desc="通用的接口">
    @Override
    public TCourseUserVideo getByPrimaryKey(String id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 带分页
     */
    @Override
    public List<TCourseUserVideo> selectByExample(TCourseUserVideoExample example) {
        return mapper.selectByExample(example);
    }

    /**
     * 带分页
     */
    @Override
    public List<TCourseUserVideo> selectByExampleWithRowbounds(TCourseUserVideoExample example, RowBounds rowBounds) {
        return mapper.selectByExampleWithRowbounds(example, rowBounds);
    }

    @Override
    public void updateByPrimaryKey(TCourseUserVideo model) {
/*        if (model.getUpdateDate() == null)
        model.setUpdateDate(new Date().getTime());*/
        mapper.updateByPrimaryKey(model);
    }

    @Override
    public void updateByPrimaryKeySelective(TCourseUserVideo model) {
/*        if (model.getUpdateDate() == null)
            model.setUpdateDate(new Date().getTime());*/
        mapper.updateByPrimaryKeySelective(model);
    }

    @Override
    public void deleteByPrimaryKey(String id) {
//        if(!canDel(id))
//            throw new ServiceException("id="+id+"的xx下面有xx不能删除,需要先删除所有xx才能删除");

        mapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteByPrimaryKeys(List<String> ids) {
        if (ids != null && ids.size() > 0) {
//            if(!canDel(ids))
//                throw new ServiceException("xx下面有xx不能删除,需要先删除所有xx才能删除");

            TCourseUserVideoExample example = new TCourseUserVideoExample();
            TCourseUserVideoExample.Criteria criteria = example.createCriteria();
            criteria.andUidIn(ids);
            mapper.deleteByExample(example);

        }
    }

    @Override
    public void insert(TCourseUserVideo model) {
/*        if (model.getCreateDate() == null) {
            model.setCreateDate(new Date().getTime());
        }*/
        mapper.insert(model);
    }
    

    /**
     * 分页查询列表方法
     * @param tCourseUserVideoList
     * @return
     */
    @Override
    public PageInfo list(TCourseUserVideoList tCourseUserVideoList) {
        List<TCourseUserVideoList> list=extTCourseUserVideoMapper.list(tCourseUserVideoList,
                new RowBounds(tCourseUserVideoList.getPage().getPageNum(),tCourseUserVideoList.getPage().getPageSize()));
        PageInfo pageInfo=new PageInfo(list);
        return pageInfo;
    }

    /**
     * 获取实体方法，可通过查询返回复杂实体到前台
     * 除了返回本记录实体，也可继续返回其他字段
     * @param id
     * @return
     */
    @Override
    public TCourseUserVideoEdit getInfo(String id) {
        TCourseUserVideoEdit tCourseUserVideoEdit=new TCourseUserVideoEdit();
        if(ToolUtil.isNotEmpty(id)){
            TCourseUserVideo tCourseUserVideo=mapper.selectByPrimaryKey(id);
            //字段相同时才能复制，排除个别业务字段请百度一下
            BeanUtils.copyProperties(tCourseUserVideo,tCourseUserVideoEdit);
            //此处可继续返回其他字段...
        }
        return tCourseUserVideoEdit;
    }

    /**
     * 保存方法
     * @param tCourseUserVideoEdit
     * @return
     */
    @Override
    @Transactional
    public int saveOrEdit(TCourseUserVideoEdit tCourseUserVideoEdit) {
        int n=0;
        tCourseUserVideoEdit.setTimestamp(ToolUtil.getTimeMillis());
        if(ToolUtil.isEmpty(tCourseUserVideoEdit.getUid())){
            //id为空，新增操作
            tCourseUserVideoEdit.setUid(ToolUtil.getUUID());
            tCourseUserVideoEdit.setCreateTime(new Date());
            tCourseUserVideoEdit.setCreateBy(ToolUtil.getCurrentUserId());
            tCourseUserVideoEdit.setIsDelete("0");
            n = mapper.insertSelective(tCourseUserVideoEdit);
        }else{
            //id不为空，编辑操作
            tCourseUserVideoEdit.setUpdateTime(new Date());
            tCourseUserVideoEdit.setUpdateBy(ToolUtil.getCurrentUserId());
            n = mapper.updateByPrimaryKeySelective(tCourseUserVideoEdit);
        }
        return n;
    }

    @Override
    public TCourseUserVideoEdit saveOrEdit2(TCourseUserVideoEdit tCourseUserVideoEdit) {
        int n=0;
        tCourseUserVideoEdit.setTimestamp(ToolUtil.getTimeMillis());
        if(ToolUtil.isEmpty(tCourseUserVideoEdit.getUid())){
            //id为空，新增操作
            tCourseUserVideoEdit.setUid(ToolUtil.getUUID());
            tCourseUserVideoEdit.setCreateTime(new Date());
            tCourseUserVideoEdit.setCreateBy(ToolUtil.getCurrentUserId());
            tCourseUserVideoEdit.setIsDelete("0");
            n = mapper.insertSelective(tCourseUserVideoEdit);
        }else{
            //id不为空，编辑操作
            tCourseUserVideoEdit.setUpdateTime(new Date());
            tCourseUserVideoEdit.setUpdateBy(ToolUtil.getCurrentUserId());
            n = mapper.updateByPrimaryKeySelective(tCourseUserVideoEdit);
        }
        return tCourseUserVideoEdit;
    }

    /**
     * 删除方法
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public int delete(String[] ids) {
        if(ids!=null&&ids.length>0){
            TCourseUserVideoExample sysuserExample = new TCourseUserVideoExample();
            TCourseUserVideoExample.Criteria criteria = sysuserExample.createCriteria();
            criteria.andUidIn(Arrays.asList(ids));
            TCourseUserVideo tCourseUserVideo=new TCourseUserVideo();
            tCourseUserVideo.setIsDelete("1");  //1 删除状态
            return mapper.updateByExampleSelective(tCourseUserVideo,sysuserExample);
        }
        return 0;
    }

}

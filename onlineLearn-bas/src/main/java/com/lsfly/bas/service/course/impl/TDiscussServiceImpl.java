package com.lsfly.bas.service.course.impl;

import com.github.pagehelper.PageInfo;
import com.lsfly.base.AbstractBaseServiceImpl;
import com.lsfly.exception.ServiceException;
import com.lsfly.sys.PageEntity;
import com.lsfly.bas.dao.course.TDiscussMapper;
import com.lsfly.bas.dao.course.ext.ExtTDiscussMapper;
import com.lsfly.bas.model.course.TDiscuss;
import com.lsfly.bas.model.course.TDiscussExample;
import com.lsfly.bas.model.course.ext.TDiscussList;
import com.lsfly.bas.model.course.ext.TDiscussEdit;
import com.lsfly.bas.service.course.ITDiscussService;
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
public class TDiscussServiceImpl extends AbstractBaseServiceImpl implements ITDiscussService {
    private static final Logger logger = LoggerFactory.getLogger(TDiscussServiceImpl.class);


    @Autowired
    TDiscussMapper mapper;

    @Autowired
    ExtTDiscussMapper extTDiscussMapper;

    //<editor-fold desc="通用的接口">
    @Override
    public TDiscuss getByPrimaryKey(String id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 带分页
     */
    @Override
    public List<TDiscuss> selectByExample(TDiscussExample example) {
        return mapper.selectByExample(example);
    }

    /**
     * 带分页
     */
    @Override
    public List<TDiscuss> selectByExampleWithRowbounds(TDiscussExample example, RowBounds rowBounds) {
        return mapper.selectByExampleWithRowbounds(example, rowBounds);
    }

    @Override
    public void updateByPrimaryKey(TDiscuss model) {
/*        if (model.getUpdateDate() == null)
        model.setUpdateDate(new Date().getTime());*/
        mapper.updateByPrimaryKey(model);
    }

    @Override
    public void updateByPrimaryKeySelective(TDiscuss model) {
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

            TDiscussExample example = new TDiscussExample();
            TDiscussExample.Criteria criteria = example.createCriteria();
            criteria.andUidIn(ids);
            mapper.deleteByExample(example);

        }
    }

    @Override
    public void insert(TDiscuss model) {
/*        if (model.getCreateDate() == null) {
            model.setCreateDate(new Date().getTime());
        }*/
        mapper.insert(model);
    }
    

    /**
     * 分页查询列表方法
     * @param tDiscussList
     * @return
     */
    @Override
    public PageInfo list(TDiscussList tDiscussList) {
        List<TDiscussList> list=extTDiscussMapper.list(tDiscussList,
                new RowBounds(tDiscussList.getPage().getPageNum(),tDiscussList.getPage().getPageSize()));
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
    public TDiscussEdit getInfo(String id) {
        TDiscussEdit tDiscussEdit=new TDiscussEdit();
        if(ToolUtil.isNotEmpty(id)){
            TDiscuss tDiscuss=mapper.selectByPrimaryKey(id);
            //字段相同时才能复制，排除个别业务字段请百度一下
            BeanUtils.copyProperties(tDiscuss,tDiscussEdit);
            //此处可继续返回其他字段...
        }
        return tDiscussEdit;
    }

    /**
     * 保存方法
     * @param tDiscussEdit
     * @return
     */
    @Override
    @Transactional
    public int saveOrEdit(TDiscussEdit tDiscussEdit) {
        int n=0;
        tDiscussEdit.setTimestamp(ToolUtil.getTimeMillis());
        if(ToolUtil.isEmpty(tDiscussEdit.getUid())){
            //id为空，新增操作
            tDiscussEdit.setUid(ToolUtil.getUUID());
            tDiscussEdit.setCreateTime(new Date());
            tDiscussEdit.setCreateBy(ToolUtil.getCurrentUserId());
            tDiscussEdit.setIsDelete("0");

            n = mapper.insertSelective(tDiscussEdit);
        }else{
            //id不为空，编辑操作
            tDiscussEdit.setUpdateTime(new Date());
            tDiscussEdit.setUpdateBy(ToolUtil.getCurrentUserId());
            n = mapper.updateByPrimaryKeySelective(tDiscussEdit);
        }
        return n;
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
            TDiscussExample sysuserExample = new TDiscussExample();
            TDiscussExample.Criteria criteria = sysuserExample.createCriteria();
            criteria.andUidIn(Arrays.asList(ids));
            TDiscuss tDiscuss=new TDiscuss();
            tDiscuss.setIsDelete("1");  //1 删除状态
            return mapper.updateByExampleSelective(tDiscuss,sysuserExample);
        }
        return 0;
    }

}

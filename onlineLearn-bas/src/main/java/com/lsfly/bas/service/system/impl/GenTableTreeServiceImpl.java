package com.lsfly.bas.service.system.impl;

import com.github.pagehelper.PageInfo;

import com.lsfly.bas.dao.system.GenTableTreeMapper;
import com.lsfly.bas.dao.system.ext.ExtGenTableTreeMapper;
import com.lsfly.bas.model.system.GenTableTree;
import com.lsfly.bas.model.system.GenTableTreeExample;
import com.lsfly.bas.model.system.ext.GenTableTreeEdit;
import com.lsfly.bas.model.system.ext.GenTableTreeList;
import com.lsfly.bas.service.system.IGenTableTreeService;
import com.lsfly.base.AbstractBaseServiceImpl;
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
public class GenTableTreeServiceImpl extends AbstractBaseServiceImpl implements IGenTableTreeService {
    private static final Logger logger = LoggerFactory.getLogger(GenTableTreeServiceImpl.class);


    @Autowired
    GenTableTreeMapper mapper;

    @Autowired
    ExtGenTableTreeMapper extGenTableTreeMapper;

    //<editor-fold desc="通用的接口">
    @Override
    public GenTableTree getByPrimaryKey(String id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 带分页
     */
    @Override
    public List<GenTableTree> selectByExample(GenTableTreeExample example) {
        return mapper.selectByExample(example);
    }

    /**
     * 带分页
     */
    @Override
    public List<GenTableTree> selectByExampleWithRowbounds(GenTableTreeExample example, RowBounds rowBounds) {
        return mapper.selectByExampleWithRowbounds(example, rowBounds);
    }

    @Override
    public void updateByPrimaryKey(GenTableTree model) {
/*        if (model.getUpdateDate() == null)
        model.setUpdateDate(new Date().getTime());*/
        mapper.updateByPrimaryKey(model);
    }

    @Override
    public void updateByPrimaryKeySelective(GenTableTree model) {
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

            GenTableTreeExample example = new GenTableTreeExample();
            GenTableTreeExample.Criteria criteria = example.createCriteria();
            criteria.andIdIn(ids);
            mapper.deleteByExample(example);

        }
    }

    @Override
    public void insert(GenTableTree model) {
/*        if (model.getCreateDate() == null) {
            model.setCreateDate(new Date().getTime());
        }*/
        mapper.insert(model);
    }
    

    /**
     * 分页查询列表方法
     * @param genTableTreeList
     * @return
     */
    @Override
    public PageInfo list(GenTableTreeList genTableTreeList) {
        List<GenTableTreeList> list=extGenTableTreeMapper.list(genTableTreeList,
                new RowBounds(genTableTreeList.getPage().getPageNum(),genTableTreeList.getPage().getPageSize()));
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
    public GenTableTreeEdit getInfo(String id) {
        GenTableTreeEdit genTableTreeEdit=new GenTableTreeEdit();
        if(ToolUtil.isNotEmpty(id)){
            GenTableTree genTableTree=mapper.selectByPrimaryKey(id);
            //字段相同时才能复制，排除个别业务字段请百度一下
            BeanUtils.copyProperties(genTableTree,genTableTreeEdit);
            //此处可继续返回其他字段...
        }
        return genTableTreeEdit;
    }

    /**
     * 保存方法
     * @param genTableTreeEdit
     * @return
     */
    @Override
    @Transactional
    public int saveOrEdit(GenTableTreeEdit genTableTreeEdit) {
        int n=0;
        if(ToolUtil.isEmpty(genTableTreeEdit.getId())){
            //id为空，新增操作
            genTableTreeEdit.setId(ToolUtil.getUUID());
            genTableTreeEdit.setCreateTime(new Date());
            //genTableTreeEdit.setCreateBy(ToolUtil.getCurrentUserId());
            genTableTreeEdit.setUpdateTime(new Date());
            //genTableTreeEdit.setUpdateBy(ToolUtil.getCurrentUserId());
            genTableTreeEdit.setIsDelete("0");
            n = mapper.insertSelective(genTableTreeEdit);
        }else{
            //id不为空，编辑操作
            genTableTreeEdit.setUpdateTime(new Date());
            //genTableTreeEdit.setUpdateBy(ToolUtil.getCurrentUserId());
            n = mapper.updateByPrimaryKeySelective(genTableTreeEdit);
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
            GenTableTreeExample sysuserExample = new GenTableTreeExample();
            GenTableTreeExample.Criteria criteria = sysuserExample.createCriteria();
            criteria.andIdIn(Arrays.asList(ids));
            GenTableTree genTableTree=new GenTableTree();
            genTableTree.setIsDelete("1");  //1 删除状态
            return mapper.updateByExampleSelective(genTableTree,sysuserExample);
        }
        return 0;
    }

}

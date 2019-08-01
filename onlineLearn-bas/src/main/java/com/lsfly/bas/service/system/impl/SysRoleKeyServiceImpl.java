package com.lsfly.bas.service.system.impl;

import com.github.pagehelper.PageInfo;
import com.lsfly.bas.model.system.SysKey;
import com.lsfly.base.AbstractBaseServiceImpl;
import com.lsfly.exception.ServiceException;
import com.lsfly.sys.PageEntity;
import com.lsfly.bas.dao.system.SysRoleKeyMapper;
import com.lsfly.bas.dao.system.ext.ExtSysRoleKeyMapper;
import com.lsfly.bas.model.system.SysRoleKey;
import com.lsfly.bas.model.system.SysRoleKeyExample;
import com.lsfly.bas.model.system.ext.SysRoleKeyList;
import com.lsfly.bas.model.system.ext.SysRoleKeyEdit;
import com.lsfly.bas.service.system.ISysRoleKeyService;
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
public class SysRoleKeyServiceImpl extends AbstractBaseServiceImpl implements ISysRoleKeyService {
    private static final Logger logger = LoggerFactory.getLogger(SysRoleKeyServiceImpl.class);


    @Autowired
    SysRoleKeyMapper mapper;

    @Autowired
    ExtSysRoleKeyMapper extSysRoleKeyMapper;

    //<editor-fold desc="通用的接口">
    @Override
    public SysRoleKey getByPrimaryKey(String id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 带分页
     */
    @Override
    public List<SysRoleKey> selectByExample(SysRoleKeyExample example) {
        return mapper.selectByExample(example);
    }

    /**
     * 带分页
     */
    @Override
    public List<SysRoleKey> selectByExampleWithRowbounds(SysRoleKeyExample example, RowBounds rowBounds) {
        return mapper.selectByExampleWithRowbounds(example, rowBounds);
    }

    @Override
    public void updateByPrimaryKey(SysRoleKey model) {
/*        if (model.getUpdateDate() == null)
        model.setUpdateDate(new Date().getTime());*/
        mapper.updateByPrimaryKey(model);
    }

    @Override
    public void updateByPrimaryKeySelective(SysRoleKey model) {
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

            SysRoleKeyExample example = new SysRoleKeyExample();
            SysRoleKeyExample.Criteria criteria = example.createCriteria();
            criteria.andIdIn(ids);
            mapper.deleteByExample(example);

        }
    }

    @Override
    public void insert(SysRoleKey model) {
/*        if (model.getCreateDate() == null) {
            model.setCreateDate(new Date().getTime());
        }*/
        mapper.insert(model);
    }
    



    /**
     * 获取实体方法，可通过查询返回复杂实体到前台
     * 除了返回本记录实体，也可继续返回其他字段
     * @param id
     * @return
     */
    @Override
    public SysRoleKeyEdit getInfo(String id) {
        SysRoleKeyEdit sysRoleKeyEdit=new SysRoleKeyEdit();
        if(ToolUtil.isNotEmpty(id)){
            SysRoleKey sysRoleKey=mapper.selectByPrimaryKey(id);
            //字段相同时才能复制，排除个别业务字段请百度一下
            BeanUtils.copyProperties(sysRoleKey,sysRoleKeyEdit);
            //此处可继续返回其他字段...
        }
        return sysRoleKeyEdit;
    }

    /**
     * 保存方法
     * @param sysRoleKeyEdit
     * @return
     */
    @Override
    @Transactional
    public int saveOrEdit(SysRoleKeyEdit sysRoleKeyEdit) {
        int n=0;
        if(ToolUtil.isEmpty(sysRoleKeyEdit.getId())){
            //id为空，新增操作
            sysRoleKeyEdit.setId(ToolUtil.getUUID());
            sysRoleKeyEdit.setCreateTime(new Date());
            //sysRoleKeyEdit.setCreateBy(ToolUtil.getCurrentUserId());
            sysRoleKeyEdit.setIsDelete("0");
            n = mapper.insertSelective(sysRoleKeyEdit);
        }else{
            //id不为空，编辑操作
            sysRoleKeyEdit.setUpdateTime(new Date());
            //sysRoleKeyEdit.setUpdateBy(ToolUtil.getCurrentUserId());
            n = mapper.updateByPrimaryKeySelective(sysRoleKeyEdit);
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
            SysRoleKeyExample sysuserExample = new SysRoleKeyExample();
            SysRoleKeyExample.Criteria criteria = sysuserExample.createCriteria();
            criteria.andIdIn(Arrays.asList(ids));
            SysRoleKey sysRoleKey=new SysRoleKey();
            sysRoleKey.setIsDelete("1");  //1 删除状态
            return mapper.updateByExampleSelective(sysRoleKey,sysuserExample);
        }
        return 0;
    }

    @Override
    public List<SysKey> getAllKeyByRole(List<String> roleList) {
        return  extSysRoleKeyMapper.getAllKeyByRole(roleList);
    }

}

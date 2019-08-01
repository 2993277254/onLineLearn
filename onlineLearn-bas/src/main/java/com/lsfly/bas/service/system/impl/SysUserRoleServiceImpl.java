package com.lsfly.bas.service.system.impl;

import com.github.pagehelper.PageInfo;
import com.lsfly.base.AbstractBaseServiceImpl;
import com.lsfly.exception.ServiceException;
import com.lsfly.sys.PageEntity;
import com.lsfly.bas.dao.system.SysUserRoleMapper;
import com.lsfly.bas.dao.system.ext.ExtSysUserRoleMapper;
import com.lsfly.bas.model.system.SysUserRole;
import com.lsfly.bas.model.system.SysUserRoleExample;
import com.lsfly.bas.model.system.ext.SysUserRoleList;
import com.lsfly.bas.model.system.ext.SysUserRoleEdit;
import com.lsfly.bas.service.system.ISysUserRoleService;
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
public class SysUserRoleServiceImpl extends AbstractBaseServiceImpl implements ISysUserRoleService {
    private static final Logger logger = LoggerFactory.getLogger(SysUserRoleServiceImpl.class);



    @Autowired
    SysUserRoleMapper mapper;

    //<editor-fold desc="通用的接口">
    @Override
    public SysUserRole getByPrimaryKey(String id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 带分页
     */
    @Override
    public List<SysUserRole> selectByExample(SysUserRoleExample example) {
        return mapper.selectByExample(example);
    }

    /**
     * 带分页
     */
    @Override
    public List<SysUserRole> selectByExampleWithRowbounds(SysUserRoleExample example, RowBounds rowBounds) {
        return mapper.selectByExampleWithRowbounds(example, rowBounds);
    }

    @Override
    public void updateByPrimaryKey(SysUserRole model) {
/*        if (model.getUpdateDate() == null)
        model.setUpdateDate(new Date().getTime());*/
        mapper.updateByPrimaryKey(model);
    }

    @Override
    public void updateByPrimaryKeySelective(SysUserRole model) {
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

            SysUserRoleExample example = new SysUserRoleExample();
            SysUserRoleExample.Criteria criteria = example.createCriteria();
            criteria.andIdIn(ids);
            mapper.deleteByExample(example);

        }
    }

    @Override
    public void insert(SysUserRole model) {
/*        if (model.getCreateDate() == null) {
            model.setCreateDate(new Date().getTime());
        }*/
        mapper.insert(model);
    }
    //</editor-fold>


    //<editor-fold desc="个性化的接口">


    //</editor-fold>

}

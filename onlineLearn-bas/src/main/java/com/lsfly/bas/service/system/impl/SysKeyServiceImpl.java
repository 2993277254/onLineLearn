package com.lsfly.bas.service.system.impl;

import com.github.pagehelper.PageInfo;
import com.lsfly.bas.dao.system.SysRoleKeyMapper;
import com.lsfly.bas.model.system.SysRoleKeyExample;
import com.lsfly.base.AbstractBaseServiceImpl;
import com.lsfly.exception.ServiceException;
import com.lsfly.sys.PageEntity;
import com.lsfly.bas.dao.system.SysKeyMapper;
import com.lsfly.bas.dao.system.ext.ExtSysKeyMapper;
import com.lsfly.bas.model.system.SysKey;
import com.lsfly.bas.model.system.SysKeyExample;

import com.lsfly.bas.service.system.ISysKeyService;
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
public class SysKeyServiceImpl extends AbstractBaseServiceImpl implements ISysKeyService {
    private static final Logger logger = LoggerFactory.getLogger(SysKeyServiceImpl.class);

    @Autowired
    SysKeyMapper mapper;

    @Autowired
    SysRoleKeyMapper sysRoleKeyMapper;

    //<editor-fold desc="通用的接口">
    @Override
    public SysKey getByPrimaryKey(String id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 带分页
     */
    @Override
    public List<SysKey> selectByExample(SysKeyExample example) {
        return mapper.selectByExample(example);
    }

    /**
     * 带分页
     */
    @Override
    public List<SysKey> selectByExampleWithRowbounds(SysKeyExample example, RowBounds rowBounds) {
        return mapper.selectByExampleWithRowbounds(example, rowBounds);
    }

    @Override
    public void updateByPrimaryKey(SysKey model) {
/*        if (model.getUpdateDate() == null)
        model.setUpdateDate(new Date().getTime());*/
        mapper.updateByPrimaryKey(model);
    }
    @Override
    public void updateByPrimaryKeySelective(SysKey model) {
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

            SysKeyExample example = new SysKeyExample();
            SysKeyExample.Criteria criteria = example.createCriteria();
            criteria.andIdIn(ids);
            mapper.deleteByExample(example);

        }
    }

    @Override
    public void insert(SysKey model) {
/*        if (model.getCreateDate() == null) {
            model.setCreateDate(new Date().getTime());
        }*/
        mapper.insert(model);
    }

    @Override
    public List<SysKey> searchBtnList(String menuId) {
        SysKeyExample example = new SysKeyExample();
        SysKeyExample.Criteria criteria = example.createCriteria();
        criteria.andMenuIdEqualTo(menuId);
        criteria.andIsDeleteEqualTo("0");
        example.setOrderByClause("key_code ASC"); //排序
        return mapper.selectByExample(example);
    }

    @Override
    public int saveBthInfo(SysKey syskey) {
        if(ToolUtil.isEmpty(syskey.getId())){  //新增
            syskey.setId(ToolUtil.getUUID());  //设置主键
            SysKeyExample example = new SysKeyExample();
            SysKeyExample.Criteria criteria = example.createCriteria();
            criteria.andIsDeleteNotEqualTo("1");
            criteria.andKeyCodeEqualTo(syskey.getKeyCode());
            List<SysKey> syskeyList=mapper.selectByExample(example);
            if(syskeyList!=null&&syskeyList.size()>0){
                throw new ServiceException("按钮code已经存在");
            }
            syskey.setIsDelete("0");
            //syskey.setCreateBy(ToolUtil.getCurrentUserId());
            syskey.setCreateTime(new Date());
            //syskey.setUpdateBy(ToolUtil.getCurrentUserId());
            syskey.setUpdateTime(new Date());
            return mapper.insert(syskey);
        }else{  //修改
            SysKeyExample example = new SysKeyExample();
            SysKeyExample.Criteria criteria = example.createCriteria();
            criteria.andIsDeleteNotEqualTo("1");
            criteria.andKeyCodeEqualTo(syskey.getKeyCode());
            criteria.andIdNotEqualTo(syskey.getId());
            List<SysKey> syskeyList=mapper.selectByExample(example);
            if(syskeyList!=null&&syskeyList.size()>0){
                throw new ServiceException("按钮code已经存在");
            }
            //syskey.setUpdateBy(ToolUtil.getCurrentUserId());
            syskey.setUpdateTime(new Date());
            return mapper.updateByPrimaryKeySelective(syskey);
        }
    }

    @Override
    @Transactional
    public int menuBthDel(String[] ids) {
        if(ids!=null&&ids.length>0){
            //首先删了关联表 RoleKey表
            SysRoleKeyExample example = new SysRoleKeyExample();
            SysRoleKeyExample.Criteria criteria = example.createCriteria();
            criteria.andMenuIdIn(Arrays.asList(ids));
            sysRoleKeyMapper.deleteByExample(example);
            //然后删了Key表
            SysKeyExample sysKeyExample = new SysKeyExample();
            SysKeyExample.Criteria sysKeyExampleCriteria = sysKeyExample.createCriteria();
            sysKeyExampleCriteria.andIdIn(Arrays.asList(ids));
            SysKey syskey=new SysKey();
            syskey.setIsDelete("1");  //删除状态
            return mapper.updateByExampleSelective(syskey,sysKeyExample);
        }
        return 0;
    }
    //</editor-fold>


    //<editor-fold desc="个性化的接口">


    //</editor-fold>


}

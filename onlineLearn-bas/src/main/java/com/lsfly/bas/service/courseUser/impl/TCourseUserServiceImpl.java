package com.lsfly.bas.service.courseUser.impl;

import com.github.pagehelper.PageInfo;
import com.lsfly.bas.dao.courseUser.TCourseUserMapper;
import com.lsfly.bas.dao.courseUser.ext.ExtTCourseUserMapper;
import com.lsfly.bas.model.courseUser.TCourseUser;
import com.lsfly.bas.model.courseUser.TCourseUserExample;
import com.lsfly.bas.model.courseUser.ext.TCourseUserEdit;
import com.lsfly.bas.model.courseUser.ext.TCourseUserList;
import com.lsfly.bas.service.courseUser.ITCourseUserService;
import com.lsfly.base.AbstractBaseServiceImpl;
import com.lsfly.exception.ServiceException;
import com.lsfly.sys.PageEntity;

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
public class TCourseUserServiceImpl extends AbstractBaseServiceImpl implements ITCourseUserService {
    private static final Logger logger = LoggerFactory.getLogger(TCourseUserServiceImpl.class);


    @Autowired
    TCourseUserMapper mapper;

    @Autowired
    ExtTCourseUserMapper extTCourseUserMapper;

    //<editor-fold desc="通用的接口">
    @Override
    public TCourseUser getByPrimaryKey(String id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 带分页
     */
    @Override
    public List<TCourseUser> selectByExample(TCourseUserExample example) {
        return mapper.selectByExample(example);
    }

    /**
     * 带分页
     */
    @Override
    public List<TCourseUser> selectByExampleWithRowbounds(TCourseUserExample example, RowBounds rowBounds) {
        return mapper.selectByExampleWithRowbounds(example, rowBounds);
    }

    @Override
    public void updateByPrimaryKey(TCourseUser model) {
/*        if (model.getUpdateDate() == null)
        model.setUpdateDate(new Date().getTime());*/
        mapper.updateByPrimaryKey(model);
    }

    @Override
    public void updateByPrimaryKeySelective(TCourseUser model) {
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

            TCourseUserExample example = new TCourseUserExample();
            TCourseUserExample.Criteria criteria = example.createCriteria();
            criteria.andUidIn(ids);
            mapper.deleteByExample(example);

        }
    }

    @Override
    public void insert(TCourseUser model) {
/*        if (model.getCreateDate() == null) {
            model.setCreateDate(new Date().getTime());
        }*/
        mapper.insert(model);
    }


    /**
     * 分页查询列表方法
     * @param tCourseUserList
     * @return
     */
    @Override
    public PageInfo list(TCourseUserList tCourseUserList) {
        List<TCourseUserList> list=extTCourseUserMapper.list(tCourseUserList,
                new RowBounds(tCourseUserList.getPage().getPageNum(),tCourseUserList.getPage().getPageSize()));
        PageInfo pageInfo=new PageInfo(list);
        return pageInfo;
    }

    @Override
    public PageInfo list2(TCourseUserList tCourseUserList) {
        List<TCourseUserList> list=extTCourseUserMapper.list2(tCourseUserList,
                new RowBounds(tCourseUserList.getPage().getPageNum(),tCourseUserList.getPage().getPageSize()));
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
    public TCourseUserEdit getInfo(String id) {
        TCourseUserEdit tCourseUserEdit=new TCourseUserEdit();
        if(ToolUtil.isNotEmpty(id)){
            TCourseUser tCourseUser=mapper.selectByPrimaryKey(id);
            //字段相同时才能复制，排除个别业务字段请百度一下
            BeanUtils.copyProperties(tCourseUser,tCourseUserEdit);
            //此处可继续返回其他字段...
        }
        return tCourseUserEdit;
    }

    /**
     * 保存方法
     * @param tCourseUserEdit
     * @return
     */
    @Override
    @Transactional
    public int saveOrEdit(TCourseUserEdit tCourseUserEdit) {
        int n=0;
        tCourseUserEdit.setTimestamp(ToolUtil.getTimeMillis());
        if(ToolUtil.isEmpty(tCourseUserEdit.getUid())){
            //id为空，新增操作
            tCourseUserEdit.setUid(ToolUtil.getUUID());
            tCourseUserEdit.setCreateTime(new Date());
            tCourseUserEdit.setCreateBy(ToolUtil.getCurrentUserId());
            tCourseUserEdit.setIsDelete("0");
            n = mapper.insertSelective(tCourseUserEdit);
        }else{
            //id不为空，编辑操作
            tCourseUserEdit.setUpdateTime(new Date());
            tCourseUserEdit.setUpdateBy(ToolUtil.getCurrentUserId());
            n = mapper.updateByPrimaryKeySelective(tCourseUserEdit);
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
            TCourseUserExample sysuserExample = new TCourseUserExample();
            TCourseUserExample.Criteria criteria = sysuserExample.createCriteria();
            criteria.andUidIn(Arrays.asList(ids));
            TCourseUser tCourseUser=new TCourseUser();
            tCourseUser.setIsDelete("1");  //1 删除状态
            return mapper.updateByExampleSelective(tCourseUser,sysuserExample);
        }
        return 0;
    }

}

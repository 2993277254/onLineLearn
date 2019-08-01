package com.lsfly.bas.service.course.impl;

import com.github.pagehelper.PageInfo;
import com.lsfly.base.AbstractBaseServiceImpl;
import com.lsfly.exception.ServiceException;
import com.lsfly.sys.PageEntity;
import com.lsfly.bas.dao.course.TCourseVideoMapper;
import com.lsfly.bas.dao.course.ext.ExtTCourseVideoMapper;
import com.lsfly.bas.model.course.TCourseVideo;
import com.lsfly.bas.model.course.TCourseVideoExample;
import com.lsfly.bas.model.course.ext.TCourseVideoList;
import com.lsfly.bas.model.course.ext.TCourseVideoEdit;
import com.lsfly.bas.service.course.ITCourseVideoService;
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
public class TCourseVideoServiceImpl extends AbstractBaseServiceImpl implements ITCourseVideoService {
    private static final Logger logger = LoggerFactory.getLogger(TCourseVideoServiceImpl.class);


    @Autowired
    TCourseVideoMapper mapper;

    @Autowired
    ExtTCourseVideoMapper extTCourseVideoMapper;

    //<editor-fold desc="通用的接口">
    @Override
    public TCourseVideo getByPrimaryKey(String id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 带分页
     */
    @Override
    public List<TCourseVideo> selectByExample(TCourseVideoExample example) {
        return mapper.selectByExample(example);
    }

    /**
     * 带分页
     */
    @Override
    public List<TCourseVideo> selectByExampleWithRowbounds(TCourseVideoExample example, RowBounds rowBounds) {
        return mapper.selectByExampleWithRowbounds(example, rowBounds);
    }

    @Override
    public void updateByPrimaryKey(TCourseVideo model) {
/*        if (model.getUpdateDate() == null)
        model.setUpdateDate(new Date().getTime());*/
        mapper.updateByPrimaryKey(model);
    }

    @Override
    public void updateByPrimaryKeySelective(TCourseVideo model) {
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

            TCourseVideoExample example = new TCourseVideoExample();
            TCourseVideoExample.Criteria criteria = example.createCriteria();
            criteria.andUidIn(ids);
            mapper.deleteByExample(example);

        }
    }

    @Override
    public void insert(TCourseVideo model) {
/*        if (model.getCreateDate() == null) {
            model.setCreateDate(new Date().getTime());
        }*/
        mapper.insert(model);
    }
    

    /**
     * 分页查询列表方法
     * @param tCourseVideoList
     * @return
     */
    @Override
    public PageInfo list(TCourseVideoList tCourseVideoList) {
        List<TCourseVideoList> list=extTCourseVideoMapper.list(tCourseVideoList,
                new RowBounds(tCourseVideoList.getPage().getPageNum(),tCourseVideoList.getPage().getPageSize()));
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
    public TCourseVideoEdit getInfo(String id) {
        TCourseVideoEdit tCourseVideoEdit=new TCourseVideoEdit();
        if(ToolUtil.isNotEmpty(id)){
            TCourseVideo tCourseVideo=mapper.selectByPrimaryKey(id);
            //字段相同时才能复制，排除个别业务字段请百度一下
            BeanUtils.copyProperties(tCourseVideo,tCourseVideoEdit);
            //此处可继续返回其他字段...
        }
        return tCourseVideoEdit;
    }

    /**
     * 保存方法
     * @param tCourseVideoEdit
     * @return
     */
    @Override
    @Transactional
    public int saveOrEdit(TCourseVideoEdit tCourseVideoEdit) {
        int n=0;
        if(ToolUtil.isEmpty(tCourseVideoEdit.getUid())){
            //id为空，新增操作
            tCourseVideoEdit.setUid(ToolUtil.getUUID());
            tCourseVideoEdit.setCreateTime(new Date());
            tCourseVideoEdit.setCreateBy(ToolUtil.getCurrentUserId());
            tCourseVideoEdit.setIsDelete("0");
            n = mapper.insertSelective(tCourseVideoEdit);
        }else{
            //id不为空，编辑操作
            tCourseVideoEdit.setUpdateTime(new Date());
            tCourseVideoEdit.setUpdateBy(ToolUtil.getCurrentUserId());
            n = mapper.updateByPrimaryKeySelective(tCourseVideoEdit);
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
            TCourseVideoExample sysuserExample = new TCourseVideoExample();
            TCourseVideoExample.Criteria criteria = sysuserExample.createCriteria();
            criteria.andUidIn(Arrays.asList(ids));
            TCourseVideo tCourseVideo=new TCourseVideo();
            tCourseVideo.setIsDelete("1");  //1 删除状态
            return mapper.updateByExampleSelective(tCourseVideo,sysuserExample);
        }
        return 0;
    }

}

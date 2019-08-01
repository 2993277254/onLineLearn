package com.lsfly.bas.service.video.impl;

import com.github.pagehelper.PageInfo;
import com.lsfly.bas.model.system.SysUser;
import com.lsfly.base.AbstractBaseServiceImpl;
import com.lsfly.exception.ServiceException;
import com.lsfly.sys.PageEntity;
import com.lsfly.bas.dao.video.TVideoMapper;
import com.lsfly.bas.dao.video.ext.ExtTVideoMapper;
import com.lsfly.bas.model.video.TVideo;
import com.lsfly.bas.model.video.TVideoExample;
import com.lsfly.bas.model.video.ext.TVideoList;
import com.lsfly.bas.model.video.ext.TVideoEdit;
import com.lsfly.bas.service.video.ITVideoService;
import com.lsfly.util.ToolUtil;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 *  服务类实现
 */
@Service
public class TVideoServiceImpl extends AbstractBaseServiceImpl implements ITVideoService {
    private static final Logger logger = LoggerFactory.getLogger(TVideoServiceImpl.class);


    @Autowired
    TVideoMapper mapper;

    @Autowired
    ExtTVideoMapper extTVideoMapper;

    //<editor-fold desc="通用的接口">
    @Override
    public TVideo getByPrimaryKey(String id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 带分页
     */
    @Override
    public List<TVideo> selectByExample(TVideoExample example) {
        return mapper.selectByExample(example);
    }

    /**
     * 带分页
     */
    @Override
    public List<TVideo> selectByExampleWithRowbounds(TVideoExample example, RowBounds rowBounds) {
        return mapper.selectByExampleWithRowbounds(example, rowBounds);
    }

    @Override
    public void updateByPrimaryKey(TVideo model) {
/*        if (model.getUpdateDate() == null)
        model.setUpdateDate(new Date().getTime());*/
        mapper.updateByPrimaryKey(model);
    }

    @Override
    public void updateByPrimaryKeySelective(TVideo model) {
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

            TVideoExample example = new TVideoExample();
            TVideoExample.Criteria criteria = example.createCriteria();
            criteria.andUidIn(ids);
            mapper.deleteByExample(example);

        }
    }

    @Override
    public void insert(TVideo model) {
/*        if (model.getCreateDate() == null) {
            model.setCreateDate(new Date().getTime());
        }*/
        mapper.insert(model);
    }
    

    /**
     * 分页查询列表方法
     * @param tVideoList
     * @return
     */
    @Override
    public PageInfo list(TVideoList tVideoList) {
        List<TVideoList> list=extTVideoMapper.list(tVideoList,
                new RowBounds(tVideoList.getPage().getPageNum(),tVideoList.getPage().getPageSize()));
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
    public TVideoEdit getInfo(String id) {
        TVideoEdit tVideoEdit=new TVideoEdit();
        if(ToolUtil.isNotEmpty(id)){
            TVideo tVideo=mapper.selectByPrimaryKey(id);
            //字段相同时才能复制，排除个别业务字段请百度一下
            BeanUtils.copyProperties(tVideo,tVideoEdit);
            //此处可继续返回其他字段...
        }
        return tVideoEdit;
    }

    /**
     * 保存方法
     * @param tVideoEdit
     * @return
     */
    @Override
    @Transactional
    public int saveOrEdit(TVideoEdit tVideoEdit) {
        int n=0;
        if(ToolUtil.isEmpty(tVideoEdit.getUid())){
            //id为空，新增操作
            List<TVideo> list = new ArrayList<TVideo>();//建一个list存储所有信息，批量插入
            //遍历videoList获取单个视频信息
            for (Map<String,String> object:tVideoEdit.getVideoList()) {
                //id为空，新增操作
                TVideo tVideoStorehouse=new TVideo();
                tVideoStorehouse.setVideoName(object.get("name"));//视频名称
                tVideoStorehouse.setVideoPath(object.get("path"));//视频路径
                tVideoStorehouse.setVideoSize(object.get("size"));//视频大小
                tVideoStorehouse.setVideoFormat(object.get("ext"));//视频格式
                tVideoStorehouse.setVideoTime(object.get("time"));//视频时长
                tVideoStorehouse.setVideoCover(object.get("videoCover"));//视频封面
                tVideoStorehouse.setUid(ToolUtil.getUUID());
                tVideoStorehouse.setCreateTime(new Date());
                //SysUser user=(SysUser)ToolUtil.getCurrentUser();
                String userId=ToolUtil.getCurrentUserId();
                if (ToolUtil.isEmpty(userId)){
                    userId="admin";
                }
                tVideoStorehouse.setCreateBy(userId);
                tVideoStorehouse.setIsDelete("0");
                tVideoStorehouse.setTimestamp(ToolUtil.getTimeMillis());
                tVideoStorehouse.setVersion(ToolUtil.getTimeMillis());
                list.add(tVideoStorehouse);
            }
            n=extTVideoMapper.insertList(list);

//            tVideoEdit.setUid(ToolUtil.getUUID());
//            tVideoEdit.setCreateTime(new Date());
//            tVideoEdit.setCreateBy(ToolUtil.getCurrentUserId());
//            tVideoEdit.setIsDelete("0");
//            n = mapper.insertSelective(tVideoEdit);
        }else{
            //id不为空，编辑操作
            tVideoEdit.setUpdateTime(new Date());
            //tVideoEdit.setUpdateBy(ToolUtil.getCurrentUserId());
            n = mapper.updateByPrimaryKeySelective(tVideoEdit);
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
            TVideoExample sysuserExample = new TVideoExample();
            TVideoExample.Criteria criteria = sysuserExample.createCriteria();
            criteria.andUidIn(Arrays.asList(ids));
            TVideo tVideo=new TVideo();
            tVideo.setIsDelete("1");  //1 删除状态
            return mapper.updateByExampleSelective(tVideo,sysuserExample);
        }
        return 0;
    }

}

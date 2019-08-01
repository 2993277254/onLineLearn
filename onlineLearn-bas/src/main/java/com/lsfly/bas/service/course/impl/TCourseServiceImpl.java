package com.lsfly.bas.service.course.impl;

import com.github.pagehelper.PageInfo;
import com.lsfly.bas.dao.course.TCourseVideoMapper;
import com.lsfly.bas.dao.course.ext.ExtTCourseVideoMapper;
import com.lsfly.bas.dao.courseUser.TCourseUserMapper;
import com.lsfly.bas.dao.courseUser.TCourseUserVideoMapper;
import com.lsfly.bas.dao.courseUser.ext.ExtTCourseUserMapper;
import com.lsfly.bas.dao.video.TVideoMapper;
import com.lsfly.bas.dao.video.ext.ExtTVideoMapper;
import com.lsfly.bas.model.course.*;
import com.lsfly.bas.model.courseUser.TCourseUser;
import com.lsfly.bas.model.courseUser.TCourseUserExample;
import com.lsfly.bas.model.courseUser.TCourseUserVideo;
import com.lsfly.bas.model.courseUser.TCourseUserVideoExample;
import com.lsfly.bas.model.system.SysUser;
import com.lsfly.bas.model.video.TVideo;
import com.lsfly.bas.model.video.ext.TVideoEdit;
import com.lsfly.bas.model.video.ext.TVideoList;
import com.lsfly.base.AbstractBaseServiceImpl;
import com.lsfly.exception.ServiceException;
import com.lsfly.sys.PageEntity;
import com.lsfly.bas.dao.course.TCourseMapper;
import com.lsfly.bas.dao.course.ext.ExtTCourseMapper;
import com.lsfly.bas.model.course.ext.TCourseList;
import com.lsfly.bas.model.course.ext.TCourseEdit;
import com.lsfly.bas.service.course.ITCourseService;
import com.lsfly.util.ToolUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 服务类实现
 */
@Service
public class TCourseServiceImpl extends AbstractBaseServiceImpl implements ITCourseService {
    private static final Logger logger = LoggerFactory.getLogger(TCourseServiceImpl.class);


    @Autowired
    TCourseMapper mapper;

    @Autowired
    ExtTCourseMapper extTCourseMapper;

    @Autowired
    TCourseUserMapper courseUserMapper;

    @Autowired
    TCourseUserVideoMapper courseUserVideoMapper;

    @Autowired
    ExtTVideoMapper extTVideoMapper;

    @Autowired
    TCourseVideoMapper tCourseVideoMapper;

    @Autowired
    TVideoMapper tVideoMapper;

    @Autowired
    ExtTCourseVideoMapper extTCourseVideoMapper;

    //<editor-fold desc="通用的接口">
    @Override
    public TCourse getByPrimaryKey(String id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 带分页
     */
    @Override
    public List<TCourse> selectByExample(TCourseExample example) {
        return mapper.selectByExample(example);
    }

    /**
     * 带分页
     */
    @Override
    public List<TCourse> selectByExampleWithRowbounds(TCourseExample example, RowBounds rowBounds) {
        return mapper.selectByExampleWithRowbounds(example, rowBounds);
    }

    @Override
    public void updateByPrimaryKey(TCourse model) {
/*        if (model.getUpdateDate() == null)
        model.setUpdateDate(new Date().getTime());*/
        mapper.updateByPrimaryKey(model);
    }

    @Override
    public void updateByPrimaryKeySelective(TCourseWithBLOBs model) {
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

            TCourseExample example = new TCourseExample();
            TCourseExample.Criteria criteria = example.createCriteria();
            criteria.andUidIn(ids);
            mapper.deleteByExample(example);

        }
    }

    @Override
    public void insert(TCourseWithBLOBs model) {
/*        if (model.getCreateDate() == null) {
            model.setCreateDate(new Date().getTime());
        }*/
        mapper.insert(model);
    }


    /**
     * 分页查询列表方法
     *
     * @param tCourseList
     * @return
     */
    @Override
    public PageInfo list(TCourseList tCourseList) {
        List<TCourseList> list = extTCourseMapper.list(tCourseList,
                new RowBounds(tCourseList.getPage().getPageNum(), tCourseList.getPage().getPageSize()));
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 获取实体方法，可通过查询返回复杂实体到前台
     * 除了返回本记录实体，也可继续返回其他字段
     *
     * @param id
     * @return
     */
    @Override
    public TCourseEdit getInfo(String id, String type) {
        TCourseEdit tCourseEdit = new TCourseEdit();
        if (ToolUtil.isNotEmpty(id)) {
            TCourse tCourse = mapper.selectByPrimaryKey(id);
            //字段相同时才能复制，排除个别业务字段请百度一下
            BeanUtils.copyProperties(tCourse, tCourseEdit);
            //此处可继续返回其他字段...
            if (ToolUtil.isNotEmpty(type)&&type.equals("1")) {
                if (ToolUtil.isNotEmpty(ToolUtil.getCurrentUserId())) {
                    if (tCourse.getTeacherId().equals(ToolUtil.getCurrentUserId())) {
                        //如果是当前开设的课程，提示不能学习，因为是老师
                        tCourseEdit.setIsHas("1");
                        tCourseEdit.setIsStudy("2");
                    } else{
                        //用户存在，查找用户与课程的关系
                        TCourseUserExample example = new TCourseUserExample();
                    TCourseUserExample.Criteria criteria = example.createCriteria();
                    criteria.andCourseIdEqualTo(id);
                    criteria.andUserIdEqualTo(ToolUtil.getCurrentUserId());
                    criteria.andIsDeleteNotEqualTo("1");
                    List<TCourseUser> list = courseUserMapper.selectByExample(example);
                    if (list != null && list.size() > 0) {
                        //表示有关系
                        tCourseEdit.setIsHas("1");
                        //判断是否已经学习了,查看视频与课程与用户表是否与记录
                        TCourseUserVideoExample tCourseUserVideoExample = new TCourseUserVideoExample();
                        TCourseUserVideoExample.Criteria criteria1 = tCourseUserVideoExample.createCriteria();
                        criteria1.andUserIdEqualTo(ToolUtil.getCurrentUserId());
                        criteria1.andCourseIdEqualTo(id);
                        criteria1.andIsDeleteNotEqualTo("1");
                        List<TCourseUserVideo> list1 = courseUserVideoMapper.selectByExample(tCourseUserVideoExample);
                        if (list1 != null && list1.size() > 0) {
                            //表示已有观看视频
                            tCourseEdit.setIsStudy("1");
                        }

                    }
                }
            }

            }
        }
        return tCourseEdit;
    }

    /**
     * 保存方法
     *
     * @param tCourseEdit
     * @return
     */
    @Override
    @Transactional
    public int saveOrEdit(TCourseEdit tCourseEdit) {
        int n = 0;
        tCourseEdit.setTimestamp(ToolUtil.getTimeMillis());
        if (ToolUtil.isEmpty(tCourseEdit.getUid())) {
            //id为空，新增操作
            //插入之前先判断是否有该课程

            if (ToolUtil.isNotEmpty(tCourseEdit.getType())&&ToolUtil.isNotEmpty(tCourseEdit.getName())){
                TCourseExample example = new TCourseExample();
                TCourseExample.Criteria criteria = example.createCriteria();
                criteria.andIsDeleteNotEqualTo("1");
                criteria.andNameEqualTo(tCourseEdit.getName());
                List<TCourse> list=mapper.selectByExample(example);
                if (list!=null&&list.size()>0){
                    throw new ServiceException("该类型的课程名称已存在");
                }
            }

            tCourseEdit.setUid(ToolUtil.getUUID());
            tCourseEdit.setCreateTime(new Date());
            tCourseEdit.setCreateBy(ToolUtil.getCurrentUserId());
            tCourseEdit.setIsDelete("0");
            tCourseEdit.setStatus("1");//插入的课程都是未审核
            tCourseEdit.setPersonNum(0);
            tCourseEdit.setTeacherId(ToolUtil.getCurrentUserId());
            n = mapper.insertSelective(tCourseEdit);
        } else {
            //id不为空，编辑操作
            if (ToolUtil.isNotEmpty(tCourseEdit.getType())&&ToolUtil.isNotEmpty(tCourseEdit.getName())){
                TCourseExample example = new TCourseExample();
                TCourseExample.Criteria criteria = example.createCriteria();
                criteria.andIsDeleteNotEqualTo("1");
                criteria.andNameEqualTo(tCourseEdit.getName());
                criteria.andUidNotEqualTo(tCourseEdit.getUid());//匹配自己以外的课程
                List<TCourse> list=mapper.selectByExample(example);
                if (list!=null&&list.size()>0){
                    throw new ServiceException("该类型的课程名称已存在");
                }
            }
//            TCourseExample example = new TCourseExample();
//            TCourseExample.Criteria criteria = example.createCriteria();
//            criteria.andIsDeleteNotEqualTo("1");
//            criteria.andTypeEqualTo(tCourseEdit.getType());
//            criteria.andNameEqualTo(tCourseEdit.getName());
//
//            List<TCourse> list=mapper.selectByExample(example);
//            if (list!=null&&list.size()>0){
//                throw new ServiceException("该类型的课程名称已存在");
//            }
            tCourseEdit.setUpdateTime(new Date());
            tCourseEdit.setUpdateBy(ToolUtil.getCurrentUserId());
            n = mapper.updateByPrimaryKeySelective(tCourseEdit);
        }

        //如果是提交视频的信息，那就插入课程视频表并关联
        if (ToolUtil.isNotEmpty(tCourseEdit.getIsInsertVideo())){
            List<TVideo> list = new ArrayList<TVideo>();//建一个list存储所有信息，批量插入
            //保存视频信息的id,用于关联课程视频表
            List<String> ids=new ArrayList<String>();
            //保存插入课程视频表的数据
            List<TCourseVideo> list2 = new ArrayList<TCourseVideo>();
            //解析课程大纲的视频数据
            JSONArray jsonArray = JSONArray.fromObject(tCourseEdit.getOutline());
            for (Object object:jsonArray){
                JSONObject jsonObject=JSONObject.fromObject(object);
                JSONArray jsonArray2=(JSONArray)jsonObject.get("courseHouse");
                for (Object object2:jsonArray2){
                    //System.out.println(object2.toString());
                    //解析课时
                    JSONObject jsonObject2=JSONObject.fromObject(object2);
                    //解析课时的视频数据
                    JSONObject jsonObject3=JSONObject.fromObject(jsonObject2.get("video"));
//                    System.out.println(jsonObject3.get("path"));
                    ids.add(jsonObject3.get("uid").toString());
                    //如果视频的uid存在，不必插入
                    TVideo tVide1=tVideoMapper.selectByPrimaryKey(jsonObject3.get("uid").toString());
                    if (tVide1==null){
                        TVideo tVideo = new TVideo();
                        tVideo.setUid(jsonObject3.get("uid").toString());
                        tVideo.setVideoPath(jsonObject3.get("path").toString());
                        tVideo.setVideoName(jsonObject3.get("fileName").toString());
                        tVideo.setVideoSize(jsonObject3.get("size").toString());
                        tVideo.setVideoTime(jsonObject3.get("time").toString());
                        tVideo.setTotalTime(jsonObject3.get("lstime").toString());
                        tVideo.setVideoFormat(jsonObject3.get("ext").toString());
                        tVideo.setTimestamp(ToolUtil.getTimeMillis());
                        tVideo.setIsDelete("0");
                        tVideo.setCreateBy(ToolUtil.getCurrentUserId());
                        tVideo.setCreateTime(new Date());
                        list.add(tVideo);
                    }
                }
            }
            //把课时的视频数据插入视频表
            if (list!=null&&list.size()>0){
                int n2=extTVideoMapper.insertList(list);
                if (n2>0){
                    System.out.println("插入大纲视频到视频表成功");
                }
            }
            //先删除课程视频的当前课程数据
            TCourseVideoExample example = new TCourseVideoExample();
            TCourseVideoExample.Criteria criteria = example.createCriteria();
            criteria.andIsDeleteNotEqualTo("1");
            criteria.andCourseIdEqualTo(tCourseEdit.getUid());//匹配课程id
            List<TCourseVideo> list1=tCourseVideoMapper.selectByExample(example);
            if (list1!=null&&list1.size()>0){
                List<String> ids2=new ArrayList<String>();
                for (TCourseVideo tCourseVideo:list1){
                    ids2.add(tCourseVideo.getUid());
                }
                TCourseVideoExample example2 = new TCourseVideoExample();
                TCourseVideoExample.Criteria criteria2 = example2.createCriteria();
                criteria2.andUidIn(ids2);
                TCourseVideo tCourseVideo=new TCourseVideo();
                tCourseVideo.setIsDelete("1");//设置删除
                tCourseVideoMapper.updateByExampleSelective(tCourseVideo,example2);
            }
            //重新关联添加课程视频表的数据
            for (String id:ids){
                TCourseVideo tCourseVideo=new TCourseVideo();
                tCourseVideo.setUid(ToolUtil.getUUID());
                tCourseVideo.setCourseId(tCourseEdit.getUid());
                tCourseVideo.setVideoId(id);
                tCourseVideo.setTimestamp(ToolUtil.getTimeMillis());
                tCourseVideo.setIsDelete("0");
                tCourseVideo.setCreateBy(ToolUtil.getCurrentUserId());
                tCourseVideo.setCreateTime(new Date());
                list2.add(tCourseVideo);
            }
            //插入课程视频表
            if(list2!=null&&list2.size()>0){
                extTCourseVideoMapper.insertList(list2);
            }

        }
        return n;
    }

    /**
     * 删除方法
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public int delete(String[] ids) {
        if (ids != null && ids.length > 0) {
            TCourseExample sysuserExample = new TCourseExample();
            TCourseExample.Criteria criteria = sysuserExample.createCriteria();
            criteria.andUidIn(Arrays.asList(ids));
            TCourseWithBLOBs tCourse = new TCourseWithBLOBs();
            tCourse.setIsDelete("1");  //1 删除状态
            return mapper.updateByExampleSelective(tCourse, sysuserExample);
        }
        return 0;
    }

}

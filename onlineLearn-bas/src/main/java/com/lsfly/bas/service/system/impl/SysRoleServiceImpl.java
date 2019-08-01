package com.lsfly.bas.service.system.impl;

import com.github.pagehelper.PageInfo;
import com.lsfly.bas.dao.system.*;
import com.lsfly.bas.dao.system.ext.ExtSysRoleKeyMapper;
import com.lsfly.bas.model.system.*;
import com.lsfly.base.AbstractBaseServiceImpl;
import com.lsfly.exception.ServiceException;
import com.lsfly.sys.PageEntity;
import com.lsfly.bas.dao.system.ext.ExtSysRoleMapper;
import com.lsfly.bas.model.system.ext.SysRoleList;
import com.lsfly.bas.model.system.ext.SysRoleEdit;
import com.lsfly.bas.service.system.ISysRoleService;
import com.lsfly.util.ToolUtil;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 *  服务类实现
 */
@Service
public class SysRoleServiceImpl extends AbstractBaseServiceImpl implements ISysRoleService {
    private static final Logger logger = LoggerFactory.getLogger(SysRoleServiceImpl.class);


    @Autowired
    SysRoleMapper mapper;

    @Autowired
    ExtSysRoleMapper extSysRoleMapper;

    @Autowired
    SysMenuMapper sysMenuMapper;

    @Autowired
    SysKeyMapper sysKeyMapper;

    @Autowired
    SysRoleKeyMapper sysRoleKeyMapper;

    @Autowired
    ExtSysRoleKeyMapper extSysRoleKeyMapper;

    @Autowired
    SysUserRoleMapper sysUserRoleMapper;

    //<editor-fold desc="通用的接口">
    @Override
    public SysRole getByPrimaryKey(String id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 带分页
     */
    @Override
    public List<SysRole> selectByExample(SysRoleExample example) {
        return mapper.selectByExample(example);
    }

    /**
     * 带分页
     */
    @Override
    public List<SysRole> selectByExampleWithRowbounds(SysRoleExample example, RowBounds rowBounds) {
        return mapper.selectByExampleWithRowbounds(example, rowBounds);
    }

    @Override
    public void updateByPrimaryKey(SysRole model) {
/*        if (model.getUpdateDate() == null)
        model.setUpdateDate(new Date().getTime());*/
        mapper.updateByPrimaryKey(model);
    }

    @Override
    public void updateByPrimaryKeySelective(SysRole model) {
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

            SysRoleExample example = new SysRoleExample();
            SysRoleExample.Criteria criteria = example.createCriteria();
            criteria.andIdIn(ids);
            mapper.deleteByExample(example);

        }
    }

    @Override
    public void insert(SysRole model) {
/*        if (model.getCreateDate() == null) {
            model.setCreateDate(new Date().getTime());
        }*/
        mapper.insert(model);
    }
    

    /**
     * 分页查询列表方法
     * @param sysRoleList
     * @return
     */
    @Override
    public PageInfo list(SysRoleList sysRoleList) {
        List<SysRoleList> list=extSysRoleMapper.list(sysRoleList,
                new RowBounds(sysRoleList.getPage().getPageNum(),sysRoleList.getPage().getPageSize()));
        PageInfo pageInfo=new PageInfo(list);
        return pageInfo;
    }

    /**
     * 查询菜单和按钮的节点树
     * @param request
     * @param sysMenu
     * @return
     */
    @Override
    public List<Map<String, Object>> selectMenu(HttpServletRequest request, SysMenu sysMenu) {
        //获取删除状态为正常的所有菜单
        SysMenuExample example = new SysMenuExample();
        SysMenuExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteNotEqualTo("1");
        criteria.andStatusEqualTo("1");
        //排序倒序，取第一条，即最大序号
        example.setOrderByClause("menu_sort ASC");
        List<SysMenu> sysMenuList=sysMenuMapper.selectByExample(example);

        //将菜单list转换为Map<String, Object>类型的list
        List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
        for (SysMenu sysMenu1:sysMenuList){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id",sysMenu1.getId());
            map.put("parentId",sysMenu1.getParentId());
            map.put("menuName",sysMenu1.getMenuName());
            list.add(map);
        }
        //获取按钮表的数据
//        SysKeyExample keyExample = new SysKeyExample();
//        SysKeyExample.Criteria criteria1 = keyExample.createCriteria();
//        criteria1.andIsDeleteNotEqualTo("1");
//        keyExample.setOrderByClause("key_sort,key_code ASC");
//        List<SysKey> sysKeyList = sysKeyMapper.selectByExample(keyExample);

        //将按钮list转换为Map<String,Object>类型的list
//        List<Map<String,Object>> list1 = new ArrayList<Map<String,Object>>();
//        for (SysKey sysKey:sysKeyList){
//            //判断有menuid才增加
//            Boolean haveMenu=false;
//            for (SysMenu sysMenu1:sysMenuList){
//                if(ToolUtil.isNotEmpty(sysMenu1.getId())
//                        &&ToolUtil.isNotEmpty(sysKey.getMenuId())
//                        &&sysMenu1.getId().equals(sysKey.getMenuId())){
//                    haveMenu=true;
//                    break;
//                }
//            }
//            if(!haveMenu){
//                continue;
//            }
//            Map<String, Object> map2 = new HashMap<String, Object>();
//            map2.put("id",sysKey.getId());
//            map2.put("parentId",sysKey.getMenuId());
//            map2.put("menuName",sysKey.getKeyName());
//            list1.add(map2);
//        }
//        //将菜单和按钮的List合并
//        list.addAll(list1);
        return list;
    }

    /**
     * 获取实体
     * @param id
     * @return
     */
    @Override
    public SysRoleEdit getInfo(String id) {
        SysRoleEdit sysRoleEdit=new SysRoleEdit();
        if(ToolUtil.isNotEmpty(id)){
            SysRole sysRole=mapper.selectByPrimaryKey(id);
            //字段相同时才能复制，排除个别业务字段请百度一下
            BeanUtils.copyProperties(sysRole,sysRoleEdit);
            //获取权限列表
            SysRoleKeyExample example = new SysRoleKeyExample();
            SysRoleKeyExample.Criteria criteria = example.createCriteria();
            criteria.andRoleIdEqualTo(id);
            criteria.andIsDeleteNotEqualTo("1");
            List<SysRoleKey> list =sysRoleKeyMapper.selectByExample(example);
            List<String> ids=new ArrayList<String>();
            if(list!=null&&list.size()>0){
                for (SysRoleKey sysRoleKey:list) {
                    ids.add(sysRoleKey.getMenuId());
                }
            }
            //返回权限列表
            sysRoleEdit.setIds(ids);
        }
        return sysRoleEdit;
    }

    /**
     * 保存方法
     * @param sysRoleEdit
     * @return
     */
    @Override
    @Transactional
    public int saveOrEdit(SysRoleEdit sysRoleEdit) {
        int n=0;
        String userId=ToolUtil.getCurrentUserId();
        if (ToolUtil.isEmpty(userId)){
            userId="admin";
        }

        if(ToolUtil.isEmpty(sysRoleEdit.getId())){
            //id为空，新增操作
            //验证该角色是否已经存在
            SysRoleExample example = new SysRoleExample();
            SysRoleExample.Criteria criteria = example.createCriteria();
            //比对角色的身份
            criteria.andIdentityEqualTo(sysRoleEdit.getIdentity());
            criteria.andIsDeleteNotEqualTo("1");
            List<SysRole> list = mapper.selectByExample(example);
            if (list!=null&&list.size() > 0) {
                throw new ServiceException("该角色已经存在,不可以再次添加");
            }
            //保存权限表
            sysRoleEdit.setId(ToolUtil.getUUID());
            sysRoleEdit.setCreateTime(new Date());
            sysRoleEdit.setCreateBy(userId);
//            sysRoleEdit.setUpdateTime(new Date());
//            sysRoleEdit.setUpdateBy(userId);
            sysRoleEdit.setIsDelete("0");
            n = mapper.insertSelective(sysRoleEdit);
        }else{
            //id不为空，编辑操作
            //判断用户和角色关系表是否有记录，如果该角色被使用不能禁用
//            if("0".equals(sysRoleEdit.getStatus())) {
//                SysUserRoleExample sysUserRoleExample = new SysUserRoleExample();
//                SysUserRoleExample.Criteria criteria2 = sysUserRoleExample.createCriteria();
//                criteria2.andRoleIdEqualTo(sysRoleEdit.getId());
//                List<SysUserRole> sysUserRoleList = sysUserRoleMapper.selectByExample(sysUserRoleExample);
//                if (sysUserRoleList != null && sysUserRoleList.size() > 0) {
//                    throw new ServiceException("该角色正在使用，不得禁用");
//                }
//            }
            //验证该用户名是否已经存在
            SysRoleExample example = new SysRoleExample();
            SysRoleExample.Criteria criteria = example.createCriteria();
            criteria.andIsDeleteNotEqualTo("1");
            //修改时名字与自身相同可以修改
            //匹配本身之外
            criteria.andIdNotEqualTo(sysRoleEdit.getId());
            criteria.andRoleNameEqualTo(sysRoleEdit.getIdentity());
            criteria.andIsDeleteNotEqualTo("1");
            List<SysRole> list = mapper.selectByExample(example);
            if (list!=null&&list.size() > 0) {
                throw new ServiceException("该角色已经存在");
            }
            sysRoleEdit.setUpdateTime(new Date());
            sysRoleEdit.setUpdateBy(userId);
            n = mapper.updateByPrimaryKeySelective(sysRoleEdit);
        }
        //物理删除角色与权限关联表
        SysRoleKeyExample example1 = new SysRoleKeyExample();
        SysRoleKeyExample.Criteria criteria1 = example1.createCriteria();
        criteria1.andRoleIdEqualTo(sysRoleEdit.getId());
        sysRoleKeyMapper.deleteByExample(example1);
        //插入角色与权限关联表
        if (sysRoleEdit.getIds()!=null&&sysRoleEdit.getIds().size()>0){
            List<SysRoleKey> list=new ArrayList<SysRoleKey>();
            for (String id:sysRoleEdit.getIds()) {
                SysRoleKey sysRoleKey = new SysRoleKey();
                sysRoleKey.setId(ToolUtil.getUUID());
                sysRoleKey.setRoleId(sysRoleEdit.getId());
                sysRoleKey.setMenuId(id);
                sysRoleKey.setIsDelete("0");
                sysRoleKey.setCreateTime(new Date());
                sysRoleKey.setCreateBy(userId);
//                n=sysRoleKeyMapper.insert(sysRoleKey);
                list.add(sysRoleKey);
            }
            if (list!=null&&list.size()>0){
                n=extSysRoleKeyMapper.insertList(list);
            }

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
        int n=0;
        if(ids!=null&&ids.length>0){
            //判断用户和角色关系表是否有记录，如果该角色被使用不能删除
/*            SysUserRoleExample sysUserRoleExample = new SysUserRoleExample();
            SysUserRoleExample.Criteria criteria2 = sysUserRoleExample.createCriteria();
            criteria2.andRoleIdIn(Arrays.asList(ids));
            criteria2.andIsDeleteNotEqualTo("1");
            List<SysUserRole> sysUserRoleList=sysUserRoleMapper.selectByExample(sysUserRoleExample);
            if(sysUserRoleList!=null&&sysUserRoleList.size()>0){
                throw new ServiceException("该角色正在使用，不得删除");
            }*/
            //虚拟删除角色
            SysRoleExample sysuserExample = new SysRoleExample();
            SysRoleExample.Criteria criteria = sysuserExample.createCriteria();
            criteria.andIdIn(Arrays.asList(ids));
            SysRole sysRole=new SysRole();
            //1 删除状态
            sysRole.setIsDelete("1");
            n=mapper.updateByExampleSelective(sysRole,sysuserExample);

            //物理删除权限关系表
            SysRoleKeyExample sysRoleKeyExample = new SysRoleKeyExample();
            SysRoleKeyExample.Criteria criteria1 =sysRoleKeyExample.createCriteria();
            criteria1.andRoleIdIn(Arrays.asList(ids));
            sysRoleKeyMapper.deleteByExample(sysRoleKeyExample);
        }
        return n;
    }

}

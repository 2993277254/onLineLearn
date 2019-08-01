package com.lsfly.bas.service.system.impl;

import com.github.pagehelper.PageInfo;
import com.lsfly.bas.dao.system.SysKeyMapper;
import com.lsfly.bas.dao.system.SysRoleKeyMapper;
import com.lsfly.bas.model.system.*;
import com.lsfly.bas.model.system.ext.SysMenuModify;
import com.lsfly.base.AbstractBaseServiceImpl;
import com.lsfly.exception.ServiceException;
import com.lsfly.sys.PageEntity;
import com.lsfly.bas.dao.system.SysMenuMapper;
import com.lsfly.bas.dao.system.ext.ExtSysMenuMapper;

import com.lsfly.bas.service.system.ISysMenuService;
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
public class SysMenuServiceImpl extends AbstractBaseServiceImpl implements ISysMenuService {
    private static final Logger logger = LoggerFactory.getLogger(SysMenuServiceImpl.class);



    @Autowired
    SysMenuMapper mapper;

    @Autowired
    ExtSysMenuMapper extSysMenuMapper;

    @Autowired
    SysKeyMapper sysKeyMapper;

    @Autowired
    SysRoleKeyMapper sysRoleKeyMapper;




    //<editor-fold desc="通用的接口">
    @Override
    public SysMenu getByPrimaryKey(String id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 带分页
     */
    @Override
    public List<SysMenu> selectByExample(SysMenuExample example) {
        return mapper.selectByExample(example);
    }

    /**
     * 带分页
     */
    @Override
    public List<SysMenu> selectByExampleWithRowbounds(SysMenuExample example, RowBounds rowBounds) {
        return mapper.selectByExampleWithRowbounds(example, rowBounds);
    }

    @Override
    public void updateByPrimaryKey(SysMenu model) {
/*        if (model.getUpdateDate() == null)
        model.setUpdateDate(new Date().getTime());*/
        mapper.updateByPrimaryKey(model);
    }
    @Override
    public void updateByPrimaryKeySelective(SysMenu model) {
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

            SysMenuExample example = new SysMenuExample();
            SysMenuExample.Criteria criteria = example.createCriteria();
            criteria.andIdIn(ids);
            mapper.deleteByExample(example);

        }
    }

    @Override
    public void insert(SysMenu model) {
/*        if (model.getCreateDate() == null) {
            model.setCreateDate(new Date().getTime());
        }*/
        mapper.insert(model);
    }

    @Override
    public List<Map<String, Object>> selectMenuByRoId(SysMenuModify sysMenuModify) {
        List<SysMenuModify> extSysmenuList=extSysMenuMapper.selectMenuByRoId(sysMenuModify);
        //遍历菜单
        List<Map<String,Object>> menulist=new ArrayList<Map<String,Object>>();
        for (SysMenuModify extSysmenu:extSysmenuList) {
            if(ToolUtil.isEmpty(extSysmenu.getParentId())){  //获取一级菜单
                Map<String,Object> map=new HashMap<String,Object>();
                map.put("id",extSysmenu.getId());
                map.put("menuName",extSysmenu.getMenuName());
                map.put("parentId",extSysmenu.getParentId());
                map.put("parentName",extSysmenu.getParentName());
                map.put("menuUrl",extSysmenu.getMenuUrl());
                map.put("menuImg",extSysmenu.getMenuImg());
                map.put("menuIcon",extSysmenu.getMenuIcon());
                List<Map<String,Object>> childrenList=new ArrayList<Map<String,Object>>();
                for (SysMenuModify child:extSysmenuList) {  //获取二级菜单
                    if(ToolUtil.isNotEmpty(extSysmenu.getId())
                            &&extSysmenu.getId().equals(child.getParentId())){
                        Map<String,Object> childMap=new HashMap<String,Object>();
                        childMap.put("id",child.getId());
                        childMap.put("menuName",child.getMenuName());
                        childMap.put("parentId",child.getParentId());
                        childMap.put("parentName",child.getParentName());
                        childMap.put("menuUrl",child.getMenuUrl());
                        childMap.put("menuImg",child.getMenuImg());
                        childMap.put("menuIcon",child.getMenuIcon());
                        List<Map<String,Object>> childrenList2=new ArrayList<Map<String,Object>>();
                        for (SysMenuModify child1:extSysmenuList) {  //获取三级菜单
                            if(ToolUtil.isNotEmpty(child.getId())
                                    &&child.getId().equals(child1.getParentId())){
                                Map<String,Object> childMap1=new HashMap<String,Object>();
                                childMap1.put("id",child1.getId());
                                childMap1.put("menuName",child1.getMenuName());
                                childMap1.put("parentId",child1.getParentId());
                                childMap1.put("parentName",child1.getParentName());
                                childMap1.put("menuUrl",child1.getMenuUrl());
                                childMap1.put("imgurl",child1.getRemark());
                                childMap1.put("menuImg",child1.getMenuImg());
                                childMap1.put("menuIcon",child1.getMenuIcon());
                                List<Map<String,Object>> childrenList3=new ArrayList<Map<String,Object>>();
                                childMap1.put("children",childrenList3);
                                childrenList2.add(childMap1);
                            }
                        }
                        childMap.put("children",childrenList2);
                        childrenList.add(childMap);
                    }
                }
                map.put("children",childrenList);
                menulist.add(map);
            }
        }
        return menulist;
    }

    @Override
    public List<SysMenu> list(SysMenu sysmenu) {
        SysMenuExample example = new SysMenuExample();
        SysMenuExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteNotEqualTo("1");
        if(ToolUtil.isNotEmpty(sysmenu.getMenuName())){
            criteria.andMenuNameLike("%" + sysmenu.getMenuName() + "%");
        }
        example.setOrderByClause("menu_sort ASC");  //排序倒序，取第一条，即最大序号
        List<SysMenu> sysMenuList=mapper.selectByExample(example);
        return sysMenuList;
    }

    @Override
    public SysMenu add(SysMenu sysmenu) {
        if(ToolUtil.isEmpty(sysmenu.getId())){  //菜单新增
            sysmenu.setId(ToolUtil.getUUID());  //主键
            //获取同级的最大排序序号，然后加1
            int sort=1;
            SysMenuExample example = new SysMenuExample();
            SysMenuExample.Criteria criteria = example.createCriteria();
            if(ToolUtil.isNotEmpty(sysmenu.getParentId())){
                criteria.andParentIdEqualTo(sysmenu.getParentId());
            }else{
                criteria.andParentIdEqualTo("");
            }
            example.setOrderByClause("menu_sort DESC");  //排序倒序，取第一条，即最大序号
            List<SysMenu> sysmenuList=mapper.selectByExample(example);
            if(sysmenuList!=null&&sysmenuList.size()>0){
                sort=sysmenuList.get(0).getMenuSort()+1;
            }
            sysmenu.setMenuSort(sort);
            sysmenu.setIsDelete("0");
            mapper.insertSelective(sysmenu);
        }else{  //菜单编辑
            //禁用前确认下有没子节点，如果有就不执行
            if(ToolUtil.isNotEmpty(sysmenu.getStatus())&&
                    sysmenu.getStatus().equals("0")){
                SysMenuExample example = new SysMenuExample();
                SysMenuExample.Criteria criteria = example.createCriteria();
                criteria.andParentIdEqualTo(sysmenu.getId());
                criteria.andIsDeleteEqualTo("0");
                criteria.andStatusEqualTo("1");
                List<SysMenu> sysMenuList=mapper.selectByExample(example);
                if(sysMenuList!=null&&sysMenuList.size()>0){
                    throw new ServiceException("该菜单下有子菜单，请先禁用子菜单");  //实现层中抛出异常
                }
            }
            mapper.updateByPrimaryKeySelective(sysmenu);
        }
        return sysmenu;
    }

    @Override
    public SysMenuModify info(SysMenu sysMenu) {
        SysMenuModify sysMenuModify=new  SysMenuModify();
        sysMenu=mapper.selectByPrimaryKey(sysMenu.getId());
        BeanUtils.copyProperties(sysMenu,sysMenuModify);//字段相同时才能复制
        SysMenu pmodel=mapper.selectByPrimaryKey(sysMenu.getParentId());  //父节点
        if(pmodel!=null){
            sysMenuModify.setParentName(pmodel.getMenuName());
        }
        return sysMenuModify;
    }

    @Override
    @Transactional
    public int delete(SysMenu sysMenu) {
        //删除前确认下有没子节点，如果有就不执行
        SysMenuExample example = new SysMenuExample();
        SysMenuExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(sysMenu.getId());
        criteria.andIsDeleteEqualTo("0");
        List<SysMenu> sysMenuList=mapper.selectByExample(example);
        if(sysMenuList!=null&&sysMenuList.size()>0){
            throw new ServiceException("该菜单下有子菜单，请先删除子菜单");  //实现层中抛出异常
        }
        SysRoleKeyExample sysRoleKeyExample = new SysRoleKeyExample();
        SysRoleKeyExample.Criteria sysRoleKeyExampleCriteria = sysRoleKeyExample.createCriteria();
        sysRoleKeyExampleCriteria.andMenuIdEqualTo(sysMenu.getId());
        sysRoleKeyMapper.deleteByExample(sysRoleKeyExample); //删除所有rolemenu关联的菜单
        //删除所有按钮，逻辑删除
        SysKeyExample syskeyExample = new SysKeyExample();
        SysKeyExample.Criteria syskeyExampleCriteria = syskeyExample.createCriteria();
        syskeyExampleCriteria.andMenuIdEqualTo(sysMenu.getId());
        syskeyExampleCriteria.andIsDeleteEqualTo("0");
        SysKey syskey=new SysKey();
        syskey.setIsDelete("1"); //删除状态
        sysKeyMapper.updateByExampleSelective(syskey,syskeyExample);
        sysMenu.setIsDelete("1"); //删除状态
        return mapper.updateByPrimaryKeySelective(sysMenu);
    }

    /**
     *菜单排序
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public int sort(List<String> ids,String parentId) {
        //执行根据ids的排序，从1开始排序
        for (int i=0;i<ids.size();i++) {
            SysMenu sysMenu=new SysMenu();
            sysMenu.setId(ids.get(i));
            sysMenu.setMenuSort(i+1);
            sysMenu.setParentId(parentId);
            mapper.updateByPrimaryKeySelective(sysMenu);
        }
        return 0;
    }

}

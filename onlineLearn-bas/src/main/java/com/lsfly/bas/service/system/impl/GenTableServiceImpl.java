package com.lsfly.bas.service.system.impl;

import com.lsfly.bas.dao.system.GenTableColumnMapper;
import com.lsfly.bas.dao.system.GenTableMapper;
import com.lsfly.bas.dao.system.ext.GenDataBaseDictMapper;
import com.lsfly.bas.model.system.GenTable;
import com.lsfly.bas.model.system.GenTableColumn;

import com.lsfly.bas.model.system.GenTableColumnExample;
import com.lsfly.bas.model.system.GenTableExample;
import com.lsfly.bas.service.system.IGenTableService;
import com.lsfly.base.AbstractBaseServiceImpl;
import com.lsfly.util.ToolUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *  服务类实现
 */
@Service
public class GenTableServiceImpl extends AbstractBaseServiceImpl implements IGenTableService {
    private static final Logger logger = LoggerFactory.getLogger(GenTableServiceImpl.class);


    @Autowired
    GenTableMapper mapper;

    @Autowired
    GenDataBaseDictMapper genDataBaseDictMapper;

    @Autowired
    GenTableColumnMapper genTableColumnMapper;

    //<editor-fold desc="通用的接口">
    public GenTable getByPrimaryKey(String id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 带分页
     */
      public List<GenTable> selectByExample(GenTableExample example) {
            return mapper.selectByExample(example);
       }

    /**
     * 带分页
     */
    public List<GenTable> selectByExampleWithRowbounds(GenTableExample example, RowBounds rowBounds) {
        return mapper.selectByExampleWithRowbounds(example, rowBounds);
    }


    public void updateByPrimaryKey(GenTable model) {
/*        if (model.getUpdateDate() == null)
        model.setUpdateDate(new Date().getTime());*/
        mapper.updateByPrimaryKey(model); 
    }

    public void updateByPrimaryKeySelective(GenTable model) {
/*        if (model.getUpdateDate() == null)
            model.setUpdateDate(new Date().getTime());*/
        mapper.updateByPrimaryKeySelective(model);
    }


    public void deleteByPrimaryKey(String id) {
//        if(!canDel(id))
//            throw new ServiceException("id="+id+"的xx下面有xx不能删除,需要先删除所有xx才能删除");

        mapper.deleteByPrimaryKey(id);
    }


    public void deleteByPrimaryKeys(List<String> ids) {
        if (ids != null && ids.size() > 0) {
//            if(!canDel(ids))
//                throw new ServiceException("xx下面有xx不能删除,需要先删除所有xx才能删除");

            GenTableExample example = new GenTableExample();
            GenTableExample.Criteria criteria = example.createCriteria();
            criteria.andIdIn(ids);
            mapper.deleteByExample(example);

        }
    }


    public void insert(GenTable model) {
/*        if (model.getCreateDate() == null) {
            model.setCreateDate(new Date().getTime());
        }*/
        mapper.insert(model);
    }
    //</editor-fold>


    //<editor-fold desc="个性化的接口">

    /**
     * 查询所有列表
     * @return
     */
    @Override
    public List<GenTable> findTableList(String dbname){
        List<GenTable> list=genDataBaseDictMapper.findTableList(dbname,"");
        for (GenTable genTable:list) {
            if(ToolUtil.isNotEmpty(genTable.getName())){
                //主键列表
                List<String> pkList=genDataBaseDictMapper.findTablePK(dbname,genTable.getName());
                if(pkList!=null&&pkList.size()>0){
                    genTable.setPkName(pkList.get(0));
                }
                genTable.setAliasName(ToolUtil.getAliasNameByUnderline(genTable.getName()));
            }
        }
        return  list;
    };

    /**
     * 获取数据表字段
     * @return
     */
    @Override
    public List<GenTableColumn> findTableColumnList(String dbname, String tableName, List<String> baseColumList){
        List<GenTableColumn> sysList=genDataBaseDictMapper.findTableColumnList(dbname,tableName);
        List<String> ids=genDataBaseDictMapper.findTablePK(dbname,tableName);  //获取主键
        //对表初始化一些属性
        for (GenTableColumn colum:sysList) {
            // 是否是主键
            colum.setIsPk(ids.contains(colum.getName())?"1":"0");
            // 设置java类型
            if (StringUtils.startsWithIgnoreCase(colum.getJdbcType(), "CHAR")
                    || StringUtils.startsWithIgnoreCase(colum.getJdbcType(), "VARCHAR")
                    || StringUtils.startsWithIgnoreCase(colum.getJdbcType(), "NARCHAR")){
                colum.setJavaType("String");
            }else if (StringUtils.startsWithIgnoreCase(colum.getJdbcType(), "DATETIME")
                    || StringUtils.startsWithIgnoreCase(colum.getJdbcType(), "DATE")
                    || StringUtils.startsWithIgnoreCase(colum.getJdbcType(), "TIMESTAMP")){
                colum.setJavaType("java.util.Date");
            }else if (StringUtils.startsWithIgnoreCase(colum.getJdbcType(), "BIGINT")
                    || StringUtils.startsWithIgnoreCase(colum.getJdbcType(), "NUMBER")){
                // 如果是浮点型
                String[] ss = StringUtils.split(StringUtils.substringBetween(colum.getJdbcType(), "(", ")"), ",");
                if (ss != null && ss.length == 2 && Integer.parseInt(ss[1])>0){
                    colum.setJavaType("Double");
                }
                // 如果是整形
                else if (ss != null && ss.length == 1 && Integer.parseInt(ss[0])<=10){
                    colum.setJavaType("Integer");
                }
                // 长整形
                else{
                    colum.setJavaType("Long");
                }
            }
            
            colum.setTableName(tableName);
            colum.setShowName(ToolUtil.isEmpty(colum.getComments())
                    ?colum.getName():colum.getComments()); //页面字段名称
            if(baseColumList.contains(colum.getName())||ids.contains(colum.getName())){
                //基础属性或者主键的就不用加了
                colum.setIsQuery("0"); //加入查询
                colum.setIsList("0"); //列表显示
                colum.setIsSort("0"); //是否排序
                colum.setIsEdit("0"); //编辑显示
                colum.setIsRequired("0"); //必填
            }else{
                colum.setIsQuery("1"); //加入查询
                colum.setIsList("1"); //列表显示
                colum.setIsSort("1"); //是否排序
                colum.setIsEdit("1"); //编辑显示
                colum.setIsRequired("1"); //必填
            }
            colum.setQueryType("input"); //查询类型
            colum.setQueryCondition("="); //查询条件
            colum.setListAlign("left"); //列表排列
            colum.setEditType("input"); //编辑类型
            colum.setEditVerify(""); //验证方式
            if(ToolUtil.isNotEmpty(colum.getJavaType())&&colum.getJavaType().equals("java.util.Date")){
                //时间格式
                colum.setQueryType("date"); //查询类型
                colum.setEditType("date"); //编辑类型
                colum.setListAlign("center");
            }
            if(ToolUtil.isNotEmpty(colum.getJavaType())&&(colum.getJavaType().equals("Integer")
                    ||colum.getJavaType().equals("Long"))){
                colum.setEditVerify("number");//验证方式
                colum.setListAlign("right");
            }
        }
        //查询本表记录的
        GenTableColumnExample example = new GenTableColumnExample();
        GenTableColumnExample.Criteria criteria = example.createCriteria();
        criteria.andTableNameEqualTo(tableName);
        List<GenTableColumn> dblist=genTableColumnMapper.selectByExample(example);
        if(dblist!=null&&dblist.size()>0){
            List<GenTableColumn> newList=new ArrayList<GenTableColumn>();
            for (GenTableColumn colum:sysList) {
                Boolean isHave=false;
                for (GenTableColumn dbcolum:dblist) {
                    if(colum.getName().equals(dbcolum.getName())){
                        isHave=true;
                        //每次更新字段的长度
                        dbcolum.setColumnLength(colum.getColumnLength());
                        newList.add(dbcolum);
                        break;
                    }
                }
                if(!isHave){
                    newList.add(colum);
                }
            }
            return  newList;
        }else{
            return  sysList;
        }
    }

    @Override
    public List<String> findTablePK(String dbname, String tableName) {
        return genDataBaseDictMapper.findTablePK(dbname,tableName);
    }

    /**
     *  获取多个表的字段
     * @param dbname 数据库类型
     * @param tables table类型
     * @return
     */
    @Override
    public List<GenTableColumn> findTableColumnLists(String dbname, List<String> tables) {
        List<GenTableColumn> data=new ArrayList<GenTableColumn>();
        for (String table:tables) {
            List<GenTableColumn> sysList=genDataBaseDictMapper.findTableColumnList(dbname,table);
            if(sysList!=null&&sysList.size()>0){
                for (GenTableColumn colum:sysList) {
                    colum.setTableName(table);
                }
                data.addAll(sysList);
            }
        }
        return data;
    }

    ;

    //</editor-fold>

}

package com.lsfly.bas.service.system.impl;


import com.lsfly.bas.dao.system.GenTableColumnMapper;
import com.lsfly.bas.dao.system.GenTableMapper;
import com.lsfly.bas.model.system.GenTable;
import com.lsfly.bas.model.system.GenTableColumn;
import com.lsfly.bas.model.system.GenTableColumnExample;
import com.lsfly.bas.model.system.GenTableExample;
import com.lsfly.bas.model.system.ext.GenTableColumnEdit;
import com.lsfly.bas.service.system.IGenTableColumnService;
import com.lsfly.base.AbstractBaseServiceImpl;
import com.lsfly.util.ToolUtil;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 *  服务类实现
 */
@Service
public class GenTableColumnServiceImpl extends AbstractBaseServiceImpl implements IGenTableColumnService {
    private static final Logger logger = LoggerFactory.getLogger(GenTableColumnServiceImpl.class);


    @Autowired
    GenTableColumnMapper mapper;


    @Autowired
    GenTableMapper genTableMapper;

    //<editor-fold desc="通用的接口">
    public GenTableColumn getByPrimaryKey(String id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 带分页
     */
      public List<GenTableColumn> selectByExample(GenTableColumnExample example) {
            return mapper.selectByExample(example);
       }

    /**
     * 带分页
     */
    public List<GenTableColumn> selectByExampleWithRowbounds(GenTableColumnExample example, RowBounds rowBounds) {
        return mapper.selectByExampleWithRowbounds(example, rowBounds);
    }


    public void updateByPrimaryKey(GenTableColumn model) {
/*        if (model.getUpdateDate() == null)
        model.setUpdateDate(new Date().getTime());*/
        mapper.updateByPrimaryKey(model); 
    }

    public void updateByPrimaryKeySelective(GenTableColumn model) {
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

            GenTableColumnExample example = new GenTableColumnExample();
            GenTableColumnExample.Criteria criteria = example.createCriteria();
            criteria.andIdIn(ids);
            mapper.deleteByExample(example);

        }
    }


    public void insert(GenTableColumn model) {
/*        if (model.getCreateDate() == null) {
            model.setCreateDate(new Date().getTime());
        }*/
        mapper.insert(model);
    }

    /**
     * 保存方法
     * @param genTableColumnEdit
     * @return
     */
    @Override
    @Transactional
    public int saveOrEdit(GenTableColumnEdit genTableColumnEdit) {
        List<GenTableColumn> genTableColumnList=genTableColumnEdit.getGenTableColumnList();
        int n=1;
        if(genTableColumnList!=null&&genTableColumnList.size()>0){
            String tableName=genTableColumnList.get(0).getTableName();
            GenTableColumnExample example = new GenTableColumnExample();
            GenTableColumnExample.Criteria criteria = example.createCriteria();
            criteria.andTableNameEqualTo(tableName);
            mapper.deleteByExample(example);
            for (GenTableColumn genTableColumn:genTableColumnList){
                //新增操作
                genTableColumn.setId(ToolUtil.getUUID());
                genTableColumn.setCreateTime(new Date());
                //genTableColumn.setCreateBy(ToolUtil.getCurrentUserId());
                genTableColumn.setUpdateTime(new Date());
                //genTableColumn.setUpdateBy(ToolUtil.getCurrentUserId());
                genTableColumn.setIsDelete("0");
                mapper.insertSelective(genTableColumn);
            }

            GenTableExample genTableExample = new GenTableExample();
            GenTableExample.Criteria genTableExampleCriteria = genTableExample.createCriteria();
            genTableExampleCriteria.andNameEqualTo(tableName);
            genTableMapper.deleteByExample(genTableExample);
            GenTable genTable=genTableColumnEdit.getGenTable();
            genTable.setId(ToolUtil.getUUID());
            genTable.setCreateTime(new Date());
            //genTable.setCreateBy(ToolUtil.getCurrentUserId());
            genTable.setUpdateTime(new Date());
            //genTable.setUpdateBy(ToolUtil.getCurrentUserId());
            genTable.setIsDelete("0");
            genTableMapper.insertSelective(genTable);

        }
        return n;
    }
    //</editor-fold>


    //<editor-fold desc="个性化的接口">


    //</editor-fold>

}

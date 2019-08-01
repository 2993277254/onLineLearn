package com.lsfly.bas.service.system;


import com.lsfly.bas.model.system.GenTable;
import com.lsfly.bas.model.system.GenTableColumn;
import com.lsfly.bas.model.system.GenTableExample;
import org.apache.ibatis.session.RowBounds;

import java.util.List;


/**
 *  服务类实现
 */
public interface IGenTableService{

 
        GenTable getByPrimaryKey(String id);

        /**
         * 不带分页
         */
        List<GenTable> selectByExample(GenTableExample example);

        /**
         * 带分页
         */
        List<GenTable> selectByExampleWithRowbounds(GenTableExample example, RowBounds rowBounds);

        void updateByPrimaryKey(GenTable model);

        void updateByPrimaryKeySelective(GenTable model);

        void deleteByPrimaryKey(String id);

        void deleteByPrimaryKeys(List<String> ids);

        void insert(GenTable model);


        //<editor-fold desc="个性化的接口">

        /**
         * 查询所有列表
         * @return
         */
        public List<GenTable> findTableList(String dbname);

        /**
         * 获取数据表字段
         * @return
         */
        public List<GenTableColumn> findTableColumnList(String dbname, String tableName, List<String> baseColumList);
        //</editor-fold>

        /**
         * 获取主键
         * @return
         */
        public List<String> findTablePK(String dbname, String tableName);

        /**
         * 获取多个表的字段
         * @param dbname
         * @param tables
         * @return
         */
        List<GenTableColumn> findTableColumnLists(String dbname, List<String> tables);
}

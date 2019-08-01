package com.lsfly.bas.service.system;



import com.lsfly.bas.model.system.GenTableColumn;
import com.lsfly.bas.model.system.GenTableColumnExample;
import com.lsfly.bas.model.system.ext.GenTableColumnEdit;
import org.apache.ibatis.session.RowBounds;

import java.util.List;


/**
 *  服务类实现
 */
public interface IGenTableColumnService{

 
        GenTableColumn getByPrimaryKey(String id);

        /**
         * 不带分页
         */
        List<GenTableColumn> selectByExample(GenTableColumnExample example);

        /**
         * 带分页
         */
        List<GenTableColumn> selectByExampleWithRowbounds(GenTableColumnExample example, RowBounds rowBounds);

        void updateByPrimaryKey(GenTableColumn model);

        void updateByPrimaryKeySelective(GenTableColumn model);

        void deleteByPrimaryKey(String id);

        void deleteByPrimaryKeys(List<String> ids);

        void insert(GenTableColumn model);

        int saveOrEdit(GenTableColumnEdit genTableColumnEdit);


    //<editor-fold desc="个性化的接口">

        //</editor-fold>

}

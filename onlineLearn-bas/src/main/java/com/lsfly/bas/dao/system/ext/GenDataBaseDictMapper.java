/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.lsfly.bas.dao.system.ext;


import com.lsfly.bas.model.system.GenTable;
import com.lsfly.bas.model.system.GenTableColumn;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务表字段DAO接口
 * @author ThinkGem
 * @version 2013-10-15
 */
public interface GenDataBaseDictMapper {

	/**
	 * 查询表列表
	 * @return
	 */
	List<GenTable> findTableList(@Param("dbName") String dbName, @Param("name") String name);

	/**
	 * 获取数据表字段
	 * @return
	 */
	List<GenTableColumn> findTableColumnList(@Param("dbName") String dbName, @Param("name") String name);
	
	/**
	 * 获取数据表主键
	 * @return
	 */
	List<String> findTablePK(@Param("dbName") String dbName, @Param("name") String name);
	
}

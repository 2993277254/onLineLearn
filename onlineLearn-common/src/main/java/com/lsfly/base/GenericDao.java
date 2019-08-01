package com.lsfly.base;


import com.lsfly.mybatis.ResultPage;

import java.util.List;
import java.util.Map;

/**
 * 所有自定义Dao的顶级接口, 封装常用的增删查改操作,
 * 可以通过Mybatis Generator Maven 插件自动生成Dao,
 * 也可以手动编码,然后继承GenericDao 即可.
 * <p/>
 * Model : 代表数据库中的表 映射的Java对象类型
 */
public interface GenericDao<Model> {

    /**
     * 插入对象
     *
     * @param model 对象
     */
    int insertSelective(Model model);

    
    int updateByPrimaryKeySelective(Model record);
    
    /**
     * 更新对象
     *
     * @param model 对象
     */
    int updateByPrimaryKey(Model model);

    /**
     * 通过主键, 删除对象
     *
     * @param id 主键
     */
    int deleteByPrimaryKey(String id);

    /**
     * 批次删除
     * @param ids
     * @return
     */
    int deleteByPrimaryKey(Map<String, Object> ids);

    /**
     * 通过主键, 查询对象
     *
     * @param id 主键
     * @return
     */
    Model selectByPrimaryKey(String id);


	/**
	 * 获取多条记录
	 * @param Map
	 * @return
	 */
    List<Model> findListByMapAndPage(Map<String, Object> map);

	List<Model> findListByMapAndPage(Map<String, Object> map, ResultPage<?> resultPage);
	
}

package com.lsfly.base;


import com.lsfly.mybatis.ResultPage;

import java.util.List;
import java.util.Map;

/**
 * 所有自定义Service的顶级接口,封装常用的增删查改操作
 * <p/>
 * Model : 代表数据库中的表 映射的Java对象类型
 */
public interface GenericService<Model> {

	/**
	 * 插入对象
	 * @param model 对象
	 * @return
	 */
	int insertSelective(Model model);
	
	/**
	 * 修改对象
	 * @param model对象
	 * @return
	 */
	int updateSelective(Model model);
	int updateByPrimaryKeySelective(Model record);
	Model selectByPrimaryKey(String id);
    /**
     * 插入对象
     *
     * @param model 对象
     */
    int insert(Model model);

    /**
     * 更新对象
     *
     * @param model 对象
     */
    int update(Model model);
    
    /**
     * 批次删除
     * @param ids 主键
     * @return
     */
    int delete(Map<String, Object> ids);

    /**
     * 通过主键, 删除对象
     *
     * @param id 主键
     */
    int delete(String id);

    /**
     * 通过主键, 查询对象
     *
     * @param id 主键
     * @return model 对象
     */
    Model selectById(String id);


    /**
     * 查询单个对象
     *
     * @return 对象
     */
    Model selectOne();


    /**
     * 查询多个对象
     *
     * @return 对象集合
     */
    List<Model> selectList();


	/**
	 * 获取多条记录
	 * @param Map
	 * @return
	 */
	List<Model> findListByMapAndPage(Map<String, Object> map, ResultPage<?> resultPage);

}

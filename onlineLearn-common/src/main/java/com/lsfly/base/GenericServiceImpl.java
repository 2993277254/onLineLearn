package com.lsfly.base;


import com.lsfly.mybatis.ResultPage;

import java.util.List;
import java.util.Map;

/**
 * GenericService的实现类, 其他的自定义 ServiceImpl, 继承自它,可以获得常用的增删查改操作,
 * 未实现的方法有 子类各自实现
 * <p/>
 * Model : 代表数据库中的表 映射的Java对象类型
 */
public abstract class GenericServiceImpl<Model> implements GenericService<Model> {

    /**
     * 定义成抽象方法,由子类实现,完成dao的注入
     *
     * @return GenericDao实现类
     */
    public abstract GenericDao<Model> getDao();

    /**
     * 插入对象
     *
     * @param model 对象
     */
    public int insertSelective(Model model){
    	return getDao().insertSelective(model);
    }
    
    /**
     * 更新对象
     *
     * @param model 对象
     */
    public int updateSelective(Model model){
    	return getDao().updateByPrimaryKey(model);
    }
    public int updateByPrimaryKeySelective(Model record){
    	return getDao().updateByPrimaryKeySelective(record);
    }
    
    public Model selectByPrimaryKey(String id){
    	return getDao().selectByPrimaryKey(id);
    }
    /**
     * 插入对象
     *
     * @param model 对象
     */
    public int insert(Model model) {
        return getDao().insertSelective(model);
    }

    /**
     * 更新对象
     *
     * @param model 对象
     */
    public int update(Model model) {
        return getDao().updateByPrimaryKey(model);
    }

    /**
     * 通过主键, 删除对象
     *
     * @param id 主键
     */
    public int delete(String id) {
        return getDao().deleteByPrimaryKey(id);
    }
    
    public int delete(Map<String,Object> ids){
    	return getDao().deleteByPrimaryKey(ids);
    }

    /**
     * 通过主键, 查询对象
     *
     * @param id 主键
     * @return
     */
    public Model selectById(String id) {
        return getDao().selectByPrimaryKey(id);
    }


    @Override
    public Model selectOne() {
        return null;
    }

    @Override
    public List<Model> selectList() {
        return null;
    }
    
	/**
	 * 获取多条记录
	 * @param Map
	 * @return
	 */    
    public List<Model> findListByMapAndPage(Map<String,Object> map, ResultPage<?> resultPage){
    	if(null == resultPage)
    		return getDao().findListByMapAndPage(map);
    	else
    		return getDao().findListByMapAndPage(map, resultPage);
    }
    
}

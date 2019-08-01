package ${service_package};


import com.github.pagehelper.PageInfo;
import ${model_package}.${DomainObjectName};
import ${model_package}.${DomainObjectName}Example;
import ${model_package}.ext.${DomainObjectName}List;
import ${model_package}.ext.${DomainObjectName}Edit;
import org.apache.ibatis.session.RowBounds;

import java.util.List;


/**
 *  服务类实现
 */
public interface I${DomainObjectName}Service{


    ${DomainObjectName} getByPrimaryKey(String id);

    /**
     * 不带分页
     */
    List<${DomainObjectName}> selectByExample(${DomainObjectName}Example example);

    /**
     * 带分页
     */
    List<${DomainObjectName}> selectByExampleWithRowbounds(${DomainObjectName}Example example, RowBounds rowBounds);

    void updateByPrimaryKey(${DomainObjectName} model);

    void updateByPrimaryKeySelective(${DomainObjectName} model);

    void deleteByPrimaryKey(String id);

    void deleteByPrimaryKeys(List<String> ids);

    void insert(${DomainObjectName} model);

    //查询列表分页方法
    PageInfo list(${DomainObjectName}List ${FileName}List);

    //获取实体方法
    ${DomainObjectName}Edit getInfo(String id);

    //保存实体方法
    int saveOrEdit(${DomainObjectName}Edit ${FileName}Edit);

    //删除方法
    int delete(String[] ids);

}

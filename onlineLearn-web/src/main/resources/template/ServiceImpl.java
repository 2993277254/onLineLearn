package ${service_package}.impl;

import com.github.pagehelper.PageInfo;
import ${ProjectPage}.base.AbstractBaseServiceImpl;
import ${ProjectPage}.exception.ServiceException;
import ${ProjectPage}.sys.PageEntity;
import ${dao_package}.${DomainObjectName}Mapper;
import ${dao_package}.ext.Ext${DomainObjectName}Mapper;
import ${model_package}.${DomainObjectName};
import ${model_package}.${DomainObjectName}Example;
import ${model_package}.ext.${DomainObjectName}List;
import ${model_package}.ext.${DomainObjectName}Edit;
import ${service_package}.I${DomainObjectName}Service;
import ${ProjectPage}.util.ToolUtil;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *  服务类实现
 */
@Service
public class ${DomainObjectName}ServiceImpl extends AbstractBaseServiceImpl implements I${DomainObjectName}Service {
    private static final Logger logger = LoggerFactory.getLogger(${DomainObjectName}ServiceImpl.class);


    @Autowired
    ${DomainObjectName}Mapper mapper;

    @Autowired
    Ext${DomainObjectName}Mapper ext${DomainObjectName}Mapper;

    //<editor-fold desc="通用的接口">
    @Override
    public ${DomainObjectName} getByPrimaryKey(String id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 带分页
     */
    @Override
    public List<${DomainObjectName}> selectByExample(${DomainObjectName}Example example) {
        return mapper.selectByExample(example);
    }

    /**
     * 带分页
     */
    @Override
    public List<${DomainObjectName}> selectByExampleWithRowbounds(${DomainObjectName}Example example, RowBounds rowBounds) {
        return mapper.selectByExampleWithRowbounds(example, rowBounds);
    }

    @Override
    public void updateByPrimaryKey(${DomainObjectName} model) {
/*        if (model.getUpdateDate() == null)
        model.setUpdateDate(new Date().getTime());*/
        mapper.updateByPrimaryKey(model);
    }

    @Override
    public void updateByPrimaryKeySelective(${DomainObjectName} model) {
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

            ${DomainObjectName}Example example = new ${DomainObjectName}Example();
            ${DomainObjectName}Example.Criteria criteria = example.createCriteria();
            criteria.and${id_JAVA}In(ids);
            mapper.deleteByExample(example);

        }
    }

    @Override
    public void insert(${DomainObjectName} model) {
/*        if (model.getCreateDate() == null) {
            model.setCreateDate(new Date().getTime());
        }*/
        mapper.insert(model);
    }
    

    /**
     * 分页查询列表方法
     * @param ${FileName}List
     * @return
     */
    @Override
    public PageInfo list(${DomainObjectName}List ${FileName}List) {
        List<${DomainObjectName}List> list=ext${DomainObjectName}Mapper.list(${FileName}List,
                new RowBounds(${FileName}List.getPage().getPageNum(),${FileName}List.getPage().getPageSize()));
        PageInfo pageInfo=new PageInfo(list);
        return pageInfo;
    }

    /**
     * 获取实体方法，可通过查询返回复杂实体到前台
     * 除了返回本记录实体，也可继续返回其他字段
     * @param id
     * @return
     */
    @Override
    public ${DomainObjectName}Edit getInfo(String id) {
        ${DomainObjectName}Edit ${FileName}Edit=new ${DomainObjectName}Edit();
        if(ToolUtil.isNotEmpty(id)){
            ${DomainObjectName} ${FileName}=mapper.selectByPrimaryKey(id);
            //字段相同时才能复制，排除个别业务字段请百度一下
            BeanUtils.copyProperties(${FileName},${FileName}Edit);
            //此处可继续返回其他字段...
        }
        return ${FileName}Edit;
    }

    /**
     * 保存方法
     * @param ${FileName}Edit
     * @return
     */
    @Override
    @Transactional
    public int saveOrEdit(${DomainObjectName}Edit ${FileName}Edit) {
        int n=0;
        if(ToolUtil.isEmpty(${FileName}Edit.get${id_JAVA}())){
            //id为空，新增操作
            ${FileName}Edit.set${id_JAVA}(ToolUtil.getUUID());
            <#if CreateTime!="">
            ${FileName}Edit.set${CreateTime}(new Date());
            </#if>
            <#if CreateBy!="">
            ${FileName}Edit.set${CreateBy}(ToolUtil.getCurrentUserId());
            </#if>
            <#if Status!="">
            ${FileName}Edit.set${Status}("${status_ok}");
            </#if>
            n = mapper.insertSelective(${FileName}Edit);
        }else{
            //id不为空，编辑操作
            <#if UpdateTime!="">
            ${FileName}Edit.set${UpdateTime}(new Date());
            </#if>
            <#if UpdateBy!="">
            ${FileName}Edit.set${UpdateBy}(ToolUtil.getCurrentUserId());
            </#if>
            n = mapper.updateByPrimaryKeySelective(${FileName}Edit);
        }
        return n;
    }

    /**
     * 删除方法
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public int delete(String[] ids) {
        if(ids!=null&&ids.length>0){
            ${DomainObjectName}Example sysuserExample = new ${DomainObjectName}Example();
            ${DomainObjectName}Example.Criteria criteria = sysuserExample.createCriteria();
            criteria.and${id_JAVA}In(Arrays.asList(ids));
            ${DomainObjectName} ${FileName}=new ${DomainObjectName}();
            ${FileName}.set${Status}("${status_delete}");  //${status_delete} 删除状态
            return mapper.updateByExampleSelective(${FileName},sysuserExample);
        }
        return 0;
    }

}

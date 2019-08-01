package ${model_package}.ext;

import ${ProjectPage}.sys.PageEntity;
import ${model_package}.${DomainObjectName};
<#list tableList as tables>
import ${tables.getField1()}.${tables.getClassName()};
</#list>
import java.util.Date;

/**
 * 用作${DomainObjectName}的列表查询页面的实体
 */
public class ${DomainObjectName}List extends ${DomainObjectName}{
    //统一封装分页、排序的实体
    private PageEntity page;
<#list tableList as tables>
    //关联的${tables.getParentTable()}表
    private ${tables.getClassName()} ${tables.getClassName()?uncap_first};
</#list>
<#list genTableColumnList as col>
<#if col.getIsQuery()=="1">
<#if col.getQueryType()=="date_range"||col.getQueryType()=="datetime_range"||col.getQueryType()=="time_range">
    //${col.getJavaField()}-开始
    private Date ${col.getJavaField()}_begin;
    //${col.getJavaField()}-结束
    private Date ${col.getJavaField()}_end;
</#if>
</#if>
</#list>


    public PageEntity getPage() {
        return page;
    }

    public void setPage(PageEntity page) {
        this.page = page;
    }
<#list tableList as tables>
    public ${tables.getClassName()} get${tables.getClassName()}() {
            return ${tables.getClassName()?uncap_first};
    }

    public void set${tables.getClassName()}(${tables.getClassName()} ${tables.getClassName()?uncap_first}) {
            this.${tables.getClassName()?uncap_first} = ${tables.getClassName()?uncap_first};
    }
</#list>
<#list genTableColumnList as col>
<#if col.getIsQuery()=="1">
<#if col.getQueryType()=="date_range"||col.getQueryType()=="datetime_range"||col.getQueryType()=="time_range">
    public Date get${col.getJavaField()?cap_first}_begin() {
        return ${col.getJavaField()}_begin;
    }

    public void set${col.getJavaField()?cap_first}_begin(Date ${col.getJavaField()}_begin) {
        this.${col.getJavaField()}_begin = ${col.getJavaField()}_begin;
    }

    public Date get${col.getJavaField()?cap_first}_end() {
        return ${col.getJavaField()}_end;
    }

    public void set${col.getJavaField()?cap_first}_end(Date ${col.getJavaField()}_end) {
        this.${col.getJavaField()}_end = ${col.getJavaField()}_end;
    }
</#if>
</#if>
</#list>

    
}

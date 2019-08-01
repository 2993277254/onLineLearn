package ${dao_package}.ext;

import ${model_package}.ext.${DomainObjectName}List;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 如果需要写sql语句，不要再${DomainObjectName}Mapper写，自己建一个ext文件和ext的xml文件，
 * 比如Ext${DomainObjectName}Mapper.java和Ext${DomainObjectName}Mapper.xml
 */
public interface Ext${DomainObjectName}Mapper {

    List<${DomainObjectName}List> list(${DomainObjectName}List ${FileName}List, RowBounds rowBounds);

}
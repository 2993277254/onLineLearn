package com.lsfly.commons.plugins;

import com.lsfly.util.PropertiesUtil;


import java.util.HashMap;
import java.util.Map;

/**
 * @author huoquan
 * @date 2018/9/13.
 */
public class GenViewPathConfig {
    public static Map<String,GenViewModule> GEN_PATH=new HashMap<String,GenViewModule>();
    static {
        //获取配置模块的基础模块的配置
        String jsp_dir=PropertiesUtil.getTemplateValue("jsp.dir");
        String js_dir=PropertiesUtil.getTemplateValue("js.dir");
        String controller_package=PropertiesUtil.getTemplateValue("controller.package");
        String controller_dir=PropertiesUtil.getTemplateValue("controller.dir");
        String xml_mapper_package= PropertiesUtil.getTemplateValue("xml.mapper.package");
        String xml_mapper_package_dir=PropertiesUtil.getTemplateValue("xml.mapper.package.dir");
        String dao_package=PropertiesUtil.getTemplateValue("dao.package");
        String dao_package_dir=PropertiesUtil.getTemplateValue("dao.package.dir");
        String model_package=PropertiesUtil.getTemplateValue("model.package");
        String model_package_dir=PropertiesUtil.getTemplateValue("model.package.dir");
        String service_package= PropertiesUtil.getTemplateValue("service.package");
        String service_package_dir=PropertiesUtil.getTemplateValue("service.package.dir");
        
        //定义系统模块路径
        String module="system";
        String description="system模块";
        GenViewPathEntity genViewPathEntity=new GenViewPathEntity();
        genViewPathEntity.setJsp_dir(jsp_dir);
        genViewPathEntity.setJs_dir(js_dir);
        genViewPathEntity.setController_package(controller_package);
        genViewPathEntity.setController_dir(controller_dir);
        genViewPathEntity.setDao_package(dao_package);
        genViewPathEntity.setDao_package_dir(dao_package_dir);
        genViewPathEntity.setModel_package(model_package);
        genViewPathEntity.setModel_package_dir(model_package_dir);
        genViewPathEntity.setXml_mapper_package(xml_mapper_package);
        genViewPathEntity.setXml_mapper_package_dir(xml_mapper_package_dir);
        genViewPathEntity.setService_package(service_package);
        genViewPathEntity.setService_package_dir(service_package_dir);
        GenViewModule genViewModule=new GenViewModule();
        genViewModule.setModule(module);
        genViewModule.setDescription(description);
        GEN_PATH.put(module,genViewModule);

        //定义

    }
}

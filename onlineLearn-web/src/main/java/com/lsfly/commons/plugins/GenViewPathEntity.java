package com.lsfly.commons.plugins;

/**
 * @author huoquan
 * @date 2018/9/13.
 */
public class GenViewPathEntity {
    //jsp的真实路径
    private  String jsp_dir="";
    //jsp的真实路径
    private  String js_dir="";
    //controller的包路径
    private  String controller_package="";
    //controller的真实路径
    private  String controller_dir="";
    //xml的包路径
    private  String xml_mapper_package="";
    //xml的真实路径
    private  String xml_mapper_package_dir="";
    //dao的包路径
    private  String dao_package="";
    //dao的真实路径
    private  String dao_package_dir="";
    //model的包路径
    private  String model_package="";
    //model的真实路径
    private  String model_package_dir="";
    //service的包路径
    private  String service_package="";
    //service的真实路径
    private  String service_package_dir="";

    public String getJsp_dir() {
        return jsp_dir;
    }

    public void setJsp_dir(String jsp_dir) {
        this.jsp_dir = jsp_dir;
    }

    public String getJs_dir() {
        return js_dir;
    }

    public void setJs_dir(String js_dir) {
        this.js_dir = js_dir;
    }

    public String getController_package() {
        return controller_package;
    }

    public void setController_package(String controller_package) {
        this.controller_package = controller_package;
    }

    public String getController_dir() {
        return controller_dir;
    }

    public void setController_dir(String controller_dir) {
        this.controller_dir = controller_dir;
    }

    public String getXml_mapper_package() {
        return xml_mapper_package;
    }

    public void setXml_mapper_package(String xml_mapper_package) {
        this.xml_mapper_package = xml_mapper_package;
    }

    public String getXml_mapper_package_dir() {
        return xml_mapper_package_dir;
    }

    public void setXml_mapper_package_dir(String xml_mapper_package_dir) {
        this.xml_mapper_package_dir = xml_mapper_package_dir;
    }

    public String getDao_package() {
        return dao_package;
    }

    public void setDao_package(String dao_package) {
        this.dao_package = dao_package;
    }

    public String getDao_package_dir() {
        return dao_package_dir;
    }

    public void setDao_package_dir(String dao_package_dir) {
        this.dao_package_dir = dao_package_dir;
    }

    public String getModel_package() {
        return model_package;
    }

    public void setModel_package(String model_package) {
        this.model_package = model_package;
    }

    public String getModel_package_dir() {
        return model_package_dir;
    }

    public void setModel_package_dir(String model_package_dir) {
        this.model_package_dir = model_package_dir;
    }

    public String getService_package() {
        return service_package;
    }

    public void setService_package(String service_package) {
        this.service_package = service_package;
    }

    public String getService_package_dir() {
        return service_package_dir;
    }

    public void setService_package_dir(String service_package_dir) {
        this.service_package_dir = service_package_dir;
    }
}

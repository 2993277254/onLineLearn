package com.lsfly.commons.plugins;

/**
 * @author huoquan
 * @date 2018/9/13.
 */
public class GenViewModule {
    //模块名字
    private  String module;
    //模块描述
    private  String description;
    //模块路径
    private GenViewPathEntity genViewPathEntity;


    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GenViewPathEntity getGenViewPathEntity() {
        return genViewPathEntity;
    }

    public void setGenViewPathEntity(GenViewPathEntity genViewPathEntity) {
        this.genViewPathEntity = genViewPathEntity;
    }
}

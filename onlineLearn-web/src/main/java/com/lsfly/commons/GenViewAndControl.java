package com.lsfly.commons;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lsfly.bas.model.system.GenTable;
import com.lsfly.bas.model.system.GenTableColumn;
import com.lsfly.bas.model.system.GenTableTree;
import com.lsfly.bas.model.system.ext.GenTableColumnEdit;
import com.lsfly.util.PropertiesUtil;
import com.lsfly.util.ToolUtil;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.GetTablesProgressCallback;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.*;
import java.util.*;

//import org.apache.commons.lang3.StringUtils;


/**
 * Created by changxin on 2016/8/14.
 * 生成表示层和控制层代码
 */
public class GenViewAndControl extends GetTablesProgressCallback {

    //private static final String templateDir = "F:\\myproject\\codegen\\src\\main\\resources\\template"; //输入目录
    public static final String templateDir = GenViewAndControl.class.getResource("/").getPath() + "template"; //输入目录
    public static final String tableName = "*";//需要生成代码的表名，*表示所有
    public static Configuration configuration = new Configuration();//freemark 的配置对象

    public static  String outputDir = PropertiesUtil.getTemplateValue("outputDir"); //额外生成的代码的输出目录
    public static  String module = PropertiesUtil.getTemplateValue("module");//配置文件的模块，比如system
    public static  String field_status_ok = PropertiesUtil.getTemplateValue("field_status_ok");//表的基础属性：状态正常code的名称
    public static  String field_status_delete = PropertiesUtil.getTemplateValue("field_status_delete");//表的基础属性：状态删除code的名称
    public static  String field_status = PropertiesUtil.getTemplateValue("field_status");//表的基础属性：状态的名称
    public static  String field_version = PropertiesUtil.getTemplateValue("field_version");//表的基础属性：版本的名称
    public static  String field_createBy = PropertiesUtil.getTemplateValue("field_createBy");//表的基础属性：创建人的名称
    public static  String field_createTime = PropertiesUtil.getTemplateValue("field_createTime");//表的基础属性：创建时间的名称
    public static  String field_updateBy = PropertiesUtil.getTemplateValue("field_updateBy");//表的基础属性：更新人的名称
    public static  String field_updateTime = PropertiesUtil.getTemplateValue("field_updateTime");//表的基础属性：更新时间的名称
    public static  String projectPage =PropertiesUtil.getTemplateValue("projectPage");

    //项目根目录
    public static  String rootDir="";
    public static  String dir_jsp="";
    public static  String dir_js="";
    public static  String dir_controller="";
    public static  String dir_service="";
    public static  String dir_serviceimpl="";
    public static  String dir_mapper="";
    public static  String dir_mapperxml="";
    public static  String dir_model="";

    //生成底层mybatis需要用的
    public static  String xml_mapper_package=PropertiesUtil.getTemplateValue("xml.mapper.package");
    public static  String xml_mapper_package_dir=PropertiesUtil.getTemplateValue("xml.mapper.package.dir");
    public static  String dao_package=PropertiesUtil.getTemplateValue("dao.package");
    public static  String dao_package_dir=PropertiesUtil.getTemplateValue("dao.package.dir");
    public static  String model_package=PropertiesUtil.getTemplateValue("model.package");
    public static  String model_package_dir=PropertiesUtil.getTemplateValue("model.package.dir");
    public static  String service_package=PropertiesUtil.getTemplateValue("service.package");
    public static  String service_package_dir=PropertiesUtil.getTemplateValue("service.package.dir");
    public static  String controller_package=PropertiesUtil.getTemplateValue("controller.package");

    private GenTableColumnEdit genTableColumnEdit;
    static {
        ToolUtil.mkdirs(outputDir);
        //创建一个合适的Configration对象
        try {
            configuration.setDirectoryForTemplateLoading(new File(templateDir));
            configuration.setObjectWrapper(new DefaultObjectWrapper());
            configuration.setDefaultEncoding("UTF-8");   //这个一定要设置，不然在生成的页面中 会乱码
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GenViewAndControl() {
        //构造方法
    }

    public GenViewAndControl(GenTableColumnEdit genTableColumnEdit) {
        //构造方法
        String projectPath="";
        try {
            File file = new DefaultResourceLoader().getResource("").getFile();
            if (file != null) {
                while (true) {
                    File f = new File(file.getPath() + File.separator + "src" + File.separator + "main");
                    if (f == null || f.exists()) {
                        break;
                    }
                    if (file.getParentFile() != null) {
                        file = file.getParentFile();
                    } else {
                        break;
                    }
                }
                projectPath = file.toString();
                projectPath=projectPath.substring(0,projectPath.lastIndexOf("\\"))+"\\";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(genTableColumnEdit.getPackage()!=null&&genTableColumnEdit.getPackage()){
            service_package =service_package.replaceAll(GenViewAndControl.module,genTableColumnEdit.getGenTable().getModule());
            dao_package =dao_package.replaceAll(GenViewAndControl.module,genTableColumnEdit.getGenTable().getModule());
            xml_mapper_package =xml_mapper_package.replaceAll(GenViewAndControl.module,genTableColumnEdit.getGenTable().getModule());
            model_package=model_package.replaceAll(GenViewAndControl.module,genTableColumnEdit.getGenTable().getModule());
        }
        rootDir=projectPath;
        dir_jsp=rootDir+PropertiesUtil.getTemplateValue("jsp.dir").replaceAll(GenViewAndControl.module,genTableColumnEdit.getGenTable().getModule())+"\\";
        dir_js=rootDir+PropertiesUtil.getTemplateValue("js.dir").replaceAll(GenViewAndControl.module,genTableColumnEdit.getGenTable().getModule())+"\\";
        dir_controller=rootDir+PropertiesUtil.getTemplateValue("controller.dir").replaceAll(GenViewAndControl.module,genTableColumnEdit.getGenTable().getModule())+"\\";
        String servicePackage =service_package.replaceAll("\\.","\\\\");
        String daoPackage =dao_package.replaceAll("\\.","\\\\");
        String xmlMapperPackag =xml_mapper_package.replaceAll("\\.","\\\\");
        String modelPackage=model_package.replaceAll("\\.","\\\\");
        dir_service=rootDir+service_package_dir.replaceAll(GenViewAndControl.module,genTableColumnEdit.getGenTable().getModule())
                +"\\"+servicePackage+"\\";
        dir_serviceimpl=rootDir+service_package_dir.replaceAll(GenViewAndControl.module,genTableColumnEdit.getGenTable().getModule())
                +"\\"+servicePackage+"\\impl\\";
        dir_mapper=rootDir+dao_package_dir.replaceAll(GenViewAndControl.module,genTableColumnEdit.getGenTable().getModule())
                +"\\"+daoPackage+"\\ext\\";
        dir_mapperxml=rootDir+xml_mapper_package_dir.replaceAll(GenViewAndControl.module,genTableColumnEdit.getGenTable().getModule())
                +"\\"+xmlMapperPackag+"\\ext\\";
        dir_model=rootDir+model_package_dir.replaceAll(GenViewAndControl.module,genTableColumnEdit.getGenTable().getModule())
        +"\\"+modelPackage+"\\ext\\";
        this.genTableColumnEdit=genTableColumnEdit;
    }


    @Override
    public void startTask(String taskName) {
        //System.out.println(taskName);
    }


    /**
     * IntrospectedTable table
     * table.getFullyQualifiedTableNameAtRuntime() 是表名
     * table.getFullyQualifiedTable().getDomainObjectName() 是Model名，也是表名去掉下划线，pascal命名法。
     * table.getAllColumns() 取得列名对象集合
     * <p>
     * IntrospectedColumn col
     * col.getActualColumnName() 列名
     * col.getJavaProperty()  列对应的java属性名，去掉下划线，对应骆驼命名法。
     * col.getJdbcTypeName()  列类型且大写，如INTEGER，BIGINT，VARCHAR
     * col.getRemarks()  列注释
     * col.getLength()  字段长度
     * <p>
     * 文件命名规则：
     * 总原则，会暴露在url路径中的全部小写
     * html  全部小写表名.html
     * js Controller   全部小写表名ctrl.js
     * java Controller pascal帕斯卡命名   AaBbController.java
     * 类命名：
     * js Controller  类命名和java 类命名都是 帕斯卡
     *
     * @param introspectedTables
     * @param tableConfigurations
     */
    @Override
    public void genFromTemplate(List<IntrospectedTable> introspectedTables, ArrayList<TableConfiguration> tableConfigurations) {

        for (IntrospectedTable table : introspectedTables) {
            // System.out.println("|--" +  tableConfigurations.get(0).getMapperName());
            String pascalTableName = table.getFullyQualifiedTable().getDomainObjectName();
            for (IntrospectedColumn col : table.getAllColumns()) {
                String remark = "";
                if (StringUtils.isNotEmpty(col.getRemarks())) {
                    String[] strings = col.getRemarks().split(",");
                    if (col.getRemarks().contains(",")) {
                        strings = col.getRemarks().split(",");
                    } else if (col.getRemarks().contains("，")) {
                        strings = col.getRemarks().split("，");
                    }
                    if (strings != null && strings.length > 0) {
                        remark = strings[0];
                    }
                }
                if (StringUtils.isEmpty(remark)) {
                    col.setRemarks(col.getJavaProperty());
                } else {
                    col.setRemarks(remark);
                }
            }

            if (tableName.equals("*")) {
                System.out.println("针对>>>" + table.getFullyQualifiedTableNameAtRuntime());
                output(table.getFullyQualifiedTableNameAtRuntime(),
                        table.getFullyQualifiedTable().getDomainObjectName(), table.getAllColumns(), pascalTableName,
                        table.getAllColumns().get(0).getActualColumnName());

            } else if (tableName.equals(table.getFullyQualifiedTableNameAtRuntime())) {
                System.out.println("针对>>>" + tableName + "开始所生成");
                output(table.getFullyQualifiedTableNameAtRuntime(),
                        table.getFullyQualifiedTable().getDomainObjectName(), table.getAllColumns(), pascalTableName,
                        table.getAllColumns().get(0).getActualColumnName());
            }
        }
    }

    /**
     * 输出,写到硬盘
     */
    private void output(String tableName, String DomainObjectName,
                        List<IntrospectedColumn> DomainObjectPropertys,
                        String pascalTableName,
                        String id) {
        try {
            GenTable genTable=genTableColumnEdit.getGenTable();
            List<GenTable> tableList=new ArrayList<GenTable>();
            List<String> aliasNameList=new ArrayList<String>();
            Map<String,String> aliasNameMap=new HashMap<String,String>();
            aliasNameList.add(ToolUtil.getAliasNameByUnderline(tableName)+"_");
            aliasNameMap.put(tableName,ToolUtil.getAliasNameByUnderline(tableName)+"_");
            if(genTable!=null){
                String pkJson=genTable.getPkJson();
                if(ToolUtil.isNotEmpty(pkJson)){
                    JSONArray jsStr = JSONArray.parseArray(pkJson); //将字符串{“id”：1}
                    if(jsStr.size()>0){
                        for(int i=0;i<jsStr.size();i++){
                            JSONObject job = jsStr.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                            GenTable genTable1=new GenTable();
                            String json_tableName=job.getString("tableName"); //外键表名
                            String json_module=job.getString("module"); //模块
                            String json_pkName=job.getString("pkName"); //主键名称
                            String json_pk=job.getString("pk"); //外键值
                            String json_aliasName= ToolUtil.getAliasNameByUnderline(json_tableName)+"_"; //外键表别名
                            if(aliasNameList.contains(json_aliasName)){
                                json_aliasName=json_aliasName+"1";
                            }
                            aliasNameList.add(json_aliasName);
                            aliasNameMap.put(json_tableName,json_aliasName);
                            genTable1.setParentTable(json_tableName);
                            genTable1.setAliasName(json_aliasName);
                            genTable1.setParentTableFk(json_pk);
                            genTable1.setPkName(json_pkName);
                            genTable1.setClassName(JavaBeansUtil.getCamelCaseString(json_tableName,true));
                            genTable1.setModule(json_module);
                            genTable1.setField1(model_package.replaceAll(genTableColumnEdit.getGenTable().getModule(),json_module));
                            tableList.add(genTable1);
                        }
                    }
                }
            }
            List<GenTableColumn> genTableColumnList=genTableColumnEdit.getGenTableColumnList();
            for (GenTableColumn genTableColumn:genTableColumnList) {
                if(ToolUtil.isEmpty(genTableColumn.getField3())){
                    genTableColumn.setField3("1");
                }
                if(ToolUtil.isNotEmpty(genTableColumn.getListField())){
                    String listField=genTableColumn.getListField();
                    if(listField.indexOf(".")>0){
                        String table=listField.substring(0,listField.indexOf("."));
                        genTableColumn.setListField(aliasNameMap.get(table)+listField.substring(listField.indexOf("."),listField.length()));
                        //存外表的java字段，比如sysUser
                        genTableColumn.setField1(JavaBeansUtil.getCamelCaseString(table,false));
                        //存外表以及字段的java字段，比如sysUser.userName
                        genTableColumn.setField2(JavaBeansUtil.getCamelCaseString(table,false)
                                +"."+JavaBeansUtil.getCamelCaseString(listField.substring(listField.indexOf(".")+1,listField.length()),false));
                    }
                }
                for (IntrospectedColumn col:DomainObjectPropertys) {
                    if(col.getActualColumnName().equals(genTableColumn.getName())){
                        genTableColumn.setJavaType(col.getFullyQualifiedJavaType().getFullyQualifiedName());
                        genTableColumn.setJavaField(col.getJavaProperty());
                    }
                }
            }
            GenTableTree genTableTree=genTableColumnEdit.getGenTableTree();
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("isLeftZtree", genTableColumnEdit.getGenTable().getIsLeftZtree()); //是否开启左侧树
            paramMap.put("genTableTree", genTableTree); //左侧树
            paramMap.put("tableName", tableName); //table的名称，数据库的名字
            paramMap.put("aliasName", aliasNameMap.get(tableName)); //table的别名
            paramMap.put("DomainObjectName", DomainObjectName); //类的名称
            paramMap.put("FileName", toLowerCaseFirstOne(DomainObjectName));  //类的名称,首个字母小写，jsp、js、avalon变量收个字母写
            paramMap.put("DomainObjectPropertys", DomainObjectPropertys); //字段
            paramMap.put("genTableColumnList", genTableColumnList); //字段
            paramMap.put("createDate", new Date());
            paramMap.put("id_table", id); //主键的java命名，首个字母大写
            paramMap.put("id_JAVA", JavaBeansUtil.getCamelCaseString(id,true)); //主键的java命名，首个字母大写
            paramMap.put("id_java", JavaBeansUtil.getCamelCaseString(id,false)); //主键的java命名，首个字母小写
            paramMap.put("ProjectPage", projectPage);  //项目包，比如：com.zhjs
            paramMap.put("moduleName", genTableColumnEdit.getGenTable().getModule());  //模块名称,比如system模块
            paramMap.put("module", module);  //基础模块名称,比如system模块
            paramMap.put("tableList", tableList); //外键tableList的别名
            //增加表的6个基本属性，分别是数据库字段形式跟java字段形式
            paramMap.put("status_ok", field_status_ok);
            paramMap.put("status_delete", field_status_delete);
            paramMap.put("status", field_status);
            paramMap.put("version", field_version);
            paramMap.put("create_by", field_createBy);
            paramMap.put("create_time", field_createTime);
            paramMap.put("update_by", field_updateBy);
            paramMap.put("update_time", field_updateTime);

            paramMap.put("Status", ToolUtil.isEmpty(field_status)?"":JavaBeansUtil.getCamelCaseString(field_status, true)); //下划线转为骆驼
            paramMap.put("Version", ToolUtil.isEmpty(field_version)?"":JavaBeansUtil.getCamelCaseString(field_version, true));
            paramMap.put("CreateBy", ToolUtil.isEmpty(field_createBy)?"":JavaBeansUtil.getCamelCaseString(field_createBy, true));
            paramMap.put("CreateTime", ToolUtil.isEmpty(field_createTime)?"":JavaBeansUtil.getCamelCaseString(field_createTime, true));
            paramMap.put("UpdateBy", ToolUtil.isEmpty(field_updateBy)?"":JavaBeansUtil.getCamelCaseString(field_updateBy, true));
            paramMap.put("UpdateTime", ToolUtil.isEmpty(field_updateTime)?"":JavaBeansUtil.getCamelCaseString(field_updateTime, true));

            //存包名
            paramMap.put("model_package",model_package);
            paramMap.put("service_package",service_package);
            paramMap.put("dao_package",dao_package);
            paramMap.put("xml_mapper_package",xml_mapper_package);
            paramMap.put("controller_package",controller_package);

            //直接覆盖在项目的
            String path_jsp=dir_jsp;
            String path_js=dir_js;
            String path_controller=dir_controller;
            String path_service=dir_service;
            String path_serviceimpl=dir_serviceimpl;
            String path_mapper=dir_mapper;
            String path_mapperxml=dir_mapperxml;
            String path_model=dir_model;

            //覆盖项目文件
            if(ToolUtil.isNotEmpty(genTableColumnEdit.getIsCover())&&genTableColumnEdit.getIsCover().equals("1")){
                //生成页面page页面
            toPage(path_jsp,pascalTableName,paramMap);
            //生成页面page页面的js文件
            toPage_js(path_js,pascalTableName,paramMap);
            //生成controller
            toController(path_controller,pascalTableName,paramMap);
            //生成service接口
            toService(path_service,pascalTableName,paramMap);
            //生成impl实现类
            toServiceImpl(path_serviceimpl,pascalTableName,paramMap);
            //生成mapper接口实现类
            toMapper(path_mapper,pascalTableName,paramMap);
            //生成mapper的xml文件
            toMapperXml(path_mapperxml,pascalTableName,paramMap);
            //生成model
            toModel(path_model,pascalTableName,paramMap);
            }

            //生成在自己文件夹的
            toPage(outputDir+tableName+"/page/",pascalTableName,paramMap);
            //生成页面page页面的js文件
            toPage_js(outputDir+tableName+"/page/",pascalTableName,paramMap);
            //生成controller
            toController(outputDir+tableName+"/controller/",pascalTableName,paramMap);
            //生成service接口
            toService(outputDir+tableName+"/service/",pascalTableName,paramMap);
            //生成impl实现类
            toServiceImpl(outputDir+tableName+"/service/",pascalTableName,paramMap);
            //生成mapper接口实现类
            toMapper(outputDir+tableName+"/mapper/",pascalTableName,paramMap);
            //生成mapper的xml文件
            toMapperXml(outputDir+tableName+"/mapper/",pascalTableName,paramMap);
            //生成model
            toModel(outputDir+tableName+"/model/",pascalTableName,paramMap);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    //生成页面page
    private void toPage(String path,String pascalTableName,Map<String, Object> paramMap) throws IOException,TemplateException{
        ToolUtil.mkdirs(path);

        String lowerAngularjsHtmlName = toLowerCaseFirstOne(pascalTableName) + "List.jsp"; //文件名，首个字母小写
        Template angularjsHtmlTemplate = configuration.getTemplate("demoList.jsp");//模板
        Writer angularjsHtmlWriter = new OutputStreamWriter(new FileOutputStream(path+ lowerAngularjsHtmlName), "UTF-8");//输出
        angularjsHtmlTemplate.process(paramMap, angularjsHtmlWriter);
        angularjsHtmlWriter.close();

        String lowerAngularjsHtmlNameEdit = toLowerCaseFirstOne(pascalTableName) + "Edit.jsp"; //文件名，首个字母小写
        Template angularjsHtmlTemplateEdit = configuration.getTemplate("demoEdit.jsp");//模板
        Writer angularjsHtmlWriterEdit = new OutputStreamWriter(new FileOutputStream(path+ lowerAngularjsHtmlNameEdit), "UTF-8");//输出
        angularjsHtmlTemplateEdit.process(paramMap, angularjsHtmlWriterEdit);
        angularjsHtmlWriter.close();
    }

    private void toPage_js(String path,String pascalTableName,Map<String, Object> paramMap) throws IOException,TemplateException{
        ToolUtil.mkdirs(path);
        String jsCtrlFileName = toLowerCaseFirstOne(pascalTableName) + "List.js";//js ctrl 的文件名
        Template angularjsCtrlTemplate = configuration.getTemplate("demoList.js");//模板
        Writer angularjsCtrlWriter = new OutputStreamWriter(new FileOutputStream(path + jsCtrlFileName), "UTF-8");//输出
        angularjsCtrlTemplate.process(paramMap, angularjsCtrlWriter);
        angularjsCtrlWriter.close();

        String jsCtrlFileNameEdit = toLowerCaseFirstOne(pascalTableName) + "Edit.js";//js ctrl 的文件名
        Template angularjsCtrlTemplateEdit = configuration.getTemplate("demoEdit.js");//模板
        Writer angularjsCtrlWriterEdit = new OutputStreamWriter(new FileOutputStream(path+ jsCtrlFileNameEdit), "UTF-8");//输出
        angularjsCtrlTemplateEdit.process(paramMap, angularjsCtrlWriterEdit);
        angularjsCtrlWriter.close();
    }

    //生成controller
    private void toController(String path,String pascalTableName,Map<String, Object> paramMap) throws IOException,TemplateException{
        ToolUtil.mkdirs(path);
        String serviceName = pascalTableName + "Controller.java"; //文件名
        Template serviceTemplate = configuration.getTemplate("DemoController.java");//模板
        Writer serviceWriter = new OutputStreamWriter(new FileOutputStream(path+ serviceName), "UTF-8");//输出
        serviceTemplate.process(paramMap, serviceWriter);
        serviceWriter.close();
    }

    //生成service
    private void toService(String path,String pascalTableName,Map<String, Object> paramMap) throws IOException,TemplateException{
        ToolUtil.mkdirs(path);
        String serviceName = "I"+pascalTableName + "Service.java"; //文件名
        Template serviceTemplate = configuration.getTemplate("Service.java");//模板
        Writer serviceWriter = new OutputStreamWriter(new FileOutputStream(path+ serviceName), "UTF-8");//输出
        serviceTemplate.process(paramMap, serviceWriter);
        serviceWriter.close();
    }

    //生成impl
    private void toServiceImpl(String path,String pascalTableName,Map<String, Object> paramMap) throws IOException,TemplateException{
        ToolUtil.mkdirs(path);
        String serviceName = pascalTableName + "ServiceImpl.java"; //文件名
        Template serviceTemplate = configuration.getTemplate("ServiceImpl.java");//模板
        Writer serviceWriter = new OutputStreamWriter(new FileOutputStream(path+ serviceName), "UTF-8");//输出
        serviceTemplate.process(paramMap, serviceWriter);
        serviceWriter.close();
    }

    //生成mapper
    private void toMapper(String path,String pascalTableName,Map<String, Object> paramMap) throws IOException,TemplateException{
        ToolUtil.mkdirs(path);
        //Ext接口部分
        String serviceName = "Ext" + pascalTableName + "Mapper.java"; //文件名
        Template serviceTemplate = configuration.getTemplate("ExtDemoMapper.java");//模板
        Writer serviceWriter = new OutputStreamWriter(new FileOutputStream(path+ serviceName), "UTF-8");//输出
        serviceTemplate.process(paramMap, serviceWriter);
        serviceWriter.close();
    }

    //生成xml
    private void toMapperXml(String path,String pascalTableName,Map<String, Object> paramMap) throws IOException,TemplateException{
        ToolUtil.mkdirs(path);
        //Ext接口部分
        String serviceName = "Ext" + pascalTableName + "Mapper.xml"; //文件名
        Template serviceTemplate = configuration.getTemplate("ExtDemoMapper.xml");//模板
        Writer serviceWriter = new OutputStreamWriter(new FileOutputStream(path+ serviceName), "UTF-8");//输出
        serviceTemplate.process(paramMap, serviceWriter);
        serviceWriter.close();
    }

    //生成Model
    private void toModel(String path,String pascalTableName,Map<String, Object> paramMap) throws IOException,TemplateException{
        ToolUtil.mkdirs(path);
        //实体List
        String serviceName =  pascalTableName + "List.java"; //文件名
        Template serviceTemplate = configuration.getTemplate("DemoList.java");//模板
        Writer serviceWriter = new OutputStreamWriter(new FileOutputStream(path+ serviceName), "UTF-8");//输出
        serviceTemplate.process(paramMap, serviceWriter);
        serviceWriter.close();

        //实体Edit
        serviceName =  pascalTableName + "Edit.java"; //文件名
        serviceTemplate = configuration.getTemplate("DemoEdit.java");//模板
        serviceWriter = new OutputStreamWriter(new FileOutputStream(path+ serviceName), "UTF-8");//输出
        serviceTemplate.process(paramMap, serviceWriter);
        serviceWriter.close();

    }

    //首字母转小写
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))){
            return s;
        }
        else{
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    //首字母转大写
    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0))){
            return s;
        }
        else{
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    public GenTableColumnEdit getGenTableColumnEdit() {
        return genTableColumnEdit;
    }

    public void setGenTableColumnEdit(GenTableColumnEdit genTableColumnEdit) {
        this.genTableColumnEdit = genTableColumnEdit;
    }

    public static void main(String[] args) {

    }
}

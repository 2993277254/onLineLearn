package com.lsfly.commons;

import com.lsfly.bas.model.system.ext.GenTableColumnEdit;
import com.lsfly.util.PropertiesUtil;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.config.*;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

;
;

/**
 * @author huoquan
 * @date 2018/8/24.
 */
public class GeneratorConfig {
    public static void main(String[] args){

    }

    public static void createGen(GenTableColumnEdit genTableColumnEdit){
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
        try {
        /*配置xml配置项*/
            List<String> warnings = new ArrayList<String>();
            boolean overwrite = true;
            Configuration config = new Configuration();
            Context context = new Context(ModelType.CONDITIONAL);
            context.setTargetRuntime("org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl");
            context.setId("MySQLTables");

            //格式化信息
            PluginConfiguration pluginConfiguration = new PluginConfiguration();
            pluginConfiguration.setConfigurationType("org.mybatis.generator.plugins.RowBoundsPlugin");
            context.addPluginConfiguration(pluginConfiguration);

            PluginConfiguration pluginConfiguration1 = new PluginConfiguration();
            pluginConfiguration1.setConfigurationType("org.mybatis.generator.plugins.SerializablePlugin");
            context.addPluginConfiguration(pluginConfiguration1);

            PluginConfiguration pluginConfiguration2 = new PluginConfiguration();
            pluginConfiguration2.setConfigurationType("org.mybatis.generator.plugins.ToStringPlugin");
            context.addPluginConfiguration(pluginConfiguration2);

            PluginConfiguration pluginConfiguration3 = new PluginConfiguration();
            pluginConfiguration3.setConfigurationType("org.mybatis.generator.plugins.EqualsHashCodePlugin");
            context.addPluginConfiguration(pluginConfiguration3);

            PluginConfiguration pluginConfiguration4 = new PluginConfiguration();
            pluginConfiguration4.setConfigurationType("org.mybatis.generator.plugins.VirtualPrimaryKeyPlugin");
            context.addPluginConfiguration(pluginConfiguration4);

            PluginConfiguration pluginConfiguration5 = new PluginConfiguration();
            pluginConfiguration5.setConfigurationType("org.mybatis.generator.plugins.RenameExampleClassPlugin");
            context.addPluginConfiguration(pluginConfiguration5);

            //自定义插件
            PluginConfiguration pluginConfiguration6 = new PluginConfiguration();
            pluginConfiguration6.setConfigurationType("com.lsfly.commons.plugins.MinMaxPlugin");
            context.addPluginConfiguration(pluginConfiguration6);



            //设置是否生成注释
            CommentGeneratorConfiguration commentGeneratorConfiguration = new CommentGeneratorConfiguration();
//            commentGeneratorConfiguration.addProperty("suppressAllComments","true");
            commentGeneratorConfiguration.addProperty("suppressDate","true");
            context.setCommentGeneratorConfiguration(commentGeneratorConfiguration);

            //设置连接数据库
            JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
            jdbcConnectionConfiguration.setDriverClass(PropertiesUtil.getValue("jdbc.driver"));
            jdbcConnectionConfiguration.setConnectionURL(PropertiesUtil.getValue("jdbc.url"));
            jdbcConnectionConfiguration.setPassword(PropertiesUtil.getValue("jdbc.password"));
            jdbcConnectionConfiguration.setUserId(PropertiesUtil.getValue("jdbc.username"));
            context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);


            JavaTypeResolverConfiguration javaTypeResolverConfiguration = new JavaTypeResolverConfiguration();
            //是否使用bigDecimal， false可自动转化以下类型（Long, Integer, Short, etc.）
            javaTypeResolverConfiguration.addProperty("forceBigDecimals","false");
            context.setJavaTypeResolverConfiguration(javaTypeResolverConfiguration);


            //生成实体类的地址
            JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
            javaModelGeneratorConfiguration.setTargetPackage(GenViewAndControl.model_package
                    .replaceAll(GenViewAndControl.module,genTableColumnEdit.getGenTable().getModule())); //${model.package}
            javaModelGeneratorConfiguration.setTargetProject(projectPath+GenViewAndControl.model_package_dir
                    .replaceAll(GenViewAndControl.module,genTableColumnEdit.getGenTable().getModule()));  //${model.package.dir}
            javaModelGeneratorConfiguration.addProperty("enableSubPackages","ture");
            javaModelGeneratorConfiguration.addProperty("trimStrings","true");
            context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);

            //生成的xml的地址
            SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
            sqlMapGeneratorConfiguration.setTargetPackage(GenViewAndControl.xml_mapper_package
                    .replaceAll(GenViewAndControl.module,genTableColumnEdit.getGenTable().getModule())); //${xml.mapper.package}
            sqlMapGeneratorConfiguration.setTargetProject(projectPath+GenViewAndControl.xml_mapper_package_dir
                    .replaceAll(GenViewAndControl.module,genTableColumnEdit.getGenTable().getModule()));  //${xml.mapper.package.dir}
            sqlMapGeneratorConfiguration.addProperty("enableSubPackages","ture");
            context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);

            //生成mapxml对应client，也就是接口dao
            JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
            javaClientGeneratorConfiguration.setTargetPackage(GenViewAndControl.dao_package
                    .replaceAll(GenViewAndControl.module,genTableColumnEdit.getGenTable().getModule())); //${dao.package}
            javaClientGeneratorConfiguration.setTargetProject(projectPath+GenViewAndControl.dao_package_dir
                    .replaceAll(GenViewAndControl.module,genTableColumnEdit.getGenTable().getModule()));  //${dao.package.dir}
            javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
            javaClientGeneratorConfiguration.addProperty("enableSubPackages","ture");
            context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);




            TableConfiguration tableConfiguration = new TableConfiguration(context);
            tableConfiguration.setTableName(genTableColumnEdit.getGenTable().getName());
            context.addTableConfiguration(tableConfiguration);


            config.addContext(context);

            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            System.out.println("warnings.size():"+warnings.size());
            ProgressCallback progressCallback =new GenViewAndControl(genTableColumnEdit);  //可以生成前端层
            myBatisGenerator.generate(progressCallback);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

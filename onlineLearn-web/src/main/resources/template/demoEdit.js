/**
 * ${FileName}Edit.jsp的js文件，包括查询，编辑操作
 */
var ${FileName}Edit = avalon.define({
    $id: "${FileName}Edit",
    baseFuncInfo: baseFuncInfo//底层基本方法

});

layui.use(['index'],function(){
    //加载layui的模块，index模块是基础模块，也可添加其它
    avalon.ready(function () {
        //所有的入口事件写在这里...
        <#list genTableColumnList as col>
        <#if col.getIsEdit()=="1">
        <#if col.getEditType()=="date"||col.getEditType()=="datetime"||col.getEditType()=="time">
        //初始化表单元素,日期时间选择器
        var laydate=layui.laydate;
        <#break>
        </#if>
        </#if>
        </#list>
        <#list genTableColumnList as col>
        <#if col.getIsEdit()=="1">
        <#if col.getEditType()=="date"||col.getEditType()=="datetime"||col.getEditType()=="time">
        laydate.render({
            elem: '#${col.getJavaField()}'
            ,type: '${col.getEditType()}'
        });
        </#if>
        </#if>
        </#list>
        var id=GetQueryString("id");  //接收变量
        //获取实体信息
        getInfo(id,function(data){
            //在回调函数中，做其它操作，比如获取下拉列表数据，获取其它信息
            //...
            <#list genTableColumnList as col>
            <#if col.getIsEdit()=="1">
            <#if col.getEditType()=="upload">
            //初始化上传组件
            initUpload(data);
            <#break>
            </#if>
            </#if>
            </#list>
        });
        avalon.scan();
    });
});

/**
 * 获取实体
 * @param id
 * @param $callback 成功验证后的回调函数，
 * 可做其它操作，比如获取下拉列表数据，获取其它信息
 */
function  getInfo(id,$callback){
    if(isEmpty(id)){
        //新增
        typeof $callback === 'function' && $callback({}); //返回一个回调事件
    }else{
        //编辑
        var param={
            "${id_java}":id
        };
        _ajax({
            type: "POST",
            //loading:false,  //是否ajax启用等待旋转框，默认是true
            url: getRootPath()+"${FileName}/getInfo.do",
            data:param,
            dataType: "json",
            done:function(data){
                //表单初始赋值
                var form=layui.form; //调用layui的form模块
                <#list genTableColumnList as col>
                <#if col.getIsEdit()=="1">
                <#if col.getEditType()=="date"||col.getEditType()=="datetime"||col.getEditType()=="time">
                //初始化表单元素,日期时间选择器
                var util=layui.util;
                <#break>
                </#if>
                </#if>
                </#list>
                <#list genTableColumnList as col>
                <#if col.getIsEdit()=="1">
                <#if col.getEditType()=="date">
                data.${col.getJavaField()}=util.toDateString(data.${col.getJavaField()},"yyyy-MM-dd");
                <#elseif col.getEditType()=="datetime">
                data.${col.getJavaField()}=util.toDateString(data.${col.getJavaField()},"yyyy-MM-dd HH:mm:ss");
                <#elseif col.getEditType()=="time">
                data.${col.getJavaField()}=util.toDateString(data.${col.getJavaField()},"HH:mm:ss");
                </#if>
                </#if>
                </#list>
                form.val('${FileName}Edit_form', data);
                typeof $callback === 'function' && $callback(data); //返回一个回调事件
            }
        });
    }
}
/**
 * 验证表单
 * @param $callback 成功验证后的回调函数
 */
function verify_form($callback){
    //监听提交,先定义个隐藏的按钮
    var form=layui.form; //调用layui的form模块
    form.on('submit(${FileName}Edit_submit)', function(data){
        //通过表单验证后
        var field = data.field; //获取提交的字段
        typeof $callback === 'function' && $callback(field); //返回一个回调事件
    });
    $("#${FileName}Edit_submit").trigger('click');
}

/**
 * 关闭弹窗
 * @param $callBack  成功修改后的回调函数，比如可用作于修改后查询列表
 */
function save($callback){  //菜单保存操作
    //对表单验证
    verify_form(function(field){
        //成功验证后
        var param=field; //表单的元素
        //可以继续添加需要上传的参数
        _ajax({
            type: "POST",
            //loading:true,  //是否ajax启用等待旋转框，默认是true
            url: getRootPath()+"${FileName}/saveOrEdit.do",
            data:param,
            dataType: "json",
            done:function(data){
                typeof $callback === 'function' && $callback(data); //返回一个回调事件
            }
        });
    });
}

<#list genTableColumnList as col>
<#if col.getIsEdit()=="1">
<#if col.getEditType()=="upload">
//初始化上传组件
function initUpload(data) {
<#break>
</#if>
</#if>
</#list>
    <#list genTableColumnList as col>
    <#if col.getIsEdit()=="1">
    <#if col.getEditType()=="upload">
    _layuiUpload({
        elem: '#${col.getJavaField()}_upload'
        ,showImgDiv:"#${col.getJavaField()}_upload_div"  //自定义字段，可选，用来显示上传后的图片的div
        ,showHttpPath:getHttpPath()  //自定义字段，可选，用于拼接显示图片的http映射路径，比如http:192.168.1.126:8081
        ,showImgSrc:data.${col.getJavaField()}  //自定义字段，可选，在div显示图片的src，通常用于编辑后的回显，相对路径，比如‘/upload/XXX/XXX.jpg’
        ,url: getRootPath()+'system/uploadFile.do' //统一调用公用的系统方法
        ,accept: 'images'
        ,method: 'post'
        ,data:{module:"${moduleName}"}  //必须填，请求上传接口的额外参数。如：data: {id: 'xxx'}
        ,acceptMime: 'image/*'
        ,done: function(res){
        //...上传成功后的事件
        }
    });
    </#if>
    </#if>
    </#list>
<#list genTableColumnList as col >
<#if col.getIsEdit() == "1" >
<#if col.getEditType() == "upload" >
}
<#break>
</#if>
</#if>
</#list>



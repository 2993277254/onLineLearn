<%@ page language="java" import="java.util.*"
         contentType="text/html;charset=utf-8"%>
<%@ include file="/WEB-INF/base/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title></title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
</head>
<body ms-controller="${FileName}Edit">
<div class="layui-form" lay-filter="${FileName}Edit_form" id="${FileName}Edit_form" style="padding: 20px 30px 0 0;">
<#assign index = 0>
<#assign cruNum = "0">
<#assign count = 0>
<#list genTableColumnList as col>
    <#if col.getJavaField()==id_java>
        <div class="layui-form-item  layui-hide">
            <label class="layui-form-label">ID</label>
            <div class="layui-input-inline">
                <input type="hidden" name="${col.getJavaField()}" placeholder="请输入" autocomplete="off" class="layui-input">
            </div>
        </div>
    <#else>
        <#if col.getIsEdit()=="1">
        <#assign index = index + 1>
        <#if (index != 1)&&(col.getField3()=="1"||cruNum!=col.getField3())||(col.getField3()==(count?c))>
        </div>
        </#if>
        <#if (col.getField3()=="1"|| cruNum!=col.getField3())||(col.getField3()==(count?c))>
        <#assign count = 0>
        <div class="layui-form-item">
        </#if>
        <#assign cruNum = col.getField3()>
        <#assign count = count + 1>
            <#if col.getField3()!="1">
          <div class="layui-inline"<#if col.getEditType()=="upload"> style="vertical-align: top"</#if>>
            </#if>
            <label class="layui-form-label"><#if col.getIsRequired()=="1"><span class="edit-verify-span">*</span></#if>${col.getShowName()}</label>
        <#if col.getEditType()=="input">
            <div class="layui-input-inline">
                <input type="input" name="${col.getJavaField()}"<#if col.getColumnLength()??> maxlength="${col.getColumnLength()}"</#if> <#if col.getIsRequired()=="1">lay-verify="required<#if col.getEditVerify()!="">|${col.getEditVerify()}</#if>"</#if> placeholder="请输入" autocomplete="off" class="layui-input">
            </div>
        <#elseif col.getEditType()=="select">
            <div class="layui-input-inline">
                <select name="${col.getJavaField()}" <#if col.getIsRequired()=="1">lay-verify="required<#if col.getEditVerify()!="">|${col.getEditVerify()}</#if>"</#if>>
                    <option value=""></option>
                    <option  ms-attr="{value:el.value}" ms-text="@el.name"
                           ms-for="($index, el) in @baseFuncInfo.getSysDictByCode('${col.getEditData()}')"></option>
                </select>
            </div>
        <#elseif col.getEditType()=="checkbox">
            <div class="layui-input-block">
                <input type="checkbox" <#if col.getIsRequired()=="1">lay-verify="checkbox" lay-verify-msg="${col.getShowName()}至少选一项" </#if> name="${col.getJavaField()}" ms-attr="{value:el.value,title:el.name}"
                       lay-skin="primary" ms-for="($index, el) in @baseFuncInfo.getSysDictByCode('${col.getEditData()}')">
            </div>
        <#elseif col.getEditType()=="radio">
            <div class="layui-input-block">
                <input type="radio" <#if col.getIsRequired()=="1">lay-verify="radio" lay-verify-msg="${col.getShowName()}至少选一项" </#if> name="${col.getJavaField()}" ms-attr="{value:el.value,title:el.name,checked:true&&$index==0}"
                       ms-for="($index, el) in @baseFuncInfo.getSysDictByCode('${col.getEditData()}')">
            </div>
        <#elseif col.getEditType()=="textarea">
            <div class="layui-input-block">
                <textarea name="${col.getJavaField()}"<#if col.getColumnLength()??> maxlength="${col.getColumnLength()}"</#if> <#if col.getIsRequired()=="1">lay-verify="required"</#if> placeholder="请输入" class="layui-textarea"></textarea>
            </div>
        <#elseif col.getEditType()=="date">
            <div class="layui-input-inline">
                <input type="input" name="${col.getJavaField()}" <#if col.getIsRequired()=="1">lay-verify="required<#if col.getEditVerify()!="">|${col.getEditVerify()}</#if>"</#if> id="${col.getJavaField()}" placeholder="yyyy-MM-dd" autocomplete="off" class="layui-input">
            </div>
        <#elseif col.getEditType()=="datetime">
            <div class="layui-input-inline">
                <input type="input" name="${col.getJavaField()}" <#if col.getIsRequired()=="1">lay-verify="required<#if col.getEditVerify()!="">|${col.getEditVerify()}</#if>"</#if> id="${col.getJavaField()}" placeholder="yyyy-MM-dd HH:mm:ss"  autocomplete="off" class="layui-input">
            </div>
        <#elseif col.getEditType()=="time">
            <div class="layui-input-inline">
                <input type="input" name="${col.getJavaField()}" <#if col.getIsRequired()=="1">lay-verify="required<#if col.getEditVerify()!="">|${col.getEditVerify()}</#if>"</#if> id="${col.getJavaField()}" placeholder="HH:mm:ss"  autocomplete="off" class="layui-input">
            </div>
        <#elseif col.getEditType()=="upload">
            <%-- 隐藏的文本域，用于存文件url--%>
            <input type="hidden" name="${col.getJavaField()}" <#if col.getIsRequired()=="1">lay-verify="required<#if col.getEditVerify()!="">|${col.getEditVerify()}</#if>"</#if> placeholder="请输入" autocomplete="off" class="layui-input">
            <button style="float: left;" type="button" class="layui-btn" id="${col.getJavaField()}_upload">上传照片</button>
            <%-- 用于显示上传完的图片--%>
            <div class="layui-input-inline pl-15" style="width: auto" id="${col.getJavaField()}_upload_div"></div>
        </#if>
        <#if col.getField3()!="1">
          </div>
        </#if>
     </#if>
    </#if>
</#list>
        <#if index != 0>
        </div>
        </#if>
        <%-- 隐藏的提交按钮，必须要，用于验证表单，触发提交表单的作用--%>
        <div class="layui-form-item layui-hide">
            <button class="layui-btn" lay-submit lay-filter="${FileName}Edit_submit" id="${FileName}Edit_submit">提交</button>
        </div>
</div>
<script type="text/javascript" src="<#noparse>${baseprefix}</#noparse>/js/${moduleName}/${FileName}Edit.js?t=<%= System.currentTimeMillis()%>"></script>
</body>
</html>
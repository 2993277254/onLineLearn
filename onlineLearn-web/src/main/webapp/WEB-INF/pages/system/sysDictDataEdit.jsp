<%@ page language="java" import="java.util.*"
         contentType="text/html;charset=utf-8"%>
<%@ include file="/WEB-INF/base/adminCommon.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title></title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
</head>
<body ms-controller="sysDictDataEdit" class="adminTop">
<div class="layui-form" lay-filter="sysDictDataEdit_form" id="sysDictDataEdit_form" style="padding: 20px 30px 0 0;">
    <div class="layui-form-item  layui-hide">
        <label class="layui-form-label">ID</label>
        <div class="layui-input-inline">
            <input type="hidden" name="id" placeholder="请输入" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item  layui-hide">
        <label class="layui-form-label">dictId</label>
        <div class="layui-input-inline">
            <input type="hidden" name="dictId" placeholder="请输入" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item  layui-hide">
        <label class="layui-form-label">dictId</label>
        <div class="layui-input-inline">
            <input type="hidden" name="dictId" placeholder="请输入" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">字典类型</label>
        <div class="layui-input-inline">
            <input type="input" name="dictType" placeholder="请输入" readonly autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label"><span class="edit-verify-span">*</span>标签名</label>
        <div class="layui-input-inline">
            <input type="input" name="dictDataName" maxlength="100" lay-verify="required" placeholder="请输入" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label"><span class="edit-verify-span">*</span>数据值</label>
        <div class="layui-input-inline">
            <input type="input" name="dictDataValue" maxlength="100" lay-verify="required" placeholder="请输入" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label"><span class="edit-verify-span">*</span>排序</label>
        <div class="layui-input-inline">
            <input type="input" name="dictDataSort" maxlength="10" lay-verify="required|number" placeholder="请输入" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">描述</label>
        <div class="layui-input-inline">
            <textarea name="dictDataDesc" maxlength="100" placeholder="请输入" class="layui-textarea"></textarea>
        </div>
    </div>
    <%-- 隐藏的提交按钮，必须要，用于验证表单，触发提交表单的作用--%>
    <div class="layui-form-item layui-hide">
        <button class="layui-btn" lay-submit lay-filter="sysDictDataEdit_submit" id="sysDictDataEdit_submit">提交</button>
    </div>
</div>
<script type="text/javascript" src="${baseprefix}/js/system/sysDictDataEdit.js?t=<%= System.currentTimeMillis()%>"></script>
</body>
</html>
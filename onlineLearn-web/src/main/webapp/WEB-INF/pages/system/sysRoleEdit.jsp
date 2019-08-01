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
    <%--<link rel="stylesheet" type="text/css" href="${baseprefix}/layuiadmin/style/admin.css" media="all">--%>

    <link rel="stylesheet" type="text/css" href="${baseprefix}/lib/zTree/v3/css/layuiStyle/layuiStyle.css">

</head>
<body ms-controller="sysRoleEdit">
<div class="layui-form layui-col-xs6" lay-filter="sysRoleEdit_form" id="sysRoleEdit_form" style=" position:fixed;">
    <br>
    <div class="layui-form-item  layui-hide">
        <label class="layui-form-label">ID</label>
        <div class="layui-input-inline">
            <input type="hidden" name="id" placeholder="请输入" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <%--<label class="layui-form-label"><span class="edit-verify-span">*</span>角色名称</label>--%>
        <%--<div class="layui-input-inline">--%>
            <%--<input type="input" name="roleName" maxlength="50" lay-verify="required" placeholder="请输入" autocomplete="off" class="layui-input">--%>
        <%--</div>--%>
            <label class="layui-form-label"><span class="edit-verify-span">*</span>角色</label>
            <div class="layui-input-inline">
                <%--<input type="input" name="identity" maxlength="2" lay-verify="required" placeholder="请输入" autocomplete="off" class="layui-input">--%>
                <input type="radio" lay-verify="radio" lay-verify-msg="单选框至少选一项"  name="identity" ms-attr="{value:el.value,title:el.name,checked:true&&$index==0}"
                       ms-for="($index, el) in @type()">

            </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">角色描述</label>
        <div class="layui-input-inline">
            <textarea name="remark" maxlength="255"  placeholder="请输入" class="layui-textarea"></textarea>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">角色状态</label>
        <div class="layui-input-inline">
            <input type="radio" lay-verify="radio" name="status"
                   ms-attr="{value:el.value,title:el.name,checked:true&&$index==0}"
                   ms-for="($index, el) in @baseFuncInfo.getSysDictByCode('sys_is_delete_')">
        </div>
    </div>
    <%-- 隐藏的提交按钮，必须要，用于验证表单，触发提交表单的作用--%>
    <div class="layui-form-item layui-hide">
        <button class="layui-btn" lay-submit lay-filter="sysRoleEdit_submit" id="sysRoleEdit_submit">提交</button>
    </div>
    <%--<div class="layui-inline layui-layout-right">--%>
    <%--<label class="layui-form-label">弹窗文本框</label>--%>
    <%--<div class="layui-input-block">--%>
    <%--<input type="text" name="username" placeholder="请选择" readonly autocomplete="off" class="layui-input" onclick="selectMenu(this);">--%>
    <%--<i class="layui-icon input-icon-right layui-icon-search"></i>--%>
    <%--</div>--%>
    <%--</div>--%>
</div>
<div class="layui-form layui-col-xs6" lay-filter="sysOpenTree_form" id="sysOpenTree_form" style="float:right;position:relative;">
    <br><span>菜单权限：</span>
    <div class="layui-inline">
        <input type="text" name="searchnName" placeholder="请输入" autocomplete="off" class="layui-input" style="width: 170px;">
    </div>
    <button class="layui-btn" lay-submit lay-filter="sysOpenTree_search"><i class="layui-icon layui-icon-search"></i></button>
    <ul id="sysOpenTree" class="ztree">

    </ul>
</div>
<!-- 引入js-->
<script type="text/javascript" src="${baseprefix}/js/system/sysRoleEdit.js?t=<%= System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="${baseprefix}/lib/zTree/v3/js/jquery.ztree.all.min.js"></script>
</body>
</html>
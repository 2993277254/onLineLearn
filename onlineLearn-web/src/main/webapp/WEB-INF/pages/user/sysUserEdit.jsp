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
<%--class="adminTop"--%>
<body ms-controller="sysUserEdit" >
<div class="layui-form" lay-filter="sysUserEdit_form" id="sysUserEdit_form" style="padding: 20px 30px 0 0;">
        <div class="layui-form-item  layui-hide">
            <label class="layui-form-label">ID</label>
            <div class="layui-input-inline">
                <input type="hidden" name="uid" placeholder="请输入" autocomplete="off" class="layui-input">
            </div>
        </div>
        <%--<div class="layui-form-item">--%>
            <%--<label class="layui-form-label"><span class="edit-verify-span">*</span>账号</label>--%>
            <%--<div class="layui-input-inline">--%>
                <%--<input type="input" name="userName" maxlength="20" lay-verify="required" placeholder="请输入" autocomplete="off" class="layui-input">--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<div class="layui-form-item">--%>
            <%--<label class="layui-form-label"><span class="edit-verify-span">*</span>昵称</label>--%>
            <%--<div class="layui-input-inline">--%>
                <%--<input type="input" name="nickName" maxlength="20" lay-verify="required" placeholder="请输入" autocomplete="off" class="layui-input">--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<div class="layui-form-item">--%>
            <%--<label class="layui-form-label"><span class="edit-verify-span">*</span>性别</label>--%>
            <%--<div class="layui-input-inline">--%>
                <%--<input type="input" name="sex" maxlength="2" lay-verify="required" placeholder="请输入" autocomplete="off" class="layui-input">--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<div class="layui-form-item">--%>
            <%--<label class="layui-form-label">头像路径</label>--%>
            <%--<div class="layui-input-inline">--%>
                <%--<input type="input" name="pictureUrl" maxlength="255"  placeholder="请输入" autocomplete="off" class="layui-input">--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<div class="layui-form-item">--%>
            <%--<label class="layui-form-label"><span class="edit-verify-span">*</span>备注</label>--%>
            <%--<div class="layui-input-inline">--%>
                <%--<input type="input" name="remark" maxlength="255" lay-verify="required" placeholder="请输入" autocomplete="off" class="layui-input">--%>
            <%--</div>--%>
        <%--</div>--%>
        <div class="layui-form-item">
            <label class="layui-form-label"><span class="edit-verify-span">*</span>身份</label>
            <div class="layui-input-inline">
                <%--<input type="input" name="identity" maxlength="2" lay-verify="required" placeholder="请输入" autocomplete="off" class="layui-input">--%>
                    <input type="radio" lay-verify="radio" lay-verify-msg="单选框至少选一项"  name="identity" ms-attr="{value:el.value,title:el.name,checked:true&&$index==0}"
                           ms-for="($index, el) in @type('${sessionScope.ollSysUser.identity}')" >

            </div>
        </div>
    <div class="layui-form-item">
        <label class="layui-form-label"><span class="edit-verify-span">*</span>状态</label>
        <div class="layui-input-inline">
            <%--<input type="input" name="identity" maxlength="2" lay-verify="required" placeholder="请输入" autocomplete="off" class="layui-input">--%>
            <input type="radio" lay-verify="radio" lay-verify-msg="单选框至少选一项"  name="isDelete" ms-attr="{value:el.value,title:el.name,checked:true&&$index==0}"
                   ms-for="($index, el) in @baseFuncInfo.getSysDictByCode('sys_is_delete_')">

        </div>
    </div>
        <%--<div class="layui-form-item">--%>
            <%--<label class="layui-form-label"><span class="edit-verify-span">*</span>在线</label>--%>
            <%--<div class="layui-input-inline">--%>
                <%--<input type="input" name="onlineStatus" maxlength="2"  placeholder="请输入" autocomplete="off" class="layui-input">--%>
            <%--</div>--%>
        <%--</div>--%>
        <%-- 隐藏的提交按钮，必须要，用于验证表单，触发提交表单的作用--%>
        <div class="layui-form-item layui-hide">
            <button class="layui-btn" lay-submit lay-filter="sysUserEdit_submit" id="sysUserEdit_submit">提交</button>
        </div>
</div>
<script type="text/javascript" src="${baseprefix}/js/user/sysUserEdit.js?t=<%= System.currentTimeMillis()%>"></script>
</body>
</html>
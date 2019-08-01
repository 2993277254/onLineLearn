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
<!--引入webuploader CSS-->
<link rel="stylesheet" type="text/css" href="${baseprefix}/lib/webuploader/webuploader.css">
<body ms-controller="tVideoEdit">
<div class="layui-fluid">
<div class=" layui-form" lay-filter="tVideoEdit_form" id="tVideoEdit_form" style="padding: 20px 30px 0 0;">
        <div class="layui-form-item  layui-hide">
            <label class="layui-form-label">ID</label>
            <div class="layui-input-inline">
                <input type="hidden" name="uid" placeholder="请输入" autocomplete="off" class="layui-input">
            </div>
        </div>
    <div class="layui-upload">
        <%--            <button type="button" class="layui-btn layui-btn-normal" id="testList">选择视频</button>--%>
        <div id="selectVideoList">选择视频</div>
        <div class="layui-upload-list">
            <table class="layui-table">
                <thead>
                <tr><th>文件名</th>
                    <th>大小</th>
                    <th>状态</th>
                    <th>操作</th>
                </tr></thead>
                <tbody id="videoList"></tbody>
            </table>
        </div>
        <button type="button" class="layui-btn" id="videoListAction">开始上传</button>
    </div>
    <br>
        <%--<div class="layui-form-item">--%>
            <%--<label class="layui-form-label"><span class="edit-verify-span">*</span>视频保存路径</label>--%>
            <%--<div class="layui-input-inline">--%>
                <%--<input type="input" name="path" maxlength="300" lay-verify="required" placeholder="请输入" autocomplete="off" class="layui-input">--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<div class="layui-form-item">--%>
            <%--<label class="layui-form-label"><span class="edit-verify-span">*</span>视频名称</label>--%>
            <%--<div class="layui-input-inline">--%>
                <%--<input type="input" name="name" maxlength="64" lay-verify="required" placeholder="请输入" autocomplete="off" class="layui-input">--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<div class="layui-form-item">--%>
            <%--<label class="layui-form-label"><span class="edit-verify-span">*</span>服务id</label>--%>
            <%--<div class="layui-input-inline">--%>
                <%--<input type="input" name="streamMediaId" maxlength="64" lay-verify="required" placeholder="请输入" autocomplete="off" class="layui-input">--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<div class="layui-form-item">--%>
            <%--<label class="layui-form-label"><span class="edit-verify-span">*</span>文件大小/单位M</label>--%>
            <%--<div class="layui-input-inline">--%>
                <%--<input type="input" name="videoSize" maxlength="64" lay-verify="required" placeholder="请输入" autocomplete="off" class="layui-input">--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<div class="layui-form-item">--%>
            <%--<label class="layui-form-label"><span class="edit-verify-span">*</span>时长</label>--%>
            <%--<div class="layui-input-inline">--%>
                <%--<input type="input" name="videoTime" maxlength="20" lay-verify="required" placeholder="请输入" autocomplete="off" class="layui-input">--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<div class="layui-form-item">--%>
            <%--<label class="layui-form-label"><span class="edit-verify-span">*</span>格式; RM、RMVB、AVI等</label>--%>
            <%--<div class="layui-input-inline">--%>
                <%--<input type="input" name="videoFormat" maxlength="64" lay-verify="required" placeholder="请输入" autocomplete="off" class="layui-input">--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<div class="layui-form-item">--%>
            <%--<label class="layui-form-label"><span class="edit-verify-span">*</span>时间戳</label>--%>
            <%--<div class="layui-input-inline">--%>
                <%--<input type="input" name="timestamp" lay-verify="required|number" placeholder="请输入" autocomplete="off" class="layui-input">--%>
            <%--</div>--%>
        <%--</div>--%>
        <%-- 隐藏的提交按钮，必须要，用于验证表单，触发提交表单的作用--%>
        <div class="layui-form-item layui-hide">
            <button class="layui-btn" lay-submit lay-filter="tVideoEdit_submit" id="tVideoEdit_submit">提交</button>
        </div>
</div>
</div>
<!--引入webuploader JS-->
<script type="text/javascript" src="${baseprefix}/lib/webuploader/webuploader.js"></script>
    <script type="text/javascript" src="${baseprefix}/js/video/tVideoEdit.js?t=<%= System.currentTimeMillis()%>"></script>
</body>
</html>
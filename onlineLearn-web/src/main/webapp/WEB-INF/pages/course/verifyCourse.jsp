<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/5/26 0026
  Time: 14:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*"
         contentType="text/html;charset=utf-8" %>
<%@ include file="/WEB-INF/base/adminCommon.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>课程审核</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
</head>
<body ms-controller="verifyCourse">
<div class="layui-fluid layui-card">
    <div class="layui-form" id="verifyCourseForm" lay-filter="verifyCourseForm" style="padding: 20px 30px 0 0;">
        <div class="layui-form-item  layui-hide">
            <label class="layui-form-label">ID</label>
            <div class="layui-input-inline">
                <input type="hidden" name="uid" placeholder="请输入" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">审核：</label>
            <div class="layui-input-inline">
                <input type="radio" lay-verify="radio" lay-verify-msg="请选择审核类型" lay-verType="tips" lay-filter="isPassfilter"
                       name="status" ms-attr="{value:el.value,title:el.name}"
                       ms-for="($index, el) in @baseFuncInfo.getSysDictByCode('course_verify_')" :if="el.value!='1'">

            </div>
        </div>
        <div class="layui-form-item layui-hide">
            <label class="layui-form-label">驳回原因</label>
            <div class="layui-input-block">
                <textarea  name="reason"   placeholder="请输入" autocomplete="off"
                          class="layui-textarea"></textarea>
            </div>
        </div>
        <div class="layui-form-item layui-hide">
            <button class="layui-btn" lay-submit lay-filter="verifyCourseForm_submit" id="verifyCourseForm_submit">提交</button>
        </div>
    </div>

</div>
<script type="text/javascript" src="${baseprefix}/js/course/verifyCourse.js?t=<%= System.currentTimeMillis()%>"></script>
</body>
</html>

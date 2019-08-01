<%@ page language="java" import="java.util.*"
         contentType="text/html;charset=utf-8" %>
<%@ include file="/WEB-INF/base/adminCommon.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title></title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
</head>
<body ms-controller="tCourseVideoEdit2">
<div class="layui-form" lay-filter="tCourseVideoEdit2_form" id="tCourseVideoEdit2_form" style="padding: 20px 30px 0 0;">
    <div class="layui-form-item  layui-hide">
        <label class="layui-form-label">ID</label>
        <div class="layui-input-inline">
            <input type="hidden" name="uid" placeholder="请输入" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label"><span class="edit-verify-span">*</span>课时视频</label>
        <div class="layui-input-inline">
            <input type="hidden" name="courseId" maxlength="64" placeholder="请输入" autocomplete="off"
                   class="layui-input">
        </div>
        <button id="uploadCourseVideo" class="layui-btn " style="float: left"><i
                class="layui-icon layui-icon-add-circle-fine">上传视频</i></button>

        <div id="uploading" class="layui-hide" style="width: 100px;float: left;margin-left: 10px">
            <div class="layui-progress layui-progress-big" lay-showpercent="true" lay-filter="filefilter">
                <div class="layui-progress-bar layui-bg-green" lay-percent="0%"></div>
                      
            </div>
            上传进度：
        </div>
        <a id="playView" class="layui-btn layui-hide" style="float:left;margin-left: 10px">预览</a>
    </div>
    <div class="layui-form-item">
        <label style="position: relative;width: 100%;margin-left: 20%;padding-bottom: 10px;color: green"
               :if="isUpload==1" >上传状态:已上传</label>
        <label style="position: relative;width: 100%;margin-left: 20%;padding-bottom: 10px;color: red"
               :if="isUpload==0">上传状态:未上传</label>
        <label style="position: relative;width: 100%;margin-left: 20%;padding-bottom: 10px;color: orange"
               :if="isUpload==2">上传状态:上传中</label>
    </div>
    <div class="layui-form-item">

        <label class="layui-form-label">视频名称</label>
        <div class="layui-input-block">
            <input type="input" name="fileName" readonly autocomplete="off" class="layui-input layui-bg-gray">
        </div>
        <br>
        <label class="layui-form-label">视频大小</label>
        <div class="layui-input-block">
            <input type="input" name="size" readonly autocomplete="off" class="layui-input layui-bg-gray">
        </div>
        <br>
        <%--<label class="layui-form-label">上传的状态</label>--%>
        <%--<div class="layui-input-block">--%>
        <%--<input type="input" name=""   readonly autocomplete="off" class="layui-input layui-bg-gray">--%>
        <%--</div>--%>
    </div>

    <%-- 隐藏的提交按钮，必须要，用于验证表单，触发提交表单的作用--%>
    <div class="layui-form-item layui-hide">
        <button class="layui-btn" lay-submit lay-filter="tCourseVideoEdit2_submit" id="tCourseVideoEdit2_submit">提交
        </button>
    </div>
</div>

<script type="text/javascript" src="${baseprefix}/lib/ckplayer/ckplayer.js?t=<%= System.currentTimeMillis()%>"
        charset="UTF-8"></script>
<script type="text/javascript"
        src="${baseprefix}/js/courseUser/tCourseVideoEdit2.js?t=<%= System.currentTimeMillis()%>"></script>
</body>
</html>
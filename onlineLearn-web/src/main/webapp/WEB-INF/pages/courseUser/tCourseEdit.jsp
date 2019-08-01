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
<body ms-controller="tCourseEdit">
<div class="layui-form layui-fluid layui-row" lay-filter="tCourseEdit_form" id="tCourseEdit_form" style="padding: 20px 30px 0 0;">
    <div class="layui-form-item  layui-hide">
            <label class="layui-form-label">ID</label>
            <div class="layui-input-inline">
                <input type="hidden" name="uid" placeholder="请输入" autocomplete="off" class="layui-input">
            </div>
    </div>
    <div class="layui-form-item  layui-hide">
        <label class="layui-form-label">学习人数</label>
        <div class="layui-input-inline">
            <input type="hidden" name="personNum" placeholder="请输入" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
            <label class="layui-form-label"><span class="edit-verify-span">*</span>课程类型</label>
            <div class="layui-input-block">
                <select name="type" lay-verify="required" lay-search lay-verType="tips" lay-verify-msg="请选择课程类型">
                    <option value="">直接选择或搜索选择</option>
                    <option  ms-attr="{value:el.value}" ms-text="@el.name"
                             ms-for="($index, el) in @baseFuncInfo.getSysDictByCode('course_type_')"></option>
                </select>
            </div>

        </div>
    <div class="layui-form-item">
        <label class="layui-form-label"><span class="edit-verify-span">*</span>课程名称</label>
        <div class="layui-input-block">
            <input lay-verType="tips" type="input" name="name" maxlength="100" lay-verify="required" placeholder="请输入" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label"><span class="edit-verify-span">*</span>简介视频</label>
        <%-- 隐藏的文本域，用于存文件url--%>
        <input type="input" name="introduction"  placeholder="请输入"
               autocomplete="off"  style="width: 1px;border: none" lay-verType="tips" lay-verify="videoData">
        <button style="float: left;" type="button" class="layui-btn" id="introductionUrl_upload">
            <i class="layui-icon layui-icon-add-circle-fine">上传视频</i>
        </button>
        <div  id="uploading" class="layui-hide" style="width: 100px;float: left">
            上传进度：
            <div class="layui-progress layui-progress-big" lay-showpercent="true" lay-filter="filefilter">
                <div class="layui-progress-bar layui-bg-green" lay-percent="0%"></div>      
            </div>
        </div>
        <span id="playView" class=" layui-btn  layui-hide" style="margin-left: 10px;float: left" >预览</span>
    </div>
    <div class="layui-form-item layui-form-text">
        <label class="layui-form-label"><span class="edit-verify-span">*</span>课程概述</label>
        <div class="layui-input-block">
                    <textarea lay-verType="tips" placeholder="请输入简介,最多500个字符" class="layui-textarea" name="summary" lay-verify="required"
                              maxlength="65535"></textarea>
        </div>
    </div>
    <div class="layui-form-item layui-form-text">
        <label class="layui-form-label"><span class="edit-verify-span">*</span>授课目标</label>
        <div class="layui-input-block">
                    <textarea lay-verType="tips" placeholder="请输入授课目标,最多255个字符" class="layui-textarea" name="target" lay-verify="required"
                              maxlength="255"></textarea>
        </div>
    </div>
    <%--<div class="layui-form-item layui-form-text">--%>
        <%--<label class="layui-form-label"><span class="edit-verify-span">*</span>证书要求</label>--%>
        <%--<div class="layui-input-block">--%>
                    <%--<textarea lay-verType="tips" placeholder="请输入证书要求,最多255个字符" class="layui-textarea" name="demand" lay-verify="required"--%>
                              <%--maxlength="255"></textarea>--%>
        <%--</div>--%>
    <%--</div>--%>
    <div class="layui-form-item">
        <label class="layui-form-label"><span class="edit-verify-span">*</span>课程封面</label>
        <%-- 隐藏的文本域，用于存文件url--%>
        <input type="hidden" name="photoUrl" lay-verify="required" placeholder="请输入" autocomplete="off" class="layui-input">
        <button style="float: left;" type="button" class="layui-btn" id="photoUrl_upload">
            <i class="layui-icon layui-icon-add-circle-fine">上传图片</i>
        </button>
        <div  id="uploading" class="layui-hide" style="width: 100px;float: left">
            上传进度：
            <div class="layui-progress" lay-showpercent="true" lay-filter="photoFilter">
                <div class="layui-progress-bar layui-bg-green" lay-percent="0%"></div>      
            </div>

        </div>
        <%-- 用于显示上传完的图片--%>
        <div class="layui-input-inline pl-15" style="width: auto" id="photoUrl_upload_div"></div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label"><span class="edit-verify-span">*</span>课程大纲</label>
        <div class="layui-input-block">
            <input type="input" name="outline"    placeholder="请输入" autocomplete="off" style="width: 1px;border: none" lay-verType="tips" lay-verify="outlineData">
            <button :visible="@type==1" class="layui-btn" style="float: left" onclick="addCourseVideo(1)">添加</button>
            <button :visible="@type==2" class="layui-btn" style="float: left" onclick="addCourseVideo(2)">查看与编辑</button>
            <div class="layui-form-mid layui-word-aux" style="float: left;left: 20px;"><p style="color: red">--添加课程大纲时需要指定相应的课时以及上传对应的视频</p></div>

        </div>
    </div>
        <%-- 隐藏的提交按钮，必须要，用于验证表单，触发提交表单的作用--%>
    <div class="layui-form-item layui-hide">
            <button class="layui-btn" lay-submit lay-filter="tCourseEdit_submit" id="tCourseEdit_submit">提交</button>
    </div>
</div>
<div class="" id="video" style="width: 100%; height: 400px;max-width: 500px;margin: auto;display: none"></div>
<script type="text/javascript" src="${baseprefix}/lib/ckplayer/ckplayer.js?t=<%= System.currentTimeMillis()%>" charset="UTF-8"></script>

<script type="text/javascript" src="${baseprefix}/js/courseUser/tCourseEdit.js?t=<%= System.currentTimeMillis()%>"></script>
</body>
</html>
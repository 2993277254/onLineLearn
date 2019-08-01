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
<body ms-controller="tCourseVideoEdit">
<div class="layui-fluid">

    <div class="layui-form" lay-filter="tCourseVideoEdit_form" id="tCourseVideoEdit_form"
         style="padding: 20px 30px 0 0;">
        <div class="layui-form-item " >
            <button class="layui-btn"  style="position: relative;" onclick="addOutlistIiem(1)"><i class="layui-icon layui-icon-add-circle-fine"></i>章节</button>
               <div class="layui-form-mid">
                   <p style="color: red;float: left">
                       请注意：大纲至少有一个章节，每个章节必须有一个课时，每个课时必须有对应的视频
                   </p>
               </div>
                <br>
            </div>
        <div class="layui-form-item">
            <%--课程章节--%>
            <div ms-for="($index, el) in @outlineList ">
                <div style="width:100%;float:left">
                 <%--<div style="width: 5%;float: left;margin-left: 5%">--%>
                     <%--<input type="checkbox"  name="" title="" lay-skin="primary" checked>--%>
                 <%--</div>--%>
                <label style="float: left;width: 10%;line-height: 38px;"><span class="edit-verify-span">*</span>{{el.name}}</label>
                <div  style="width: 50%;float: left">
                    <input type="input" name="courseId" maxlength="64" lay-verify="required" lay-verType="tips" placeholder="请输入章节名称"
                           autocomplete="off" class="layui-input" style="width: 100%;float: left" ms-duplex="@el.text">
                </div>
                <div class="layui-form-mid" style="float: left;margin-left: 2%;margin-top: -8px;">
                    <button class="layui-btn layui-btn-normal" :click="@addCourseClass(el,$index)"><i class="layui-icon layui-icon-add-circle-fine"></i>课时</button>
                    <button class="layui-btn layui-btn-danger layui-btn-xs" :if="$index!=0" :click="@removeChapter(1,el,$index)">删除章节</button>
                </div>
                </div>
                <%--课时模板--%>
                <div style="width:100%;float:left;padding-top: 10px;padding-bottom: 10px"
                     ms-for="($index2, e2) in @el.courseHouse">

                    <label style="width: 15%;float: left;text-align: right;line-height: 38px;">
                        <span  class="edit-verify-span">*</span>{{e2.name}}
                    </label>
                    <div style="width: 40%;float: left;margin-left: 1%">
                        <input type="input" name="courseId" maxlength="64" lay-verify="required" lay-verType="tips" placeholder="请输入课时名称"
                               autocomplete="off" class="layui-input" ms-duplex="@e2.text">
                    </div>
                    <div class="layui-form-mid" style="float: left;margin-left: 2%;margin-top: -8px;width: 40%">
                        <div style="float: left;line-height: 38px;">课时视频：</div>
                       <input  style="width: 1px;border: none" type="input" lay-verType="tips" lay-verify="videoData" ms-duplex="@e2.video">
                        <button :visible="e2.video==''" class="layui-btn layui-btn-warm"  :click="uplaodCourseVideo(e2.video,$index,$index2)"><i class="layui-icon layui-icon-add-circle-fine"></i>视频</button>
                        <%--<button class="layui-btn layui-btn-sm layui-btn-warm"><i class="layui-icon layui-icon-add-circle-fine"></i>重新上传</button>--%>
                        <button :visible="e2.video!=''" class="layui-btn" :click="uplaodCourseVideo(e2.video,$index,$index2)">查看与编辑视频</button>
                        <button class="layui-btn layui-btn-danger layui-btn-xs" :if="$index2!=0" :click="@removeChapter(2,el,$index,$index2)">删除课时</button>
                    </div>

                </div>
            </div>
        </div>
        <%-- 隐藏的提交按钮，必须要，用于验证表单，触发提交表单的作用--%>
        <div class="layui-form-item layui-hide">
            <button class="layui-btn" lay-submit lay-filter="tCourseVideoEdit_submit" id="tCourseVideoEdit_submit">提交
            </button>
        </div>
    </div>
</div>
<script type="text/javascript" src="${baseprefix}/js/courseUser/tCourseVideoEdit.js?t=<%= System.currentTimeMillis()%>"></script>
</body>
</html>
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
<body ms-controller="tCourseTest">

<%--<div class="layui-form layui-fluid layui-row" lay-filter="tCourseEdit_form" id="tCourseEdit_form" style="padding: 20px 30px 0 0;">--%>
    <%--<div class="layui-form-item">--%>
        <%--<label class="layui-form-label">章节:</label>--%>
        <%--<div class="layui-input-block">--%>
            <%--<input type="radio" name="sex"--%>
                   <%--ms-attr="{value:el.value,title:el.name}"--%>
                   <%--ms-for="($index, el) in @testList"--%>
                   <%-->--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</div>--%>
<div class="layui-fluid">
    <div class="layui-row">
        <div class="layui-col-sm8">
            <div class="layui-form layui-fluid layui-row" lay-filter="tCourseEdit_form" id="tCourseEdit_form"
                 style="padding: 20px 30px 0 0;">
                <div class="layui-form-item">
                    <label class="layui-form-label">章节:</label>
                    <div class="layui-input-block">
                        <input type="radio" lay-filter="zid" name="sex" ms-attr="{value:el.id,title:el.name+':'+el.text,index:$index}" ms-for="($index, el) in @testList">
                        <%--<input type="radio" lay-filter="zid" name="sex" ms-attr="{value:el.id,title:el.name+':'+el.text}" ms-for="($index, el) in @testList">--%>
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-col-sm4" style="padding-top: 20px;">
            <span>请选择左侧的章节</span>
            <br>
            <br>
            <button :if="@iselect==0" class="layui-btn layui-btn-disabled" disabled>添加测验</button>
            <button :if="@iselect==1" class="layui-btn " onclick="testEdit()">添加测验</button>
        </div>
    </div>
</div>
<script type="text/javascript" src="${baseprefix}/js/courseUser/tCourseTest.js?t=<%= System.currentTimeMillis()%>"></script>
</body>
</html>
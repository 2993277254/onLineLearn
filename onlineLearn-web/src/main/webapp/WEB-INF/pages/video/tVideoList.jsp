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
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
</head>
<!--layuiadmin的css-->
<link rel="stylesheet" type="text/css" href="${baseprefix}/layuiadmin/style/admin.css" media="all">
<body ms-controller="tVideoList">
<div class="layui-fluid">
    <div class="layui-card">
        <!--搜素栏的div-->
        <div class="layui-form layui-card-header layuiadmin-card-header-auto search-form"
             id="tVideoList_search" lay-filter="tVideoList_search">
        </div>

        <div class="layui-card-body">
            <!--工具栏的按钮的div，注意：需要增加权限控制-->
            <div style="padding-bottom: 10px;" id="tVideoList_tool">
                <button
                        class="layui-btn"  onclick="batchDel()">删除</button>
                <button
                        class="layui-btn"  onclick="saveOrEdit()">添加</button>
            </div>
            <!--table定义-->
            <table id="tVideoList_table" lay-filter="tVideoList_table"></table>
            <!--table的工具栏按钮定义，注意：需要增加权限控制-->
            <script type="text/html" id="tVideoList_bar">


                <%--<a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>--%>

                <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>

            </script>
        </div>
    </div>
</div>

<div class="" id="video" style="width: 100%; height: 400px;max-width: 500px;margin: auto;display: none"></div>
<script type="text/javascript" src="${baseprefix}/lib/ckplayer/ckplayer.js?t=<%= System.currentTimeMillis()%>" charset="UTF-8"></script>
<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="${baseprefix}/js/video/tVideoList.js?t=<%= System.currentTimeMillis()%>"></script>
</body>
</html>
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
<%--<link rel="stylesheet" type="text/css" href="${baseprefix}/layuiadmin/style/admin.css" media="all">--%>

<body ms-controller="tCourseVideoList">
<div class="layui-fluid">
    <div class="layui-card">
        <div class="layui-form">
            <div class="layui-form-item ">

                    <div class="layui-row">
                        <label class="layui-form-label">用户id</label>
                        <div class="layui-input-block">
                            <input class="layui-input " >
                        </div>
                    </div>
                <br>
                    <div class="layui-row">
                        <div style="margin-left: 20px;">
                        <label class="layui-form-label">用户id</label>
                        <div class="layui-input-inline">
                            <input class="layui-input " >
                        </div>
                        </div>
                    </div>

            </div>

        </div>
    </div>
</div>

<!--请在下方写此页面业务相关的脚本-->

<script type="text/javascript" src="${baseprefix}/js/course/tCourseVideoList.js?t=<%= System.currentTimeMillis()%>"></script>
</body>
</html>
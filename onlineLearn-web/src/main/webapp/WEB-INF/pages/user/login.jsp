<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/4/14 0014
  Time: 0:13
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/WEB-INF/base/common.jsp" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <%--<link rel="stylesheet" href="${baseprefix}/layuiadmin/style/login.css" media="all">--%>
    <link rel="stylesheet" type="text/css" href="${baseprefix}/layuiadmin/style/admin.css" media="all">
    <title>登录界面</title>
</head>
<body ms-controller="login">
<div class="layui-fluid">
    <div class="layui-row  layui-container " id="loginCard">
    <div class="layui-fluid layui-col-md6 layui-col-md-offset3 layui-form layui-form-pane layui-card" lay-filter="loginFilter" id="loginForm" style="background: white">
        <div class="layui-form-item">
            <label class="layui-form-label">账号</label>
            <div class="layui-input-inline">
                <input type="text" name="userName" lay-verify="required" placeholder="请输入账号" lay-verType="tips" autocomplete="off" class="layui-input" maxlength="20">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">密码</label>
            <div class="layui-input-inline">
                <input type="password" name="passWord" lay-verify="required" placeholder="请输入密码" lay-verType="tips" autocomplete="off" class="layui-input" maxlength="20">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">验证码</label>
            <div class="layui-input-inline">
                <input type="text" name="loginImgVerify" lay-verify="required" lay-verType="tips" placeholder="请输入验证码" autocomplete="off" class="layui-input" maxlength="4">
            </div>
            <div class="layui-form-mid layui-word-aux">
                <img style="" class=" flowImg" id="loginImgVerify" src="${baseprefix}/images/srcLoading.gif" alt="点击更换验证码">
                <%--<span class="hrefStyle" onclick="getImg(1)">看不清，换一张</span>--%>
                <span class="hrefStyle" onclick="getVeryCode()">看不清，换一张</span>
            </div>
        </div>
        <div class="layui-form-item ">
                <button class="layui-btn layui-btn-fluid " style="" lay-submit lay-filter="login_submit" id="login_submit">登录</button>
        </div>
    </div>
    </div>

</div>
<%--<div style="position: fixed;bottom: 0;" class="fly-footer"><p><a>111111111111111111</a></p></div>--%>
<script type="text/javascript" src="${baseprefix}/js/user/login.js?t=<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="${baseprefix}/js/footer.js?t=<%= System.currentTimeMillis()%>"></script>
</body>
</html>

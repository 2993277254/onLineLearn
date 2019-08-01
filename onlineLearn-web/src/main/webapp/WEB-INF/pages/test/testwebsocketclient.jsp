<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/4/14 0014
  Time: 22:24
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
    <link rel="stylesheet" href="${baseprefix}/layuiadmin/style/login.css" media="all">
    <link rel="stylesheet" type="text/css" href="${baseprefix}/layuiadmin/style/admin.css" media="all">
    <title>测试websocket即时通讯</title>
</head>
<body>
<div class="layui-fluid layui-container layui-card">
    <!-- 最外边框 -->
    <div style="margin: 20px auto; border: 1px solid blue; width: 300px; height: 500px;">

        <!-- 消息展示框 -->
        <div id="msg" style="width: 100%; height: 70%; border: 1px solid yellow;overflow: auto;"></div>

        <!-- 消息编辑框 -->
        <textarea id="tx" style="width: 100%; height: 20%;"></textarea>

        <!-- 消息发送按钮 -->
        <button id="TXBTN" style="width: 100%; height: 8%;">发送数据</button>

    </div>
</div>
<script type="text/javascript" src="${baseprefix}/lib/sockjs-client/dist/sockjs.min.js"></script>
<script type="text/javascript" src="${baseprefix}/js/test/testwebsocketclient.js?t=<%=System.currentTimeMillis()%>"></script>
</body>
</html>

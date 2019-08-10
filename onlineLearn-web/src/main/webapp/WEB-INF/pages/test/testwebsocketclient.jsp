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

    请输入：<textarea rows="5" cols="10" id="inputMsg" name="inputMsg"></textarea>
<%--    <button οnclick="doSendUser();">发送</button>--%>
    <button onclick="doSendUsers()">群发</button>



    </div>
</div>
<script type="text/javascript" src="${baseprefix}/lib/sockjs-client/dist/sockjs.min.js"></script>
<script type="text/javascript" src="${baseprefix}/js/test/testwebsocketclient.js?t=<%=System.currentTimeMillis()%>"></script>
</body>
</html>

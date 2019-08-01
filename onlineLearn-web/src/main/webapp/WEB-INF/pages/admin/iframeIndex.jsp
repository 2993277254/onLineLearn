<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/1/8
  Time: 20:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/WEB-INF/base/adminCommon.jsp" %>
<html>
<head>
    <title>Title</title>
    <title>后台主页</title>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" type="text/css" href="${baseprefix}/layuiadmin/style/admin.css" media="all">
</head>
<body ms-controller="iframeIndex">


<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="${baseprefix}/js/admin/iframeIndex.js?t=<%= System.currentTimeMillis()%>"></script>
</body>

</html>

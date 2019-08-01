<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/5/10 0010
  Time: 0:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*"
         contentType="text/html;charset=utf-8" %>
<%@ include file="/WEB-INF/base/adminCommon.jsp" %>
<html>
<head>
    <title>预览视频界面</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <link rel="stylesheet" type="text/css" href="${baseprefix}/layuiadmin/style/admin.css" media="all">
</head>
<body ms-controller="preview">
<div class="" id="video" style="width: 100%; height: 400px;max-width: 500px;margin: auto;"></div>
<script type="text/javascript" src="${baseprefix}/lib/ckplayer/ckplayer.js?t=<%= System.currentTimeMillis()%>" charset="UTF-8"></script>
<script type="text/javascript" src="${baseprefix}/js/video/preview.js?t=<%= System.currentTimeMillis()%>"></script>

</body>
</html>

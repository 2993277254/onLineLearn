<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/4/15 0015
  Time: 21:06
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
    <title>无权限页面</title>
    <link rel="stylesheet" type="text/css" href="${baseprefix}/layuiadmin/style/admin.css" media="all">

</head>
<body>
<body layadmin-themealias="default">


<div class="layui-fluid">
    <div class="layadmin-tips">
        <i class="layui-icon layui-icon-password" ></i>

        <div class="layui-text" style="font-size: 20px;">
            抱歉，您无权限访问
        </div>

    </div>
</div>
<style id="LAY_layadmin_theme">.layui-side-menu,.layadmin-pagetabs .layui-tab-title li:after,.layadmin-pagetabs .layui-tab-title li.layui-this:after,.layui-layer-admin .layui-layer-title,.layadmin-side-shrink .layui-side-menu .layui-nav>.layui-nav-item>.layui-nav-child{background-color:#20222A !important;}.layui-nav-tree .layui-this,.layui-nav-tree .layui-this>a,.layui-nav-tree .layui-nav-child dd.layui-this,.layui-nav-tree .layui-nav-child dd.layui-this a{background-color:#009688 !important;}.layui-layout-admin .layui-logo{background-color:#20222A !important;}</style></body>

<script>
    layui.use(['index'],function(){
        avalon.ready(function () {

        });
        avalon.scan();
    });
</script>
<script type="text/javascript" src="${baseprefix}/js/footer.js?t=<%= System.currentTimeMillis()%>"></script>

</body>
</html>

<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/12/19
  Time: 17:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@include file="/WEB-INF/base/base.jsp" %>
<!DOCTYPE html>
<html>
<head>

    <!--此页面请切勿轻易改动-->
    <!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
    <!--[if lt IE 9]>
    <script src="${baseprefix}/lib/html5.min.js"></script>
    <script src="${baseprefix}/lib/respond.min.js"></script>
    <![endif]-->
    <%--layui的css--%>
    <link rel="stylesheet" type="text/css" href="${baseprefix}/layuiadmin/layui/css/layui.css" media="all">
    <%--加载公用css--%>
    <link rel="stylesheet" type="text/css" href="${baseprefix}/css/common.css?t=<%=System.currentTimeMillis()%>">
    <%--加载图标--%>
    <link rel="shortcut icon" href="${baseprefix}/images/fly.ico"/>

    <%--加载jq--%>
    <script type="text/javascript" src="${baseprefix}/lib/jquery/jquery-3.3.1.min.js?"></script>
    <%--加载layui的js--%>
    <script type="text/javascript" src="${baseprefix}/layuiadmin/layui/layui.js"></script>

    <%--加载公用工具类--%>
    <script type="text/javascript" src="${baseprefix}/js/tool.js?t=<%=System.currentTimeMillis()%>"></script>
    <%--加载avalon--%>
    <script type="text/javascript" src="${baseprefix}/lib/avalon/dist/avalon.js"></script>
    <%--avalon过滤器--%>
    <script type="text/javascript" src="${baseprefix}/js/filters.js"></script>
    <%--加载公用js--%>
    <script type="text/javascript" src="${baseprefix}/js/common.js?t=<%=System.currentTimeMillis()%>"></script>
    <script>
        layui.config({
            base: '${baseprefix}/layuiadmin/'//静态资源所在路径
            , version: true //一般用于更新组件缓存，默认不开启。设为true即让浏览器不缓存。也可以设为一个固定的值
        }).extend({
            index: 'lib/index'
        });

    </script>
</head>
<input id="uploadHttpPath" class="layui-hide" value="${sessionScope.uploadHttpPath}">
<input id="userId" class="layui-hide" value="${sessionScope.ollUserId}">
</html>

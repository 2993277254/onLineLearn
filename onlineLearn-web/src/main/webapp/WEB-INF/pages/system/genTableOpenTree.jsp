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
<body ms-controller="sysOpenTree">

<div class="layui-form" lay-filter="sysOpenTree_form" id="sysOpenTree_form" style="padding-top:20px;padding-left: 20px;">
        <div class="layui-inline">
            <input type="text" name="searchnName" placeholder="请输入" autocomplete="off" class="layui-input" style="width: 250px;">
        </div>
        <button class="layui-btn" lay-submit lay-filter="sysOpenTree_search"><i class="layui-icon layui-icon-search"></i></button>
        <a class="layui-btn" lay-href="${ctxsta}/system/genTableTreeList" lay-text="树维护列表"><i class="layui-icon layui-icon-add-1"></i></a>
        <ul id="sysOpenTree" class="ztree">

        </ul>
</div>

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="${baseprefix}/lib/zTree/v3/js/jquery.ztree.all.min.js"></script>
<script type="text/javascript" src="${baseprefix}/js/system/sysOpenTree.js?t=<%= System.currentTimeMillis()%>"></script>
</body>
</html>
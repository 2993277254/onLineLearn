<%@ page language="java" import="java.util.*"
         contentType="text/html;charset=utf-8" %>
<%@ include file="/WEB-INF/base/common.jsp" %>
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
<link rel="stylesheet" type="text/css" href="<#noparse>${ctxsta}</#noparse>/layuiadmin/style/admin.css" media="all">
<#if isLeftZtree?? && isLeftZtree=="1">
<link rel="stylesheet" type="text/css" href="<#noparse>${ctxsta}</#noparse>/lib/zTree/v3/css/layuiStyle/layuiStyle.css">
</#if>
<body ms-controller="${FileName}List">
<#if isLeftZtree?? && isLeftZtree=="1">
<!--左侧树的div-->
<div id="left_tree"></div>
</#if>
<div class="layui-fluid">
    <div class="layui-card">
        <!--搜素栏的div-->
        <div class="layui-form layui-card-header layuiadmin-card-header-auto search-form"
             id="${FileName}List_search" lay-filter="${FileName}List_search">
        </div>

        <div class="layui-card-body">
            <!--工具栏的按钮的div，注意：需要增加权限控制-->
            <div style="padding-bottom: 10px;" id="${FileName}List_tool">
                <button :visible="@baseFuncInfo.authorityTag('${FileName}List#batchDel')"
                        class="layui-btn"  onclick="batchDel()">删除</button>
                <button :visible="@baseFuncInfo.authorityTag('${FileName}List#add')"
                        class="layui-btn"  onclick="saveOrEdit()">添加</button>
            </div>
            <!--table定义-->
            <table id="${FileName}List_table" lay-filter="${FileName}List_table"></table>
            <!--table的工具栏按钮定义，注意：需要增加权限控制-->
            <script type="text/html" id="${FileName}List_bar">
                {{#  if(baseFuncInfo.authorityTag('${FileName}List#detail')){ }}
                <a class="layui-btn layui-btn-xs " lay-event="detail">查看</a>
                {{#  } }}
                {{#  if(baseFuncInfo.authorityTag('${FileName}List#edit')){ }}
                <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
                {{#  } }}
                {{#  if(baseFuncInfo.authorityTag('${FileName}List#del')){ }}
                <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
                {{#  } }}
            </script>
        </div>
    </div>
</div>
<#if isLeftZtree?? && isLeftZtree=="1">
<script type="text/javascript" src="<#noparse>${baseprefix}</#noparse>/lib/zTree/v3/js/jquery.ztree.all.min.js"></script>
</#if>
<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="<#noparse>${baseprefix}</#noparse>/js/${moduleName}/${FileName}List.js?t=<%= System.currentTimeMillis()%>"></script>
</body>
</html>
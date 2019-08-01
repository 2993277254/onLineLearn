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
    <link rel="stylesheet" type="text/css" href="${baseprefix}/layuiadmin/style/admin.css" media="all">
    <link rel="stylesheet" type="text/css" href="${baseprefix}/lib/zTree/v3/css/layuiStyle/layuiStyle.css">
</head>

<body ms-controller="sysMenuList">
<!--左侧树的div-->
<div id="left_tree"></div>
<div class="layui-fluid">
    <div class="layui-card" style="height: -webkit-fill-available;">
        <div class="layui-card-body">
            <div id="sysMenuList_top" >
                <button class="layui-btn"
                        onclick="saveOrEdit(1)">添加一级菜单</button>
            </div>
        </div>
        <div class="layui-tab layui-tab-brief">
            <ul class="layui-tab-title">
                <li class="layui-this">菜单信息</li>
                <li>菜单按钮</li>
            </ul>
            <div class="layui-tab-content">
                <div class="layui-tab-item layui-show" id="sysMenuEdit_div">
                    <div class="layui-form" lay-filter="sysMenuEdit_form" id="sysMenuEdit_form" style="padding: 20px 30px 0 0;">
                        <div class="layui-form-item  layui-hide">
                            <label class="layui-form-label">ID</label>
                            <div class="layui-input-inline">
                                <input type="hidden" name="id" placeholder="请输入" autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item  layui-hide">
                            <label class="layui-form-label">parentId</label>
                            <div class="layui-input-inline">
                                <input type="hidden" name="parentId" placeholder="请输入" autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"><span class="edit-verify-span">*</span>菜单名称</label>
                            <div class="layui-input-inline" style="width:300px;">
                                <input type="text" name="menuName" maxlength="50" lay-verify="required" placeholder="请输入" autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"><span class="edit-verify-span">*</span>目标地址</label>
                            <div class="layui-input-inline" style="width:300px;">
                                <input type="text" name="menuUrl" maxlength="255" lay-verify="required" placeholder="请输入" autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">菜单图标</label>
                            <div class="layui-input-inline" style="width:300px;">
                                <input type="text" name="menuIcon" maxlength="50"  placeholder="请输入" autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">菜单图标(图片)</label>
                            <div class="layui-input-inline" style="width:300px;">
                                <input type="text" name="menuImg" maxlength="50"  placeholder="请输入" autocomplete="off" class="layui-input">
                            </div>
                            <div class="layui-form-mid layui-word-aux">（当图标与图片共存时，图片优先显示）</div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">状态</label>
                            <div class="layui-input-block">
                                <input type="radio" name="status" value="1" title="启用" checked>
                                <input type="radio" name="status" value="0" title="禁用">
                            </div>
                        </div>
                        <div class="layui-form-item layui-form-text">
                            <label class="layui-form-label">备注</label>
                            <div class="layui-input-block">
                                <textarea name="remark" style="width: 300px;height: 100px;"  placeholder="请输入" class="layui-textarea"></textarea>
                            </div>
                        </div>
                        <div class="layui-form-item layui-hide">
                            <button class="layui-btn" lay-submit lay-filter="sysMenuEdit_submit" id="sysMenuEdit_submit">提交</button>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"></label>
                            <button class="layui-btn" onclick="save()">保存</button>
                        </div>
                    </div>
                </div>
                <div class="layui-tab-item">
                    <div class="layui-card-body">
                        <div style="padding-bottom: 10px;" id="sysMenuList_tool">
                            <button class="layui-btn" :visible="@baseFuncInfo.authorityTag('sysMenuList#batchDel')"
                                    onclick="batchDel()">删除</button>
                            <button class="layui-btn" :visible="@baseFuncInfo.authorityTag('sysMenuList#add')"
                                    onclick="btnSaveOrEdit()">添加</button>
                        </div>
                        <!--table定义-->
                        <table id="sysMenuList_table" lay-filter="sysMenuList_table"></table>
                        <!--table的工具栏按钮定义，注意：需要增加权限控制-->
                        <script type="text/html" id="sysMenuList_bar">
                            {{#  if(baseFuncInfo.authorityTag('sysMenuList#detail')){ }}
                            <a class="layui-btn layui-btn-xs " lay-event="detail">查看</a>
                            {{#  } }}
                            {{#  if(baseFuncInfo.authorityTag('sysMenuList#edit')){ }}
                            <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
                            {{#  } }}
                            {{#  if(baseFuncInfo.authorityTag('sysMenuList#del')){ }}
                            <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
                            {{#  } }}
                        </script>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="${baseprefix}/lib/zTree/v3/js/jquery.ztree.all.min.js"></script>
<script type="text/javascript" src="${baseprefix}/js/system/sysMenuList.js?t=<%= System.currentTimeMillis()%>"></script>
</body>
</html>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/12/31
  Time: 21:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/WEB-INF/base/adminCommon.jsp" %>
<html>
<head>
    <title>后台主页</title>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" type="text/css" href="${baseprefix}/layuiadmin/style/admin.css" media="all">
</head>

<body class="layui-layout-body" ms-controller="adminIndex">

<div id="LAY_app">
    <div class="layui-layout layui-layout-admin">
        <div class="layui-header">
            <!-- 头部区域 -->
            <ul class="layui-nav layui-layout-left">
                <li class="layui-nav-item layadmin-flexible" lay-unselect>
                    <a href="javascript:;" layadmin-event="flexible" title="侧边伸缩">
                        <i class="layui-icon layui-icon-shrink-right" id="LAY_app_flexible"></i>
                    </a>
                </li>
                <li class="layui-nav-item layui-hide-xs" lay-unselect>
                    <a href="${baseprefix}/" target="_blank" title="前台">
                        <i class="layui-icon layui-icon-website">前台</i>
                    </a>
                </li>
                <li class="layui-nav-item" lay-unselect>
                    <a href="javascript:;" layadmin-event="refresh" title="刷新">
                        <i class="layui-icon layui-icon-refresh-3"></i>
                    </a>

                </li>
                <li>
                    <p>${manu}</p>
                </li>

                <%--<li class="layui-nav-item layui-hide-xs" lay-unselect>--%>
                    <%--<input type="text" placeholder="搜索..." autocomplete="off" class="layui-input layui-input-search" layadmin-event="serach" lay-action="template/search.html?keywords=">--%>
                <%--</li>--%>
            </ul>
            <ul class="layui-nav layui-layout-right" lay-filter="layadmin-layout-right">

                <%--<li class="layui-nav-item" lay-unselect>--%>
                    <%--<a lay-href="app/message/index.html" layadmin-event="message" lay-text="消息中心">--%>
                        <%--<i class="layui-icon layui-icon-notice"></i>--%>

                        <%--<!-- 如果有新消息，则显示小圆点 -->--%>
                        <%--<span class="layui-badge-dot"></span>--%>
                    <%--</a>--%>
                <%--</li>--%>
                <%--<li class="layui-nav-item layui-hide-xs" lay-unselect>--%>
                    <%--<a href="javascript:;" layadmin-event="theme">--%>
                        <%--<i class="layui-icon layui-icon-theme"></i>--%>
                    <%--</a>--%>
                <%--</li>--%>
                <%--<li class="layui-nav-item layui-hide-xs" lay-unselect>--%>
                    <%--<a href="javascript:;" layadmin-event="note">--%>
                        <%--<i class="layui-icon layui-icon-note"></i>--%>
                    <%--</a>--%>
                <%--</li>--%>
                <%--<li class="layui-nav-item layui-hide-xs" lay-unselect>--%>
                    <%--<a href="javascript:;" layadmin-event="fullscreen">--%>
                        <%--<i class="layui-icon layui-icon-screen-full"></i>--%>
                    <%--</a>--%>
                <%--</li>--%>
                <li class="layui-nav-item" lay-unselect>
                    <a href="javascript:;">
                        <cite>${sessionScope.ollSysUser.nickName}</cite>
                    </a>
                    <dl class="layui-nav-child" style="margin-right: 10px">
                        <%--<dd><a lay-href="set/user/info.html">基本资料</a></dd>--%>
                        <%--<dd><a lay-href="set/user/password.html">修改密码</a></dd>--%>
                        <%--<hr>--%>
                        <dd layadmin-event="" style="text-align: center;"><a href="${baseprefix}/logout.do"> 退出</a></dd>
                    </dl>
                </li>
                <%--<li class="layui-nav-item layui-hide-xs" lay-unselect>--%>
                    <%--<a href="javascript:;" layadmin-event="about"><i class="layui-icon layui-icon-more-vertical"></i></a>--%>
                <%--</li>--%>
                <%--<li class="layui-nav-item layui-show-xs-inline-block layui-hide-sm" lay-unselect>--%>
                    <%--<a href="javascript:;" layadmin-event="more"><i class="layui-icon layui-icon-more-vertical"></i></a>--%>
                <%--</li>--%>
            </ul>
        </div>

        <!-- 侧边菜单 -->
        <div class="layui-side layui-side-menu">
            <div class="layui-side-scroll">
                <div class="layui-logo" lay-href="home/console.html">
                    <span>后台管理</span>
                </div>

                <ul class="layui-nav layui-nav-tree" lay-shrink="all" id="LAY-system-side-menu" lay-filter="layadmin-system-side-menu">

                    <li data-name="home" :class="['layui-nav-item',$index==0&&'layui-nav-itemed']"
                        ms-for="($index,el) in @baseFuncInfo.getMenus()">
                        <%--没有二级菜单--%>
                        <a ms-if="el.children.length==0" href="javascript:;"
                           ms-attr="{'lay-href':'${baseprefix}'+@el.menuUrl,'lay-tips':@el.menuName}" lay-direction="2">
                            <i ms-if="@el.menuImg==null||el.menuImg==''"
                               :class="['layui-icon',@el.menuIcon==''?'layui-icon-component':el.menuIcon]"></i>
                            <img class="menu-img" ms-if="@el.menuImg!=null&&el.menuImg!=''"
                                 ms-attr="{src: '${baseprefix}'+@el.menuImg}" onerror="this.style.display='none'">
                            <cite ms-text="@el.menuName"></cite>
                        </a>
                        <%--有二级菜单--%>
                        <a ms-if="el.children.length>0" href="javascript:;" ms-attr="{'lay-tips':@el.menuName}"
                           lay-direction="2">
                            <i ms-if="@el.menuImg==null||el.menuImg==''"
                               :class="['layui-icon',@el.menuIcon==''?'layui-icon-component':el.menuIcon]"></i>
                            <img class="menu-img" ms-if="@el.menuImg!=null&&el.menuImg!=''"
                                 ms-attr="{src: '${baseprefix}'+@el.menuImg}" onerror="this.style.display='none'">
                            <cite ms-text="@el.menuName"></cite>
                        </a>
                        <%--有二级菜单--%>
                        <dl ms-if="el.children.length>0" class="layui-nav-child">
                            <dd ms-for="($index2,e2) in el.children">
                                <%--没有三级菜单--%>
                                <a ms-if="@e2.children.length==0" ms-attr="{'lay-href': '${baseprefix}'+@e2.menuUrl}">
                                    <i ms-if="@e2.menuImg==null||e2.menuImg==''"
                                       :class="['layui-icon',@e2.menuIcon==''?'':e2.menuIcon]"></i>
                                    <img class="menu-img" ms-if="@e2.menuImg!=null&&e2.menuImg!=''"
                                         ms-attr="{src: '${baseprefix}'+@e2.menuImg}" onerror="this.style.display='none'">
                                    <cite ms-text="@e2.menuName"></cite>
                                </a>
                                <%--有三级菜单--%>
                                <a ms-if="@e2.children.length>0" href="javascript:;">
                                    <i ms-if="@e2.menuImg==null||e2.menuImg==''"
                                       :class="['layui-icon',@e2.menuIcon==''?'':e2.menuIcon]"></i>
                                    <img class="menu-img" ms-if="@e2.menuImg!=null&&e2.menuImg!=''"
                                         ms-attr="{src: '${baseprefix}'+@e2.menuImg}" onerror="this.style.display='none'">
                                    <cite ms-text="@e2.menuName"></cite>
                                </a>
                                <dl ms-if="@e2.children.length>0" class="layui-nav-child">
                                    <dd ms-for="($index3,e3) in e2.children">
                                        <a ms-attr="{'lay-href': '${baseprefix}'+@e3.menuUrl}">
                                            <i ms-if="@e3.menuImg==null||e3.menuImg==''"
                                               :class="['layui-icon',@e3.menuIcon==''?'':e3.menuIcon]"
                                               style="left:35px"></i>
                                            <img class='menu-img' style="left:35px;"
                                                 ms-if="@e3.menuImg!=null&&e3.menuImg!=''"
                                                 ms-attr="{src: '${baseprefix}'+@e3.menuImg}"
                                                 onerror="this.style.display='none'">
                                            <cite ms-text="@e3.menuName"></cite>
                                        </a>
                                    </dd>
                                </dl>
                            </dd>
                        </dl>
                    </li>
                    <%--<li data-name="home" class="layui-nav-item layui-nav-itemed">--%>
                        <%--<a href="javascript:;" lay-tips="系统管理" lay-direction="2">--%>
                            <%--<i class="layui-icon layui-icon-home"></i>--%>
                            <%--<cite>系统管理</cite>--%>
                        <%--</a>--%>
                        <%--<dl class="layui-nav-child">--%>
                            <%--<dd data-name="console" >--%>
                                <%--<a lay-href="${baseprefix}/system/sysUserList">用户列表</a>--%>
                            <%--</dd>--%>
                        <%--</dl>--%>
                        <%--<dl class="layui-nav-child">--%>
                            <%--<dd data-name="console" >--%>
                                <%--<a lay-href="${baseprefix}/system/genTableList">生成工具</a>--%>
                            <%--</dd>--%>
                        <%--</dl>--%>
                        <%--<dl class="layui-nav-child">--%>
                            <%--<dd data-name="console" >--%>
                                <%--<a lay-href="${baseprefix}/system/sysDictList">字典管理</a>--%>
                            <%--</dd>--%>
                        <%--</dl>--%>
                        <%--<dl class="layui-nav-child">--%>
                            <%--<dd data-name="console" >--%>
                                <%--<a lay-href="${baseprefix}/system/sysMenuList">菜单管理</a>--%>
                            <%--</dd>--%>
                        <%--</dl>--%>
                    <%--</li>--%>
                </ul>
            </div>
        </div>

        <!-- 页面标签 -->
        <div class="layadmin-pagetabs" id="LAY_app_tabs">
            <div class="layui-icon layadmin-tabs-control layui-icon-prev" layadmin-event="leftPage"></div>
            <div class="layui-icon layadmin-tabs-control layui-icon-next" layadmin-event="rightPage"></div>
            <div class="layui-icon layadmin-tabs-control layui-icon-down">
                <ul class="layui-nav layadmin-tabs-select" lay-filter="layadmin-pagetabs-nav">
                    <li class="layui-nav-item" lay-unselect>
                        <a href="javascript:;"></a>
                        <dl class="layui-nav-child layui-anim-fadein">
                            <dd layadmin-event="closeThisTabs"><a href="javascript:;">关闭当前标签页</a></dd>
                            <dd layadmin-event="closeOtherTabs"><a href="javascript:;">关闭其它标签页</a></dd>
                            <dd layadmin-event="closeAllTabs"><a href="javascript:;">关闭全部标签页</a></dd>
                        </dl>
                    </li>
                </ul>
            </div>
            <div class="layui-tab" lay-unauto lay-allowClose="true" lay-filter="layadmin-layout-tabs">
                <ul class="layui-tab-title" id="LAY_app_tabsheader">
                    <li lay-id="home/console.html" lay-attr="home/console.html" class="layui-this"><i class="layui-icon layui-icon-home"></i></li>
                </ul>
            </div>
        </div>


        <!-- 主体内容 -->
        <div class="layui-body" id="LAY_app_body">
            <div class="layadmin-tabsbody-item layui-show">
                <iframe src="${baseprefix}/admin/iframeIndex" frameborder="0" class="layadmin-iframe"></iframe>
            </div>
        </div>

        <!-- 辅助元素，一般用于移动设备下遮罩 -->
        <div class="layadmin-body-shade" layadmin-event="shade"></div>
    </div>
</div>
<script type="text/javascript" src="${baseprefix}/js/admin/index.js?t=<%= System.currentTimeMillis()%>"></script>
</body>


</html>

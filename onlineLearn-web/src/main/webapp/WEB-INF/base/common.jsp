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

    <%--加在fly的global.css--%>
    <link rel="stylesheet" type="text/css" href="${baseprefix}/css/global.css?t=<%=System.currentTimeMillis()%>">
    <!--加载项目的扩展图标css 参考：https://fly.layui.com/jie/9149/ 访问阿里字体图标库 http://www.iconfont.cn-->
    <link rel="stylesheet" type="text/css"
          href="${baseprefix}/css/font/iconfont.css?t=<%= System.currentTimeMillis()%>"/>
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
    <style type="text/css">
        /* 全局 */
        html,body{overflow-x: hidden;}
        html body{margin-top: 61px;}
        html{background-color: #F2F2F2;}
        i{font-style: normal;}
    </style>
</head>
<body>
<div class="fly-header layui-bg-black" id="cusTop">
    <%--头部--%>
    <div class="layui-container">
        <a class="fly-logo" href="${baseprefix}/mainPage.do" style="">
            <img src="${baseprefix}/images/logo.png" alt="layui">
            <input id="uploadHttpPath" class="layui-hide" value="${sessionScope.uploadHttpPath}">
            <input id="userId" class="layui-hide" value="${sessionScope.ollUserId}">
        </a>
        <ul class="layui-nav fly-nav  layui-hide-xs" style="float: left;">
            <li class="layui-nav-item ">
                <a href="${baseprefix}/">首页</a>
            </li>
            <li class="layui-nav-item ">
                <a href="${baseprefix}/course/courseList">课程</a>
            </li>
        </ul>
        <ul class="layui-nav " style="float: right">
            <c:choose>
                <c:when test="${not empty sessionScope.ollSysUser}">
                    <li class="layui-nav-item">
                        <a href="">消息<span class="" id="reMsg"></span></a>
                    </li>
                    <li class="layui-nav-item">
                        <%--<span class="layui-badge-dot"></span>--%>
                        <a href="${baseprefix}/courseUser/courseUserMain">我的课程</a>
                    </li>
                    <li class="layui-nav-item">

                    </li>
                    <li class="layui-nav-item" lay-unselect="">

                        <dl class="layui-nav-child">
                            <dd>
                                <a href="${baseprefix}/user/sysUserSet?userId=${sessionScope.ollSysUser.uid}"><i
                                    class="layui-icon">&#xe620;</i>基本设置</a></dd>
                                <%--<dd><a href="user/message.html"><i class="iconfont icon-tongzhi" style="top: 4px;"></i>我的消息</a>--%>
                                <%--</dd>--%>
                            <%--<dd><a href="user/home.html"><i class="layui-icon"--%>
                                                            <%--style="margin-left: 2px; font-size: 22px;">&#xe68e;</i>我的主页</a>--%>
                            <%--</dd>--%>
                            <c:if test="${sessionScope.ollSysUser.identity eq 1 || sessionScope.ollSysUser.identity eq 2}">
                                <dd><a style="cursor: pointer" onclick="toAdminPage()"><i
                                        class="layui-icon">&#xe674;</i>后台管理</a></dd>
                            </c:if>
                            <hr style="margin: 5px 0;">
                            <dd><a href="${baseprefix}/logout.do" style="text-align: center;">退出</a></dd>
                        </dl>
                        <a href="javascript:;">${sessionScope.ollSysUser.nickName}</a>
                    </li>

                    <%--${sessionScope.ollSysUser.pictureUrl}--%>
                    <%--${sessionScope.ollSysUser.nickName}--%>
                    <c:choose>
                        <c:when test="${not empty sessionScope.ollSysUser.pictureUrl}">
                            <img src="${uploadHttpPath}${sessionScope.ollSysUser.pictureUrl}" class="layui-nav-img">
                        </c:when>
                        <c:otherwise>
                            <%--<img src="${baseprefix}/images/126.jpg" class="layui-nav-img">--%>
                            <c:choose>
                                <c:when test="${sessionScope.ollSysUser.sex eq 1}">
                                    <img src="${baseprefix}/images/man.png" class="layui-nav-img">
                                </c:when>
                                <c:otherwise>
                                    <img src="${baseprefix}/images/women.jpg" class="layui-nav-img">
                                </c:otherwise>
                            </c:choose>


                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <li class="layui-nav-item">
                        <i class="layui-icon iconfont  icon-manphoto" style="font-size: 30px;color: darkgray">
                            <%--<img src="${baseprefix}/images/touxiang.png" class="layui-nav-img ">--%>
                        </i>
                    </li>
                    <li class="layui-nav-item">
                        <a href="${baseprefix}/user/login">登录</a>
                    </li>
                    <li class="layui-nav-item">
                        <a href="${baseprefix}/user/register">注册</a>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</div>
</body>

</html>

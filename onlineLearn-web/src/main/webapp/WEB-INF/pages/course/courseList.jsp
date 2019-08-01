<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/1/19
  Time: 15:13
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
    <title>课程列表</title>
</head>
<body ms-controller="courseList" style="overflow: hidden">
<div class="layui-container ">
    <div class="layui-row layui-col-space15 ">
        <div class="layui-row">
            <a class="acursor" onclick="toCourse(1)">课程</a>
            >
            <a style="color: grey" >{{@typeName.length<=0? '全部':@typeName}}</a>
        </div>
        <div class="layui-col-md12">
            <%--课程分类--%>
            <div class="fly-panel  layui-fluid bottom20">
                <ul class="layui-row courseWrap">
                    <li class="courseType layui-col-lg2  layui-col-lg2 layui-col-sm3 layui-col-xs4"
                        ms-for="($index, el) in @baseFuncInfo.getSysDictByCode('course_type_')">
                        <div>
                            <input type="hidden" ms-attr="{value:el.value}">
                            <%--<i  :class="['layui-icon',@el.dictDataDesc==''?'':el.icon]">--%>
                                <span   ms-click="@courseTypeclick(el.value,el.name)">{{el.name}}</span>
                            <%--</i>--%>
                        </div>
                    </li>
                </ul>
                <div class="layui-row ">
                    <hr>
                </div>
                <div class="layui-row top20"></div>
            </div>
            <%--课程列表--%>
            <div class="fly-panel" style="">
                <br>
                <div id="isSearch" class="layui-hide" style="margin-left: 15px">
                    搜索
                    <span style="color: lightgreen;font-size: 16px"></span>
                    相关的结果，共<i style="color: deepskyblue">{{@total}}</i>条
                </div>
                <br>
                <div class="fly-panel-title fly-filter layui-row">
                    <%--<p class="layui-fluid">共<span style="color: lightskyblue">514</span>条<span style="color: lightgreen;font-size: 16px">c</span>相关的结果</p>--%>
                    <span class="">
                          <a class="courseOrderby"
                             ms-for="($index, el) in @baseFuncInfo.getSysDictByCode('course_orderBy_')">
                            <input type="hidden" ms-attr="{value:el.value}">
                             <span :click="@courseOrderbyclick(el.value)">{{el.name}}</span>
                            <span class="fly-mid"
                                  ms-if="$index<@baseFuncInfo.getSysDictByCode('course_orderBy_').length-1"></span>
                        </a>
                        <%--<a class=" courseStatus"--%>
                           <%--ms-for="($index, el) in @baseFuncInfo.getSysDictByCode('course_status_',true)">--%>
                            <%--<input type="hidden" ms-attr="{value:el.value}">--%>
                            <%--<span :click="@courseStatusclick(el.value)">{{el.name}}</span>--%>
                            <%--<span class="fly-mid"--%>
                                  <%--ms-if="$index<@baseFuncInfo.getSysDictByCode('course_status_',true).length-1"></span>--%>
                        <%--</a>--%>
                        </span>
                    <%--<input type="radio"   name="status" ms-attr="{value:el.value,title:el.name,checked:true&&$index==0}"--%>
                    <%--ms-for="($index, el) in @baseFuncInfo.getSysDictByCode('course_status_',true)">--%>
                   <%--排序--%>
                    <%--<span class="fly-filter-right ">--%>
                         <%--<a class="courseOrderby"--%>
                            <%--ms-for="($index, el) in @baseFuncInfo.getSysDictByCode('course_orderBy_')">--%>
                            <%--<input type="hidden" ms-attr="{value:el.value}">--%>
                             <%--<span :click="@courseOrderbyclick(el.value)">{{el.name}}</span>--%>
                            <%--<span class="fly-mid"--%>
                                  <%--ms-if="$index<@baseFuncInfo.getSysDictByCode('course_orderBy_').length-1"></span>--%>
                        <%--</a>--%>
                    <%--</span>--%>
                </div>
                <br>
                <div id="course_List">
                </div>

                <div :visible="@total!=0" class="layui-fluid" style="text-align: center" id="course-page">
                </div>

            </div>
        </div>
        <%--<div class="layui-col-md4">--%>
            <%--<dl class="fly-panel fly-list-one">--%>
                <%--<dt class="fly-panel-title">本周热议</dt>--%>
                <%--<dd>--%>
                    <%--<a href="">基于 layui 的极简社区页面模版</a>--%>
                    <%--<span><i class="iconfont icon-pinglun1"></i> 16</span>--%>
                <%--</dd>--%>
                <%--<dd>--%>
                    <%--<a href="">基于 layui 的极简社区页面模版</a>--%>
                    <%--<span><i class="iconfont icon-pinglun1"></i> 16</span>--%>
                <%--</dd>--%>
                <%--<dd>--%>
                    <%--<a href="">基于 layui 的极简社区页面模版</a>--%>
                    <%--<span><i class="iconfont icon-pinglun1"></i> 16</span>--%>
                <%--</dd>--%>
                <%--<dd>--%>
                    <%--<a href="">基于 layui 的极简社区页面模版</a>--%>
                    <%--<span><i class="iconfont icon-pinglun1"></i> 16</span>--%>
                <%--</dd>--%>
                <%--<dd>--%>
                    <%--<a href="">基于 layui 的极简社区页面模版</a>--%>
                    <%--<span><i class="iconfont icon-pinglun1"></i> 16</span>--%>
                <%--</dd>--%>
                <%--<dd>--%>
                    <%--<a href="">基于 layui 的极简社区页面模版</a>--%>
                    <%--<span><i class="iconfont icon-pinglun1"></i> 16</span>--%>
                <%--</dd>--%>
                <%--<dd>--%>
                    <%--<a href="">基于 layui 的极简社区页面模版</a>--%>
                    <%--<span><i class="iconfont icon-pinglun1"></i> 16</span>--%>
                <%--</dd>--%>
                <%--<dd>--%>
                    <%--<a href="">基于 layui 的极简社区页面模版</a>--%>
                    <%--<span><i class="iconfont icon-pinglun1"></i> 16</span>--%>
                <%--</dd>--%>
                <%--<dd>--%>
                    <%--<a href="">基于 layui 的极简社区页面模版</a>--%>
                    <%--<span><i class="iconfont icon-pinglun1"></i> 16</span>--%>
                <%--</dd>--%>
                <%--<dd>--%>
                    <%--<a href="">基于 layui 的极简社区页面模版</a>--%>
                    <%--<span><i class="iconfont icon-pinglun1"></i> 16</span>--%>
                <%--</dd>--%>

                <%--<!-- 无数据时 -->--%>
                <%--<!----%>
                <%--<div class="fly-none">没有相关数据</div>--%>
                <%---->--%>
            <%--</dl>--%>

            <%--<div class="fly-panel">--%>
                <%--<div class="fly-panel-title">--%>
                    <%--这里可作为广告区域--%>
                <%--</div>--%>
                <%--<div class="fly-panel-main ">--%>
                    <%--<a href="" target="_blank" class="fly-zanzhu" style="background-color: #393D49;">虚席以待</a>--%>
                <%--</div>--%>
            <%--</div>--%>

            <%--<div class="fly-panel fly-link">--%>
                <%--<h3 class="fly-panel-title">友情链接</h3>--%>
                <%--<dl class="fly-panel-main">--%>
                    <%--<dd><a href="http://www.layui.com/" target="_blank">layui</a>--%>
                    <%--<dd>--%>
                    <%--<dd><a href="http://layim.layui.com/" target="_blank">WebIM</a>--%>
                    <%--<dd>--%>
                    <%--<dd><a href="http://layer.layui.com/" target="_blank">layer</a>--%>
                    <%--<dd>--%>
                    <%--<dd><a href="http://www.layui.com/laydate/" target="_blank">layDate</a>--%>
                    <%--<dd>--%>
                    <%--<dd>--%>
                        <%--<a href="mailto:xianxin@layui-inc.com?subject=%E7%94%B3%E8%AF%B7Fly%E7%A4%BE%E5%8C%BA%E5%8F%8B%E9%93%BE"--%>
                           <%--class="fly-link">申请友链</a>--%>
                    <%--<dd>--%>
                <%--</dl>--%>
            <%--</div>--%>

        <%--</div>--%>
    </div>
</div>
<script type="text/javascript" src="${baseprefix}/js/course/courseList.js?t=<%= System.currentTimeMillis()%>"></script>
<%--<script type="text/javascript" src="${baseprefix}/js/footer.js?t=<%= System.currentTimeMillis()%>"></script>--%>
<%--使用模板--%>
<script id="courseListTemp" type="text/html">
    <ul class="course-list layui-fluid">
        {{# layui.each(d.list, function(index, item){ }}
        <li class="layui-row " onclick="courseClick('{{item.uid}}','{{item.photoUrl}}')" style="">
            <br>
            <div class="hidden layui-col-md3 layui-col-xs4 layui-col-sm3 " style="margin-bottom: 2%">
                <%--src="${baseprefix}/images/imgLoad.gif"--%>
                {{# if(isNotEmpty(item.photoUrl)){ }}
                <img class="img-box transition  flowImg" src="{{getHttpPath()+item.photoUrl}}"
                     alt="图片" >
                 {{# }else{ }}
                 <img class="img-box transition  flowImg" src="${baseprefix}/images/img_error.jpg"
                         alt="图片" >
                  {{# } }}
                <br>
            </div>
            <div class="layui-col-md8 layui-col-xs8 layui-col-sm9 bottom20 layui-fluid">
                <div class="layui-row">
                <h2>
                    <a href="${baseprefix}/course/courseDetails?courseId={{item.uid}}">{{item.name}}</a>
                    <span class="layui-badge" style="left: 10px;top: -3px;">
                       {{ baseFuncInfo.getSysDictName('course_type_',item.type) }}
                    </span>
                </h2>
                </div>
                <br>
                <div class="layui-row">
                    <a class="layui-col-md12" href="${baseprefix}/course/courseDetails?courseId={{item.uid}}" >
                        <%--概述--%>
                        {{# if(item.summary.length>200){ }}
                        {{ item.summary.slice(0,90)+'...' }}
                        {{# }else{ }}
                        {{ item.summary }}
                        {{# } }}
                    </a>
                </div>
                <br>
                <div class="layui-row">
                           <span class="layui-col-md3 layui-col-sm3 layui-col-xs6">
                                <i class="layui-icon layui-icon-username" title="学习人数">{{item.personNum}}人参加</i>
                           </span>

                    <%--<span class="layui-col-md5 layui-col-sm5 layui-col-xs6">--%>
                                <%--<i class="layui-icon layui-icon-find-fill" title="课程进度">进行至第一周</i>--%>
                            <%--</span>--%>
                </div>
            </div>
        </li>
        {{# }); }}
        {{# if(isEmpty(d.list)){ }}
        <li class="layui-row" style="text-align: center;padding: 10px 0px 10px 0px;">暂无数据</li>
        {{# } }}
    </ul>
</script>
</body>
</html>

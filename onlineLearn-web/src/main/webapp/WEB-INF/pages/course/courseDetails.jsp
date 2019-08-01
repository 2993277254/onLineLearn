<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/3/24 0024
  Time: 18:48
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

    <title>课程详情</title>
</head>
<body ms-controller="courseDetails">
<div class="layui-container">
    <div class="layui-row layui-fluid" style="padding-bottom: 10px;">
        <div class="layui-row " style="padding-top: 20px;padding-bottom: 20px;">
            <span onclick="toCourse(1)" class="acursor">全部课程</span>>
            <span onclick="toCourse(2)" class="acursor">{{@baseFuncInfo.getSysDictName('course_type_',@type)}}</span>>
            <span style="color: grey">{{@name}}</span>
        </div>
        <div class="layui-row  layui-card " style="padding-bottom: 40px">
            <%--左边播放视频--%>
            <div class="layui-col-md5 layui-fluid top20">
                <div class="layui-row " style="height: 280px;">
                    <div class="" style="width:100%;height: 100%;" id="video">
                        <button class="layui-btn  layui-btn-lg
                            layui-btn-radius layui-col-md4 layui-col-md-offset4
                            layui-col-sm8 layui-col-sm-offset4 layui-col-xs9 layui-col-xs-offset3 "
                                onclick="playPreview()">播放视频简介
                        </button>
                    </div>
                </div>
            </div>
            <div class="layui-col-md7 layui-fluid top20">
                <div class="layui-row  bottom20"><h1>{{@name}}</h1></div>
                <%--<div class="layui-row " style="height: 100px;bottom: 30px;">--%>
                    <%--&lt;%&ndash;C语言是目前世界上流行、使用最广泛的高级程序设计语言。　　 C语言对操作系统和系统使用程序以及需要对硬件进行操作的场合，用C语言明显优于其它高级语言，许多大型应用软件都是用C语言编写的。&ndash;%&gt;--%>
                <%--</div>--%>
                <div class="layui-row top20">已有{{@personNum}}人参加</div>
                <div class="layui-row ">
                    <div class="layui-col-md4" style="top: 20px;" id="isAddDiv">
                        <button :if="@isHas==0" class="layui-btn layui-btn-fluid "
                                onclick="addOrStudyCourse(0)"><h3>立即参加</h3></button>
                        <button :if="@isHas==1" class="layui-btn layui-btn-fluid "
                                onclick="addOrStudyCourse(1)"><h3>开始学习</h3></button>
                        <button :if="@isHas==2" class="layui-btn layui-btn-fluid layui-bg-orange "
                                onclick="addOrStudyCourse(2)"><h2>继续学习</h2></button>
                        <button :if="@isHas==3" class="layui-btn layui-btn-fluid  layui-btn-disabled"
                                 disabled><h2>不支持报名学习</h2></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="layui-row" style="">
        <div class="layui-fluid">
            <div class="layui-tab layui-tab-brief layui-card">
                <ul class="layui-tab-title layui-fluid">
                    <li class="layui-this">课程详情</li>
                    <li>课程评价({{ @total }})</li>
                </ul>
                <div class="layui-tab-content layui-card" style="margin-bottom: 10px;">
                    <%--课程详情--%>
                    <div class="layui-tab-item  layui-show ">
                        <div style="margin-left: 30px;">
                        <div class="layui-row">
                            <div class="layui-row courseDeatailsRow">
                                <i class="layui-icon layui-icon-app courseDeatailsi"><span
                                        class="courseDeatailsspan">课程概述</span></i>
                            </div>
                            <div class="layui-row">
                                <p style="text-indent: 2em">
                                    {{@summary}}
                                </p>
                            </div>
                        </div>
                        <div class="layui-row">
                            <div class="layui-row courseDeatailsRow">
                                <i class="layui-icon layui-icon-template-1 courseDeatailsi"><span
                                        class="courseDeatailsspan">课程大纲</span></i>
                            </div>
                            <div class="layui-row">
                                <div ms-for="($index, el) in @outlineList">
                                    <span style="font-weight: bold">{{el.name}}:{{el.text}}</span>
                                    <br>
                                    <br>
                                    <p style="text-indent: 2em;color: darkgray" ms-for="($index2, e2) in @el.courseHouse">
                                        {{e2.name}}:{{e2.text}}
                                        <br>
                                        <br>
                                    </p>
                                </div>
                            </div>
                        </div>
                        <%--<div class="layui-row">--%>
                            <%--<div class="layui-row courseDeatailsRow">--%>
                                <%--<i class="layui-icon layui-icon-form courseDeatailsi"><span--%>
                                        <%--class="courseDeatailsspan">证书要求</span></i>--%>
                            <%--</div>--%>
                            <%--<div class="layui-row">--%>
                                <%--{{@demand}}--%>
                            <%--</div>--%>
                        <%--</div>--%>
                        </div>
                    </div>

                <%--课程评价--%>
                <div class="layui-tab-item">
                    <div class="layui-row top20 bottom20" style="text-align: center">
                        共{{ @total }}条评论
                    </div>
                    <div class="layui-row" id="course_discuss_list">

                    </div>
                    <div :if="@total!=0" class="layui-row" style="text-align: center" id="discuss-page">
                    </div>
                </div>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
<script type="text/javascript" src="${baseprefix}/lib/ckplayer/ckplayer.js?t=<%= System.currentTimeMillis()%>" charset="UTF-8"></script>
<script type="text/javascript" src="${baseprefix}/js/course/courseDetails.js?t=<%= System.currentTimeMillis()%>"></script>
<%--<script type="text/javascript" src="${baseprefix}/js/footer.js?t=<%= System.currentTimeMillis()%>"></script>--%>
<%--讨论模板--%>
<script id="courseDiscussListTemp" type="text/html">
    <ul class="layui-fluid">
        {{# layui.each(d.list, function(index, item){ }}
        <li>
            <div class="layui-row layui-col-space30">
                <div class="layui-col-md1 layui-col-sm1 layui-col-xs2 " style="width: 100px;height: 100px;">
                    {{# if(isNotEmpty(item.photoUrl)){ }}
                    <img src="{{getHttpPath()+item.photoUrl}}" style="width: 100%;height: 100%" class="layui-circle"
                         alt="头像"/>
                    {{# }else{ }}
                    <img  style="width: 100%;height: 100%" class="layui-circle" src="${baseprefix}/images/img_error.jpg"
                         alt="图片" >
                    {{# } }}
                </div>
                <div class="layui-col-md10   layui-col-sm10 layui-col-xs9">
                    <div class="layui-row">
                        用户： {{item.userName}}

                    </div>
                    <br>
                    <div class="layui-row " style="color: grey">
                        发表时间{{# var util=layui.util; }}
                        {{# var time=util.toDateString(item.createTime,"yyyy-MM-dd HH:mm:ss"); }}
                        {{time}}

                    </div>

                    <%--点赞--%>
                    <%--<div class="layui-row">--%>
                    <%--<i class="iconfont icon-zan " style="font-size: 20px;color: grey" ><span style="margin-left:10px;font-size:14px;">0</span></i>--%>
                    <%--</div>--%>
                </div>
                <br>
                <br>
                <div class="layui-row">

                    {{item.content}}
                </div>
            </div>
        </li>
        {{# }); }}
        {{# if(isEmpty(d.list)){ }}

        {{# } }}
    </ul>
</script>
</body>
</html>

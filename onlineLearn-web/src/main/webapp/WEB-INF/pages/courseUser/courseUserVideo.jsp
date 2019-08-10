<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/4/18 0018
  Time: 22:42
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
    <title>课程学习</title>
    <style>
        .layui-nav-tree{
            width: 100%;
            /*background: white;*/
        }
    </style>
</head>
<body ms-controller="tCourseUserVideo">
<div class="layui-fluid top20">
    <div class="layui-card">
        <div class="layui-row ">
            <%--播放视频--%>
            <div class="layui-col-md9 layui-fluid" style="">
                <div class="layui-row" style="padding-bottom: 20px;padding-top: 5px">

                    <div class="layui-col-md2 layui-col-md-offset1">
                        <a class="acursor" href="${baseprefix}/courseUser/courseUserMain"><i
                                class="layui-icon layui-icon-return">&nbsp;返回课程主页</i></a>
                    </div>
                    <div :if="@playName" class="layui-col-md8 layui-bg-green layui-fluid"
                         style="height: 30px;line-height: 30px;border-radius: 10px;">在观看的章节:&nbsp;{{@playName}}
                    </div>
                    <div :if="!@playName" class="layui-col-md6 layui-col-sm8 layui-bg-green layui-fluid"
                         style="height: 30px;line-height: 30px;border-radius: 10px; ">无正在观看的章节,请点击右侧观看视频
                    </div>
                </div>
                <%--播放视频--%>
                <div class="layui-row " style="padding-top:10px;padding-bottom: 20px;background: darkgrey">
                    <div class="layui-col-md10 layui-col-md-offset1" id="video">
                        <%--<div class="layui-fluid" id="video" >--%>

                    </div>

                </div>
                <br>
                <br>
                <br>
                <br>

            </div>

            <%--播放列表--%>
            <div class="layui-col-md3 ">
                <div class="layui-row layui-card">
                    <div class="layui-col-md8">
                        <div class="layui-fluid" style="padding-top: 10px">
                            <span style="font-size: 20px;font-weight: bolder">课程：{{@name}}</span>
                        </div>
                    </div>
                    <div class="layui-col-md4" style="height: 100px">
                        <img ms-attr="{'src':@photoUrl}" style="width: 100%;height: 100%">
                    </div>
                </div>
                <div class="layui-row ">
                    <div class="layui-row layui-fluid" style="text-align: left;font-weight: bolder;font-size: 18px;">
                        课程目录
                    </div>
                    <br>
<%--                    <ul class="layui-row layui-card layui-timeline" id="courseVideoList" style="overflow: auto">--%>

<%--                        <li if="@outlineList.length>0" class="layui-timeline-item" ms-for="($index, item) in @outlineList">--%>
<%--                            <i class="layui-icon layui-timeline-axis">&#xe63f;</i>--%>
<%--                            <div class="layui-timeline-content layui-text">--%>
<%--                                <h3 class="layui-timeline-title">{{item.name}}:{{item.text}}</h3>--%>
<%--                                <br>--%>
<%--                                <div :if="@item.courseHouse.length>0" class="layui-row" ms-for="($index2, item2) in @item.courseHouse">--%>
<%--                                    <i :if="item2.video.progress==100"--%>
<%--                                       class="layui-col-md1 layui-icon layui-icon-ok-circle"--%>
<%--                                       style="color: green;height: 40px;line-height: 40px;font-size: 25px"></i>--%>
<%--                                    <i :if="item2.video.progress==0"--%>
<%--                                       class="layui-col-md1 layui-icon layui-icon-add-circle "--%>
<%--                                       style="color: red;height: 40px;line-height: 40px;font-size: 25px"></i>--%>
<%--                                    <i :if="item2.video.progress>0&&item2.video.progress<100"--%>
<%--                                       class="layui-col-md1 layui-icon layui-icon-loading "--%>
<%--                                       style="color: orange;height: 40px;line-height: 40px;font-size: 25px"></i>--%>
<%--                                    <div class="layui-col-md11">--%>
<%--                            <span style="height: 40px;line-height: 40px;"--%>
<%--                                  class="layui-col-md12 layui-col-sm12 layui-fluid  acursor videospan"--%>
<%--                                  :click="studyPlay($index,$index2)">{{item2.name}}:{{item2.text}}</span>--%>
<%--                                        <div class="layui-col-md12 layui-col-sm12 layui-fluid layui-progress"--%>
<%--                                             ms-attr="{'lay-filter':'videoProgress_'+$index+'_'+$index2,'progress':item2.video.progress}"--%>
<%--                                             style="margin-bottom: 10px;margin-top: 1px;">--%>
<%--                                            <div :if="item2.video.progress==100" class="layui-progress-bar layui-bg-green"--%>
<%--                                                 ms-attr="{'lay-percent':item2.video.progress+'%'}" ></div>--%>
<%--                                            <div :if="item2.video.progress==0" class="layui-progress-bar layui-bg-red"--%>
<%--                                                 ms-attr="{'lay-percent':item2.video.progress+'%'}"></div>--%>
<%--                                            <div :if="item2.video.progress>0&&item2.video.progress<100" class="layui-progress-bar layui-bg-orange"--%>
<%--                                                 ms-attr="{'lay-percent':item2.video.progress+'%'}"></div>--%>
<%--                                        </div>--%>
<%--                                    </div>--%>
<%--                                    <br>--%>
<%--                                </div>--%>
<%--                            </div>--%>
<%--                        </li>--%>
<%--                        <li :if="@outlineList.length==0" class="layui-timeline-item">--%>
<%--                            暂无课时--%>
<%--                        </li>--%>
<%--                    </ul>--%>

                    <ul class="layui-row   layui-card layui-nav layui-nav-tree layui-bg-white layui-inline" id="courseVideoList" style="overflow: auto">
                        <li class="layui-row  layui-nav-item layui-nav-itemed" ms-for="($index, item) in @outlineList">
                            <a class="layui-row" href="javascript:;">{{item.name}}:{{item.text}}</a>
                            <dl  class="layui-row  layui-nav-child">
                                <dd :if="@item.courseHouse.length>0" class="layui-row layui-fluid" ms-for="($index2, item2) in @item.courseHouse">
                                    <i :if="item2.video.progress==100"
                                       class="layui-col-md1 layui-icon layui-icon-ok-circle"
                                       style="color: green;height: 40px;line-height: 40px;font-size: 25px"></i>
                                    <i :if="item2.video.progress==0"
                                       class="layui-col-md1 layui-icon layui-icon-add-circle "
                                       style="color: red;height: 40px;line-height: 40px;font-size: 25px"></i>
                                    <i :if="item2.video.progress>0&&item2.video.progress<100"
                                       class="layui-col-md1 layui-icon layui-icon-loading "
                                       style="color: orange;height: 40px;line-height: 40px;font-size: 25px"></i>
                                    <div class="layui-col-md11">
                            <span style="height: 40px;line-height: 40px;"
                                  class="layui-col-md12 layui-col-sm12 layui-fluid  acursor videospan"
                                  :click="studyPlay($index,$index2)">{{item2.name}}:{{item2.text}}</span>
                                        <div class="layui-col-md12 layui-col-sm12 layui-fluid layui-progress"
                                             ms-attr="{'lay-filter':'videoProgress_'+$index+'_'+$index2,'progress':item2.video.progress}"
                                             style="margin-bottom: 10px;margin-top: 1px;">
                                            <div :if="item2.video.progress==100" class="layui-progress-bar layui-bg-green"
                                                 ms-attr="{'lay-percent':item2.video.progress+'%'}" ></div>
                                            <div :if="item2.video.progress==0" class="layui-progress-bar layui-bg-red"
                                                 ms-attr="{'lay-percent':item2.video.progress+'%'}"></div>
                                            <div :if="item2.video.progress>0&&item2.video.progress<100" class="layui-progress-bar layui-bg-orange"
                                                 ms-attr="{'lay-percent':item2.video.progress+'%'}"></div>
                                        </div>
                                    </div>
                                </dd>
                            </dl>
                        </li>
                        <li :if="@outlineList.length==0" class="layui-row layui-nav-item">
                            暂无课时
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript" src="${baseprefix}/lib/ckplayer/ckplayer.js?t=<%= System.currentTimeMillis()%>" charset="UTF-8"></script>
<script type="text/javascript" src="${baseprefix}/js/courseUser/courseUserVideo.js?t=<%= System.currentTimeMillis()%>"></script>
<%--<script type="text/javascript" src="${baseprefix}/js/footer.js?t=<%= System.currentTimeMillis()%>"></script>--%>
<%--<script type="text/html" id="courseHoursListTemp">--%>
    <%--{{# layui.each(d.list, function(index, item){ }}--%>
    <%--<li class="layui-timeline-item">--%>
        <%--<i class="layui-icon layui-timeline-axis">&#xe63f;</i>--%>
        <%--<div class="layui-timeline-content layui-text">--%>
            <%--<h3 class="layui-timeline-title">{{item.name}}:{{item.text}}</h3>--%>
            <%--&lt;%&ndash;<p class="layui-row">&ndash;%&gt;--%>
            <%--<br>--%>
            <%--{{# layui.each(item.courseHouse, function(index2, item2){ }}--%>
            <%--<div class="layui-row">--%>
                <%--{{# if(item2.video.progress==100){ }}--%>
                <%--<i class="layui-col-md1 layui-icon layui-icon-ok-circle"--%>
                   <%--style="color: green;height: 40px;line-height: 40px;font-size: 25px"></i>--%>
                <%--{{# }else if(item2.video.progress==0){ }}--%>
                <%--<i class="layui-col-md1 layui-icon layui-icon-add-circle "--%>
                   <%--style="color: red;height: 40px;line-height: 40px;font-size: 25px"></i>--%>
                <%--{{# }else{ }}--%>
                <%--<i class="layui-col-md1 layui-icon layui-icon-loading "--%>
                   <%--style="color: orange;height: 40px;line-height: 40px;font-size: 25px"></i>--%>
                <%--{{# } }}--%>
                <%--<div class="layui-col-md11">--%>
                <%--<span style="height: 40px;line-height: 40px;"--%>
                      <%--class="layui-col-md12 layui-col-sm12 layui-fluid  acursor videospan"--%>
                      <%--onclick="studyPlay('{{index}}','{{index2}}')">--%>
                    <%--{{item2.name}}:{{item2.text}}--%>
                <%--</span>--%>
                    <%--<div class="layui-col-md12 layui-col-sm12 layui-fluid layui-progress"--%>
                         <%--lay-filter="videoProgress{{index2}}" progress="{{item2.video.progress}}"--%>
                         <%--style="margin-bottom: 10px;">--%>
                        <%--{{# if(item2.video.progress==100){ }}--%>
                        <%--<div class="layui-progress-bar layui-bg-green" lay-percent="{{item2.video.progress+'%'}}"></div>--%>
                        <%--{{# }else{ }}--%>
                        <%--<div class="layui-progress-bar layui-bg-red" lay-percent="{{item2.video.progress+'%'}}"></div>--%>
                        <%--{{# } }}--%>
                    <%--</div>--%>
                <%--</div>--%>
                <%--<br>--%>
            <%--</div>--%>
            <%--{{# }); }}--%>
            <%--&lt;%&ndash;</p>&ndash;%&gt;--%>
        <%--</div>--%>
    <%--</li>--%>
    <%--{{# }); }}--%>
    <%--{{# if(isEmpty(d.list)){ }}--%>
    <%--<li class="layui-timeline-item">--%>
        <%--暂无课时--%>
    <%--</li>--%>
    <%--{{# } }}--%>
<%--</script>--%>
</body>
</html>

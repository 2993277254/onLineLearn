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
    <title>个人主页</title>
</head>
<body ms-controller="courseUserMain">
<div class="layui-container layui-card">
    <div class="layui-fluid">
        <div class="layui-row">
            <div class="layui-tab layui-tab-brief">
                <ul class="layui-tab-title" id="courseUl">
                    <li class="layui-this">参加的课程({{@total}})</li>
                    <c:choose>
                        <c:when test="${sessionScope.ollSysUser.identity  != 4}">
                            <li >开设的课程({{owntotal}})</li>
                        </c:when>
                    </c:choose>
                </ul>
                <div class="layui-tab-content">
                    <div class="layui-tab-item layui-show">
                        <div class="layui-row" id="hasCourseList">

                        </div>
                        <div :if="@total!=0" class="layui-row layui-fluid" style="text-align: center"
                             id="hasCourse-page">

                        </div>
                    </div>
                    <div class="layui-tab-item">
                        <ul class="layui-row" >
                            <%--添加课程--%>
                            <li class="layui-row acursor" onclick="saveOrEditCourse()">
                                <div class="layui-col-md4 layui-bg-gray" style="height: 150px;text-align: center;line-height: 150px;">
                                    <i class="layui-icon layui-icon-add-circle ">添加课程</i>
                                </div>
                            </li>
                        </ul>
                        <div class="layui-row" id="ownOPenCourseList">

                        </div>
                        <div :if="@owntotal!=0" class="layui-row layui-fluid" style="text-align: center"
                             id="ownCourse-page">

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>
<div class="layui-fluid layui-card" id="courseprogrss" style="display: none;margin-top: 20px;">

    <div class="layui-progress layui-progress-big" lay-showPercent="yes">
        <div class="layui-progress-bar" lay-percent="10%"></div>
    </div>
    视频学习进度：

    <div class="layui-progress layui-progress-big" lay-showPercent="yes" lay-filter="progressTotal">
        <div class="layui-progress-bar" lay-percent="0%"></div>
    </div>
    参加测验进度：
</div>
<script type="text/javascript" src="${baseprefix}/js/courseUser/courseUserMain.js?t=<%= System.currentTimeMillis()%>"></script>
<%--<script type="text/javascript" src="${baseprefix}/js/footer.js?t=<%= System.currentTimeMillis()%>"></script>--%>
<%--参加的课程模板--%>
<script type="text/html" id="hasCourseListTemp">
    <ul class="layui-row layui-col-space10 top20 ownCourse">
        {{# layui.each(d.list, function(index, item){ }}
        {{# if(item.tCourse.status==2){}}
        <li class="layui-col-md4 ownCourseli"   >
            <div class="layui-row layui-col-md10 layui-col-md-offset1  layui-fluid  addedcourseli" >
                <div class="layui-row" style="padding-top: 10px">
                    <img src="{{getHttpPath()+item.tCourse.photoUrl}}" class="img-box transition "
                         style="height: 150px;" >
                </div>
                <div class="layui-row top20">
                    <span class="layui-badge layui-col-md3"> {{ baseFuncInfo.getSysDictName('course_type_',item.tCourse.type) }}</span>
                    <div class="layui-col-md8 layui-col-md-offset1">{{item.tCourse.name}}</div>
                </div>
                <br>
                <%--<div class="layui-row">已更新xx课时</div>--%>
                <br>
                <div class="layui-row">
                        <div class="layui-col-md12 layui-btn layui-btn-sm" onclick="goCourseVideo('{{item.courseId}}')">学习</div>
                </div>
                <hr>
                <div class="layui-row">
                    <div class="layui-col-md12 layui-btn layui-btn-sm" onclick="goDiscuss('{{item.courseId}}','{{item.tCourse.name}}')">讨论</div>
                </div>
                <hr>
                <%--<div class="layui-row">--%>
                    <%--<button class="layui-col-md12 layui-btn layui-btn-sm" onclick="showprogess('{{item.courseId}}','{{item.tCourse.outline}}')">查看进度</button>--%>
                <%--</div>--%>
                <hr>
            </div>
            <%--右上角的垂直图标--%>
            <%--<div class="layui-col-md1">  <i class="layui-icon layui-icon-more-vertical" style="font-size: 25px;margin-left: -30px;color: white" onclick="courseDel()"></i></div>--%>
        </li>
        {{# }else{}}
        <li class="layui-col-md4 ownCourseli"   >
            <div class="layui-row layui-col-md10 layui-col-md-offset1  layui-fluid  addedcourseli" >
                <div class="layui-row" style="padding-top: 10px">
                    <img src="{{getHttpPath()+item.tCourse.photoUrl}}" class="img-box transition "
                         style="height: 150px;" >
                </div>
                <div class="layui-row top20">
                    <span class="layui-badge layui-col-md3"> {{ baseFuncInfo.getSysDictName('course_type_',item.tCourse.type) }}</span>
                    <div class="layui-col-md8 layui-col-md-offset1">{{item.tCourse.name}}</div>
                </div>
                <br>
                <%--<div class="layui-row">已更新xx课时</div>--%>
                <hr>
                <div class="layui-row" style="padding-bottom: 20px;">
                    <span style="color: red">由于课程有变动，所以该课程暂时关闭</span>
                </div>

            </div>
            <%--右上角的垂直图标--%>
            <%--<div class="layui-col-md1">  <i class="layui-icon layui-icon-more-vertical" style="font-size: 25px;margin-left: -30px;color: white" onclick="courseDel()"></i></div>--%>
        </li>
        {{#}}}
        {{# }); }}
        {{# if(isEmpty(d.list)){ }}
        <li class="layui-col-md4 layui-bg-gray" style="height: 150px;text-align: center;line-height: 150px;">
            暂无参加的课程
        </li>

        {{# } }}
    </ul>
    <br>
</script>
<%--讲师开设的课程模板--%>
<script type="text/html" id="ownOPenCourseListTemp">
    <ul class="layui-row layui-col-space10 top20 ownCourse">
        {{# layui.each(d.list, function(index, item){ }}
        <li class="layui-col-md4 ownCourseli"  style="">
            <div class="layui-row layui-col-md10 layui-col-md-offset1  layui-fluid  addedcourseli" style="border: 1px solid grey;">
                <div class="layui-row" style="padding-top: 10px">
                    <img src="{{getHttpPath()+item.photoUrl}}" class="img-box transition "
                         style="height: 150px;" >
                </div>
                <div class="layui-row top20">
                    <span class="layui-badge layui-col-md3"> {{ baseFuncInfo.getSysDictName('course_type_',item.type) }}</span>
                    <div class="layui-col-md8 layui-col-md-offset1">{{item.name}}</div>
                </div>
                <hr>
                <div class="layui-row">参加人数:<span style="color: green;font-weight: bolder;font-size: 20px;">{{item.personNum}}</span>人</div>
                <hr>
                <div class="layui-row">
                    <div class="layui-col-md12">
                        {{# if(item.status==1){}}
                        <div class="layui-btn layui-btn-fluid layui-btn-disabled" disabled>课程管理</div>
                        {{# }else{ }}
                        <div class="layui-btn layui-btn-fluid" onclick="saveOrEditCourse('{{item.uid}}')">课程管理</div>
                        {{# } }}
                    </div>

                 </div>
                <hr>
                <%--<div class="layui-row">--%>
                    <%--<div class="layui-col-md12">--%>
                        <%--<div class="layui-btn layui-btn-fluid" onclick="courseTest('{{item.uid}}','{{item.name}}')">测验管理</div>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <%--<hr>--%>
                <div class="layui-row">
                    <div class="layui-col-md12">
                        {{# if(item.status==1){}}
                        <div class="layui-btn layui-btn-fluid layui-btn-disabled" disabled>讨论管理</div>
                        {{# }else{ }}
                        <div class="layui-btn layui-btn-fluid" onclick="goDiscuss('{{item.uid}}','{{item.name}}',true)">讨论管理</div>
                        {{# } }}
                    </div>
                </div>
                <hr>
                <%--<div class="layui-row">--%>
                    <%--<div class="layui-col-md12">--%>
                        <%--<div class="layui-btn layui-btn-fluid" onclick="courseExam('{{item.uid}}','{{item.name}}')">考试管理</div>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <%--<hr>--%>
                <div class="layui-row">
                    <%--状态:{{ baseFuncInfo.getSysDictName('course_status_',item.status) }}--%>
                    审核状态：
                    {{# if(item.status==1){}}
                        <span style="color:orange"><i class="layui-icon layui-icon-loading"></i>等待审核中...</span>
                    {{# }else if(item.status==2){}}
                        <span style="color:green"><i class="layui-icon layui-icon-ok-circle " style="font-size: 20px;"></i>审核成功，课程已发布</span>
                    {{# }else{}}
                        <span style="color:red"><i class="layui-icon layui-icon-close-fill" style="font-size: 20px;"></i>审核失败&nbsp;&nbsp;&nbsp;<span style="color: #0e97e2" onclick="seeSeason('{{item.reason}}','{{item.name}}')">查看原因</span></span>

                    {{# }}}
                </div>
                <br>
            </div>
         </li>
        {{# }); }}
        {{# if(isEmpty(d.list)){ }}
        <%--<li class="layui-col-md4 layui-bg-gray">--%>

        <%--</li>--%>
        {{# } }}
    </ul>
    <br>
</script>
</body>
</html>

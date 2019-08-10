<%@ page language="java" import="java.util.*"
         contentType="text/html;charset=utf-8" %>
<%@ include file="/WEB-INF/base/common.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>讨论列表</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <style>
        .layui-input-block{
            margin-left:0px;
        }
    </style>
</head>
<!--layuiadmin的css-->
<link rel="stylesheet" type="text/css" href="${baseprefix}/layuiadmin/style/admin.css" media="all">
<body ms-controller="tDiscussList">
<div class="layui-fluid ">
    <div class="layui-container layui-card">
        <div class="layui-row" style="padding-top: 10px">
            <div class="layui-col-md2 layui-col-md-offset1">
                <a class="acursor" href="${baseprefix}/courseUser/courseUserMain"><i
                        class="layui-icon layui-icon-return">&nbsp;返回课程主页</i></a>
            </div>

        </div>
        <%--发帖区--%>
        <div class="layui-row layui-fluid layui-card" >

            <div class="layui-form " lay-filter="tDiscussEdit_form" id="tDiscussEdit_form">
                <div class="layui-form-item  layui-hide">
                    <label class="layui-form-label">ID</label>
                    <div class="layui-input-inline">
                        <input type="hidden" name="uid" placeholder="请输入" autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item  layui-hide">
                    <label class="layui-form-label">userId</label>
                    <div class="layui-input-inline">
                        <input type="hidden" name="userId" placeholder="请输入" autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item  layui-hide">
                    <label class="layui-form-label">courseId</label>
                    <div class="layui-input-inline">
                        <input type="hidden" name="courseId" placeholder="请输入" autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item ">
                    <div class="layui-input-block">
                        <textarea id="addDiscuss"   name="content"   placeholder="请输入内容"  class="layui-textarea"></textarea>
                    </div>
                </div>
                <div class="layui-form-item">
                    <button class="layui-btn" lay-filter="tDiscussEdit_submit" id="tDiscussEdit_submit" lay-submit>发表评论</button>
                </div>
            </div>
        </div>

        <div class="layui-row layui-fluid">
            <div class="layui-row" style="text-align: center;font-weight: bolder;font-size: 18px;">
                课程<span style="color: #00a2d4">{{@name}}</span>的讨论

            </div>
            <div class="layui-row top20 bottom20" style="text-align: center">
                共{{ @total }}条评论
            </div>
            <div class="layui-row" id="course_discuss_list">

            </div>
            <div :if="@total!=0" class="layui-row" style="text-align: center" id="discuss-page"></div>

        </div>
        <br>

    </div>
</div>
<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="${baseprefix}/js/course/tDiscussList.js?t=<%= System.currentTimeMillis()%>"></script>
<%--讨论模板--%>
<script id="courseDiscussListTemp" type="text/html">
    <ul class="layui-fluid">
        {{#  layui.each(d.list, function(index, item){ }}
        <li>
            <div class="layui-row layui-col-space30">
                {{# if(isNotEmpty(item.photoUrl)){}}
                <div class="layui-col-md1 layui-col-sm1 layui-col-xs2 " style="width: 100px;height: 100px;">
                    <img src="{{getHttpPath()+item.photoUrl}}" style="width: 100%;height: 100%;border: 1px gray solid " class="layui-circle"  alt="头像"/>
                </div>
                {{# }else{ }}
                <div class="layui-col-md1 layui-col-sm1 layui-col-xs2 " style="width: 100px;height: 100px;">
                    <img src="${baseprefix}/images/img_error.jpg" style="width: 100%;height: 100%;border: 1px gray solid " class="layui-circle"  alt="头像"/>
                </div>
                {{# } }}
                <div class="layui-col-md10   layui-col-sm10 layui-col-xs9">
                    <div class="layui-row">
                        用户： {{item.userName}}

                    </div>
                    <br>
                    <div class="layui-row " style="color: grey">
                        发表时间:{{# var util=layui.util;  }}
                        {{# var time=util.toDateString(item.createTime,"yyyy-MM-dd HH:mm:ss");  }}
                        {{time}}

                    </div>
                    <br>

                    <%--点赞--%>
                    <%--<div class="layui-row">--%>
                    <%--<i class="iconfont icon-zan " style="font-size: 20px;color: grey" ><span style="margin-left:10px;font-size:14px;">0</span></i>--%>
                    <%--</div>--%>
                </div>
                <br>
                <br>
                <div class="layui-row ">
                    {{item.content}}
                </div>
            </div>
        </li>
        {{#  }); }}
        {{#  if(isEmpty(d.list)){ }}

        {{#  } }}
    </ul>
</script>
</body>
</html>
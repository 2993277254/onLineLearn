<%@ page language="java" import="java.util.*"
         contentType="text/html;charset=utf-8"%>
<%@ include file="/WEB-INF/base/adminCommon.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title></title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <style>
        .layui-input-block{
            margin-left: 10px;
        }
    </style>
</head>
<body ms-controller="tCourseExam">
<div class="layui-fluid">
    <div class="layui-row">
        <div class="layui-form" lay-filter="tCourseExam_form" id="tCourseExam_form" style="padding: 20px 30px 0 0;">
            <div class="layui-row" style="padding-bottom: 20px;">
                <button class="layui-btn">添加题目</button>
            </div>
            <div class="layui-form-item">
                <div class="layui-col-md1 layui-col-sm2" style="text-align: center;line-height: 36px">
                    第一题
                </div>
                <div class="layui-col-md9 layui-col-sm8">
                    <div class="layui-input-block">
                        <input  class="layui-input">
                    </div>
                </div>
                <div class="layui-col-md2 layui-col-sm2 layui-text-center" style="padding-bottom: 20px;">
                    <button class="layui-btn layui-btn-sm layui-btn-danger" style="margin-left: 10px;">删除</button>
                </div>
                <%--选项模板--%>
                <div class="layui-row" >
                    <div class="layui-col-md11 layui-col-md-offset1 layui-col-sm11 layui-col-sm-offset1">
                        <div class="layui-form-item">
                            <div class=" layui-col-sm2 layui-text-center" style="text-align: right">
                                <input type="radio" class="layui-input" title="A">
                            </div>
                            <div class="layui-col-md8 layui-col-sm8">
                                <div class="layui-row layui-input-block">
                                    <input class="layui-input">
                                </div>
                            </div>
                            <%--<div class="layui-col-md1 layui-col-sm2 layui-text-center" >--%>
                                <%--<button class="layui-btn layui-btn-sm layui-btn-danger" style="margin-left: 10px">删除</button>--%>
                            <%--</div>--%>
                        </div>
                    </div>
                </div>

            </div>

            <%-- 隐藏的提交按钮，必须要，用于验证表单，触发提交表单的作用--%>
            <div class="layui-form-item layui-hide">
                <button class="layui-btn" lay-submit lay-filter="tCourseExam_submit" id="tCourseExam_submit">提交</button>
            </div>
        </div>

    </div>
</div>

<script type="text/javascript" src="${baseprefix}/js/courseUser/tCourseExam.js?t=<%= System.currentTimeMillis()%>"></script>
</body>
</html>
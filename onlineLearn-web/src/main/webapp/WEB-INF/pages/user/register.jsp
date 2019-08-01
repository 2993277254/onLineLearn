<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/12/20
  Time: 20:19
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
    <title>登录界面</title>
</head>
<body ms-controller="register">
<div class="layui-container">
    <div class="">
        <%--注册表单--%>
        <div class="layui-row layui-card">
        <div class="layui-col-md8 layui-col-md-offset2 layui-form layui-form-pane layui-fluid  top20" lay-filter="registerFilter" style="padding-top: 20px;">
            <div class="layui-form-item">
                <label class="layui-form-label"><span class="edit-verify-span">*</span>账号</label>
                <div class="layui-input-inline">
                    <input type="text" name="userName" lay-verify="required" placeholder="请输入" autocomplete="off"
                           class="layui-input" maxlength="20" lay-verType="tips">
                </div>
                <div class="layui-form-mid layui-word-aux">将会成为您唯一的登入名</div>

            </div>
            <div class="layui-form-item">
                <label class="layui-form-label"><span class="edit-verify-span">*</span>昵称</label>
                <div class="layui-input-inline">
                    <input type="text" name="nickName" lay-verify="required" placeholder="请输入" autocomplete="off"
                           class="layui-input" maxlength="20" lay-verType="tips">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label"><span class="edit-verify-span">*</span>密码</label>
                <div class="layui-input-inline">
                    <input type="password" name="passWord" lay-verify="required" placeholder="请输入" autocomplete="off"
                           class="layui-input" maxlength="20" lay-verType="tips">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label"><span class="edit-verify-span">*</span>确认密码</label>
                <div class="layui-input-inline">
                    <input type="password" name="rePassword" lay-verify="required" placeholder="请输入" autocomplete="off"
                           class="layui-input" maxlength="20" lay-verType="tips">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label"><span class="edit-verify-span">*</span>单选框</label>
                <div class="layui-input-block">
                    <%--<input type="radio" name="sex" value="1" title="男" checked>--%>
                    <%--<input type="radio" name="sex" value="2" title="女" >--%>
                    <input lay-verType="tips" type="radio" lay-verify="radio" lay-verify-msg="单选框至少选一项" name="sex"
                           ms-attr="{value:el.value,title:el.name,checked:true&&$index==0}"
                           ms-for="($index, el) in @baseFuncInfo.getSysDictByCode('sys_sex_')">
                </div>
            </div>
            <div class="layui-form-item layui-form-text">
                <label class="layui-form-label">签名</label>
                <div class="layui-input-block">
                    <textarea placeholder="你的签名，天下谁人不识君" class="layui-textarea" name="remark"
                              maxlength="255" lay-verType="tips"></textarea>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label"><span class="edit-verify-span">*</span>验证码</label>
                <div class="layui-input-inline">
                    <input type="text" name="registerImgVerify" lay-verify="required" placeholder="请输入验证码"
                           autocomplete="off" class="layui-input" maxlength="4" lay-verType="tips">
                </div>
                <div class="layui-form-mid layui-word-aux">
                    <img class=" flowImg" id="registerImgVerify" src="${baseprefix}/images/srcLoading.gif"
                         alt="点击更换验证码">
                    <span class="hrefStyle" onclick="getImg()">看不清，换一张</span></div>
            </div>
            <div class="layui-form-item ">
                <button class="layui-btn" lay-submit lay-filter="register_submit" id="register_submit">注册</button>
            </div>
        </div>
        </div>
        </div>
</div>

<script type="text/javascript" src="${baseprefix}/js/user/register.js?t=<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="${baseprefix}/js/footer.js?t=<%= System.currentTimeMillis()%>"></script>
</body>

</html>

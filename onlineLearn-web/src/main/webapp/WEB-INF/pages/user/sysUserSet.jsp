<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/12/31
  Time: 20:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/WEB-INF/base/common.jsp" %>
<html>
<head>
    <title>账号设置</title>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
</head>
<body ms-controller="sysUserSet">
<div class="layui-container fly-marginTop fly-user-main">
    <div class="fly-panel fly-panel-user" pad20>
        <div class="layui-tab layui-tab-brief">
            <ul class="layui-tab-title" id="LAY_mine">
                <li class="layui-this">我的资料</li>
                <li lay-id="pass">密码</li>
            </ul>
            <div class="layui-tab-content" style="padding: 20px 0;">
                <div class="layui-form layui-form-pane layui-tab-item layui-show" lay-filter="base_form" id="base_form">
                    <div class="layui-form-item layui-hide">
                        <label class="layui-form-label">ID</label>
                        <div class="layui-input-inline">
                            <input type="text" name="uid" value="" placeholder="请输入" autocomplete="off"
                                   class="layui-input" maxlength="20">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">昵称</label>
                        <div class="layui-input-inline">
                            <input type="text" name="nickName" value="" placeholder="请输入" autocomplete="off"
                                   class="layui-input" maxlength="20" lay-verType="tips">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">性别</label>
                        <div class="layui-input-block">
                             <input type="radio" lay-verify="radio" lay-verify-msg="单选框至少选一项" name="sex"
                                   ms-attr="{value:el.value,title:el.name,checked:true&&$index==0}"
                                   ms-for="($index, el) in @baseFuncInfo.getSysDictByCode('sys_sex_')"
                                   lay-verType="tips">
                        </div>
                    </div>
                    <div class="layui-form-item layui-form-text">
                        <label class="layui-form-label">签名</label>
                        <div class="layui-input-block">
                            <textarea placeholder="随便输入一点什么刷存在感" class="layui-textarea" name="remark"
                                      maxlength="255" lay-verType="tips"></textarea>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">头像</label>
                        <%-- 隐藏的文本域，用于存文件url--%>
                        <input type="hidden" name="pictureUrl" placeholder="请输入"
                               autocomplete="off" class="layui-input">
                        <button style="float: left;" type="button" class="layui-btn" id="pictureUrl_upload">
                            <i class="layui-icon layui-icon-add-circle-fine">上传图片</i>
                        </button>
                        <div id="uploading" class="layui-hide" style="width: 100px;float: left">
                            上传进度：
                            <div class="layui-progress layui-progress-big" lay-showpercent="true"
                                 lay-filter="filefilter">
                                <div class="layui-progress-bar layui-bg-green" lay-percent="0%"></div>
                                      
                            </div>

                        </div>
                        <%--用于显示上传完的图片--%>
                        <div class="layui-input-inline pl-15" style="width: auto;" id="pictureUrl_upload_div">

                        </div>
                    </div>
                    <div class="layui-form-item ">
                        <button class="layui-btn" lay-submit lay-filter="sysUserEdit_submit" id="sysUserEdit_submit">
                            保存
                        </button>
                    </div>
                </div>
                <div class="layui-form layui-form-pane layui-tab-item" lay-filter="psw_form" id="psw_form">
                    <%--<form action="/user/repass" method="post">--%>
                    <%--<div class="layui-form-item">--%>
                    <%--<label for="L_nowpass" class="layui-form-label">当前密码</label>--%>
                    <%--<div class="layui-input-inline">--%>
                    <%--<input type="password" id="L_nowpass" name="nowpass" required lay-verify="required" autocomplete="off" class="layui-input">--%>
                    <%--</div>--%>
                    <%--</div>--%>
                    <div class="layui-form-item layui-hide">
                        <label class="layui-form-label">ID</label>
                        <div class="layui-input-inline">
                            <input type="text" name="uid" value="" placeholder="请输入" autocomplete="off"
                                   class="layui-input" maxlength="20">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">新密码</label>
                        <div class="layui-input-inline">
                            <input type="password" lay-verType="tips" name="passWord" lay-verify="required" autocomplete="off"
                                   class="layui-input">
                        </div>
                        <div class="layui-form-mid layui-word-aux">6到16个字符</div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">确认密码</label>
                        <div class="layui-input-inline">
                            <input type="password" lay-verType="tips" name="repassWord" lay-verify="required"
                                   autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <button class="layui-btn" key="set-mine" lay-filter="rePassWord" lay-submit>确认修改</button>
                    </div>
                    <%--</form>--%>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${baseprefix}/js/user/sysUserSet.js?t=<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="${baseprefix}/js/footer.js?t=<%= System.currentTimeMillis()%>"></script>
</body>

</html>

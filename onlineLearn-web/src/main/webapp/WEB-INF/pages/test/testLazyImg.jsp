<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/1/18
  Time: 21:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/WEB-INF/base/common.jsp" %>
<html>
<head>
    <title>测试图片懒加载</title>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

</head>
<body>
<div class="layui-container" >
    <div class="site-demo-flow" id="demo3">
        <img lay-src="https://gw.alicdn.com/bao/uploaded/i2/701696736/TB2PNl5ahQa61Bjy0FhXXaalFXa_!!701696736.jpg_400x400q90.jpg?t=1565412426992">
        <img lay-src="https://gw.alicdn.com/bao/uploaded/i2/162734861/TB2V5rsX_gc61BjSZFkXXcTkFXa_!!162734861.jpg_400x400q90.jpg?t=1565412426992">
        <img lay-src="https://gw.alicdn.com/bao/uploaded/i4/69476562/TB2htq4XTka61BjSszfXXXN8pXa_!!69476562.jpg_400x400q90.jpg?t=1565412426992">
        <img lay-src="https://gw.alicdn.com/bao/uploaded/i1/180558071/TB2sy6jXMQc61BjSZFGXXa1DFXa_!!180558071.jpg_400x400q90.jpg?t=1565412426992">
        <img lay-src="https://gw.alicdn.com/bao/uploaded/i2/701696736/TB2PNl5ahQa61Bjy0FhXXaalFXa_!!701696736.jpg_400x400q90.jpg?t=1565412426992">
        <img lay-src="https://gw.alicdn.com/bao/uploaded/i3/285892163/TB2qu2HX9Zb61BjSZPfXXaU.pXa_!!285892163.jpg_400x400q90.jpg?t=1565412426992">
        <img lay-src="https://gw.alicdn.com/bao/uploaded/i2/704998060/TB2S.gAXTgc61BjSZFkXXcTkFXa_!!704998060.jpg_400x400q90.jpg?t=1565412426992">
        <img lay-src="https://gw.alicdn.com/bao/uploaded/i3/117202952/TB25lckX_AX61Bjy0FcXXaSlFXa_!!117202952.jpg_400x400q90.jpg?t=1565412426992">
        <img lay-src="https://gw.alicdn.com/bao/uploaded/i2/162734861/TB2qijoX9Zb61BjSZPfXXaU.pXa_!!162734861.jpg_400x400q90.jpg?t=1565412426992">
        <img lay-src="https://gw.alicdn.com/bao/uploaded/i3/96216586/TB2S7EfXHMc61BjSZFFXXaDLFXa_!!96216586.jpg_400x400q90.jpg?t=1565412426992">
        <img lay-src="https://gw.alicdn.com/bao/uploaded/i8/TB1jSSFNFXXXXcoXFXXYXGcGpXX_M2.SS2_400x400q90.jpg?t=1565412426992">
        <img lay-src="https://gw.alicdn.com/bao/uploaded/i2/162734861/TB2ylbsX37c61BjSZFKXXb6hFXa_!!162734861.jpg_400x400q90.jpg?t=1565412426992">
        <img lay-src="https://gw.alicdn.com/bao/uploaded/i2/117202952/TB2FdyZajUd61BjSZPcXXc6hXXa_!!117202952.jpg_400x400q90.jpg?t=1565412426992">
        <img lay-src="https://gw.alicdn.com/bao/uploaded/i4/777105914/TB2t.qbXZwb61Bjy0FfXXXvlpXa_!!777105914.jpg_400x400q90.jpg?t=1565412426992">
        <img lay-src="https://gw.alicdn.com/bao/uploaded/i4/TB1XzupNFXXXXcpXXXXXXXXXXXX_!!0-item_pic.jpg_400x400q90.jpg?t=1565412426992">
    </div>
</div>

<script>
    layui.use(['index'],function () {
        avalon.ready(function () {

        })

    });
</script>
</body>
</html>

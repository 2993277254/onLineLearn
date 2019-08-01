<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/1/18
  Time: 21:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/WEB-INF/base/adminCommon.jsp" %>
<html>
<head>
    <title>ckplayer播放</title>

    <meta charset="utf-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
</head>
<body>
<div class="layui-container" id="video" style="width: 100%; height: 400px;max-width: 600px;"></div>
<script type="text/javascript" src="${baseprefix}/lib/ckplayer/ckplayer.js?t=<%= System.currentTimeMillis()%>" charset="UTF-8"></script>
<script type="text/javascript">
    var videoObject = {
            container: '#video',//“#”代表容器的ID，“.”或“”代表容器的class
            variable: 'player',//该属性必需设置，值等于下面的new chplayer()的对象
            video: '' +
                'http://localhost:8020/uploadfile/video/E131891A15124CD7B6367C9FC4906FA0/E131891A15124CD7B6367C9FC4906FA0.m3u8'
        };
    var player=new ckplayer(videoObject);
    // function loadedHandler12(){//播放器加载后会调用该函数
    //     //player.addListener('time', timeHandler); //监听播放时间,addListener是监听函数，需要传递二个参数，'time'是监听属性，这里是监听时间，timeHandler是监听接受的函数
    //     //player.addListener('seekTime', seekTimeHandler);
    //     //player.addListener('screenshot', screenshotHandler);
    //     //player.
    //     //player.videoMute();
    //     //debugger;
    //     console.log(player.isMobile());
    // }
    // function timeHandler(t){
    //
    //     //player.videoSeek();
    //     //console.log('当前播放的时间：'+t);
    //     //player.videoSeek(player);
    // }
    //
    // function seekTimeHandler(time) {
    //     //'seekTime:'+time);
    //     console.log('seekTime:'+time);
    // }
    //
    // function screenshotHandler(obj){
    //     console.log(obj);
    // }
    /*player.addListener('seek',seek);
    function seek() {
        
    }*/
    //player.addListener('duration', durationHandler); //监听播放时间
    // function durationHandler(duration) {
    //     //'总时间（秒）：' + duration);
    //     console.log(duration);
    // }
    //player.addListener('time', timeHandler);
    // function timeHandler(time) {
    //     //'当前播放时间（秒）：' + time;
    //     console.log(time);
    // }
</script>
<script>
    layui.use('index');
</script>
</body>
</html>

var preview = avalon.define({
    $id: "preview",
    baseFuncInfo: baseFuncInfo//底层基本方法
});
layui.use(['index'], function() {
    //加载layui的模块，index模块是基础模块，也可添加其它
    avalon.ready(function () {
        //所有的入口事件写在这里...
        debugger;
        var path=GetQueryString("path");
        playvidoe(path);
        avalon.scan();
    });
});
/**
 * 预览
 */
var prePlayer="";
function playvidoe(path,name) {

    var videoObject = {
        container: '#video',//“#”代表容器的ID，“.”或“”代表容器的class
        variable: 'prePlayer',//该属性必需设置，值等于下面的new chplayer()的对象
        //loaded:'loadedHandler',//监听播放器加载成功
        //seek:408.068,
        loop:true,
        video: path
    };
    prePlayer=new ckplayer(videoObject);
}
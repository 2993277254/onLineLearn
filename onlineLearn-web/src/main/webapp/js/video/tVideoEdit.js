/**
 * tVideoEdit.jsp的js文件，包括查询，编辑操作
 */
var tVideoEdit = avalon.define({
    $id: "tVideoEdit",
    baseFuncInfo: baseFuncInfo//底层基本方法

});
//定义一个全局变量存放上传成功的视频信息
var videoList=[];
layui.use(['index'],function(){
    //加载layui的模块，index模块是基础模块，也可添加其它
    avalon.ready(function () {
        //所有的入口事件写在这里...
        var id=GetQueryString("id");  //接收变量
        //获取实体信息
        getInfo(id,function(data){
            //在回调函数中，做其它操作，比如获取下拉列表数据，获取其它信息
            //...
        });
        initUpload();
        avalon.scan();
    });
});

/**
 * 获取实体
 * @param id
 * @param $callback 成功验证后的回调函数，
 * 可做其它操作，比如获取下拉列表数据，获取其它信息
 */
function  getInfo(id,$callback){
    if(isEmpty(id)){
        //新增
        typeof $callback === 'function' && $callback({}); //返回一个回调事件
    }else{
        //编辑
        var param={
            "uid":id
        };
        _ajax({
            type: "POST",
            //loading:false,  //是否ajax启用等待旋转框，默认是true
            url: getRootPath()+"tVideo/getInfo.do",
            data:param,
            dataType: "json",
            done:function(data){
                //表单初始赋值
                var form=layui.form; //调用layui的form模块
                form.val('tVideoEdit_form', data);
                typeof $callback === 'function' && $callback(data); //返回一个回调事件
            }
        });
    }
}
/**
 * 验证表单
 * @param $callback 成功验证后的回调函数
 */
function verify_form($callback){
    //监听提交,先定义个隐藏的按钮
    var form=layui.form; //调用layui的form模块
    form.on('submit(tVideoEdit_submit)', function(data){
        //通过表单验证后
        var field = data.field; //获取提交的字段
        typeof $callback === 'function' && $callback(field); //返回一个回调事件
    });
    $("#tVideoEdit_submit").trigger('click');
}

/**
 * 关闭弹窗
 * @param $callBack  成功修改后的回调函数，比如可用作于修改后查询列表
 */
function save($callback){  //菜单保存操作
    //对表单验证
    verify_form(function(field){
        //成功验证后
        var param={
            "videoList":videoList
        }; //表单的元素
        //可以继续添加需要上传的参数
        _ajax({
            type: "POST",
            contentType: "application/json; charset=utf-8", //此设置后台可接受复杂参数，后台接收需要@RequestBody标签
            //loading:true,  //是否ajax启用等待旋转框，默认是true
            url: getRootPath()+"tVideo/saveOrEdit.do",
            data:JSON.stringify(param),
            dataType: "json",
            done:function(data){
                typeof $callback === 'function' && $callback(data); //返回一个回调事件
            }
        });
    });
}
//初始化上传组件
function initUpload() {
    var videoListView=$("#videoList");
    var $btn=$("#videoListAction");
    var time=new Date().getTime()+''+(1000+Math.ceil(Math.random()*1000));
    var module="video";//文件存在的模块
    var chunkSize=10*1024*1024;//每片10m
    //监听分块上传过程中的三个时间点
    WebUploader.Uploader.register({
        "after-send-file":"afterSendFile"  //分片上传完毕
    },{
        //所有分块上传成功后调用此函数
        afterSendFile:function(file){
            _ajax({
                type: "POST",
                loading:false,  //是否ajax启用等待旋转框，默认是true
                url:getRootPath() + 'system/mergeChunks.do',  //ajax将所有片段合并成整体
                data:{
                    module:module,
                    type:"1", //后台接收类型：1.视频 2.音频
                    fileName : file.name,
                    fileSize : file.size,
                    temFileName:time+"_"+file.id
                },
                dataType: "json",
                success:function(res){
                    //合并成功之后的操作
                    var tr = videoListView.find("tr#"+file.id)
                        ,tds = tr.children();
                    var data=res.bizData;
                    if(res.rtnCode=="0"){
                        var video={
                            "fileid":file.id,
                            "name":data.fileName,
                            "path":data.path,
                            "size":WebUploader.Base.formatSize(file.size,2, ['B', 'KB', 'MB','GB']),
                            "ext":data.ext,
                            "time":data.time,
                        };
                        videoList.push(video);
                        tds.eq(2).html('<span style="color: #5FB878;">上传成功</span>');
                        tds.eq(3).find('.demo-reload').addClass('layui-hide'); //显示重传
                        tds.eq(3).find('.demo-delete').removeClass('layui-hide'); //显示删除
                        var uploadfile=uploader.getFile(file.id);
                        if(uploadfile!=undefined&&uploadfile!=null){
                            uploader.removeFile(uploadfile); //删除对应的文件
                        }
                        //上传文件列表中的下一个文件
                        var files=uploader.getFiles("inited");
                        if(files!=undefined&&files!=null&&files.length>0){
                            //上传文件列表中的下一个文件
                            uploader.upload(files[0].id);
                        }
                    }else{
                        tds.eq(2).html('<span style="color: #FF5722;">上传失败，系统错误</span>');
                        tds.eq(3).find('.demo-reload').removeClass('layui-hide'); //显示重传
                        tds.eq(3).find('.demo-delete').removeClass('layui-hide'); //显示删除
                    }
                }
            });
        }

    });
    var uploader = WebUploader.create({
        // swf文件路径
        swf: getRootPath()+ 'lib/webuploader/Uploader.swf',
        // 文件接收服务端。
        server: getRootPath() + 'system/uploadFileByChunks.do',
        // 选择文件的按钮。可选。
        pick : '#selectVideoList',
        // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
        compress:false, // 不压缩image
        prepareNextFile: true,
        accept:{
            title: 'video',
            mimeTypes: 'video/*'
        },
        chunked : true, //开启分片
        chunkSize :  chunkSize, //每片10M
        chunkRetry : 3,//如果失败，则重试3次
        threads : 3,//上传并发数。允许同时最大上传进程数。
        // fileNumLimit : 5,//验证文件总数量, 超出则不允许加入队列
        // fileSizeLimit:6*1024*1024*1024,//6G 验证文件总大小是否超出限制, 超出则不允许加入队列
        // fileSingleSizeLimit:3*1024*1024*1024,  //3G 验证单个文件大小是否超出限制, 超出则不允许加入队列
        // 禁掉全局的拖拽功能。这样不会出现图片拖进页面的时候，把图片打开。
        // disableGlobalDnd : true,
        resize: false
    });
    uploader.on('uploadBeforeSend', function (object, data,headers) {
        //自定义传入表单参数
        data = $.extend(data, {
            "module": module,
            "temFileName": time+"_"+object.file.id  //临时文件名称
        });
    });
    uploader.on('uploadAccept', function (object, ret ) {
        //当某个文件上传到服务端响应后，会派送此事件来询问服务端响应是否有效。
        if ( ret.rtnCode=="1") {
            // 通过return false来告诉组件，此文件上传有错。
            return false;
        }
    });
    //当文件被加入队列以后触发。
    uploader.on( 'fileQueued', function( file ) {
        var tr = $(['<tr id="' + file.id + '">'
            , '<td>' + file.name + '</td>'
            , '<td>' + WebUploader.Base.formatSize(file.size,2, ['B', 'KB', 'MB','GB'])+'</td>'
            , '<td>等待上传</td>'
            , '<td>'
            , '<button class="layui-btn layui-btn-xs demo-reload layui-hide">重传</button>'
            , '<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete">删除</button>'
            , '</td>'
            , '</tr>'].join(''));

        //单个重传
        tr.find('.demo-reload').on('click', function () {
            uploader.upload(file.id); //重新上传
        });
        //删除
        tr.find('.demo-delete').on('click', function () {
            var uploadfile=uploader.getFile(file.id);
            if(uploadfile!=undefined&&uploadfile!=null){
                uploader.removeFile(uploadfile); //删除对应的文件
                tr.remove();
                var list=[];
                $.each(videoList,function(i,item){
                    if(item.fileid!=file.id){
                        list.push(item);
                    }
                });
                videoList=list;
            }
        });
        videoListView.append(tr);
    });
    //当文件上传成功时触发。
    uploader.on( 'uploadSuccess', function( file,res) {
        var tr = videoListView.find("tr#"+file.id)
            ,tds = tr.children();
        tds.eq(2).html('文件已上传，正在处理中...');
    });
    //上传过程中触发，携带上传进度。
    uploader.on( 'uploadProgress', function( file,percentage ) {
        var tr = videoListView.find("tr#"+file.id)
            ,tds = tr.children();
        tds.eq(2).html('<div class="layui-progress layui-progress-big" lay-showpercent="true">' +
            '<div class="layui-progress-bar"  ' +
            'lay-percent="'+Math.round(percentage * 100)+'%" style=" width: '+Math.round(percentage * 100)+'%;">' +
            '<span class="layui-progress-text">'+Math.round(percentage * 100)+'%</span></div></div>');
        tds.eq(3).find('.demo-reload').addClass('layui-hide'); //显示重传
        tds.eq(3).find('.demo-delete').addClass('layui-hide'); //显示重传
    });
    //当文件上传出错时触发。
    uploader.on( 'uploadError', function( file,reason ) {
        var tr = videoListView.find('tr#' + file.id)
            , tds = tr.children();
        tds.eq(2).html('<span style="color: #FF5722;">上传失败，请检查网络</span>');
        tds.eq(3).find('.demo-reload').removeClass('layui-hide'); //显示重传
        tds.eq(3).find('.demo-delete').removeClass('layui-hide'); //显示删除
    });
    //当文件状态触发。
    uploader.on('all', function(type){
        if (type === 'uploadStart'|| type === 'uploadAccept'||type === 'uploadBeforeSend'
            ||type === 'uploadProgress'||type === 'uploadSuccess'||type === 'uploadComplete'
            ||type === 'uploadFinished'){
            //禁止按钮
            $btn.addClass('layui-btn-disabled');
            $btn.attr('disabled',true);
        }else{
            //开启按钮
            $btn.removeClass('layui-btn-disabled');
            $btn.attr('disabled',false);
        }
    });
    //上传按钮的onclick
    $btn.on('click', function() {
        //上传文件列表中的下一个文件
        var files=uploader.getFiles("inited");
        if(files==null||files.length==0){
            warningToast("请选择需要的上传文件");
            return false;
        }
        //开始上传文件
        uploader.upload(files[0].id);
    });

}



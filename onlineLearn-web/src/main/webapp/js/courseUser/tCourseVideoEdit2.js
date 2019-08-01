/**
 * tCourseVideoEdit2.jsp的js文件，包括查询，编辑操作
 */
var tCourseVideoEdit2 = avalon.define({
    $id: "tCourseVideoEdit2",
    baseFuncInfo: baseFuncInfo//底层基本方法
    ,videoData:""
    ,isUpload:0
});

layui.use(['index'],function(){
    //加载layui的模块，index模块是基础模块，也可添加其它
    avalon.ready(function () {
        //所有的入口事件写在这里...
        var fileName=GetQueryString("fileName");
        var path=GetQueryString("path");
        var size=GetQueryString("size");
        var ext=GetQueryString("ext");
        var lstime=GetQueryString("lstime");
        var time=GetQueryString("time");
        var uid=GetQueryString("uid");
       //debugger;
        // var id=GetQueryString("id");  //接收变量
        // //获取实体信息
        // getInfo(id,function(data){
        //     //在回调函数中，做其它操作，比如获取下拉列表数据，获取其它信息
        //     //...
        // });
        if (isNotEmpty(fileName)&&isNotEmpty(path)&&isNotEmpty(size)){
            tCourseVideoEdit2.isUpload=1;
            //渲染简介视频的input
            $("#uploadCourseVideo").find("i").removeClass("layui-icon-add-circle-fine").addClass("iconfont icon-newupload").text("重新选择");
            $("#playView").removeClass("layui-hide");
            $("#playView").click(function () {
                preview(getHttpPath()+path,fileName);
            });
            var form=layui.form;
            form.val('tCourseVideoEdit2_form',{
                "fileName":fileName,
                "size":change(size)
            });
            var param={
                fileName:fileName,
                path:path,
                size:size,
                ext:ext,
                lstime:lstime,
                time:time,
                uid:uid
            };
            tCourseVideoEdit2.videoData=param;

        }
        initUploadVideo(path);
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
            url: getRootPath()+"tCourseVideo/getInfo.do",
            data:param,
            dataType: "json",
            done:function(data){
                //表单初始赋值
                var form=layui.form; //调用layui的form模块
                form.val('tCourseVideoEdit2_form', data);
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
    form.on('submit(tCourseVideoEdit2_submit)', function(data){
        //通过表单验证后
        var field = data.field; //获取提交的字段
        if (isEmpty( tCourseVideoEdit2.videoData)){
            warningToast("请上传视频",2000);
            return false;
        }
        typeof $callback === 'function' && $callback(field); //返回一个回调事件
    });
    $("#tCourseVideoEdit2_submit").trigger('click');
}

/**
 * 关闭弹窗
 * @param $callBack  成功修改后的回调函数，比如可用作于修改后查询列表
 */
function save($callback){  //菜单保存操作
    //对表单验证
    verify_form(function(field){
        //成功验证后
        //可以继续添加需要上传的参数
        typeof $callback === 'function' && $callback(tCourseVideoEdit2.videoData); //返回一个回调事件
    });
}


//上传课时视频
function initUploadVideo(data) {
    var element = layui.element,form=layui.form;
    _layuiUpload({
        elem: '#uploadCourseVideo'
        // ,showImgDiv:"#photoUrl_upload_div"  //自定义字段，可选，用来显示上传后的图片的div
        // ,showHttpPath:getHttpPath()  //自定义字段，可选，用于拼接显示图片的http映射路径，比如http:192.168.1.126:8081
        // ,showImgSrc:data.photoUrl  //自定义字段，可选，在div显示图片的src，通常用于编辑后的回显，相对路径，比如‘/upload/XXX/XXX.jpg’
        ,url: getRootPath()+'system/uploadFile.do' //统一调用公用的系统方法
        ,accept: 'video'
        ,method: 'post'
        ,data:{
            module:"course"
            ,type:"1"////后台接收类型：1.视频 2.音频
        }  //必须填，请求上传接口的额外参数。如：data: {id: 'xxx'}
        ,acceptMime: 'video/*'

        // ,size: 20*1024 //限制文件大小，单位 KB,限制为20m
        ,xhr: xhrOnProgress
        , choose: function (obj) {
            //console.log(obj);
            obj.preview(function(index, file, result){
                //console.log(file);
                form.val('tCourseVideoEdit2_form',{
                    "fileName":file.name,
                    "size":change(file.size)
                });
            });
            $("#uploading").removeClass("layui-hide");
            tCourseVideoEdit2.isUpload=2;
            element.progress('filefilter', 0 + '%');
            $("#playView").addClass("layui-hide");
        }
        , progress: function (value) {//上传进度回调 value进度值
            //console.log("上传的进度" + value);
            element.progress('filefilter', value + '%');
        }
        ,done: function(res){
            //...上传成功后的事件
            tCourseVideoEdit2.isUpload=1;
            // element.progress('filefilter', 100 + '%');
            setTimeout(function () {
                $("#uploading").addClass("layui-hide");
                //渲染简介视频的input
                $("#uploadCourseVideo").find("i").removeClass("layui-icon-add-circle-fine").addClass("iconfont icon-newupload").text("重新选择");
                $("#playView").removeClass("layui-hide");
                $("#playView").click(function () {
                    preview(getHttpPath()+res.bizData.path,res.bizData.fileName);
                });
            },1000);

            tCourseVideoEdit2.videoData=res.bizData;
            console.log(res.bizData);
        }
    });

}

//转换单位
function change(limit){
    var size = "";
    if(limit < 0.1 * 1024){                            //小于0.1KB，则转化成B
        size = limit.toFixed(2) + "B"
    }else if(limit < 0.1 * 1024 * 1024){            //小于0.1MB，则转化成KB
        size = (limit/1024).toFixed(2) + "KB"
    }else if(limit < 0.1 * 1024 * 1024 * 1024){        //小于0.1GB，则转化成MB
        size = (limit/(1024 * 1024)).toFixed(2) + "MB"
    }else{                                            //其他转化成GB
        size = (limit/(1024 * 1024 * 1024)).toFixed(2) + "GB"
    }

    var sizeStr = size + "";                        //转成字符串
    var index = sizeStr.indexOf(".");                    //获取小数点处的索引
    var dou = sizeStr.substr(index + 1 ,2);            //获取小数点后两位的值
    if(dou == "00"){                                //判断后两位是否为00，如果是则删除00
        return sizeStr.substring(0, index) + sizeStr.substr(index + 3, 2)
    }
    return size;
}
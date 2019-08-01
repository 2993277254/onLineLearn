/**
 * tCourseEdit.jsp的js文件，包括查询，编辑操作
 */
var tCourseEdit = avalon.define({
    $id: "tCourseEdit",
    baseFuncInfo: baseFuncInfo//底层基本方法
    ,id:""
    ,type:1
});

layui.use(['index'],function(){
    //加载layui的模块，index模块是基础模块，也可添加其它
    avalon.ready(function () {
        //所有的入口事件写在这里...
        var id=GetQueryString("id");  //接收变量
        //获取实体信息
        getInfo(id,function(data){
            //在回调函数中，做其它操作，比如获取下拉列表数据，获取其它信息
            //...
            initUpload(data);
            initUploadVideo(data);
            if (isNotEmpty(data.introduction)){
                var intr=JSON.parse(data.introduction);
                $("#introductionUrl_upload").find("i").removeClass("layui-icon-add-circle-fine").addClass("iconfont icon-newupload").text("重新选择");
                $("#playView").removeClass("layui-hide");
                $("#playView").click(function () {
                    preview(getHttpPath()+intr.path,intr.fileName);
                });
            }
        });
        var form=layui.form;
        form.verify({
            videoData:function (value, item) {//value：表单的值、item：表单的DOM对象
                if (isEmpty(value)){
                    return '请上传简介视频';
                }
            },
            outlineData:function (value, item) {//value：表单的值、item：表单的DOM对象
                if (isEmpty(value)){
                    return '请添加大纲';
                }
            }
        });
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
            url: getRootPath()+"tCourse/getInfo.do",
            data:param,
            dataType: "json",
            done:function(data){
                tCourseEdit.id=data.uid;
                tCourseEdit.type=2;
                //表单初始赋值
                var form=layui.form; //调用layui的form模块
                form.val('tCourseEdit_form', data);
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
    form.on('submit(tCourseEdit_submit)', function(data){
        //通过表单验证后
        var field = data.field; //获取提交的字段
        typeof $callback === 'function' && $callback(field); //返回一个回调事件
    });
    $("#tCourseEdit_submit").trigger('click');
}

/**
 * 关闭弹窗
 * @param $callBack  成功修改后的回调函数，比如可用作于修改后查询列表
 */
function save($callback){  //菜单保存操作
    //对表单验证
    verify_form(function(field){
        //成功验证后
        var param=field; //表单的元素
        var param2={
          "isInsertVideo":"need"
            ,"status":"1"
        };
        $.extend(param,param2);
        //debugger;
        //可以继续添加需要上传的参数
        _ajax({
            type: "POST",
            // contentType: "application/json; charset=utf-8", //此设置后台可接受复杂参数，后台接收需要@RequestBody标签
            //loading:true,  //是否ajax启用等待旋转框，默认是true
            url: getRootPath()+"tCourse/saveOrEdit.do",
            data:param,
            dataType: "json",
            done:function(data){
                typeof $callback === 'function' && $callback(data); //返回一个回调事件
            }
        });
    });
}


//初始化上传组件
function initUpload(data) {
    var element = layui.element;
    _layuiUpload({
        elem: '#photoUrl_upload'
        ,showImgDiv:"#photoUrl_upload_div"  //自定义字段，可选，用来显示上传后的图片的div
        ,showHttpPath:getHttpPath()  //自定义字段，可选，用于拼接显示图片的http映射路径，比如http:192.168.1.126:8081
        ,showImgSrc:data.photoUrl  //自定义字段，可选，在div显示图片的src，通常用于编辑后的回显，相对路径，比如‘/upload/XXX/XXX.jpg’
        ,url: getRootPath()+'system/uploadFile.do' //统一调用公用的系统方法
        ,accept: 'images'
        ,method: 'post'
        ,data:{module:"course"}  //必须填，请求上传接口的额外参数。如：data: {id: 'xxx'}
        ,acceptMime: 'image/*'
        ,xhr: xhrOnProgress
        , choose: function (obj) {
            //console.log(obj);
            $("#uploading").removeClass("layui-hide");
            element.progress('photoFilter', 0 + '%');
        }
        , progress: function (value) {//上传进度回调 value进度值
            //console.log("上传的进度" + value);
            element.progress('photoFilter', value + '%');
        }
        ,done: function(res){
            //...上传成功后的事件
            // element.progress('filefilter', 100 + '%');
            setTimeout(function () {
                $("#uploading").addClass("layui-hide");
            },1000);
        }
    });
}

//上传视频简介
function initUploadVideo(data) {
    var element = layui.element;
    _layuiUpload({
        elem: '#introductionUrl_upload'
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
            $("#uploading").removeClass("layui-hide");
            element.progress('filefilter', 0 + '%');
            $("#playView").addClass("layui-hide");

        }
        , progress: function (value) {//上传进度回调 value进度值
            //console.log("上传的进度" + value);
            element.progress('filefilter', value + '%');
        }
        ,done: function(res){
            //...上传成功后的事件
            // element.progress('filefilter', 100 + '%');
            setTimeout(function () {
                $("#uploading").addClass("layui-hide");
                //渲染简介视频的input
                var form=layui.form; //调用layui的form模块
                form.val('tCourseEdit_form', {
                    //保存简介视频信息
                    "introduction":JSON.stringify(res.bizData)
                });
                $("#introductionUrl_upload").find("i").removeClass("layui-icon-add-circle-fine").addClass("iconfont icon-newupload").text("重新选择");
                $("#playView").removeClass("layui-hide");
                $("#playView").click(function () {
                    preview(getHttpPath()+res.bizData.path,res.bizData.fileName);
                });
            },1000);
            console.log(res);
        }
    });
    
}

//添加大纲|课时|视频
function addCourseVideo(type) {
    var url="";
    var title="";
    if(type==1){  //id为空,新增操作
        title="新增大纲";
        url=getRootPath()+"courseUser/tCourseVideoEdit";
    }else{  //编辑
        title="编辑大纲";
        url=getRootPath()+"courseUser/tCourseVideoEdit?id="+tCourseEdit.id;
    }
    //_layerOpen系统弹框统一调用方法，done事件是定义的按钮的执行事件
   parent._layerOpen({
        url:url,  //弹框自定义的url，会默认采取type=2
        width:900, //弹框自定义的宽度，方法会自动判断是否超过宽度
        height:700,  //弹框自定义的高度，方法会自动判断是否超过高度
        // readonly:readonly,  //弹框自定义参数，用于是否只能查看,默认是false，true代表只能查看,done事件不执行
        title:title, //弹框标题
        btns:["保存","取消"],
        done:function(index,iframeWin){
            /**
             * 确定按钮的回调,说明：index是关闭弹框用的，iframeWin是操作子iframe窗口的变量，
             * 利用iframeWin可以执行弹框的方法，比如save方法
             */
            var ids = iframeWin.save(
                //成功保存之后的操作，刷新页面
                function success(data) {
                    //successToast("保存成功",500);
                    //debugger;
                    //返回的大纲数据
                    console.log('大纲='+data);
                    if (isNotEmpty(data)){
                        var form=layui.form; //调用layui的form模块
                        form.val('tCourseEdit_form', {
                            "outline":JSON.stringify(data)  //以json的格式保存大纲数据
                        });
                    }
                    parent.layer.close(index); //如果设定了yes回调，需进行手工关闭
                }
            );
        }
    });
}


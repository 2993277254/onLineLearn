var sysUserSet = avalon.define({
    $id: "sysUserSet",
    baseFuncInfo: baseFuncInfo//底层基本方法
});
layui.use(['index'], function () {
    avalon.ready(function () {

        var userId = GetQueryString("userId");
        //console.log(userId);
        getUserMsg(userId);
        var form = layui.form; //调用layui的form模块
        //修改基本信息
        form.on('submit(sysUserEdit_submit)', function (data) {
            //通过表单验证后
            //debugger;
            var param = data.field; //获取提交的字段
            save(param,1);
        });

        //密码
        form.on('submit(rePassWord)', function (data) {
            var field = data.field;
            if (field.passWord!=field.repassWord) {
                warningToast("两次输入的密码不一致,请重新输入");
                return false;
            }
            save(field,2);

        });
    });
    avalon.scan();
});

function save(param,type) {
    var param = param;
    _ajax({
        type: "POST",
        url: getRootPath() + "sysUser/saveOrEdit2.do",
        data: param,
        dataType: "json",
        done: function (data) {

            var form = layui.form;
            if (type==1){
                successToast("保存成功");
                form.val("base_form", data);

            } else {
                successToast("密码修改成功");
                form.val("psw_form", {
                    "passWord":"",
                    "repassWord":""
                });
                //form.render();
            }
            //window.location = getRootPath() + "logout.do";
            //var form=layui.form;
            //form.val("base_form",data);
        }
    });
}
/*获取用户信息*/
function getUserMsg(id) {
    var param = {
        "uid": id
    };
    _ajax({
        type: "POST",
        url: getRootPath() + "sysUser/getInfo.do",
        data: param,
        dataType: "json",
        done: function (data) {
            console.log(data);
            initUpload(data.pictureUrl);
            var form = layui.form;
            form.val("base_form", data);
            form.val("picUrl_form", {
                "uid": data.uid
            });
            form.val("psw_form", {
                "uid": data.uid
            })
        }
    });
}

//初始化上传组件
function initUpload(data) {
    //debugger;
    var element = layui.element,layer=layui.layer;
    _layuiUpload({
        elem: '#pictureUrl_upload'
        , showImgDiv: "#pictureUrl_upload_div"  //自定义字段，可选，用来显示上传后的图片的div
        , showHttpPath: getHttpPath()  //自定义字段，可选，用于拼接显示图片的http映射路径，比如http:192.168.1.126:8081
        , showImgSrc: data  //自定义字段，可选，在div显示图片的src，通常用于编辑后的回显，相对路径，比如‘/upload/XXX/XXX.jpg’
        , url: getRootPath() + 'system/uploadFile.do' //统一调用公用的系统方法
        , accept: 'images'
        , method: 'post'
        , data: {module: "system"}  //必须填，请求上传接口的额外参数。如：data: {id: 'xxx'}
        , acceptMime: 'image/*'
        , xhr: xhrOnProgress
        , choose: function (obj) {
            //console.log(obj);
            $("#uploading").removeClass("layui-hide");

        }
        , progress: function (value) {//上传进度回调 value进度值
            //console.log("上传的进度" + value);
            element.progress('filefilter', value + '%');
        }, done: function (res) {
            //...上传成功后的事件
            //element.progress('filefilter', 100 + '%');

            setTimeout(function () {
                $("#uploading").addClass("layui-hide");
            },1000);

        }, error: function () {

        }
    });
}


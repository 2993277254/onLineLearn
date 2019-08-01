var verifyCourse = avalon.define({
    $id: "verifyCourse",
    baseFuncInfo: baseFuncInfo//底层基本方法
});
layui.use(['index'], function () {
    //加载layui的模块，index模块是基础模块，也可添加其它
    avalon.ready(function () {
        //所有的入口事件写在这里...
        var id = GetQueryString("id");  //接收变量
        //获取实体信息
        var form = layui.form;
        form.val("verifyCourseForm", {
            "uid": id
        });
        form.on('radio(isPassfilter)', function (data) {
            // console.log(data.elem); //得到radio原始DOM对象
            console.log(data.value); //被点击的radio的value值
            var val = data.value;
            //lay-verify="radio"
            var inp = $("textarea[name='reason']");
            if (val == 3) {
                //lay-verType="tips" lay-verify-msg
                inp.attr("lay-verify", "required").attr("lay-verType","tips");
                inp.parent().parent().removeClass("layui-hide");
            } else {

                inp.removeAttr("lay-verify").removeAttr("lay-verType");
                inp.parent().parent().addClass("layui-hide");
            }
            form.render();
        });
        avalon.scan();
    });
});

function verify_form($callback) {
    //监听提交,先定义个隐藏的按钮
    var form=layui.form; //调用layui的form模块
    form.on('submit(verifyCourseForm_submit)', function (data) {
        //通过表单验证后
        var field = data.field; //获取提交的字段
        typeof $callback === 'function' && $callback(field); //返回一个回调事件
    });
    $("#verifyCourseForm_submit").trigger('click');
}

function save($callback) {  //菜单保存操作
    //对表单验证
    //debugger;
    verify_form(function (field) {
        //成功验证后
        var param = field; //表单的元素
        var time=new Date();
        var util=layui.util;
        var mtime=util.toDateString(time.getTime(),"yyyy-MM-dd HH:mm:ss");
        var p={
            "verifyTime":mtime
        };
        //debugger;
        $.extend(param,p);
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

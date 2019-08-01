<!--引入layui的模块方式-->
var register = avalon.define({
    $id: "register",
    baseFuncInfo: baseFuncInfo//底层基本方法

});
layui.use(['index','element','flow'],function(){

    avalon.ready(function () {


        //每个页面在字典缓存消失时都应该重新加载
        register.baseFuncInfo.getDict(function success() {
            //入口事件
            //console.log("进入注册");
            getImg();
            var form=layui.form;
            form.on('submit(register_submit)', function(data){
                data.field.registerImgVerify=data.field.registerImgVerify.toUpperCase(); ////将输入的字母全部转换成大写
                var param=data.field;

                goResgiter(param);
            });

        });
        });
    avalon.scan();
});




//注册
function goResgiter(param) {
    //判断输入的密码是否一致
    if(param.passWord!=param.rePassword){
        warningToast("两次输入的密码不一致，请重新输入");
        return false;
    }
    _ajax({
        type: "POST",
        url:getRootPath()+"sysUser/save.do",
        data: param,
        dataType:"json",
        done:function(data){
           successToast("注册成功,2秒后跳转登录");
           console.log(param);
           var param1={
               "userName":param.userName,
               "passWord":param.passWord,
               "type":1
           };
           //设置2秒后跳转
            successToast("注册成功",2000);
           setTimeout(function () {
               goLogin(param1);
           },2000);
           //goLogin(param1);
        }
    });
}

//刷新验证码
function getImg() {
        $("#registerImgVerify").attr("src",getRootPath()+"sysUser/getVerify.do?t="+Math.random());
}




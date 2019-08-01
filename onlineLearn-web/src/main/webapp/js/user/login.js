<!--引入layui的模块方式-->
var login = avalon.define({
    $id: "login",
    baseFuncInfo: baseFuncInfo//底层基本方法

});
layui.use(['index','element','flow'],function() {

    avalon.ready(function () {

        //每个页面在字典缓存消失时都应该重新加载
        login.baseFuncInfo.getDict(function success() {
            var bg=getRootPath()+"images/bg.jpg";
            //  $("#loginCard").css('background','url('+bg+')')
            // .css('height',$(document).height()*0.8);
            getVeryCode();
            $("input[name='userName']").keydown(function (event) {
                if (event.keyCode == 13) {
                    $("input[name='passWord']").focus();
                }
            });
            $("input[name='passWord']").keydown(function (event) {
                if (event.keyCode == 13) {
                    $("input[name='loginImgVerify']").focus();
                }
            });
            $("input[name='loginImgVerify']").keydown(function (event) {
                if (event.keyCode == 13) {
                    $("#login_submit").click();
                }
            });
            var form=layui.form;
            form.on('submit(login_submit)', function(data){
                data.field.loginImgVerify=data.field.loginImgVerify.toUpperCase(); ////将输入的字母全部转换成大写
                var param=data.field;
                goLogin(param);

            });
        });
        avalon.scan();
    });
});



function getVeryCode() {
    $("#loginImgVerify").attr("src",getRootPath()+"sysUser/getVerify.do?t="+Math.random());
}


// function jump(count) {
//     window.setTimeout(function(){
//         successToast("登录成功,"+count+"秒正在跳转页面");
//         count--;
//         if(count > 0) {
//             //$('#num').attr('innerHTML', count);
//             jump(count);
//         } else {
//             debugger;
//             var url=
//             if (isEmpty(url)){
//
//                 location.href=url;
//             }else {
//                 location.href=url,location.reload();
//             }
//
//         }
//     }, 1000);
// }





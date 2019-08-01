/**
 * 首页
 * Created by huoquan on 2018/8/14.
 */
var adminIndex = avalon.define({
    $id: "adminIndex",
     baseFuncInfo:baseFuncInfo
});

<!--引入layui的模块方式-->
layui.use(['index'],function(){
    avalon.ready(function () {
        //入口事件
        adminIndex.baseFuncInfo.getDict(function success() {
            if (isEmpty(getStorage("KEY_MENUS"))){
                getMenu();
                //isNotEmpty()
                console.log("加载后台菜单")
            }else {
                console.log("已有后台菜单");
            }
        });

        avalon.scan();
    });

});

function getMenu() {
    var param={};
    _ajax({
        type: "POST",
        url: getRootPath()+"sysUser/selectAdminData.do",
        data:param,  //必须字符串后台才能接收list,
        //loading:false,  //是否ajax启用等待旋转框，默认是true
        dataType: "json",
        done: function(data){
            removeStorage("KEY_MENUS");
            addStorage("KEY_MENUS",data.menus); //菜单
            location.replace(location);
            //location.reload();
            //if ()

        }
    });
}



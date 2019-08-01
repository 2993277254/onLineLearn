var iframeIndex = avalon.define({
    $id: "iframeIndex",
    baseFuncInfo:baseFuncInfo
});
layui.use(['index'],function(){
    avalon.ready(function () {
        //successToast("欢迎进入后台管理");
        //getMunus();
        iframeIndex.baseFuncInfo.getDict(function () {
            console.log("进入后台");
        });
    });
    avalon.scan();
});


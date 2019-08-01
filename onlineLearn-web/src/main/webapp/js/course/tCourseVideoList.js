/**
 * tCourseVideoList.jsp的js文件，包括列表查询、排序、增加、修改、删除基础操作
 */
var tCourseVideoList = avalon.define({
    $id: "tCourseVideoList",
    baseFuncInfo: baseFuncInfo//底层基本方法
    ,chapter:""//章节
    , classHour:""//课时
    ,outlineList:[]    //大纲列表
});
var treeObj,id;
layui.use(['index'], function() {
    //加载layui的模块，index模块是基础模块，也可添加其它
    avalon.ready(function () {
        //所有的入口事件写在这里...
        avalon.scan();
    });
});



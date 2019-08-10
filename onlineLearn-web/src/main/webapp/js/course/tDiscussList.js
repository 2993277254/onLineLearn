/**
 * tDiscussList.jsp的js文件，包括列表查询、排序、增加、修改、删除基础操作
 */
var tDiscussList = avalon.define({
    $id: "tDiscussList",
    baseFuncInfo: baseFuncInfo//底层基本方法
    ,courseId:""
    ,total:0
    ,name:""
});
layui.use(['index','layedit'], function() {
    //加载layui的模块，index模块是基础模块，也可添加其它
    avalon.ready(function () {
        //所有的入口事件写在这里...
        // initSearch(); //初始化搜索框
        // getList();  //查询列表
        var courseId=GetQueryString("id");
        var name=GetQueryString("name");
        tDiscussList.courseId=courseId;
        tDiscussList.name=name;
        var form=layui.form;
        form.val('tDiscussEdit_form',{
            "courseId":courseId
            ,"userId":"user"//表示在controller设置该字段为登录用户id
        });
        getDiscussList();
        var layedit = layui.layedit,index;
        layedit.set({
            uploadImage: {
                url: getRootPath()+'system/uploadFile.do' //接口url
                ,type: 'post' //默认post
            }
        });
        index=layedit.build('addDiscuss',{
            'height':'150px'
        }); //建立编辑器
        form.on('submit(tDiscussEdit_submit)', function(data){
            //debugger;
            //通过表单验证后
            var content={
                "content":layedit.getContent(index)
            }
            var field = $.extend(data.field,content); //获取提交的字段
            if (isEmpty(layedit.getContent(index))){

                layer.tips('请填写内容',$("#addDiscuss").next().children(),{
                    tips:[3,'red']
                })
                return false;
            }
            // layedit.sync(index);//用于同步编辑器内容到textarea
            // console.log(field);
            verify_form(field);
        });

        avalon.scan();
    });
});


//查询讨论列表
function getDiscussList(pageNum,pageSize) {
    var param={
        "page.pageNum": pageNum || 1,
        "page.pageSize": pageSize || 20,
        "courseId":tDiscussList.courseId
    };
    _ajax({
        type: "POST",
        url: getRootPath() + "tDiscuss/list.do",
        data: param,  //必须字符串后台才能接收list,
        loading:false,  //是否ajax启用等待旋转框，默认是true
        dataType: "json",
        done: function (data) {

            //加载模板
            loadTemp(courseDiscussListTemp, course_discuss_list, data.list);
            tDiscussList.total=data.total;
            if (isEmpty(tDiscussList.total)) {
                tDiscussList.total=0;
            }
            //加载分页
             loadPage('discuss-page', data.total,getDiscussList);
        }
    });
}


function verify_form($callback){
    //监听提交,先定义个隐藏的按钮
    var form=layui.form; //调用layui的form模块
    form.on('submit(tDiscussEdit_submit)', function(data){
        //通过表单验证后
        var field = data.field; //获取提交的字段
        typeof $callback === 'function' && $callback(field); //返回一个回调事件
    });
    $("#tDiscussEdit_submit").trigger('click');
}

//对表单验证
function verify_form(field){
    //成功验证后
    var param=field; //表单的元素
    //可以继续添加需要上传的参数
    _ajax({
        type: "POST",
        //loading:true,  //是否ajax启用等待旋转框，默认是true
        url: getRootPath()+"tDiscuss/saveOrEdit.do",
        data:param,
        dataType: "json",
        done:function(data){
            var form=layui.form;
            form.val('tDiscussEdit_form',{
                "content":""
            });
            successToast("发表成功",1000);
            getDiscussList();

        }
    });
}
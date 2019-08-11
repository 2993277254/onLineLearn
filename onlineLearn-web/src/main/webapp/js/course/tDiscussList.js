/**
 * tDiscussList.jsp的js文件，包括列表查询、排序、增加、修改、删除基础操作
 */
var websocket=null,layedit=null,index;
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
        layedit = layui.layedit;
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
        console.log('开始连接websoket------------');
        getWsConnection();
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
        url: getRootPath() + "tDiscuss/list2.do",
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
        url: getRootPath()+"tDiscuss/saveOrEdit2.do",
        data:param,
        dataType: "json",
        done:function(data){
            var form=layui.form;
            form.val('tDiscussEdit_form',{
                "content":""
            });
            layedit.setContent(index,"");
            successToast("发表成功",1000);
            getDiscussList();//刷新列表
            doSendUsers();
        }
    });
}

//websocket

function getWsConnection() {
    var path=getRootPath().replace('http://','');
    var pullPath="ws://"+path+"websocket.action";
    // debugger;
    // 首先判断是否 支持 WebSocket
    if('WebSocket' in window) {
        websocket = new WebSocket(pullPath);

    } else if('MozWebSocket' in window) {
        websocket = new MozWebSocket(pullPath);
    } else {
        websocket = new SockJS(getRootPath()+"sockjs/websocket.action");
    }
    console.log('websocket链接路径'+websocket);
    // 打开时
    websocket.onopen = function(evnt) {
        console.log("  websocket.onopen，连接成功  ");
        // console.log(evnt);
    };
    // 处理消息时
    websocket.onmessage = function(evnt) {
        console.log('处理消息');
        // console.log(evnt);
        // var data=JSON.parse(evnt.data);
        // alert(data.content);
        getDiscussList();//用户接受到新信息就刷新消息列表
        var i=isEmpty($("#reMsg").text())==true?1:parseInt($("#reMsg").text())+1;
        $("#reMsg").text(i);
        $("#reMsg").hasClass("layui-badge")==false?$("#reMsg").addClass("layui-badge"):'';
    };
    websocket.onerror = function(evnt) {
        console.log('连接出错');
        console.log(JSON.stringify(evnt));
        // websocket.onclose();
    };
    websocket.onclose = function(evnt) {
        console.log("  websocket.onclose  ");
    };
}
function doSendUsers() {
        if (websocket.readyState == websocket.OPEN) {
            var msg = {
                //userId:getUserId(),
                content: layedit.getContent(index)
            };
            websocket.send(JSON.stringify(msg));//调用后台handleTextMessage方法
            console.log(getUserId()+"发送"+msg+"成功!");
        } else {
            console.log(getUserId()+"发送"+msg+"失败!");
        }
}
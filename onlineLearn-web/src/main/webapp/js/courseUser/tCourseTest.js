
var tCourseTest = avalon.define({
    $id: "tCourseTest",
    baseFuncInfo: baseFuncInfo//底层基本方法
    ,testList:[]    //大纲列表
    ,iselect:0
    ,zid:""
    ,index:"",
    courseId:""
});

layui.use(['index'],function(){
    //加载layui的模块，index模块是基础模块，也可添加其它
    avalon.ready(function () {
        //所有的入口事件写在这里...
        var id=GetQueryString("id");  //接收变量
        //获取实体信息
        var form=layui.form; //调用layui的form模块
        getInfo(id,function(data){
            //在回调函数中，做其它操作，比如获取下拉列表数据，获取其它信息
            //...
            tCourseTest.courseId=data.uid;
            if (isNotEmpty(data.outline)){

                //说明没有课程大纲，需要添加
                //addOutlistIiem(1);
                //addOutlistIiem(1);
                //console.log("初次加载后"+tCourseTest.testList.length);
                var arr=[];
                var outline=JSON.parse(data.outline);
                $.each(outline,function (i,item) {
                   var param={
                       id: item.id,
                       name: item.name,
                       text: item.text,
                       content:""
                   };
                    arr.push(param);
                });
                console.log(arr);
                tCourseTest.testList=[];
                tCourseTest.testList.pushArray(arr);
            }
            form.render();
        });

        form.on('radio(zid)', function(data){
            // console.log(data.elem); //得到radio原始DOM对象
            // console.log(data.value); //被点击的radio的value值
            tCourseTest.zid=data.value;
            //tCourseTest.index=$(data.elem).attr('index');
            tCourseTest.iselect=true;

        });
        // form.render();
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
                //表单初始赋值
                var form=layui.form; //调用layui的form模块
                form.val('tCourseTest_form', data);
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
    form.on('submit(tCourseTest_submit)', function(data){
        //通过表单验证后
        var field = data.field; //获取提交的字段
        typeof $callback === 'function' && $callback(field); //返回一个回调事件
    });
    $("#tCourseTest_submit").trigger('click');
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
        var arr=[];
        arr=arr.concat(tCourseTest.testList);
        //可以继续添加需要上传的参数
        typeof $callback === 'function' && $callback(JSON.stringify(arr)); //返回一个回调事件
    });
}


function testEdit() {
    //debugger;
    // var data=tCourseTest.testList[tCourseTest.index];
    //data=JSON.stringify(data).replace(/{/g,"").replace(/}/g,"").replace(/:/g,"=").replace(/,/g,"&").replace(/"/g,"");
    //data=data;
    //console.log(data);
    var url="";
    var title="测验管理";
    url=getRootPath()+"courseUser/tCourseTest2?id="+tCourseTest.zid+'&courseId='+tCourseTest.courseId;
    //_layerOpen系统弹框统一调用方法，done事件是定义的按钮的执行事件
    parent._layerOpen({
        url:url,  //弹框自定义的url，会默认采取type=2
        width:800, //弹框自定义的宽度，方法会自动判断是否超过宽度
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
                    parent.layer.close(index); //如果设定了yes回调，需进行手工关闭
                }
            );
        }
    });
}




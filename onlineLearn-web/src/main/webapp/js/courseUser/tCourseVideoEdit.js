/**
 * tCourseVideoEdit.jsp的js文件，包括查询，编辑操作
 */
var tCourseVideoEdit = avalon.define({
    $id: "tCourseVideoEdit",
    baseFuncInfo: baseFuncInfo//底层基本方法
    ,outlineList:[]    //大纲列表
    ,addCourseClass:function (el,index) {//添加课时
        //console.log(el);
        addOutlistIiem(2,el,index);
    }
    ,removeChapter:function (type,el,index,index2) {
        removeOutlistIiem(type,el,index,index2);
    }
    ,uplaodCourseVideo:function (videoData,index1,index2) {
         uplaodCourseVideo(videoData,index1,index2);
    }
    
});

layui.use(['index'],function(){
    //加载layui的模块，index模块是基础模块，也可添加其它
    avalon.ready(function () {
        //所有的入口事件写在这里...
        var id=GetQueryString("id");  //接收变量
        console.log(id);
        //获取实体信息
        getInfo(id,function(data){
            //在回调函数中，做其它操作，比如获取下拉列表数据，获取其它信息
            //...
            if (isEmpty(data.outline)){
                //说明没有课程大纲，需要添加
                //addOutlistIiem(1);
                addOutlistIiem(1);
                console.log("初次加载后"+tCourseVideoEdit.outlineList.length);
            }else {
                //加载大纲
                tCourseVideoEdit.outlineList=[];
                tCourseVideoEdit.outlineList.pushArray(JSON.parse(data.outline
                ));
            }
        });
        var form=layui.form; //调用layui的form模块
        form.verify({
            videoData:function (value, item) {//value：表单的值、item：表单的DOM对象
                if (isEmpty(value)){
                    return '请上传课时视频';
                }
            }
        });
        // form.render();
        // initUploadVideo();
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
                // var form=layui.form; //调用layui的form模块
                // form.val('tCourseVideoEdit_form', data);
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
    form.on('submit(tCourseVideoEdit_submit)', function(data){
        //通过表单验证后
        var field = data.field; //获取提交的字段
        typeof $callback === 'function' && $callback(field); //返回一个回调事件
    });
    $("#tCourseVideoEdit_submit").trigger('click');
}

/**
 * 关闭弹窗
 * @param $callBack  成功修改后的回调函数，比如可用作于修改后查询列表
 */
function save($callback){  //菜单保存操作
    //对表单验证
    verify_form(function(field){
        // debugger;
        //成功验证后
        var param=field; //表单的元素
        //可以继续添加需要上传的参数
                typeof $callback === 'function' && $callback(tCourseVideoEdit.outlineList); //返回一个回调事件
    });
}

//添加章节和课时
/**
 *
 * @param data
 * @param type 1：表示添加章节，2：表示添加课时
 * @param data 表示添加课时
 * @param index 表示添加课时的当前章节
 */
function addOutlistIiem(type,data,index) {
    var arr=[];  //章节数组
    var arr2=[];//课时数组
    var zparam="",cparam="",pid="",id="";
    //var id=getUuid(32,16);
    if (type==1){
        pid=getUuid(32,16),id=getUuid(32,16);
        //如果章节不为空
        if (isNotEmpty(tCourseVideoEdit.outlineList)) {
            console.log("=====追加章节===");
            var len=tCourseVideoEdit.outlineList.length;
            //课时参数
            cparam={
                "id":id,
                "parent":pid,
                "text":"",
                "name":'第'+(len+1)+'.1节'
                ,"video":""
            };
            arr2.push(cparam);
            //章节参数
            zparam={
                "id":pid,
                "name":'第'+(len+1)+'章',
                "text":"",
                "courseHouse":arr2//课时宿主
            };
        }else {
            console.log("=====添加第一个章节===");
            //课时参数
            cparam={
                "id":id,
                "parent":pid,
                "text":"",
                "name":'第1.1节'
                ,"video":""
            };
            arr2.push(cparam);
            //章节参数
            zparam={
                "id":pid,
                "name":'第1章',
                "text":"",
                "courseHouse":arr2//课时宿主
            };
        }
        arr.push(zparam);
        tCourseVideoEdit.outlineList.pushArray(arr);
    } else {//添加课时
        id=getUuid(32,16);
        console.log("=====添加课时===");
        //课时参数
        cparam={
            "id":id,
            "parent":data.id,
            "text":"",
            "name":'第'+(index+1)+'.'+(data.courseHouse.length+1)+'节'
            ,"video":""
        };
        tCourseVideoEdit.outlineList[index].courseHouse.push(cparam);
    }

    console.log(tCourseVideoEdit.outlineList);
    var form=layui.form; //调用layui的form模块
    form.render();
}

//删除章节|课时
function removeOutlistIiem(type,el,index,index2) {
    //debugger;
    if (type==1){
        //删除章节
        var len=tCourseVideoEdit.outlineList.length;//数组的长度
        if (len==(index+1)){//删除最后一个
            tCourseVideoEdit.outlineList.removeAt(index);
        } else {//删除中间的那个，把当前元素后面的数据往前移一个，再删除最后一个
            for (var i=index;i<=len-2;i++){
                tCourseVideoEdit.outlineList[i].id= tCourseVideoEdit.outlineList[i+1].id;
                tCourseVideoEdit.outlineList[i].text=tCourseVideoEdit.outlineList[i+1].text;
                // tCourseVideoEdit.outlineList[i].name=tCourseVideoEdit.outlineList[i+1].name;
                var arr=[];
                $.each(tCourseVideoEdit.outlineList[i+1].courseHouse,function (j,item) {

                    var param={
                        "id":item.id,
                        "parent":item.parent,
                        "text":item.text,
                        "name":'第'+(i+1)+'.'+(j+1)+'节',
                        "video":item.video
                    };
                    arr.push(param);
                });
                tCourseVideoEdit.outlineList[i].courseHouse =arr;
            }
            tCourseVideoEdit.outlineList.removeAt(len-1);
        }

    } else{
        //debugger;
        //删除课时
        var len=el.courseHouse.length;
        // var arr=[];
         if (len==(index2+1)){//删除最后一个
             tCourseVideoEdit.outlineList[index].courseHouse.removeAt(index2);
         }else {//删除中间的那个，把当前元素后面的数据往前移一个，再删除最后一个
             var j=1;
             //var arr=[];

             for (var i=index2;i<=len-2;i++){
                 //debugger;
                 var num=getCoursehouse(el.courseHouse[i+1].name);
                 var param={
                     "id":el.courseHouse[i+1].id,
                     "parent":el.courseHouse[i+1].parent,
                     "text":el.courseHouse[i+1].text,
                     "name":'第'+num+'节',
                     "video":el.courseHouse[i+1].video
                 };
                 //arr.push(param);
                 tCourseVideoEdit.outlineList[index].courseHouse[i]=param;
                 j++;
             }
             //tCourseVideoEdit.outlineList[index].courseHouse=arr;
             tCourseVideoEdit.outlineList[index].courseHouse.removeAt(len-1);
         }
    }
    console.log(tCourseVideoEdit.outlineList);
    var form=layui.form; //调用layui的form模块
    form.render();
}


//上传课时视频
function uplaodCourseVideo(videoData,index1,index2) {
    //debugger;
    var url="";
    var title="";
    if(videoData.length==0) {
        title = "添加课时视频";
        url = getRootPath() + "courseUser/tCourseVideoEdit2";
    }else {
        //已有上传视频
        title = "编辑课时视频";
        url = getRootPath() + "courseUser/tCourseVideoEdit2?" +
            "fileName="+videoData.fileName+
            "&path="+videoData.path+
            "&size="+videoData.size+
            "&ext="+videoData.ext+
            "&lstime="+videoData.lstime+
            "&time="+videoData.time+
            "&uid="+videoData.uid;
    }
    //_layerOpen系统弹框统一调用方法，done事件是定义的按钮的执行事件
    parent._layerOpen({
        url:url,  //弹框自定义的url，会默认采取type=2
        width:600, //弹框自定义的宽度，方法会自动判断是否超过宽度
        height:500,  //弹框自定义的高度，方法会自动判断是否超过高度
        // readonly:readonly,  //弹框自定义参数，用于是否只能查看,默认是false，true代表只能查看,done事件不执行
        title:title, //弹框标题
        done:function(index,iframeWin){
            /**
             * 确定按钮的回调,说明：index是关闭弹框用的，iframeWin是操作子iframe窗口的变量，
             * 利用iframeWin可以执行弹框的方法，比如save方法
             */
            var ids = iframeWin.save(
                //成功保存之后的操作，刷新页面
                function success(data) {
                    //debugger;
                    console.log(data);
                    var arr=[];
                    $.each(data,function (i,item) {
                        arr.push(item);
                    });
                    tCourseVideoEdit.outlineList[index1].courseHouse[index2].video=data;
                    console.log(tCourseVideoEdit.outlineList);
                    parent.layer.close(index); //如果设定了yes回调，需进行手工关闭
                }
            );
        }
    });
}

//获取添加|删除时的课时名称
function getCoursehouse(str) {
    var arr=str.split(".");
    arr[0]=arr[0].toString().replace(/[^0-9]+/g, '');
    arr[1]=arr[1].toString().replace(/[^0-9]+/g, '');
    arr[0]=parseInt(arr[0]);
    arr[1]=parseInt(arr[1])-1;
    return arr[0]+'.'+arr[1];
}










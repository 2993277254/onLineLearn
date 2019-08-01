var courseUserMain = avalon.define({
    $id: "courseUserMain",
    baseFuncInfo: baseFuncInfo//底层基本方法
    ,total:0,  //参加的课程总数
    owntotal:0 //讲师开设的课程总数
});

layui.use(['index'],function () {
    avalon.ready(function () {
        courseUserMain.baseFuncInfo.getDict(function success() {
            getAttendList();
            var len=$('#courseUl').find("li").length;
            if (len>1){
                ownOpenCourseList();
            }
        });
    });
});


//获取参加课程
function getAttendList(pageNum,pageSize) {
    var param = {
        // "userId":getUserId(),
        "page.pageNum": pageNum || 1,
        "page.pageSize": pageSize || 20
    };

    _ajax({
        type: "POST",
        url: getRootPath() + "tCourseUser/list2.do",
        data: param,  //必须字符串后台才能接收list,
        //loading:false,  //是否ajax启用等待旋转框，默认是true
        dataType: "json",
        done: function (data) {
            //console.log(data);
            loadTemp(hasCourseListTemp, hasCourseList, data.list);
            //courseList.total="";
            courseUserMain.total=data.total;
            loadPage('hasCourse-page', data.total,getAttendList);
        }
    });
}

//获取讲师开设的课程
function ownOpenCourseList(pageNum,pageSize) {
    var param = {
        "teacherId":"teacher",//不为空表示查询当前讲师开始的课程
        "page.pageNum": pageNum || 1,
        "page.pageSize": pageSize || 20
    };
    _ajax({
        type: "POST",
        url: getRootPath() + "tCourse/list2.do",
        data: param,  //必须字符串后台才能接收list,
        //loading:false,  //是否ajax启用等待旋转框，默认是true
        dataType: "json",
        done: function (data) {
            //console.log(data);
            loadTemp(ownOPenCourseListTemp, ownOPenCourseList, data.list);
            //courseList.total="";
            courseUserMain.owntotal=data.total;
            loadPage('ownCourse-page', data.total,ownOpenCourseList);
        }
    });
}


function goCourseVideo(id) {
    location.href=getRootPath()+"courseUser/courseUserVideo?id="+id;
}

//用户添加自己开设的课程
function saveOrEditCourse(id){
    var url="";
    var title="";
    if(isEmpty(id)){  //id为空,新增操作
        title="新增课程";
        url=getRootPath()+"courseUser/tCourseEdit";
    }else{  //编辑
        title="编辑课程";
        url=getRootPath()+"courseUser/tCourseEdit?id="+id;
    }
    //_layerOpen系统弹框统一调用方法，done事件是定义的按钮的执行事件
    _layerOpen({
        url:url,  //弹框自定义的url，会默认采取type=2
        width:800, //弹框自定义的宽度，方法会自动判断是否超过宽度
        height:700,  //弹框自定义的高度，方法会自动判断是否超过高度
        // readonly:readonly,  //弹框自定义参数，用于是否只能查看,默认是false，true代表只能查看,done事件不执行
        title:title, //弹框标题
        btns:["提交","取消"],
        done:function(index,iframeWin){
            /**
             * 确定按钮的回调,说明：index是关闭弹框用的，iframeWin是操作子iframe窗口的变量，
             * 利用iframeWin可以执行弹框的方法，比如save方法
             */
            var ids = iframeWin.save(
                //成功保存之后的操作，刷新页面
                function success() {
                    successToast("保存成功",500);
                    ownOpenCourseList();
                    layer.close(index); //如果设定了yes回调，需进行手工关闭
                }
            );
        }
    });
}
//查询进度的方法
function showprogess(id,outline) {

            var param2 = {
                "courseId": id,
                "userId": "user"
            }; //表单的元素
            //可以继续添加需要上传的参数
            _ajax({
                type: "POST",
                //loading:true,  //是否ajax启用等待旋转框，默认是true
                url: getRootPath() + "tCourseUserVideo/getList2.do",
                data: param2,
                dataType: "json",
                done: function (data2) {
                    var progressTotal=0;
                    console.log(data2);
                    //data2是课程用户视频表的数据
                    if (isEmpty(data2)) {
                        progressTotal=0;
                    } else {
                        //比对用户观看视频的进度
                        var str=JSON.parse(outline);
                        $.each(str, function (i, item) {
                            $.each(item.courseHouse, function (i2, item2) {
                                $.each(data2, function (i3, item3) {
                                    var progress = 0, param2 = '';
                                    if (item2.video.uid == item3.videoId) {
                                        //已观看视频了
                                        console.log("已观看");
                                        if (item3.isEnd =='1') {
                                            //观看100%
                                            //console.log("已观看完成");
                                            progress = 100;
                                            param2 = {
                                                "tuvId": item3.uid,
                                                "currentTime":item2.video.lstime,
                                                "progress":progress
                                            };
                                            $.extend(item2.video, param2);
                                        } else {
                                            console.log("未观看完成");
                                            //判断是否观看完成
                                            progress = Math.floor((parseInt(item3.currentTime) / parseInt(item2.video.lstime)) * 100);
                                            console.log('观看的进度' + progress);
                                            //如果已有记录，保存id，用于更新观看时间
                                            param2 = {
                                                "tuvId": item3.uid,
                                                "currentTime":item3.currentTime,
                                                "progress":progress
                                            };
                                            $.extend(item2.video, param2);
                                        }
                                    }
                                });
                                //用户未观看的视频，
                                if (isEmpty(item2.video.progress)){
                                    var param2 = {
                                        "tuvId": "",
                                        "currentTime":0,
                                        "progress":0
                                    };
                                    $.extend(item2.video, param2);
                                }
                            });
                        });
                    }
                    var element = layui.element;
                    element.progress('progressTotal', progressTotal+'%');
                    var layer=layui.layer;
                    layer.open({
                        type:1,
                        id:"xxxx",
                        area:['600px','300px'],
                        title:"学习进度",
                        content:$("#courseprogrss")
                    });
                }
            });


}

/**
 *
 * @param id
 * @param name
 * @param isAdmin 表示管理者，讲师或者
 */
//跳转到讨论界面
function goDiscuss(id,name,isAdmin){
    var url="";
    var title="";
        title=name+"课程讨论";
        url=getRootPath()+"course/tDiscussList?id="+id+"&name="+name;
        window.location.href=url;
    //_layerOpen系统弹框统一调用方法，done事件是定义的按钮的执行事件
    // _layerOpen({
    //     url:url,  //弹框自定义的url，会默认采取type=2
    //     width:800, //弹框自定义的宽度，方法会自动判断是否超过宽度
    //     height:700,  //弹框自定义的高度，方法会自动判断是否超过高度
    //     // readonly:readonly,  //弹框自定义参数，用于是否只能查看,默认是false，true代表只能查看,done事件不执行
    //     title:title, //弹框标题
    //     btns:["提交","取消"],
    //     done:function(index,iframeWin){
    //         /**
    //          * 确定按钮的回调,说明：index是关闭弹框用的，iframeWin是操作子iframe窗口的变量，
    //          * 利用iframeWin可以执行弹框的方法，比如save方法
    //          */
    //     }
    // });
}


//出测验
function courseTest(id,name) {

    var url="";
    var title="课程《"+name+"》测验管理";
    url=getRootPath()+"courseUser/tCourseTest?id="+id;
    //_layerOpen系统弹框统一调用方法，done事件是定义的按钮的执行事件
    _layerOpen({
        url:url,  //弹框自定义的url，会默认采取type=2
        width:800, //弹框自定义的宽度，方法会自动判断是否超过宽度
        height:400,  //弹框自定义的高度，方法会自动判断是否超过高度
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
                    ownOpenCourseList();
                    layer.close(index); //如果设定了yes回调，需进行手工关闭
                }
            );
        }
    });
}

//考试管理
function courseExam(id,name) {
    var url="";
    var title="课程《"+name+"》考试管理";
    url=getRootPath()+"courseUser/tCourseExam?id="+id;
    //_layerOpen系统弹框统一调用方法，done事件是定义的按钮的执行事件
    _layerOpen({
        url:url,  //弹框自定义的url，会默认采取type=2
        width:800, //弹框自定义的宽度，方法会自动判断是否超过宽度
        height:600,  //弹框自定义的高度，方法会自动判断是否超过高度
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
                    ownOpenCourseList();
                    layer.close(index); //如果设定了yes回调，需进行手工关闭
                }
            );
        }
    });
}



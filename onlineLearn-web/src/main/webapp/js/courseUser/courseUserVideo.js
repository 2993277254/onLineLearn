var tCourseUserVideo = avalon.define({
    $id: "tCourseUserVideo"
    , name: ""
    , photoUrl: ""
    , outlineList: []    //大纲列表
    , playName: ""
    , courseId: ""
    , currentTime: ""
    ,studyPlay:function (index1,index2) {
        studyPlay(index1,index2);
    }
});

layui.use(['index'], function () {
    avalon.ready(function () {
        $("#video").css('height', $(document).height() * 0.75 + 'px');
        $("#courseVideoList").css('max-height', $(document).height() * 0.65 + 'px');
        var id = GetQueryString("id");//课程id
        console.log(id);
        tCourseUserVideo.courseId = id;
        getInfo(id, function (data) {
            if (isNotEmpty(data)) {
                tCourseUserVideo.name = data.name;
                tCourseUserVideo.photoUrl = getHttpPath() + data.photoUrl;
                var str = JSON.parse(data.outline);
                //查询课程用户视频表进行比对观看视频
                getCourseUserVideoList(str);
            }


        });
        //play();
        // var element = layui.element;
        // element.progress('demo', '50%');
    });
});

//获取课程信息
function getInfo(id, $callback) {
    if (isEmpty(id)) {
        //新增
        typeof $callback === 'function' && $callback({}); //返回一个回调事件
    } else {
        //编辑
        var param = {
            "uid": id
        };
        _ajax({
            type: "POST",
            //loading:false,  //是否ajax启用等待旋转框，默认是true
            url: getRootPath() + "tCourse/getInfo.do",
            data: param,
            dataType: "json",
            done: function (data) {
                //表单初始赋值
                var form = layui.form; //调用layui的form模块
                form.val('sysDictEdit_form', data);
                typeof $callback === 'function' && $callback(data); //返回一个回调事件
            }
        });
    }
}

var stuPlayer = "",
    progress = '',
    $index1 = '',
    $index2 = '',
    t = 0,
    // isPLay = '',
    videoId = '',
    timerData = '',
    totalTime = '',
    tuvId = '';

//播放视频
function studyPlay(index1, index2,isPlay) {

    progress = tCourseUserVideo.outlineList[index1].courseHouse[index2].video.progress;
    //console.log('进度=='+progress);
    $index1 = index1;
    $index2 = index2;
    videoId = tCourseUserVideo.outlineList[index1].courseHouse[index2].video.uid;
    totalTime = tCourseUserVideo.outlineList[index1].courseHouse[index2].video.lstime;
    tuvId = tCourseUserVideo.outlineList[index1].courseHouse[index2].video.tuvId;
    var seek='',autoplay=true,playbackrate=false;
    //debugger;
    if (progress==100){
        seek=0;
        autoplay=false;
        playbackrate=1;
    } else {
        seek=tCourseUserVideo.outlineList[index1].courseHouse[index2].video.currentTime;
    }
    if (isNotEmpty(isPlay)&&isPlay){
        autoplay=true;
    }
    console.log("需要播放"+autoplay);
    coursevideoclick();
    tCourseUserVideo.playName = tCourseUserVideo.outlineList[index1].courseHouse[index2].name + ":" + tCourseUserVideo.outlineList[index1].courseHouse[index2].text;
    //加载播放器
    var videoObject = {
        playerID: 'studyVideo',
        container: '#video',//“#”代表容器的ID，“.”或“”代表容器的class
        variable: 'stuPlayer',//该属性必需设置，值等于下面的new chplayer()的对象
        loaded: 'loadedHandler',//监听播放器加载成功
        seek: seek,
        // seek:50,
        autoplay: autoplay,
        loop: true,
        playbackrate: playbackrate,//禁止播放倍速
        video: getHttpPath() + tCourseUserVideo.outlineList[index1].courseHouse[index2].video.path
    };
    stuPlayer = new ckplayer(videoObject);

}
//视频监听
function loadedHandler() {//播放器加载后会调用该函数
    // console.log('进度=' + progress);
    if (progress == 100) {
        //观看完成，正常拖动
        stuPlayer.changeConfig('config', 'timeScheduleAdjust', 1);
    } else {
        //看过的地方可以随意拖动
        stuPlayer.changeConfig('config', 'timeScheduleAdjust', 5);
    }
    //监听函数
    stuPlayer.addListener('time', timeHandler); //监听播放时间,addListener是监听函数，需要传递二个参数，'time'是监听属性，这里是监听时间，timeHandler是监听接受的函数
    //stuPlayer.addListener('clickEvent', clickEventHandler); //监听点击事件
    // stuPlayer.addListener('mouse', mouseHandler); //监听鼠标坐标
    stuPlayer.addListener('ended', endedHandler); //监听播放结束
    // stuPlayer.addListener('buffer', bufferHandler); //监听缓冲状态
    stuPlayer.addListener('play', playHandler);
    stuPlayer.addListener('pause', pauseHandler);
}

function bufferHandler(buffer) {
    //console.log('缓冲==' + buffer);
}

function playHandler() {
    console.log('正在播放');
    // isPLay = true;
    opentimer(1);


}

function pauseHandler() {
    console.log('已暂停');
    // isPLay = false;
    opentimer(2);

}

function timeHandler(t) {
    //console.log('当前播放的时间：' + t);
    tCourseUserVideo.currentTime = t;
}

function endedHandler() {
    console.log("播放结束");
    isPLay = false;
    if (progress!=100){
        progress=100;
        opentimer(2);
        saveProgress();
        // stuPlayer.changeConfig('config', 'timeScheduleAdjust', 1);
        // stuPlayer.changeConfig('config', 'playbackrate', 1);
        //stuPlayer.videoSeek(0);
        //stuPlayer.videoPause();
        // stuPlayer.changeConfig('flashvars','autoplay',false);
         console.log('第一次观看结束');

    }else {
        console.log('已观看100%，无需关闭定时器');
        //stuPlayer.videoSeek(0);
        //stuPlayer.videoPause();
    }
    reloadVideo();
   // console.log('第一次观看结束，重新加载播放器');

}

// function mouseHandler(obj) {
//     console.log(obj);
// }
//保存用户的观看记录
function saveProgress() {
    //debugger;
    var param2 = '',isEnd=false;
    var param = {
        "courseId": tCourseUserVideo.courseId,
        "videoId": videoId,
        "userId": "user",
        "currentTime": tCourseUserVideo.currentTime
    }; //表单的元素
    if (progress != 100) {//再未观看完成时推流
        param2 = {
            "isEnd": 0
        }
    } else if (progress == 100) {//设置结束标识
        param2 = {
            "isEnd": 1,
            "currentTime":totalTime
        };
        isEnd=true;
        //progress=100;
    }
    $.extend(param, param2);
    if (isNotEmpty(tuvId)) {//表示记录是更新
        var param3 = {
            "uid": tuvId
        };
        $.extend(param, param3);
    }
    console.log('推送的参数' + JSON.stringify(param));
    //可以继续添加需要上传的参数
    _ajax({
        type: "POST",
        loading:false,  //是否ajax启用等待旋转框，默认是true
        url: getRootPath() + "tCourseUserVideo/saveOrEdit2.do",
        data: param,
        dataType: "json",
        done: function (data) {
            //opentimer(2);
            //debugger;
            console.log('保存视频进度成功');
            console.log('保存进度的返回数据==='+data);
            /**
             * data==32表示插入数据
             * data==1表示修改数据
             * data=noUpdate 表示观看时间小于数据库记录时间，不执行更新
             */
            if (isNotEmpty(data)){
                    var progressCurr='';
                    if (data!='noUpdate') {
                        if (isEnd) {
                            progressCurr = 100;
                        } else {
                            progressCurr = Math.floor((parseInt(tCourseUserVideo.currentTime) / parseInt(totalTime)) * 100);
                        }
                        // console.log("更新的进度=" + progressCurr);
                        tCourseUserVideo.outlineList[$index1].courseHouse[$index2].video.progress = progressCurr;//更新进度条
                        //progress=progressCurr;
                        if (isEmpty(tuvId)&&data!="1"){//数据已插入表，把uid保存在数组中
                            console.log('插入记录成功');
                            var param={
                                "tuvId": data
                            };
                            $.extend(tCourseUserVideo.outlineList[$index1].courseHouse[$index2].video,param);
                            tuvId = tCourseUserVideo.outlineList[$index1].courseHouse[$index2].video.tuvId;
                        }
                        // console.log(tCourseUserVideo.outlineList);
                        var element = layui.element;
                        var filter = 'videoProgress_'+$index1+'_'+$index2;
                        // console.log('进度条的监听器='+filter);
                        element.progress(filter, progressCurr + '%');//重新加载进度条
                    }else if (data=='noUpdate') {
                        console.log('观看时间小于数据库记录时间，不执行更新');
                    }
            }

            //element.render('progress');
        }
    });

}

//获取课程用户视频表的数据，用于比对用户是否观看视频，判断关系
function getCourseUserVideoList(data) {
    var param = {
        "courseId": tCourseUserVideo.courseId,
        "userId": "user"
    }; //表单的元素
    //可以继续添加需要上传的参数
    _ajax({
        type: "POST",
        //loading:true,  //是否ajax启用等待旋转框，默认是true
        url: getRootPath() + "tCourseUserVideo/getList2.do",
        data: param,
        dataType: "json",
        done: function (data2) {
            console.log(data2);
            //data2是课程用户视频表的数据
            if (isEmpty(data2)) {
                $.each(data, function (i, item) {
                    $.each(item.courseHouse, function (i, item2) {
                        var param = {
                            "progress": 0,
                            "currentTime": 0,
                            "tuvId": ""
                        };
                        $.extend(item2.video, param);
                    });
                });
                console.log("添加进度字段" + data);
            } else {
                $.each(data, function (i, item) {
                    //debugger;
                    $.each(item.courseHouse, function (i2, item2) {
                        //debugger;
                        //console.log(item.courseHouse);
                        $.each(data2, function (i3, item3) {
                            //debugger;
                            var progress = 0, param2 = '';
                             //debugger;
                            if (item2.video.uid == item3.videoId) {
                                //已观看视频了
                                console.log("已观看");
                                if (item3.isEnd =='1') {
                                    //观看100%
                                    console.log("已观看完成");
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
            console.log(data);
            tCourseUserVideo.outlineList = [];
            tCourseUserVideo.outlineList.pushArray(data);

            // loadTemp(courseHoursListTemp, courseVideoList, tCourseUserVideo.outlineList);
            coursevideoclick();
            var element = layui.element;
            element.init(); //重新加载进度条
        }
    });
}

//判断用户与视频的关系
function showprogress(data) {
    $.each(data, function (i, item) {
        if (item.isEnd) {//视频已观看
            //课时显示已完成图标
            $(this).addClass("layui-icon layui-icon-ok");
        } else {//未观看完成
            $(this).addClass("layui-icon layui-icon-loading");
        }
    });
}

//显示正在观看视频的样式
function coursevideoclick() {
    var bg = "layui-bg-green", index = "", tip = '', layer = layui.layer, bg2 = "layui-bg-gray";
    $(".videospan").each(function (i, item) {
        // console.log($(this));
        //单击事件
        $(this).click(function () {
            opentimer(2);
            $(".videospan").removeClass(bg).removeClass(bg2);
            $(this).addClass(bg);
        });
        //hover事件
        $(this).hover(function () {
            if (!$(this).hasClass(bg)) {
                $(this).addClass(bg2);
            }
            //var filter = $(this).next().attr('lay-filter');//进度条的监听器
            //  var progress = $(this).next().children().eq(0).attr("lay-percent");//进度条的值
            var progress = $(this).next().attr("progress");//进度条的值
            //progress=progress.replace("%","");
            console.log(progress);
            var str = '观看累积进度:' + progress+'%';
            if (progress ==100) {
                str += ',已观看完成';
                tip = [3, 'green'];
            } else if (progress ==0) {
                str += ',未开始观看';
                tip = [3, 'red'];
            } else {
                str += ',未观看完成';
                tip = [3, 'orange'];
            }
            index = layer.tips(str, this, {
                tips: tip
            });
        }, function () {
            $(this).removeClass(bg2);
            layer.close(index);
        });
    });
}

//开|关 ->定时器
function opentimer(type) {
    //debugger;
    if (progress!=100) {
        if (type == 1) {
            if (isEmpty(timerData)) {
                console.log('播放了，开启定时器');
                timerData = window.setInterval(saveUserVideoStudy, 1000);
            } else {
                // t=0;
                console.log('播放了，清空之前的播放器,重新开启定时器');
                window.clearInterval(timerData);
                timerData = window.setInterval(saveUserVideoStudy, 1000);
            }
        } else if (type == 2) {
            if (isNotEmpty(timerData)) {
                console.log('暂停或者播放结束，关闭定时器');
                window.clearInterval(timerData);
                t = 0;
            }
        }
    }else if (progress==100&&type==2){
        if (isNotEmpty(timerData)) {
            console.log('暂停或者播放结束，关闭定时器');
            window.clearInterval(timerData);
            t = 0;
        }
    }
}
//定时器执行的方法
function saveUserVideoStudy() {

       console.log('开启定时器' + tCourseUserVideo.currentTime);
       console.log('定时的次数' + t);
       t++;
       if (t > 10) {//表示20s推送数据一次
           t = 0;
           console.log('观看20s推送数据一次');
           saveProgress();
       }
}

//观看完本节视频，判断是否播放下一节
function reloadVideo() {
    $("#video").empty();
    var str= '<div class="layui-row">' +
        '<span class="layui-row" style="color: white;font-size: 30px;text-align: center">本节播放结束</span><button  class="layui-btn  layui-btn-lg layui-btn-radius layui-col-md4 layui-col-md-offset2 layui-col-sm4 layui-col-sm-offset2 layui-col-xs6 layui-col-xs-offset1 " onclick="reloadOrNetx($index1,$index2,1)">' +
        '重新播放</button><button class="layui-btn  layui-btn-lg layui-btn-radius layui-col-md4 layui-col-sm3 layui-col-xs4" onclick="reloadOrNetx($index1,$index2,2)">下一节</button>' +
        '</div>';
    $("#video").append(str);
    var height=($("#video").height()- $("#video button").height())/2;
    $("#video button").css('margin-top',height);

}
//播放结束后按钮事件
function reloadOrNetx(index1,index2,type) {
    if (type==1){
        studyPlay(index1,index2,true);
    } else {
        //debugger;
        //当前本章节有下一课时
        if (isNotEmpty(tCourseUserVideo.outlineList[index1].courseHouse[index2+1])){
            console.log('当前本章节有下一课时');
            $(".videospan").eq(index2+1).trigger("click");
            studyPlay(index1,index2+1,true);
        }else if(isNotEmpty(tCourseUserVideo.outlineList[index1+1])){//判断有下一章节
            //播放下一章的第一个课时
            console.log('下一章的第一个课时');
            $(".videospan").eq(index2+1).trigger("click");
            studyPlay(index1+1,0,true);
        }else {
            //warningToast("暂无下一章节",2000);
            //配置一个透明的询问框
            var layer=layui.layer;
            layer.alert('暂无下一章节', {icon: 2});
        }
    }
}

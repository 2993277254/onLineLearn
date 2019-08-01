
var courseDetails = avalon.define({
    $id: "courseDetails",
    baseFuncInfo: baseFuncInfo//底层基本方法
    ,isHas:""
    ,type:""
    ,name:""
    ,courseId:""
    ,total:""
    ,isStudy:""
    ,path:""//简介视频路径
    ,personNum:0
    ,summary:""
    ,demand:""
    ,outlineList:[]    //大纲列表
});

layui.use(['index'],function () {
    avalon.ready(function () {
        courseDetails.baseFuncInfo.getDict(function success() {
            var courseId=GetQueryString("courseId");
            //var name=GetQueryString("name");

            // courseDetails.courseId="";
            courseDetails.courseId=courseId;
            //courseDetails.name=name;
            //console.log("===="+courseId);
            //用户登录查询

               //接受课程的id后到后台查询,
               // 1.如果已登录，查询当前用户是否参加了该课程，
               // 如果参加，‘显示已参加，继续学习’，否则2
               //2.如果未登录，显示‘立即参加’
               isAddCourse(courseId,function (data) {
                   //未登录不判断按钮情况
                   if (isNotEmpty(getUserId())){
                       if (isNotEmpty(data.isHas)){
                           //表示用户已参加该课程
                           //courseDetails.isHas=1;
                           //已观看视频，提示继续学习
                           if (data.isStudy==1){
                               courseDetails.isHas=2;
                           }else if(data.isStudy==2){//表示是开设的课程
                               courseDetails.isHas=3;
                           } else {
                               //未观看视频，表示开始学习
                               courseDetails.isHas=1;
                           }

                       } else {
                           courseDetails.isHas=0;
                       }
                   } else {
                       courseDetails.isHas=0;
                   }
                   suitHeight(1,data.photoUrl);
                   courseDetails.type=data.type;
                   courseDetails.name=data.name;
                   var str=JSON.parse(data.introduction);
                   courseDetails.path=str.path;//简介路径
                   courseDetails.personNum=data.personNum;
                   courseDetails.summary=data.summary;
                   courseDetails.demand=data.demand;
                   courseDetails.outlineList=[];
                   courseDetails.outlineList.pushArray(JSON.parse(data.outline));
                   $(document).attr('title','课程-'+data.name);
                   getDiscussList();
                   //toCourse();
               });


        });
    });
    avalon.scan();
});

/*
播放简介
 */
var introductionPlayer="",video="";
function playPreview() {
    introductionPlayer="";video=$("#video");
    video.empty();//清空div
    // introductionPlayer="";
    //加载播放器
    var videoObject = {
        playerID:'introductionVideo',
        container: '#video',//“#”代表容器的ID，“.”或“”代表容器的class
        variable: 'introductionPlayer',//该属性必需设置，值等于下面的new chplayer()的对象
        loaded:'loadedHandler',//监听播放器加载成功
        // seek:500,
        autoplay:true,
        loop: true, //播放结束是否循环播放
        // playbackrate:false,//禁止播放倍速
        video: getHttpPath()+courseDetails.path
        // mobileCkControls:true,//是否在移动端（包括ios）环境中显示控制栏
        // mobileAutoFull:false//在移动端播放后是否按系统设置的全屏播放
    };
    introductionPlayer=new ckplayer(videoObject);
}
function loadedHandler() {//播放器加载后会调用该函数
    //添加内部键盘事件
    // introductionPlayer.addListener('keypress', introductionPlayer.keypress());
    introductionPlayer.addListener('time', timeHandler); //监听播放时间,addListener是监听函数，需要传递二个参数，'time'是监听属性，这里是监听时间，timeHandler是监听接受的函数
    introductionPlayer.addListener('mouse', mouseHandler); //监听鼠标坐标
    introductionPlayer.addListener('ended', endedHandler); //监听播放结束
    introductionPlayer.addListener('duration', durationHandler); //监听播放时间
    introductionPlayer.addListener('buffer', bufferHandler); //监听缓冲状态
    introductionPlayer.addListener('play', playHandler);
    introductionPlayer.addListener('pause', pauseHandler);

}
function playHandler() {
    console.log('正在播放');
}
function pauseHandler() {
    console.log('已暂停');
}
function timeHandler(t){
    // console.log(t);
    //    console.log(introductionPlayer.interFace());

}
function mouseHandler(obj) {
    console.log(obj);
}
function endedHandler() {
    console.log("播放结束");
    video.empty();
    var html='<button class="layui-btn layui-btn-lg  layui-btn-radius layui-col-md4 layui-col-md-offset4 layui-col-sm4 layui-col-sm-offset4 layui-col-xs6 layui-col-xs-offset3" onclick="playPreview()"><i class="layui-icon layui-icon-refresh-3">&nbsp;重新播放</i></button>';
    video.append(html);
    suitHeight(2,'');
}
function durationHandler(duration) {
    //console.log('总时间（秒）：' + duration);
}
function bufferHandler(buffer) {
    //console.log('缓冲=='+buffer);

}
//让播放简介的按钮居中，type表示初次加载页面显示按钮
function suitHeight(type,photoUrl) {
    var video=$("#video"),button=$("#video button");
    var height=(video.height()-button.height())/2;
    button.css('margin-top',height+'px');
    if (type==1){
        if (isNotEmpty(photoUrl)){
            var url='url('+getHttpPath()+photoUrl+')';
            video.css('background-image',url).css('background-size','100% 100%');
        }
     }
 }
 //判断是否参加了课程,如果参加了判断有没有开始学习
function isAddCourse(id,$callback){
    var param={
        "uid":id,
        "type":"1"
    };
    _ajax({
        type: "POST",
        url: getRootPath() + "tCourse/getInfo.do",
        data: param,  //必须字符串后台才能接收list,
        //loading:false,  //是否ajax启用等待旋转框，默认是true
        dataType: "json",
        done: function (data) {
             //console.log(data);
            typeof $callback === 'function' && $callback(data);
        }
    });
}

/**
 * 按钮的点击事件
 * type :1表示有关系，0表示没有
 */
function addOrStudyCourse(type) {
    if(type==0){//要参加课程
        var param={
            "courseId":courseDetails.courseId
            ,"type":type
        };
        _ajax({
            type: "POST",
            url: getRootPath() + "tCourseUser/addOrStudyCourse.do",
            data: param,  //必须字符串后台才能接收list,
            //loading:false,  //是否ajax启用等待旋转框，默认是true
            dataType: "json",
            done: function (data) {
                courseDetails.isHas=1;
                var layer=layui.layer;
                var title='课程'+courseDetails.name+'报名成功,是否开始学习';
                var index=layer.confirm(title, {
                    closeBtn: 0,
                    btn: ['开始学习','暂时不学习'], //按钮
                    icon:1
                }, function(){
                    //layer.msg('的确很重要', {icon: 1});
                    goLearn(courseDetails.courseId);
                }, function(){

                    layer.close(index);
                });
            }
        });
    }else {//学习课程
        goLearn(courseDetails.courseId);
    }
    //alert(getUserId());


}

//跳转学习页面
//id 课程id
function goLearn(id) {
    location.href=getRootPath()+"courseUser/courseUserVideo?id="+id;
}

//查询讨论列表
function getDiscussList(pageNum,pageSize) {
    var param={
        "page.pageNum": pageNum || 1,
        "page.pageSize": pageSize || 20,
        "courseId":courseDetails.courseId
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
            courseDetails.total="";
            courseDetails.total=data.total;
            if (isEmpty(courseDetails.total)) {
                courseDetails.total=0;
            }
            //加载分页
            loadPage('discuss-page', data.total,getDiscussList);
        }
    });
}

//导航条跳转
function toCourse(type) {
    if (type==1){
        //第一级
        window.location = getRootPath() + "course/courseList";
    } else if (type==2){
        window.location = getRootPath() + "course/courseList?courseType="+courseDetails.type;
    } else {
        window.location = getRootPath() + "course/courseList?courseId="+courseDetails.courseId;
    }
}

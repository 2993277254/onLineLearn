var courseList = avalon.define({
    $id: "courseList",
    baseFuncInfo: baseFuncInfo,//底层基本方法
    typeName:'',
    courseTypeclick: function (value,name) {

        if (isNotEmpty(value)) {
            courseList.baseFuncInfo.courseType = value;
        } else {
            courseList.baseFuncInfo.courseType = '';
        }
        if (isNotEmpty(name)){
            courseList.typeName=name;
        }
        getList();
    },
    courseStatusclick: function (value) {

        if (isNotEmpty(value)) {
            courseList.baseFuncInfo.courseStatus = value;
        } else {
            courseList.baseFuncInfo.courseStatus = '';
        }
        getList();

    },
    courseOrderbyclick: function (value) {

        if (isNotEmpty(value)) {
            courseList.baseFuncInfo.courseOrderby = value;
        } else {
            courseList.baseFuncInfo.courseOrderby = '';
        }
        getList();
    }
    ,total:""

});

layui.use(['index','flow'], function () {
    avalon.ready(function () {
        //每个页面在字典缓存消失时都应该重新加载
        courseList.baseFuncInfo.getDict(function success() {
            addTopSearchHtml();//添加头部搜索框
            topSearch();
            //debugger;
            // var name=decodeURI(GetQueryString("name"));
            // if (name=="null"){
            //     name=null;
            // }
            //console.log("name===="+name);//会中文乱码，需要在解码一次
            var val = GetQueryString("keyword")||$("#keyword").val();
            if (isEmpty(val)){
                val='';
            }
            courseList.baseFuncInfo.courseKeyword=val;
            //console.log("val===="+val);
            $("#keyword").val(val);
            var courseId=GetQueryString("courseId");
            if (isNotEmpty(courseId)) {
                courseList.baseFuncInfo.courseId=courseId;
            }
            var courseType=GetQueryString("courseType");
            if (isNotEmpty(courseType)) {
                courseList.baseFuncInfo.courseType=courseType;
            }
            isHasKeyWord(val);
            getList();
            courseStatusOrOrderbyOrType();
            // layui.img();
        });
    });
    avalon.scan();
});
//获取课程
function getList(pageNum,pageSize) {
    var val=$("#keyword").val();
    //console.log("xxxxxx="+val);
    var param = {
        "uid":courseList.baseFuncInfo.courseId||"",
        "name": val||"",
        "type": courseList.baseFuncInfo.courseType,
        "status": "2",
        "page.orderBy": courseList.baseFuncInfo.courseOrderby,
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
            // console.log(data);
            isHasKeyWord(val);
            //完整功能
            loadTemp(courseListTemp, course_List, data.list);
            courseList.total=data.total;
            if (isEmpty(courseList.total)) {
                courseList.total=0;
            }
            loadPage('course-page', data.total,getList);
            imgFadeBig();
        }
    });
}
//课程的类型,状态,排序
    function courseStatusOrOrderbyOrType() {
        //课程状态
        $(".courseStatus input").each(function (i, item) {
            if (isEmpty($(this).val())) {
                $(this).parent().addClass("layui-this");
            } else if ($(this).val() == courseList.baseFuncInfo.courseStatus) {
                $(".courseStatus").removeClass("layui-this");
                $(this).parent().addClass("layui-this");
            }
            $(this).parent().click(function () {
                $(".courseStatus").removeClass("layui-this");
                $(this).addClass("layui-this");
                //加入筛选
            });
        });
        //课程排序
        $(".courseOrderby input").each(function (i, item) {
            if ($(this).val() == '1') {
                $(this).parent().addClass("layui-this");
            } else if ($(this).val() == courseList.baseFuncInfo.courseOrderby) {
                $(".courseOrderby").removeClass("layui-this");
                $(this).parent().addClass("layui-this");
            }
            $(this).parent().click(function () {
                $(".courseOrderby").removeClass("layui-this");
                $(this).addClass("layui-this");
                //加入筛选
            });
        });

        //课程类型
        $(".courseType input").each(function (i, item) {

            if (isEmpty($(this).val())) {
                $(this).parent().parent().addClass("layui-bg-green");
            } else if ($(this).val() == courseList.baseFuncInfo.courseType) {
                $(".courseType").removeClass("layui-bg-green");
                $(this).parent().parent().addClass("layui-bg-green");
            }
            $(this).next().click(function () {
                $(".courseType").removeClass("layui-bg-green").removeClass("layui-bg-gray");
                $(this).parent().parent().addClass("layui-bg-green");
                //console.log($(this).text());
            });
            $(this).next().hover(function () {
                if ( !$(this).parent().parent().hasClass("layui-bg-green")){
                    $(this).parent().parent().addClass("layui-bg-gray").addClass("acursor");
                }
            },function () {
                $(this).parent().parent().removeClass("layui-bg-gray").removeClass("acursor");
            });
        });
    }

//加载懒加载

function imgLazy() {
    // var flow=layui.flow;
    // //开启懒加载
    // flow.lazyimg({
    //     elem: '.flowImg'
    //     //,scrollElem: '#LAY_demo3' //一般不用设置，此处只是演示需要。
    // });
}
//显示为搜索

function isHasKeyWord(val) {
    if (isEmpty(val)) {
        $("#isSearch").addClass("layui-hide");
    } else {
        $("#isSearch").removeClass("layui-hide");
        $("#isSearch span").text(val);
        //加入筛选
    }
}
/*
鼠标进入图片时加载动画
 */
function imgFadeBig() {
    //
    $(".course-list li").hover(function () {
        $(this).addClass('layui-bg-gray').addClass("cursorHover").find("img").addClass("slowBig");
    }, function () {
        $(this).removeClass("layui-bg-gray").removeClass("cursorHover").find("img").removeClass("slowBig");
    });
}

//课程单击跳转课程详情
function courseClick(id,photoUrl) {
    console.log(id);
    window.location.href=getRootPath()+"course/courseDetails?courseId="+id;
}

function toCourse(type) {
    if (type==1){
        //第一级
        window.location = getRootPath() + "course/courseList";
    } else if (type==2){
        courseList.baseFuncInfo.courseType="";
        courseList.baseFuncInfo.courseType=courseList.typeName;
        getList();
    }
}

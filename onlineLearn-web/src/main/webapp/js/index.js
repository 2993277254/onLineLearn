var frontIndex = avalon.define({
    $id: "frontIndex",
    baseFuncInfo: baseFuncInfo//底层基本方法
    , typeClick: function (value) {
        typeClick(value);
    }
    , hotList: []
    , imgfix: getHttpPath()
    , imgClick: function (id) {
        imgClick(id);
    }
    , categorizeList: []
    ,typeData:[]

});
layui.use(['index', 'carousel'], function () {
    avalon.ready(function () {
        //每个页面在字典缓存消失时都应该重新加载
        frontIndex.baseFuncInfo.getDict(function success() {
            addTopSearchHtml();//添加头部搜索框
            topSearch();
            frontIndex.baseFuncInfo.courseStatus = 3;
            loadHot();
            /*  var carousel = layui.carousel;

  //监听轮播切换事件
              carousel.on('change(test1)', function(obj){ //test1来源于对应HTML容器的 lay-filter="test1" 属性值
                  console.log(obj.index); //当前条目的索引
                  console.log(obj.prevIndex); //上一个条目的索引
                  console.log(obj.item); //当前条目的元素对象
              });*/
            frontIndex.typeData=[];
            frontIndex.typeData.pushArray(frontIndex.baseFuncInfo.getSysDictByCode('course_type_'));
            typeStyle();
            loadCategorize(frontIndex.baseFuncInfo.getSysDictByCode('course_type_'));
            loadBig();
            //imgFadeBig();
        });

    });
    avalon.scan();
});


//加载最热门的轮播，前五条
function loadHot() {
    var param = {
        "page.orderBy": "1",
        "status": "2",
        "page.pageNum": 1,
        "page.pageSize": 5
    };
    _ajax({
        type: "POST",
        url: getRootPath() + "tCourse/list2.do",
        data: param,  //必须字符串后台才能接收list,
        //loading:false,  //是否ajax启用等待旋转框，默认是true
        dataType: "json",
        done: function (data) {
            frontIndex.hotList = [];
            frontIndex.hotList.pushArray(data.list);
            //var str='<div></div>';
            loadWheel();


        }
    });

}

//加载样式
function typeStyle() {
    var bg = "layui-bg-green", oldbg = "layui-bg-black";
    $(".courseType").each(function (i, item) {
        // $(this).click(function () {
        //
        // });
        $(this).hover(function () {
            $(".courseType").removeClass(bg);
            $(this).addClass(bg);
        }, function () {
            if ($(this).hasClass(bg)) {
                $(this).removeClass(bg);
            }
        });
    });
}

//课程类型点击事件
function typeClick(value) {
    location.href = getRootPath() + "course/courseList?courseType=" + value;
}

//加载轮播
function loadWheel() {
    var carousel = layui.carousel;
    //建造实例
    carousel.render({
        elem: '#carouselWheel'
        , width: '100%' //设置容器宽度
        , arrow: 'always' //始终显示箭头
        //,anim: 'updown' //切换动画方式
    });
}

//点击图片
function imgClick(id) {
    location.href = getRootPath() + "course/courseDetails?courseId=" + id;
}

//分类加载数据，每个分类加载前3条
function loadCategorize(data) {
    //debugger;
    frontIndex.categorizeList = [];
    //var arr=[];
    //arr=arr.concat(frontIndex.baseFuncInfo.getSysDictByCode('course_type_'));
    //frontIndex.categorizeList.pushArray();
    $.each(data, function (i, item) {
        var param = {
            "page.orderBy": "2",
            "status": "2",
            "type": item.value,//根据分类查询
            "page.pageNum": 1,
            "page.pageSize": 3
        };
        _ajax({
            type: "POST",
            url: getRootPath() + "tCourse/list2.do",
            data: param,  //必须字符串后台才能接收list,
            //loading:false,  //是否ajax启用等待旋转框，默认是true
            dataType: "json",
            done: function (data) {
                //debugger;
                var str = {};
                if (isNotEmpty(data.list)) {
                    str = {
                        "content": data.list
                    }
                } else {
                    str = {
                        "content": []
                    }
                }
                //item.push(str);
                $.extend(item, str);
                //console.log(item);
                frontIndex.categorizeList.push(item);
            }
        });
    });
    loadBig();
}

function loadBig() {
    var bg = "slowBig";
    $(".hot-list li").hover(function () {
        $(this).find("img").addClass(bg);
    }, function () {
        $(this).find("img").removeClass(bg);
    });
}

/**
 * tCourseList.jsp的js文件，包括列表查询、排序、增加、修改、删除基础操作
 */
var tCourseList = avalon.define({
    $id: "tCourseList",
    baseFuncInfo: baseFuncInfo//底层基本方法
    ,index:0//面板的下标
});
layui.use(['index'], function() {
    //加载layui的模块，index模块是基础模块，也可添加其它
    avalon.ready(function () {
        //所有的入口事件写在这里...

        initSearch(); //初始化搜索框
        getList();  //待审核
        getList2();//通过
        getList3();//驳回
        var element=layui.element;
        //切换面板监听
        element.on('tab(CourseTabBrief)', function(data){
            // console.log(this); //当前Tab标题所在的原始DOM元素
            // console.log(data.index); //得到当前Tab的所在下标
            // console.log(data.elem); //得到当前的Tab大容器
            tCourseList.index=data.index;
            initSearch();
        });
        avalon.scan();
    });
});
/**
 * 初始化搜索框
 */
function initSearch(){
    _initSearch({
        elem: '#tCourseList_search' //指定搜索框表单的元素选择器（推荐id选择器）
        ,filter:'tCourseList_search'  //指定的lay-filter
        ,conds:[
            {field: 'type', title: '类型',type:'select',
                data:getSysDictByCode("course_type_",true)}
            ,{field: 'name', title: '课程名称',type:'input'}
            // ,{field: 'status', title: '课程状态',type:'select',
            //     data:getSysDictByCode("course_status_",true)}
            ,{field: 'orderby', title: '课程排序',type:'select',
                data:getSysDictByCode("course_orderBy_",true)}

        ]
        ,done:function(filter,data){
            //完成html载入，可进行一些插件方法的初始化事件，或者加载下拉框
            //...
        }
        ,search:function(data){
            //点击搜索返回的事件，用于处理搜索方法;获取搜索条件：data.field
            var field = data.field;
            var table = layui.table; //获取layui的table模块
            //重新刷新table,where:接口的其它参数。如：where: {token: 'sasasas', id: 123}
            var index=tCourseList.index;
            if (index==0) {
                table.reload('tCourseList_table', {
                    where: field
                });
            }else if (index==1){
                table.reload('tCourseList_table2', {
                    where: field
                });
            }else {
                table.reload('tCourseList_table3', {
                    where: field
                });
            }
        }
    });
}
/**
 * 查询列表事件，待审核
 */
function getList() {
    var param = {
        "status":"1"
    };
    //获取layui的table模块
    var table=layui.table;
    //获取layui的util模块
    var util=layui.util;
    _layuiTable({
        elem: '#tCourseList_table', //必填，指定原始表格元素选择器（推荐id选择器）
        filter:'tCourseList_table', ////必填，指定的lay-filter的名字
        //执行渲染table配置
        render:{
            height:'full-230', //table的高度，页面最大高度减去差值
            url: getRootPath() + "tCourse/list.do", // ajax的url必须加上getRootPath()方法
            where:param, //接口的参数。如：where: {token: 'sasasas', id: 123}
            cols: [[ //表头
                {fixed: 'left',type:'checkbox'}  //开启编辑框
                ,{type: 'numbers', title: '序号',width:60 }  //序号
                ,{field: 'type', title: '类型',templet:function (d) {
                        return getSysDictName('course_type_',d.type);
                    }}
                ,{field: 'name', title: '名称'}
                // ,{field: 'introduction', title: '课程简介',sort: true,sortField:'tc_.introduction_'}
                ,{field: 'personNum', title: '学习者人数'}
                ,{field: 'summary', title: '概述'}
                ,{field: 'target', title: '授课目标'}
                // ,{field: 'outline', title: '课程大纲',sort: true,sortField:'tc_.outline'}
                ,{field: 'demand', title: '证书要求'}
                // ,{field: 'classTime', title: '开课时间',sort: true,sortField:'tc_.class_time_'}
                // ,{field: 'arrangement', title: '学时安排',sort: true,sortField:'tc_.arrangement'}
                ,{field: 'status', title: '状态',templet:function (d) {
                        return getSysDictName('course_status_',d.status);
                    }}
                // ,{field: 'orderby', title: '课程排序',sort: true,sortField:'tc_.orderby',templet:function (d) {
                //         return getSysDictName('course_orderBy_',d.orderby);
                //     }}
                ,{fixed: 'right',title: '操作',width: 180, align:'center'
                    ,toolbar: '#tCourseList_bar'}
            ]]
        },
        //监听工具条事件
        tool:function(obj,filter){
            var data = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
            var tr = obj.tr; //获得当前行 tr 的DOM对象
            if(layEvent === 'detail'){ //查看
                if(isNotEmpty(data.uid)){
                    saveOrEdit(data.uid,true);
                }
                //do somehing
            } else if(layEvent === 'edit'){ //编辑
                //do something
                if(isNotEmpty(data.uid)){
                    saveOrEdit(data.uid);
                }
            }else if(layEvent === 'del'){ //删除
                layer.confirm('确认删除所选纪录吗？', function(index){
                    // obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
                    layer.close(index);
                    //向服务端发送删除指令
                    if(isNotEmpty(data.uid)){
                        var ids=[];
                        ids.push(data.uid);
                        del(ids);
                    }
                });
            }else if (layEvent === 'verify'){
                verifyCourse(data.uid,data.name);
            }
        }
    });
}
//已通过
function getList2() {
    var param = {
        "status":"2"
    };
    //获取layui的table模块
    var table=layui.table;
    //获取layui的util模块
    var util=layui.util;
    _layuiTable({
        elem: '#tCourseList_table2', //必填，指定原始表格元素选择器（推荐id选择器）
        filter:'tCourseList_table2', ////必填，指定的lay-filter的名字
        //执行渲染table配置
        render:{
            height:'full-190', //table的高度，页面最大高度减去差值
            url: getRootPath() + "tCourse/list.do", // ajax的url必须加上getRootPath()方法
            where:param, //接口的参数。如：where: {token: 'sasasas', id: 123}
            cols: [[ //表头
                // {fixed: 'left',type:'checkbox'}  //开启编辑框
                {type: 'numbers', title: '序号',width:60 }  //序号
                ,{field: 'type', title: '类型',templet:function (d) {
                        return getSysDictName('course_type_',d.type);
                    }}
                ,{field: 'name', title: '名称'}
                // ,{field: 'introduction', title: '课程简介',sort: true,sortField:'tc_.introduction_'}
                ,{field: 'personNum', title: '学习人数'}
                ,{field: 'summary', title: '概述'}
                ,{field: 'target', title: '授课目标'}
                // ,{field: 'outline', title: '课程大纲',sort: true,sortField:'tc_.outline'}
                ,{field: 'demand', title: '证书要求'}
                ,{field: 'status', title: '状态',templet:function (d) {
                        return getSysDictName('course_status_',d.status);
                    }}
                ,{field: '', title: '审核时间',templet:function (d) {
                        return util.toDateString(d.verifyTime,"yyyy-MM-dd HH:mm:ss");
                    }}
                //

            ]]
        }

    });
}
//驳回
function getList3() {
    var param = {
        "status":"3"
    };
    //获取layui的table模块
    var table=layui.table;
    //获取layui的util模块
    var util=layui.util;
    _layuiTable({
        elem: '#tCourseList_table3', //必填，指定原始表格元素选择器（推荐id选择器）
        filter:'tCourseList_table3', ////必填，指定的lay-filter的名字
        //执行渲染table配置
        render:{
            height:'full-190', //table的高度，页面最大高度减去差值
            url: getRootPath() + "tCourse/list.do", // ajax的url必须加上getRootPath()方法
            where:param, //接口的参数。如：where: {token: 'sasasas', id: 123}
            //page:true,
            cols: [[ //表头
                // {fixed: 'left',type:'checkbox'}  //开启编辑框
                {type: 'numbers', title: '序号',width:60 }  //序号
                ,{field: 'type', title: '类型',sort: true,sortField:'tc_.type_',templet:function (d) {
                        return getSysDictName('course_type_',d.type);
                    }}
                ,{field: 'name', title: '名称'}
                // ,{field: 'introduction', title: '课程简介',sort: true,sortField:'tc_.introduction_'}
                 ,{field: 'personNum', title: '学习人数'}
                ,{field: 'summary', title: '概述'}
                ,{field: 'target', title: '授课目标'}
                // ,{field: 'outline', title: '课程大纲',sort: true,sortField:'tc_.outline'}
                ,{field: 'demand', title: '证书要求'}
                ,{field: 'status', title: '状态',templet:function (d) {
                        return getSysDictName('course_status_',d.status);
                    }}
                ,{field: '', title: '审核时间',templet:function (d) {
                        return util.toDateString(d.verifyTime,"yyyy-MM-dd HH:mm:ss");
                    }}
                ,{field: '', title: '驳回原因',templet:function (d) {
                        //preview(\''+path+"','"+d.videoName+'\')
                        var str='<span class="acursor" style="color: deepskyblue" onclick="seeSeason(\''+d.reason+"','"+d.name+'\')">查看</span>'
                        return str;
                    }}

            ]]
        }

    });
}
//已驳回


/**
 * 获取单个实体
 */
function saveOrEdit(id,readonly){
    var url="";
    var title="";
    if(isEmpty(id)){  //id为空,新增操作
        title="新增";
        url=getRootPath()+"courseUser/tCourseEdit";
    }else{  //编辑
        title="编辑";
        url=getRootPath()+"courseUser/tCourseEdit?id="+id;
    }
    //_layerOpen系统弹框统一调用方法，done事件是定义的按钮的执行事件
    _layerOpen({
        url:url,  //弹框自定义的url，会默认采取type=2
        width:800, //弹框自定义的宽度，方法会自动判断是否超过宽度
        height:600,  //弹框自定义的高度，方法会自动判断是否超过高度
        readonly:readonly,  //弹框自定义参数，用于是否只能查看,默认是false，true代表只能查看,done事件不执行
        title:title, //弹框标题
        done:function(index,iframeWin){
            /**
             * 确定按钮的回调,说明：index是关闭弹框用的，iframeWin是操作子iframe窗口的变量，
             * 利用iframeWin可以执行弹框的方法，比如save方法
             */
            var ids = iframeWin.save(
                //成功保存之后的操作，刷新页面
                function success() {
                    successToast("保存成功",500);
                    var table = layui.table; //获取layui的table模块
                    table.reload('tCourseList_table'); //重新刷新table
                    layer.close(index); //如果设定了yes回调，需进行手工关闭
                }
            );
        }
    });
}

/**
 * 删除事件
 * @param ids
 */
function del(ids){
    var param={
        "ids":ids
    };
    _ajax({
        type: "POST",
        url: getRootPath()+"tCourse/delete.do",
        data:param,  //必须字符串后台才能接收list,
        //loading:false,  //是否ajax启用等待旋转框，默认是true
        dataType: "json",
        done: function(data){
            successToast("删除成功",500);
            var table = layui.table; //获取layui的table模块
            table.reload('tCourseList_table'); //重新刷新table
        }
    });
}

/**
 * 批量删除
 */
function batchDel(){
    var table = layui.table; //获取layui的table模块
    var checkStatus = table.checkStatus('tCourseList_table'); //test即为基础参数id对应的值
    var data=checkStatus.data; //获取选中行的数据
    if(data.length==0){
        warningToast("请至少选择一条纪录");
        return false;
    }else{
        layer.confirm('确认删除所选纪录吗？', function(index){
            layer.close(index);
            var ids=[];
            $.each(data,function(i,item){
                ids.push(item.uid);
            });
            del(ids);
        });
    }
}

function verify_form($callback){
    //监听提交,先定义个隐藏的按钮
    var form=layui.form; //调用layui的form模块
    form.on('submit(verifyForm_submit)', function(data){
        //通过表单验证后
        //debugger;
        var field = data.field; //获取提交的字段
        typeof $callback === 'function' && $callback(field); //返回一个回调事件
    });
    $("#verifyForm_submit").trigger('click');
}

function save() {
    verify_form(function(field){
        var param=field; //表单的元素
        console.log('参数==='+JSON.stringify(param));
        //可以继续添加需要上传的参数
        // _ajax({
        //     type: "POST",
        //     //loading:true,  //是否ajax启用等待旋转框，默认是true
        //     url: getRootPath()+"tCourse/saveOrEdit.do",
        //     data:param,
        //     dataType: "json",
        //     done:function(data){
        //         var table = layui.table; //获取layui的table模块
        //         table.reload('tCourseList_table'); //重新刷新table
        //     }
        // });
    });
}
//审核课程
function verifyCourse(id,name) {
    var url="";
    var title="";
     //编辑
     title="课程《"+name+"》的审核";
     url=getRootPath()+"course/verifyCourse?id="+id;

    //_layerOpen系统弹框统一调用方法，done事件是定义的按钮的执行事件
    _layerOpen({
        url:url,  //弹框自定义的url，会默认采取type=2
        width:600, //弹框自定义的宽度，方法会自动判断是否超过宽度
        height:300,  //弹框自定义的高度，方法会自动判断是否超过高度
        // readonly:readonly,  //弹框自定义参数，用于是否只能查看,默认是false，true代表只能查看,done事件不执行
        title:title, //弹框标题
        done:function(index,iframeWin){
            /**
             * 确定按钮的回调,说明：index是关闭弹框用的，iframeWin是操作子iframe窗口的变量，
             * 利用iframeWin可以执行弹框的方法，比如save方法
             */
            var ids = iframeWin.save(
                //成功保存之后的操作，刷新页面
                function success() {
                    successToast("保存成功",500);
                    var table = layui.table; //获取layui的table模块

                    table.reload('tCourseList_table');
                    table.reload('tCourseList_table2');
                    table.reload('tCourseList_table3');
                    layer.close(index); //如果设定了yes回调，需进行手工关闭
                }
            );
        }
    });
}



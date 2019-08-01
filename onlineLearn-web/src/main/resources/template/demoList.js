/**
 * ${FileName}List.jsp的js文件，包括列表查询、排序、增加、修改、删除基础操作
 */
var ${FileName}List = avalon.define({
    $id: "${FileName}List",
    baseFuncInfo: baseFuncInfo<#if isLeftZtree?? && isLeftZtree=="1">,</#if>//底层基本方法
    <#if isLeftZtree?? && isLeftZtree=="1">
    treeId:""  //左侧树的id
    </#if>
});
<#if isLeftZtree?? && isLeftZtree=="1">
var treeObj; //左侧树对象
</#if>
layui.use(['index'], function() {
    //加载layui的模块，index模块是基础模块，也可添加其它
    avalon.ready(function () {
        //所有的入口事件写在这里...
        <#if isLeftZtree?? && isLeftZtree=="1">
        initLeftZtree(); //初始化左侧树
        </#if>
        initSearch(); //初始化搜索框
        getList();  //查询列表
        avalon.scan();
    });
});
<#if isLeftZtree?? && isLeftZtree=="1">
//获取树结点列表
function initLeftZtree(){
    //设置左侧树的基本属性
    var opt={
        title:"${genTableTree.getName()}", //div树的标题
        initZtree:initZtree //初始化树方法
    };
    //加载ztree左侧树
    _initLeftZtree($("#left_tree"),opt);

    // 初始化树方法,obj：代表当前树对象
    function initZtree(obj){
        var param = {};
        // 渲染表格
        _ajax({
            type: "POST",
            //loading:true,  //是否ajax启用等待旋转框，默认是true
            url:getRootPath()+"${genTableTree.getTreeUrl()}",
            data: param,
            done:function(data){
                var setting={
                    id: '${genTableTree.getTreeId()}', //新增自定义参数，同ztree的data.simpleData.idKey
                    pId: '${genTableTree.getTreeParentId()}', //新增自定义参数，同ztree的data.simpleData.pIdKey
                    name:'${genTableTree.getTreeName()}',  //新增自定义参数，同ztree的data.key.name
                    //radio:true //新增自定义参数，开启radio
                    //checkbox:true, //新增自定义参数，开启checkbox
                    //其它具体参数请参考ztree文档
                    callback: {
                        beforeClick:zTreeBeforeClick //单击节点事件
                    }
                };
                //初始化树,加载左侧树直接用方法的提供的obj
                treeObj=_initZtree(obj,setting,data);
            }
        });
    }
}
</#if>
/**
 * 初始化搜索框
 */
function initSearch(){
    _initSearch({
        elem: '#${FileName}List_search' //指定搜索框表单的元素选择器（推荐id选择器）
        ,filter:'${FileName}List_search'  //指定的lay-filter
        ,conds:[
        <#assign index = 0>
        <#list genTableColumnList as col>
            <#if col.getIsQuery()=="1">
                <#if col.getQueryType()=="select">
            <#if index != 0>,</#if>{field: '${col.getJavaField()}',type:'${col.getQueryType()}', title: '${col.getShowName()}'<#if col.getListAlign()=="center"||col.getListAlign()=="right">,align:'${col.getListAlign()}'</#if><#if col.getIsSort()=="1">,sort: true,sortField:'${col.getName()}'</#if>
            ,data:getSysDictByCode("${col.getEditData()}",true)} //加载数据字典
                <#elseif col.getQueryType()=="checkbox"||col.getQueryType()=="radio">
            <#if index != 0>,</#if>{field: '${col.getJavaField()}',type:'${col.getQueryType()}',title: '${col.getShowName()}'<#if col.getListAlign()=="center"||col.getListAlign()=="right">,align:'${col.getListAlign()}'</#if><#if col.getIsSort()=="1">,sort: true,sortField:'${col.getName()}'</#if>
            ,data:getSysDictByCode("${col.getEditData()}")} //加载数据字典
                <#else>
            <#if index != 0>,</#if>{field: '${col.getJavaField()}', title: '${col.getShowName()}',type:'${col.getQueryType()}'}
                </#if>
            <#assign index = index + 1>
            </#if>
        </#list>
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
            table.reload('${FileName}List_table',{
                where:field
            });
        }
    });
}
/**
 * 查询列表事件
 */
function getList() {
    var param = {
    };
    //获取layui的table模块
    var table=layui.table;
    //获取layui的util模块
    var util=layui.util;
    _layuiTable({
        elem: '#${FileName}List_table', //必填，指定原始表格元素选择器（推荐id选择器）
        filter:'${FileName}List_table', ////必填，指定的lay-filter的名字
        //执行渲染table配置
        render:{
            height:'full-180', //table的高度，页面最大高度减去差值
            url: getRootPath() + "${FileName}/list.do", // ajax的url必须加上getRootPath()方法
            where:param, //接口的参数。如：where: {token: 'sasasas', id: 123}
            cols: [[ //表头
                {fixed: 'left',type:'checkbox'}  //开启编辑框
                ,{type: 'numbers', title: '序号',width:60 }  //序号
                <#list genTableColumnList as col>
                <#if col.getIsList()=="1">
                    <#if col.getJavaType()=="java.util.Date">
                ,{field: '${col.getJavaField()}', title: '${col.getShowName()}'<#if col.getListAlign()=="center"||col.getListAlign()=="right">,align:'${col.getListAlign()}'</#if><#if col.getIsSort()=="1">,sort: true,sortField:'${aliasName}.${col.getName()}'</#if>
                    ,templet: function(d){
                    return util.toDateString(d.${col.getJavaField()},"yyyy-MM-dd HH:mm:ss");
                }}
                    <#elseif col.getEditType()=="select"||col.getEditType()=="checkbox"||col.getEditType()=="radio">
                ,{field: '${col.getJavaField()}', title: '${col.getShowName()}'<#if col.getListAlign()=="center"||col.getListAlign()=="right">,align:'${col.getListAlign()}'</#if><#if col.getIsSort()=="1">,sort: true,sortField:'${aliasName}.${col.getName()}'</#if>
                    ,templet: function(d){
                    //返回数据字典的名称
                    return getSysDictName("${col.getEditData()}",d.${col.getJavaField()});
                }}
                    <#elseif (col.getListField()?? && col.getListField()!="")>
                ,{field: '${col.getJavaField()}', title: '${col.getShowName()}'<#if col.getListAlign()=="center"||col.getListAlign()=="right">,align:'${col.getListAlign()}'</#if><#if col.getIsSort()=="1">,sort: true,sortField:'${col.getListField()}'</#if>
                    ,templet: function(d){
                    var str="";
                    if(isNotEmpty(d.${col.getField1()})&&isNotEmpty(d.${col.getField2()})){
                        str=d.${col.getField2()};
                    }
                    return str;
                }}
                    <#else>
                ,{field: '${col.getJavaField()}', title: '${col.getShowName()}'<#if col.getListAlign()=="center"||col.getListAlign()=="right">,align:'${col.getListAlign()}'</#if><#if col.getIsSort()=="1">,sort: true,sortField:'${aliasName}.${col.getName()}'</#if>}
                    </#if>
                </#if>
                </#list>
                ,{fixed: 'right',title: '操作',width: 180, align:'center'
                    ,toolbar: '#${FileName}List_bar'}
            ]]
        },
        //监听工具条事件
        tool:function(obj,filter){
            var data = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
            var tr = obj.tr; //获得当前行 tr 的DOM对象
            if(layEvent === 'detail'){ //查看
                if(isNotEmpty(data.${id_java})){
                    saveOrEdit(data.${id_java},true);
                }
                //do somehing
            } else if(layEvent === 'edit'){ //编辑
                //do something
                if(isNotEmpty(data.${id_java})){
                    saveOrEdit(data.${id_java});
                }
            }else if(layEvent === 'del'){ //删除
                layer.confirm('确认删除所选纪录吗？', function(index){
                    // obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
                    layer.close(index);
                    //向服务端发送删除指令
                    if(isNotEmpty(data.${id_java})){
                        var ids=[];
                        ids.push(data.${id_java});
                        del(ids);
                    }
                });
            }
        }
    });
}

/**
 * 获取单个实体
 */
function saveOrEdit(id,readonly){
    var url="";
    var title="";
    if(isEmpty(id)){  //id为空,新增操作
        title="新增";
        url=getRootPath()+"${moduleName}/${FileName}Edit";
    }else{  //编辑
        title="编辑";
        url=getRootPath()+"${moduleName}/${FileName}Edit?id="+id;
    }
    //_layerOpen系统弹框统一调用方法，done事件是定义的按钮的执行事件
<#assign maxCount = 0>
<#list genTableColumnList as col>
    <#if (col.getField3()?number) gte maxCount>
        <#assign maxCount = col.getField3()?number>
    </#if>
</#list>
    _layerOpen({
        url:url,  //弹框自定义的url，会默认采取type=2
        width:${(maxCount*400)?c}, //弹框自定义的宽度，方法会自动判断是否超过宽度
        height:500,  //弹框自定义的高度，方法会自动判断是否超过高度
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
                    table.reload('${FileName}List_table'); //重新刷新table
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
        url: getRootPath()+"${FileName}/delete.do",
        data:param,  //必须字符串后台才能接收list,
        //loading:false,  //是否ajax启用等待旋转框，默认是true
        dataType: "json",
        done: function(data){
            successToast("删除成功",500);
            var table = layui.table; //获取layui的table模块
            table.reload('${FileName}List_table'); //重新刷新table
        }
    });
}

/**
 * 批量删除
 */
function batchDel(){
    var table = layui.table; //获取layui的table模块
    var checkStatus = table.checkStatus('${FileName}List_table'); //test即为基础参数id对应的值
    var data=checkStatus.data; //获取选中行的数据
    if(data.length==0){
        warningToast("请至少选择一条纪录");
        return false;
    }else{
        layer.confirm('确认删除所选纪录吗？', function(index){
            layer.close(index);
            var ids=[];
            $.each(data,function(i,item){
                ids.push(item.${id_java});
            });
            del(ids);
        });
    }
}
<#if isLeftZtree?? && isLeftZtree=="1">
//ztree事件：单击节点事件
function zTreeBeforeClick(treeId, treeNode, clickFlag) {
    var zTree = $.fn.zTree.getZTreeObj(treeId);
    if(zTree.isSelectedNode(treeNode)){
        zTree.cancelSelectedNode(treeNode);
    }else {
        ${FileName}List.treeId=treeNode.${genTableTree.getTreeId()};
        zTree.selectNode(treeNode);
        successToast("点击了："+treeNode.${genTableTree.getTreeName()});
    }
    return false;
}
</#if>
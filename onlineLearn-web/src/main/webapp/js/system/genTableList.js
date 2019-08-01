/**
 * Created by huoquan on 2017/9/28.
 */
var columnListMap={};  //全部字段列表Map，包括单表跟外键表
var pkMap={};  //table的主键
var genTableList = avalon.define({
    $id: "genTableList",
    baseFuncInfo: baseFuncInfo,//底层基本方法
    tableName:"",  //记录table的name
    tableList:[],  //全部table的列表
    moduleList:[
        {value:'base',name:'base模块(基础数据)'}
        ,{value:'system',name:'system模块(系统管理)'}
        ,{value:'video',name:'video模块(视频管理)'}
        ,{value:'course',name:'course模块(课程管理)'}
        ,{value:'courseUser',name:'courseUser模块(用户课程管理)'}
    ], //模块列表，自己添加
    tableColumnList:[],  //字段列表
    pkJson:[],  //外键表列表
    isCover:"", //是否覆盖
    isLeftZtree:"", //是否左侧树
    leftZtreeId:"", //左侧树id
    addPk:function(){
        genTableList.pkJson.push({
            tableName:"",
            pkName:"",
            module:"",
            pk:""
        });
        var form = layui.form;
        form.render();
    },
    delPk:function(index){
        avalon.Array.removeAt(genTableList.pkJson,index);
        var form = layui.form;
        form.render();
    }
});
layui.use(['index'], function(){
    avalon.ready(function () {
        noShowCusTop();
        noShowSearch();
        var form=layui.form;
        form.on('checkbox(isCover)', function(data){
            genTableList.isCover=data.elem.checked?"1":"0";
        });
        form.on('checkbox(isLeftZtree)', function(data){
            genTableList.isLeftZtree=data.elem.checked?"1":"0";
        });
        getlist();

        avalon.scan();
    });
});


//获取树结点列表
function getlist(id){
    //设置左侧树的基本属性
    var opt={
        title:"数据库管理", //div树的标题
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
            contentType: 'application/json;charset=utf-8', //设置请求头信息
            url:getRootPath()+"genTable/findTableList.do",
            data: JSON.stringify(param),  //必须字符串
            done:function(data){
                genTableList.tableList=[];
                genTableList.tableList.pushArray(data);
                var ztreeData=[];
                $.each(data,function(i,item){
                    //存每个表的主键
                    pkMap[item.name]=item.pkName;
                    //排除某些系统表，不让别人修改
                    // if(item.name!="gen_table"&&item.name!="gen_table_column"
                    //     &&item.name!="gen_table_tree"
                    //     && item.name!="sys_key"
                    //     &&item.name!="sys_menu"
                    //     && item.name!="sys_role"
                    //     &&item.name!="sys_role_key"
                    //     &&item.name!="sys_user"
                    //     &&item.name!="sys_user_role"
                    //     &&item.name!="sys_dict"
                    //     &&item.name!="sys_dict_data"){
                    //     ztreeData.push(item);
                    // }
                    ztreeData.push(item);
                });
                var setting={
                    idKey: 'id', //新增自定义参数，同ztree的data.simpleData.idKey
                    pIdKey: 'parentId', //新增自定义参数，同ztree的data.simpleData.pIdKey
                    name:'name',  //新增自定义参数，同ztree的data.key.name
                    callback: {
                        beforeClick:zTreeBeforeClick //单击节点事件
                    }
                };
                //单击节点事件
                function zTreeBeforeClick(treeId, treeNode, clickFlag) {
                    var zTree = $.fn.zTree.getZTreeObj(treeId);
                    if(zTree.isSelectedNode(treeNode)){
                        zTree.cancelSelectedNode(treeNode);
                    }else {
                        genTableList.tableName=treeNode.name;
                        zTree.selectNode(treeNode);
                        // getInfo(treeNode.id);
                        getTableColumnList(treeNode.name,function(){
                            getGenTableList(treeNode.name);
                        });
                    }
                    return false;
                }
                //初始化树,加载左侧树直接用方法的提供的obj
                _initZtree(obj,setting,ztreeData);
            }
        });
    }
}

//根据菜单id获取按钮列表
function getTableColumnList(tableName,$callback){
    var param={
        "tableName":tableName
    };
    _ajax({
        type: "POST",
        //loading:false,  //是否ajax启用等待旋转框，默认是true
        url: getRootPath()+"genTable/findTableColumnList.do",
        data:param,
        dataType: "json",
        done:function(data){
            //获取layui的table模块
            var form = layui.form;
            genTableList.tableColumnList=[];
            genTableList.tableColumnList.pushArray(data);
            columnListMap[tableName]=data;
            form.val('genTableList_search_form', {
                isCover:false
            });
            typeof $callback === 'function' && $callback(data); //返回一个回调事件
            //监听下拉框操作,外表关联
            form.on('select(pkJson_tableName)', function(data){
                var index=$(data.elem).attr("index");
                genTableList.pkJson[index].tableName=data.value;
                genTableList.pkJson[index].pkName=pkMap[data.value];
            });

            //监听下拉框操作,外表关联
            form.on('select(pkJson_module)', function(data){
                var index=$(data.elem).attr("index");
                genTableList.pkJson[index].module=data.value;
            });
            //监听下拉框操作,外表关联
            form.on('select(pkJson_pk)', function(data){
                var index=$(data.elem).attr("index");
                genTableList.pkJson[index].pk=data.value;
            });
            _layuiTable({
                elem: '#genTableList_table', //必填，指定原始表格元素选择器（推荐id选择器）
                filter:'genTableList_table', ////必填，指定的lay-filter的名字
                //执行渲染table配置
                render:{
                    height:'full-260', //table的高度，页面最大高度减去差值
                    data:genTableList.tableColumnList,
                    page:false,
                    limit:genTableList.tableColumnList.length,
                    cols: [[ //表头
                        {fixed: 'left',type: 'numbers', title: '序号',width:60,rowspan:2 }  //序号
                        ,{fixed: 'left',field: 'name', title: '表字段名称',width:150,rowspan:2}
                        // ,{field: 'comments', title: '表字段说明'}
                        ,{fixed: 'left',field: 'showName',width:150,rowspan:2, title: '<i class="layui-icon layui-icon-edit"></i>页面字段名称' +
                        '<i class="layui-icon layui-icon-tips" lay-tips="页面字段名称可以直接点击单元格进行修改"></i>',edit:true}
                        ,{field: 'jdbcType', title: '表字段类型',width:150,rowspan:2}
                        ,{field: 'editData', rowspan:2, align:'center',width:150
                            , title: '<span class="layui-badge-dot"></span>配置数据源<i class="layui-icon layui-icon-tips" lay-tips="目前仅支持选择数据字典，通常用作下拉框、复选框、单选框"></i>'
                            , templet: function(d){
                                return '<input type="text" name="editData" placeholder="请选择" value="'+d.editData+'" readonly onclick="getDicList(this,'+(d.LAY_INDEX-1)+',\'editData\')" ' +
                                    'autocomplete="off" class="layui-input layui-search-input">'
                                    +'<i class="layui-icon input-icon-right layui-icon-search"></i>';
                            }}
                        ,{title: '查询页面的搜索条件', align:'center',colspan:3}
                        ,{title: '查询页面的列表查询', align:'center',colspan:4}
                        ,{title: '编辑页面的条件设置', align:'center',colspan:4}
                    ],
                        [ //表头
                            {field: 'isQuery', title: '加入查询',style:'background-color: #E5E34C;', align:'center',width:110, templet: '#isQueryTpl'}
                            ,{field: 'queryType', title: '查询类型',style:'background-color: #E5E34C;', align:'center',width:190, templet: '#queryTypeTpl'}
                            ,{field: 'queryCondition', title: '查询条件',style:'background-color: #E5E34C;', align:'center',width:110, templet: '#queryConditionTpl'}

                            ,{field: 'isList', title: '列表显示',style:'background-color: #A0A3EB;', align:'center',width:110, templet: '#isListTpl'}
                            ,{field: 'listAlign', title: '列表排列',style:'background-color: #A0A3EB;', align:'center',width:150, templet: '#listAlignTpl'}
                            ,{field: 'isSort', title: '是否排序',style:'background-color: #A0A3EB;', align:'center',width:110, templet: '#isSortTpl'}
                            ,{field: 'listField', title: '<span class="layui-badge-dot"></span>列表显示值<i class="layui-icon layui-icon-tips" lay-tips="此功能是为了显示父表的字段内容，否则只显示本字段的内容"> </i>',style:'background-color: #A0A3EB;', align:'center',width:150
                            , templet: function(d){
                                return '<input type="text" name="listField" placeholder="本字段值" value="'+d.listField+'" readonly onclick="getFkColumnList(this,'+(d.LAY_INDEX-1)+')" ' +
                                    'autocomplete="off" class="layui-input layui-search-input">'
                                    +'<i class="layui-icon input-icon-right layui-icon-search"></i>';
                            }}
                            ,{field: 'isEdit', title: '编辑显示',style:'background-color: #5FDAD7;', align:'center',width:110, templet: '#isEditTpl'}
                            ,{field: 'editType', title: '<span class="layui-badge-dot"></span>编辑类型<i class="layui-icon layui-icon-tips" lay-tips="新增了上传图片功能"></i>',style:'background-color: #5FDAD7;', align:'center',width:190, templet: '#editTypeTpl'}
                            ,{field: 'isRequired', title: '是否必填',style:'background-color: #5FDAD7;', align:'center',width:110, templet: '#isRequiredTpl'}
                            ,{field: 'field3', title: '<span class="layui-badge-dot"></span>栅格<i class="layui-icon layui-icon-tips" lay-tips="用法：连续相同栅格的字段系统会自动合并一行，比如：连续两个<一行两个>字段，最后这2个在表单会合并成一行"></i>',style:'background-color: #5FDAD7;', align:'center',width:190, templet: '#field3Tpl'}
                            ,{field: 'editVerify', title: '验证方式',style:'background-color: #5FDAD7;', align:'center',width:150, templet: '#editVerifyTpl'}
                        ]]
                },
                edit:function(obj,filter){
                    //console.log(obj.field+"::"+genTableList.tableColumnList[obj.data.LAY_TABLE_INDEX][obj.field]);
                    genTableList.tableColumnList[obj.data.LAY_TABLE_INDEX][obj.field]=obj.value;
                }
            });
            $(".layui-table-cell select").parent().addClass('cell_overflow');
            //监听切换操作
            form.on('switch(isQuery)', function(data){
                console.log("name::"+genTableList.tableColumnList[data.value].name);
                console.log("isQuery::"+genTableList.tableColumnList[data.value].isQuery);
                genTableList.tableColumnList[data.value].isQuery=data.elem.checked?'1':'0';
            });
            //监听下拉框操作
            form.on('select(listAlign)', function(data){
                var  index=data.value.split("|")[0];
                var  value=data.value.split("|")[1];
                //console.log(genTableList.tableColumnList[index].listAlign);
                genTableList.tableColumnList[index].listAlign=value;
            });
            //监听下拉框操作
            form.on('select(queryType)', function(data){
                var  index=data.value.split("|")[0];
                var  value=data.value.split("|")[1];
                console.log(genTableList.tableColumnList[index].queryType);
                genTableList.tableColumnList[index].queryType=value;
            });
            //监听下拉框操作
            form.on('select(queryCondition)', function(data){
                var  index=data.value.split("|")[0];
                var  value=data.value.split("|")[1];
                console.log(genTableList.tableColumnList[index].queryCondition);
                genTableList.tableColumnList[index].queryCondition=value;
            });

            //监听切换操作
            form.on('switch(isList)', function(data){
                //console.log("isList::"+genTableList.tableColumnList[data.value].isList);
                genTableList.tableColumnList[data.value].isList=data.elem.checked?'1':'0';
            });
            //监听切换操作
            form.on('switch(isSort)', function(data){
                //console.log("isSort::"+genTableList.tableColumnList[data.value].isSort);
                genTableList.tableColumnList[data.value].isSort=data.elem.checked?'1':'0';
            });
            //监听切换操作
            form.on('switch(isEdit)', function(data){
                //console.log("isEdit::"+genTableList.tableColumnList[data.value].isEdit);
                genTableList.tableColumnList[data.value].isEdit=data.elem.checked?'1':'0';
            });
            //监听切换操作
            form.on('switch(isRequired)', function(data){
                //console.log("isRequired::"+genTableList.tableColumnList[data.value].isRequired);
                genTableList.tableColumnList[data.value].isRequired=data.elem.checked?'1':'0';
            });
            //监听下拉框操作
            form.on('select(editType)', function(data){
                var  index=data.value.split("|")[0];
                var  value=data.value.split("|")[1];
                console.log(genTableList.tableColumnList[index].editType);
                genTableList.tableColumnList[index].editType=value;
            });
            //监听下拉框操作
            form.on('select(field3)', function(data){
                var  index=data.value.split("|")[0];
                var  value=data.value.split("|")[1];
                console.log(genTableList.tableColumnList[index].field3);
                genTableList.tableColumnList[index].field3=value;
            });
            //监听下拉框操作
            form.on('select(editVerify)', function(data){
                var  index=data.value.split("|")[0];
                var  value=data.value.split("|")[1];
                console.log(genTableList.tableColumnList[index].editVerify);
                genTableList.tableColumnList[index].editVerify=value;
            });
        }
    });
}
/**
 * 关闭弹窗
 * @param $callBack  成功修改后的回调函数，比如可用作于修改后查询列表
 */
function save(type,$callback){  //菜单保存操作
    //对表单验证
    verify_form(function(field){
        //成功验证后
        var genTable={
            module:field.module,
            name:field.name,
            leftZtreeId:genTableList.leftZtreeId,
            isLeftZtree:genTableList.isLeftZtree,
            leftZtreeName:field.leftZtreeName,
            pkJson:JSON.stringify(genTableList.pkJson)
        };
        //成功验证后
        var param={
            genTableColumnList:genTableList.tableColumnList,
            genTable:genTable
        }; //表单的元素
        //可以继续添加需要上传的参数
        var url="";
        if(type==1){
            //保存
            url=getRootPath()+"genTable/saveOrEdit.do";
        }else if(type==2){
            //一键生成
            url=getRootPath()+"genTable/autoCreate.do";
            param.isCover=genTableList.isCover;
        }
        if(type==2){
            var layer=layui.layer;
            var msg="确认要生成代码？";
            if(type==2&&genTableList.isCover=="1"){
                msg="确认要生成代码？将会覆盖项目中的文件，请小心操作";
            }
            layer.confirm(msg, function(index){
                _ajax({
                    type: "POST",
                    //loading:true,  //是否ajax启用等待旋转框，默认是true
                    contentType: 'application/json;charset=utf-8', //设置请求头信息
                    url: url,
                    data: JSON.stringify(param),  //必须字符串
                    dataType: "json",
                    done:function(data){
                        if(type==1){
                            //保存
                            successToast("保存成功");
                        }else if(type==2){
                            //一键生成
                            successToast("生成成功");
                        }
                        typeof $callback === 'function' && $callback(data); //返回一个回调事件
                    }
                });
            });
        }else{
            _ajax({
                type: "POST",
                //loading:true,  //是否ajax启用等待旋转框，默认是true
                contentType: 'application/json;charset=utf-8', //设置请求头信息
                url: url,
                data: JSON.stringify(param),  //必须字符串
                dataType: "json",
                done:function(data){
                    if(type==1){
                        //保存
                        successToast("保存成功");
                    }else if(type==2){
                        //一键生成
                        successToast("生成成功，请重启服务器重新运行即可");
                    }
                    typeof $callback === 'function' && $callback(data); //返回一个回调事件
                }
            });
        }
    });
}

//验证表单
function verify_form($callback){
    //监听提交,先定义个隐藏的按钮
    var form=layui.form; //调用layui的form模块
    form.on('submit(genTableList_submit)', function(data){
        //通过表单验证后
        var field = data.field; //获取提交的字段
        typeof $callback === 'function' && $callback(field); //返回一个回调事件
    });
    $("#genTableList_submit").trigger('click');
}


function getGenTableList(tableName){
    _ajax({
        type: "POST",
        //loading:true,  //是否ajax启用等待旋转框，默认是true
        url: getRootPath()+"genTable/genTableList.do",
        data:{tableName:tableName},
        dataType: "json",
        done:function(data){
            var form=layui.form; //调用layui的form模块
            genTableList.pkJson=[];
            if(isNotEmpty(data)){
                var formData={
                    module:data.module,
                    name:data.name
                };
                if(isNotEmpty(data.pkJson)){
                    var pkJson=eval('(' + data.pkJson + ')');
                    genTableList.pkJson.pushArray(pkJson);
                    $.each(pkJson,function(i,item){
                        formData["tableName"+i]=item.tableName;
                        formData["module"+i]=item.module;
                        formData["pk"+i]=item.pk;
                    });
                }
                if(isNotEmpty(data.isLeftZtree)&&data.isLeftZtree=="1"){
                    genTableList.isLeftZtree="1";
                    formData.isLeftZtree=true;
                }else{
                    genTableList.isLeftZtree="0";
                    formData.isLeftZtree=false;
                }
                genTableList.leftZtreeId=data.leftZtreeId;
                $("#leftZtreeName").val(data.leftZtreeName);
                //表单重新赋值
                form.val('genTableList_form', formData);
            }else{
                var formData={
                    module:""
                };
                genTableList.isLeftZtree="0";
                genTableList.leftZtreeId="";
                formData.isLeftZtree=false;
                $("#leftZtreeName").val("");
                form.val('genTableList_form', formData);
            }
        }
    });
}

//弹出tips层-右
function alertTip(msg,obj){
    var layer=layui.layer;
    layer.tips(msg,obj);
}

//获取数据字典
function getDicList(obj,index,column){
    var param = {};
    // 渲染表格
    _ajax({
        type: "POST",
        //loading:true,  //是否ajax启用等待旋转框，默认是true
        url:getRootPath()+"sysDict/getLists.do",
        data: param,
        done:function(data){
            $.each(data,function(i,item){
                item.dictName=item.dictName+"("+item.dictType+")";
            });
            var setting={
                id: 'id', //新增自定义参数，同ztree的data.simpleData.idKey
                pId: 'pid', //新增自定义参数，同ztree的data.simpleData.pIdKey
                name:'dictName',  //新增自定义参数，同ztree的data.key.name
                radio:true, //新增自定义参数，开启radio
                //checkbox:true, //新增自定义参数，开启checkbox
                done:function(treeObj){
                    //新增自定义函数,完成初始化加载后的事件，比如可做一些反勾选操作
                    var dictType=genTableList.tableColumnList[index][column];
                    if(isNotEmpty(dictType)){
                        $.each(data,function(i,item){
                            if(item.dictType==dictType){
                                var node = treeObj.getNodeByParam("dictType",item.dictType,null);
                                if(node!=null){
                                    treeObj.checkNode(node, true, false);
                                    treeObj.selectNode(node);
                                }
                            }
                        });
                    }
                }
                //其它具体参数请参考ztree文档
            };
            //加载ztree弹框树
            _initLayOpenZtree(setting,data,function(nodes){
                //确定事件，返回选择的nodes节点列表
                var value="";
                if(nodes.length>0){
                    value=nodes[0].dictType;
                }
                genTableList.tableColumnList[index][column]=value;
                $(obj).val(value);
            });
        }
    });
}

//获取外键字段
function getFkColumnList(obj,index){
    var tables=[];
    $.each(genTableList.pkJson,function(i,item){
        tables.push(item.tableName);
    });
    if(tables.length==0){
        warningToast("目前没有关联的父表，只能显示本字段");
        return false;
    }
    var param = {
        tables:tables
    };
    // 渲染表格
    _ajax({
        type: "POST",
        //loading:true,  //是否ajax启用等待旋转框，默认是true
        url:getRootPath()+"genTable/findTableColumnLists.do",
        data: param,
        done:function(data){
            $.each(data,function(i,item){
                item.name=item.tableName+"."+item.name;
            });
            var setting={
                id: 'id', //新增自定义参数，同ztree的data.simpleData.idKey
                pId: 'pid', //新增自定义参数，同ztree的data.simpleData.pIdKey
                name:'name',  //新增自定义参数，同ztree的data.key.name
                radio:true, //新增自定义参数，开启radio
                //checkbox:true, //新增自定义参数，开启checkbox
                done:function(treeObj){
                    //新增自定义函数,完成初始化加载后的事件，比如可做一些反勾选操作
                    var name=genTableList.tableColumnList[index].listField;
                    if(isNotEmpty(name)){
                        $.each(data,function(i,item){
                            if(item.name==name){
                                var node = treeObj.getNodeByParam("name",item.name,null);
                                if(node!=null){
                                    treeObj.checkNode(node, true, false);
                                    treeObj.selectNode(node);
                                }
                            }
                        });
                    }
                }
                //其它具体参数请参考ztree文档
            };
            //加载ztree弹框树
            _initLayOpenZtree(setting,data,function(nodes){
                //确定事件，返回选择的nodes节点列表
                var value="";
                if(nodes.length>0){
                    value=nodes[0].name;
                }
                genTableList.tableColumnList[index].listField=value;
                $(obj).val(value);
            });
        }
    });
}

//获取外键字段
function getLeftZtree(obj){
    var param = {
    };
    // 渲染表格
    _ajax({
        type: "POST",
        //loading:true,  //是否ajax启用等待旋转框，默认是true
        url:getRootPath()+"genTableTree/getLists.do",
        data: param,
        done:function(data){
            var setting={
                id: 'id', //新增自定义参数，同ztree的data.simpleData.idKey
                pId: 'pid', //新增自定义参数，同ztree的data.simpleData.pIdKey
                name:'name',  //新增自定义参数，同ztree的data.key.name
                radio:true, //新增自定义参数，开启radio
                //checkbox:true, //新增自定义参数，开启checkbox
                done:function(treeObj){
                    //新增自定义函数,完成初始化加载后的事件，比如可做一些反勾选操作
                    var id=genTableList.leftZtreeId;
                    if(isNotEmpty(id)){
                        $.each(data,function(i,item){
                            if(item.id==id){
                                var node = treeObj.getNodeByParam("id",item.id,null);
                                if(node!=null){
                                    treeObj.checkNode(node, true, false);
                                    treeObj.selectNode(node);
                                }
                            }
                        });
                    }
                }
                //其它具体参数请参考ztree文档
            };
            setting.url=getRootPath()+"system/genTableOpenTree";
            setting.width=420;
            //加载ztree弹框树
            _initLayOpenZtree(setting,data,function(nodes){
                //确定事件，返回选择的nodes节点列表
                var value="";
                var id="";
                if(nodes.length>0){
                    value=nodes[0].name;
                    id=nodes[0].id;
                }
                genTableList.leftZtreeId=id;
                $(obj).val(value);
            });
        }
    });
}
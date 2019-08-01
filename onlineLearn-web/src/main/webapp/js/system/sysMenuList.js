/**
 * Created by huoquan on 2017/9/28.
 */
var treeObj;
var sysMenuList = avalon.define({
    $id: "sysMenuList",
    baseFuncInfo: baseFuncInfo,//底层基本方法
    menuId:"",  //记录点击的菜单id
    menuName:""  //记录点击的菜单name
});
layui.use(['index'], function(){
    avalon.ready(function () {
        getlist();
        avalon.scan();
    });
});


//获取树结点列表
function getlist(id){
    //设置左侧树的基本属性
    var opt={
        title:"菜单管理", //div树的标题
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
            url:getRootPath()+"sysMenu/list.do",
            data:param,
            done:function(data){
                var setting={
                    id: 'id', //新增自定义参数，同ztree的data.simpleData.idKey
                    pId: 'parentId', //新增自定义参数，同ztree的data.simpleData.pIdKey
                    name:'menuName',  //新增自定义参数，同ztree的data.key.name
                    //radio:true //新增自定义参数，开启radio
                    //checkbox:true, //新增自定义参数，开启checkbox
                    done:function(treeObj){
                        //新增自定义函数,完成初始化加载后的事件，比如可做一些反勾选操作
                        if(isNotEmpty(id)){
                            var node = treeObj.getNodeByParam("id",id,null);
                            if(node!=null){
                                treeObj.selectNode(node);
                            }
                        }
                    },
                    //其它具体参数请参考ztree文档
                    view: {
                        addHoverDom: addHoverDom, //用于当鼠标移动到节点上时，显示用户自定义控件
                        removeHoverDom: removeHoverDom,  //用于当鼠标移出节点时，隐藏用户自定义控件
                        selectedMulti: false  //设置是否允许同时选中多个节点。默认值: true
                    },
                    edit: {
                        enable: true,
                        showRemoveBtn: true, //设置是否显示删除按钮。[setting.edit.enable = true 时生效]
                        showRenameBtn: false, //设置是否显示编辑名称按钮。[setting.edit.enable = true 时生效]
                        drag: {
                            prev: true,
                            next: true,
                            inner: true //拖拽到目标节点时，设置是不允许成为目标节点的子节点
                        }
                    },
                    callback: {
                        beforeDrop:beforeDrop, //拖拽释放之后事件
                        onDrop:onDrop, //拖拽操作结束事件
                        beforeRemove: beforeRemove,   //删除事件
                        beforeClick:zTreeBeforeClick //单击节点事件
                    }
                };
                //初始化树,加载左侧树直接用方法的提供的obj
                treeObj=_initZtree(obj,setting,data);
            }
        });
    }
}

//根据菜单id获取按钮列表
function getBtnList(menuid){
    var param={
        "menuId":menuid
    };
    //获取layui的table模块
    var table=layui.table;
    _layuiTable({
        elem: '#sysMenuList_table', //必填，指定原始表格元素选择器（推荐id选择器）
        filter:'sysMenuList_table', ////必填，指定的lay-filter的名字
        //执行渲染table配置
        render:{
            height:'full-240', //table的高度，页面最大高度减去差值
            url: getRootPath()+"sysMenu/searchBtnList.do", // ajax的url必须加上getRootPath()方法
            where:param, //接口的参数。如：where: {token: 'sasasas', id: 123}
            cols: [[ //表头
                {fixed: 'left',type:'checkbox'}  //开启编辑框
                ,{type: 'numbers', title: '序号',width:60 }  //序号
                ,{field: 'menuName', title: '菜单名称'
                    ,templet: function(d){
                        return sysMenuList.menuName;
                    }}
                ,{field: 'keyName', title: '按钮名称'}
                ,{field: 'keyCode', title: '按钮code'}
                ,{fixed: 'right',title: '操作',width: 180, align:'center'
                    ,toolbar: '#sysMenuList_bar'}
            ]]
        },
        //监听工具条事件
        tool:function(obj,filter){
            var data = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
            var tr = obj.tr; //获得当前行 tr 的DOM对象
            if(layEvent === 'detail'){ //查看
                if(isNotEmpty(data.id)){
                    btnSaveOrEdit(data.id,true);
                }
                //do somehing
            } else if(layEvent === 'edit'){ //编辑
                //do something
                if(isNotEmpty(data.id)){
                    btnSaveOrEdit(data.id);
                }
            }else if(layEvent === 'del'){ //删除
                layer.confirm('确认删除所选纪录吗？', function(index){
                    // obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
                    layer.close(index);
                    //向服务端发送删除指令
                    if(isNotEmpty(data.id)){
                        var ids=[];
                        ids.push(data.id);
                        menuBthDel(ids);
                    }
                });
            }
        }
    })
}
/**
 * 增加，修改菜单事件
 * type:
 *   1新增节点，增加父节点时候id为空，其它时候，id为父节点
 *   2编辑 id为需要编辑的菜单id
 */
function saveOrEdit(type,id,name,readonly){
    var url="";
    var title="";
    if(type===1){
        //增加节点
        if(isEmpty(id)){  //id为空,新增操作
            title="新增一级节点";
            url=getRootPath()+"system/sysMenuEdit";
        }else{  //编辑
            title="新增子节点";
            url=getRootPath()+"system/sysMenuEdit?parentId="+id+"&parentName="+name;
        }
    }else if(type===2){
        //编辑
        if(isNotEmpty(id)){  //id为空,新增操作
            title="编辑";
            url=getRootPath()+"system/sysMenuEdit?id="+id;
        }
    }
    //sysLayerOpen系统弹框统一调用方法，done事件是定义的按钮的执行事件
    _layerOpen({
        url:url,  //弹框自定义的url，会默认采取type=2
        width:440, //弹框自定义的宽度，方法会自动判断是否超过宽度
        height:520,  //弹框自定义的高度，方法会自动判断是否超过高度
        readonly:readonly,  //弹框自定义参数，用于是否只能查看,默认是false，true代表只能查看,done事件不执行
        title:title, //弹框标题
        done:function(index,iframeWin){
            /**
             * 确定按钮的回调,说明：index是关闭弹框用的，iframeWin是操作子iframe窗口的变量，
             * 利用iframeWin可以执行弹框的方法，比如save方法
             */
            var ids = iframeWin.save(
                //成功保存之后的操作，刷新页面
                function success(data) {
                    successToast("保存成功",500);
                    var id=isEmpty(sysMenuList.menuId)?"":sysMenuList.menuId;
                    getlist(id);
                    layer.close(index); //如果设定了yes回调，需进行手工关闭
                }
            );
        }
    });
}

/**
 * 增加，修改菜单按钮事件
 */
function btnSaveOrEdit(id,readonly){
    var url="";
    var title="";
    //增加节点
    if(isEmpty(id)){  //id为空,新增操作
        title="新增";
        if(isEmpty(sysMenuList.menuId)){
            errorToast("请选择左侧菜单");
            return false;
        }
        url=getRootPath()+"system/sysKeyEdit?menuId="+sysMenuList.menuId+"&menuName="+sysMenuList.menuName;
    }else{  //编辑
        title="编辑";
        url=getRootPath()+"system/sysKeyEdit?id="+id+"&menuName="+sysMenuList.menuName;
    }
    _layerOpen({
        url:url,  //弹框自定义的url，会默认采取type=2
        width:400, //弹框自定义的宽度，方法会自动判断是否超过宽度
        height:350,  //弹框自定义的高度，方法会自动判断是否超过高度
        readonly:readonly,  //弹框自定义参数，用于是否只能查看,默认是false，true代表只能查看,done事件不执行
        title:title, //弹框标题
        done:function(index,iframeWin){
            /**
             * 确定按钮的回调,说明：index是关闭弹框用的，iframeWin是操作子iframe窗口的变量，
             * 利用iframeWin可以执行弹框的方法，比如save方法
             */
            var ids = iframeWin.save(
                //成功保存之后的操作，刷新页面
                function success(data) {
                    successToast("保存成功",500);
                    getBtnList(sysMenuList.menuId);
                    layer.close(index); //如果设定了yes回调，需进行手工关闭
                }
            );
        }
    });
}

function  getInfo(id){
    var param={
        "id":id
    };
    _ajax({
        type: "POST",
        //loading:false,  //是否ajax启用等待旋转框，默认是true
        url: getRootPath()+"sysMenu/getInfo.do",
        data:param,
        dataType: "json",
        done:function(data){
            //表单初始赋值
            var form=layui.form; //调用layui的form模块
            form.val('sysMenuEdit_form',data);
        }
    });
}

//验证表单
function verify_form($callback){
    //监听提交,先定义个隐藏的按钮
    var form=layui.form; //调用layui的form模块
    form.on('submit(sysMenuEdit_submit)', function(data){
        //通过表单验证后
        var field = data.field; //获取提交的字段
        typeof $callback === 'function' && $callback(field); //返回一个回调事件
    });
    $("#sysMenuEdit_submit").trigger('click');
}

/**
 * 关闭弹窗
 * @param $callBack  成功修改后的回调函数，比如可用作于修改后查询列表
 */
function save($callBack){  //菜单保存操作
    //对表单验证
    verify_form(function(field){
        //成功验证后
        var param=field; //表单的元素
        //可以继续添加需要上传的参数
        _ajax({
            type: "POST",
            //loading:true,  //是否ajax启用等待旋转框，默认是true
            url: getRootPath()+"sysMenu/saveOrEdit.do",
            data:param,
            dataType: "json",
            done:function(data){
                successToast("修改成功");
                var node = treeObj.getNodeByParam("id",field.id,null);
                if(node!=undefined&&node!=null){
                    //同时改树上面的菜单名称
                    node.menuName=field.menuName;
                    treeObj.updateNode(node);
                }
                if($callBack!=undefined)
                    $callBack(data);
            }
        });
    });
}

/**
 * 菜单删除
 * @param id  该菜单id
 * @param parentid  该菜单的父节点，用于展开节点
 */
function menuDel(id,$callback){
    layer.confirm('确认要删除吗？',function(index){
        var param={
            "id":id
        };
        _ajax({
            type: "POST",
            url: getRootPath()+"sysMenu/delete.do",
            data:param,
            //loading:false,  //是否ajax启用等待旋转框，默认是true
            dataType: "json",
            done: function(data){
                typeof $callback === 'function' && $callback(data);
                successToast("菜单删除成功",500);
            }
        });
    });
}

//删除按钮
function menuBthDel(ids){
    var param={
        "ids":ids
    };
    _ajax({
        type: "POST",
        url: getRootPath()+"sysMenu/menuBthDel.do",
        data:param,
        //loading:false,  //是否ajax启用等待旋转框，默认是true
        dataType: "json",
        done: function(data){
            successToast("按钮删除成功",500);
            getBtnList(sysMenuList.menuId);
        }
    });
}

/**
 * 批量删除
 */
function batchDel(){
    var table = layui.table; //获取layui的table模块
    var checkStatus = table.checkStatus('sysMenuList_table'); //test即为基础参数id对应的值
    //console.log(checkStatus.data);//获取选中行的数据
    //console.log(checkStatus.data.length);//获取选中行数量，可作为是否有选中行的条件
    //console.log(checkStatus.isAll );//表格是否全选
    var data=checkStatus.data;
    if(data.length==0){
        warningToast("请至少选择一条纪录");
        return false;
    }else{
        layer.confirm('确认删除所选纪录吗？', function(index){
            layer.close(index);
            var ids=[];
            $.each(data,function(i,item){
                ids.push(item.id);
            });
            menuBthDel(ids);
        });
    }
}
/**
 * 菜单排序
 * @param type 0上移  1下移
 * @param id 当前的菜单id
 */
function menuSort(ids,parentId) {
    if(isEmpty(ids)||ids.length==0){
        return false;
    }
    var param={
        ids:ids,
        parentId:parentId
    };
    _ajax({
        type: "POST",
        url: getRootPath()+"sysMenu/sort.do",
        data:param,
        //loading:false,  //是否ajax启用等待旋转框，默认是true
        dataType: "json",
        done: function(data){
            successToast("排序成功");
            // var id=isEmpty(data.id)?"":data.id;
            // getlist(id);
        }
    });
}


//ztree事件：单击节点事件
function zTreeBeforeClick(treeId, treeNode, clickFlag) {
    var zTree = $.fn.zTree.getZTreeObj(treeId);
    if(zTree.isSelectedNode(treeNode)){
        zTree.cancelSelectedNode(treeNode);
    }else {
        sysMenuList.menuId=treeNode.id;
        sysMenuList.menuName=treeNode.menuName;
        zTree.selectNode(treeNode);
        getInfo(treeNode.id);
        getBtnList(treeNode.id);
    }
    return false;
}
//ztree事件：拖拽释放之后执行
function beforeDrop(treeId, treeNodes, targetNode, moveType, isCopy) {
    var moveNode = treeNodes[0];
    // if(moveNode.level===targetNode.level){
    //     if(moveNode.parentTId==targetNode.parentTId){
    //         return true;
    //     }
    // }else{
    //     if(targetNode.level==0){
    //         warningToast("根节点");
    //     }
    // }
    //warningToast("只能在本节点同级中排序");
    return true;
}
//ztree事件：拖拽操作结束事件
function onDrop(event, treeId, treeNodes, targetNode, moveType, isCopy){
    var moveNode = treeNodes[0];
    var pnode =moveNode.getParentNode();
    var parentId="";
    var peerNodes=[];
    if(pnode!=null){
        peerNodes=pnode.children;
        parentId=pnode.id;
    }else{
        //返回根节点集合
        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        peerNodes = treeObj.getNodesByFilter(function (node) { return node.level == 0 })
    }
    if(peerNodes!=undefined&&peerNodes.length>0){
        var ids=[];
        $.each(peerNodes,function(i,item){
            console.log(item.menuName);
            ids.push(item.id);
        });
        //排序功能
        menuSort(ids,parentId);
    }
}
//ztree事件：删除事件
function beforeRemove(treeId, treeNode) {
    var zTree = $.fn.zTree.getZTreeObj(treeId);
    zTree.selectNode(treeNode);
    menuDel(treeNode.id,function(){
        //成功删除后
        zTree.removeNode(treeNode);
    });
    return false;
}
//ztree事件：增加按钮事件
function addHoverDom(treeId, treeNode) {
    var sObj = $("#" + treeNode.tId + "_span");
    if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
    var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
        + "' title='增加' onfocus='this.blur();'></span>";
    sObj.after(addStr);
    var btn = $("#addBtn_"+treeNode.tId);
    if (btn) btn.bind("click", function(){
        saveOrEdit(1,treeNode.id,treeNode.menuName);
        return false;
    });
}
//ztree事件：解除增加事件
function removeHoverDom(treeId, treeNode) {
    $("#addBtn_"+treeNode.tId).unbind().remove();
}
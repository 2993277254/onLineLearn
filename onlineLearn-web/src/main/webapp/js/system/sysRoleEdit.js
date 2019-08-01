/**
 * sysRoleEdit.jsp的js文件，包括查询，编辑操作
 */
var ztreeObj;
var muneList=[];
var sysRoleEdit = avalon.define({
    $id: "sysRoleEdit",
    baseFuncInfo: baseFuncInfo,//底层基本方法
    id:"",
    type:function () {
       var arr=sysRoleEdit.baseFuncInfo.getSysDictByCode('sys_user_type_');
       var newArr=[];
       $.each(arr,function (i,item) {
           if (item.value==1||item.value==2)
           newArr.push(item);
       });
       return newArr;
    }
});

layui.use(['index'],function(){
    //加载layui的模块，index模块是基础模块，也可添加其它
    avalon.ready(function () {
        var util = layui.util;
        var form = layui.form;
        //执行
        util.fixbar({});
        form.on('submit(sysOpenTree_search)', function(data){
            //搜索事件
            ztreeObj.searchTree(data.field.searchnName);
        });
        //所有的入口事件写在这里...
        var id=GetQueryString("id");  //接收变量
        sysRoleEdit.id=id;
        //获取实体信息
        getInfo(id,function(data){
            //若为修改时，查询权限关联表里已有的权限
            muneList=[];
            muneList=muneList.concat(data.ids);//连接数组
            selectMenu();//初始化菜单树
        });
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
            "id":id
        };
        _ajax({
            type: "POST",
            //loading:false,  //是否ajax启用等待旋转框，默认是true
            url: getRootPath()+"sysRole/getInfo.do",
            data:param,
            dataType: "json",
            done:function(data){
                //表单初始赋值
                var form=layui.form; //调用layui的form模块
                form.val('sysRoleEdit_form', data);
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
    form.on('submit(sysRoleEdit_submit)', function(data){
        //通过表单验证后
        var field = data.field; //获取提交的字段
        typeof $callback === 'function' && $callback(field); //返回一个回调事件
    });
    $("#sysRoleEdit_submit").trigger('click');
}

/**
 * 关闭弹窗
 * @param $callBack  成功修改后的回调函数，比如可用作于修改后查询列表
 */
function save($callback){  //菜单保存操作
    //对表单验证
    verify_form(function(field){
        //成功验证后
        //确定事件，获取全部勾选的
        var nodes= ztreeObj.getCheckedList();
        var ids=[];

        if (nodes.length>0){
            for (var i=0;i<nodes.length;i++) {
                ids.push(nodes[i].id);
            }
        }else {
            warningToast("请选择右侧菜单权限");
            return false;
        }
        //debugger;
        //var param=field; //表单的元素
        //可以继续添加需要上传的参数
        var param={
            "id":field.id,
            "identity":field.identity,
            // "roleName":field.roleName,
            "remark":field.remark,
            "status":field.status,
            "ids":ids
        };
        _ajax({
            type: "POST",
            //loading:true,  //是否ajax启用等待旋转框，默认是true
            contentType: "application/json; charset=utf-8", //此设置后台可接受复杂参数，后台接收需要@RequestBody标签
            url: getRootPath()+"sysRole/saveOrEdit.do",
            data:JSON.stringify(param), //此设置后台可接受复杂参数
            dataType: "json",
            done:function(data){
                typeof $callback === 'function' && $callback(data); //返回一个回调事件
            }
        });

    });
}

/**
 * 查询菜单树
 * */
function selectMenu(){
    var param = {};
    // 渲染表格
    _ajax({
        type: "POST",
        //loading:true,  //是否ajax启用等待旋转框，默认是true
        url:getRootPath()+"sysRole/selectMenu.do",
        data: param,
        done:function(data){
            //编辑树时还有一种方法初始化已有权限
            var setting={
                id: 'id', //新增自定义参数，同ztree的data.simpleData.idKey
                pId: 'parentId', //新增自定义参数，同ztree的data.simpleData.pIdKey
                name:'menuName',  //新增自定义参数，同ztree的data.key.name
                //radio:true //新增自定义参数，开启radio
                checkbox:true, //新增自定义参数，开启checkbox
                done:function(treeObj){
                    //如果拥有的id不为空，则与全部菜单对比，
                    if(muneList!=null&&muneList.length>0){
                        $.each(data,function(i,item) {
                            $.each(muneList,function(i1,item1) {
                                if (item.id==item1) {
                                    var node = treeObj.getNodeByParam("id", item.id, null);
                                    if (node != null) {
                                        treeObj.checkNode(node, true, false);//加载勾选
                                        treeObj.selectNode(node);//选中展开
                                    }
                                }
                            });
                        });
                    }
                }
                //其它具体参数请参考ztree文档
            };
            //加载ztree树
            ztreeObj=_initZtree($("#sysOpenTree"),setting,data);

        }
    });
}


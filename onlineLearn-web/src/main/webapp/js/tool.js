/**
 * Created by huoquan on 2018/8/14.
 * 封装好的工具js方法，其它人请勿轻易动此文件,此文件以后会直接覆盖更新
 */

//获取项目根路径
function getRootPath(){
    var strFullPath=window.document.location.href;
    var strPath=window.document.location.pathname;
    var pos=strFullPath.indexOf(strPath);
    var prePath=strFullPath.substring(0,pos);
    var postPath=strPath.substring(0,strPath.substr(1).indexOf('/')+1);
    if(postPath === '/onLineLearn-web')
        postPath = '/onLineLearn-web';
    else if(postPath === '/onLineLearn')
        postPath ='/onLineLearn';
    else
        postPath ='';
    var basePath=getStorage("KEY_BASEPATH");
    //获取根路径
    var path;
    if(isNotEmpty(basePath)){
        path=prePath+basePath+"/";
    }else{
        path = prePath+postPath+"/";
    }
    return path;
}

/**
 * 获取数据字典
 * @param code 数据字典的code
 * @param addEmpty true 或者false； 是否加入空的一项（即“{value:"",name:"全部"}”），多用于搜索下拉框，默认false
 * @returns {Array}
 */
function getSysDictByCode(code,addEmpty) {
    var dicts=[];
    if(isNotEmpty(addEmpty)&&addEmpty){
        dicts.push({value:"",name:"全部"});
    }
    if(isNotEmpty(code)){
        try{
            var list=eval("("+getStorage("KEY_SYSDICT")+")")||{};//eval方法解析json
            if(list[code]!=null){
                dicts=dicts.concat(list[code]);

            }
        }catch(err){}
    }
    return dicts;
}

/**
 * 获取数据字典的名称，专用于列表显示
 * @param code
 * @param value
 */
function getSysDictName(code,value) {
    var name="";
    var dicts=[];
    if(isNotEmpty(code)){
        try{
            var list=eval("("+getStorage("KEY_SYSDICT")+")")||{};
            if(list[code]!=null){
                dicts=list[code];
            }
        }catch(err){}
    }
    if(isNotEmpty(value)){
        if(dicts.length>0){
            if(value.indexOf(",")>-1){
                //列表形式
                var vals=value.split(",");
                var names=[];
                $.each(vals,function(i1,val){
                    $.each(dicts,function(i,item){
                        if(item.value==val){
                            names.push(item.name);
                            return true;
                        }
                    });
                });
                name=names.join(",");
            }else{
                $.each(dicts,function(i,item){
                    if(item.value==value){
                        name=item.name;
                        return true;
                    }
                })
            }
        }
    }
    return name;
}

/**
 * 隐藏ztree的div
 */
function _hideZreeDiv(type){
    if(isNotEmpty(type)){
        if(type==="auto"){
            //自动调整
            if($(window).width()<900){
                if(!$(".tree-side-div").hasClass("tree-hide-div")){
                    $(".tree-side-div").addClass("tree-hide-div");
                }
            }
            if($(window).width()>=1100){
                $(".tree-side-div").removeClass("tree-hide-div");
            }
        }
    }else{
        if($(".tree-side-div").hasClass("tree-hide-div")){
            $(".tree-side-div").removeClass("tree-hide-div");
        }else{
            $(".tree-side-div").addClass("tree-hide-div");
        }
    }
}

/**
 * 弹框打开图片预览
 * @param src 图片的真实路径
 */
imagesOpen=function(src){
    layui.use(['layer'], function() {
        layer.photos({
            photos: {
                "title": "查看图片" //相册标题
                ,"data": [{
                    "src": src //原图地址
                }]
            }
            ,shade: 0.01
            ,closeBtn: 1
            ,anim: 5
        });
    });
};

//系统定义的错误框
errorToast=function(msg,time){
    layui.use(['layer'], function() {
        var layer = layui.layer;
        var t=1500;
        if(time){
            t= time;
        }
        layer.msg(msg, {
            icon: 2,
            time: t,
            shade: [0.2,'#838B83'] //0.1透明度的白色背景
        });
    });
}

//系统定义的成功框
successToast=function(msg,time){
    layui.use(['layer'], function() {
        var layer = layui.layer;
        var t=1000;
        if(time){
            t= time;
        }
        layer.msg(msg, {
            icon: 1,
            time: t,
            shade: [0.2,'#838B83'] //0.1透明度的白色背景
        });
    });
}

//系统定义的警告框
warningToast=function(msg,time){
    layui.use(['layer'], function() {
        var layer = layui.layer;
        var t=1000;
        if(time){
            t= time;
        }
        layer.msg(msg, {
            icon: 7,
            time: t,
            shade: [0.2,'#838B83'] //0.1透明度的白色背景
        });
    });
}

function showMoreCondition(obj){
    if($(obj).hasClass("condition-open")){
        $(obj).removeClass("condition-open");
        $("#condition_more").slideUp();
        $(obj).html('<cite>更多筛选条件</cite><span class="layui-icon layui-icon-down condition-icon"></span>');
    }else{
        $(obj).addClass("condition-open");
        $("#condition_more").slideDown();
        $(obj).html('<cite>收起筛选条件</cite><span class="layui-icon layui-icon-up condition-icon"></span>');
    }
}

/**
 * 重新封装ztree
 * @param opt
 */
_initZtree=function(obj, opt, zNodes){
    var zTreeObj;
    layui.use(['layer'], function() {
        var layer=layui.layer;
        //重新定义一些常用的属性
        opt.id = opt.id || ''; //新增自定义参数，同ztree的data.simpleData.idKey
        opt.pId = opt.pId || ''; //新增自定义参数，同ztree的data.simpleData.pIdKey
        opt.name = opt.name || ''; //新增自定义参数，同ztree的data.key.name
        opt.checkbox = opt.checkbox || false; //新增自定义参数，开启checkbox
        opt.radio = opt.radio || false; //新增自定义参数，开启radio
        var check={};
        var view={};
        var callback={};
        if(opt.checkbox){
            check.enable=true;
            check.chkStyle="checkbox";
            check.chkboxType={ "Y": "ps", "N": "ps" };//关联父子节点
            view.showIcon=false;
            //定义默认的beforeClick事件
            callback.beforeClick = function (treeId, treeNode, clickFlag) {
                var treeObj = $.fn.zTree.getZTreeObj(treeId);
                treeObj.checkNode(treeNode, !treeNode.checked, true, true);
                return true;
            };
        }
        if(opt.radio){
            check.enable=true;
            check.chkStyle="radio";
            check.radioType="all";
            view.showIcon=false;
            //定义默认的beforeClick事件
            callback.beforeClick = function (treeId, treeNode, clickFlag) {
                var treeObj = $.fn.zTree.getZTreeObj(treeId);
                treeObj.checkNode(treeNode, !treeNode.checked, false, true);
                return true;
            };
        }
        //data默认配置
        var data={
            key:{},
            simpleData:{
                enable: true
            }
        };
        if(isNotEmpty(opt.id)){
            data.simpleData.idKey=opt.id;
        }
        if(isNotEmpty(opt.pId)){
            data.simpleData.pIdKey=opt.pId;
        }
        if(isNotEmpty(opt.name)){
            data.key.name=opt.name;
        }
        var setting=$.extend(true,{
                data: data,
                check: check,
                view: view,
                callback:callback
            },opt);
        zTreeObj= $.fn.zTree.init(obj, setting, zNodes);
        typeof opt.done === 'function' && opt.done(zTreeObj);
    });
    //获取勾选的记录
    zTreeObj.getCheckedList = function () {
        var treeObj = zTreeObj;
        var nodes = treeObj.getCheckedNodes(true);//获取勾选的节点
        return nodes;
    };
    //查询方法
    zTreeObj.searchTree = function (searchStr) {
        var treeObj = zTreeObj;
        var objs = treeObj.transformToArray(treeObj.getNodes());
        treeObj.expandAll(false);
        treeObj.refresh();  //特殊处理，刷新重置tree，
        var highlight=false;
        for (var j = 0; j < objs.length; j++) {
            if(objs[j].highlight){
                highlight=true;
            }
        }
        if (isNotEmpty(searchStr)) {
            var nodes = treeObj.getNodesByParamFuzzy(opt.name, searchStr, null);
            if (nodes.length === 0) {
                warningToast("没有搜索到记录", 1000);
            }
            var isSelect=false;
            for (var i = 0; i < nodes.length; i++) {
                if(!highlight&&i===0){
                    treeObj.selectNode(nodes[i]);
                    nodes[i].highlight = true;
                    isSelect=true;
                }else{
                    //将搜索到的节点展开
                    if(nodes[i].highlight){
                        nodes[i].highlight = false;
                        if(i+1<nodes.length){
                            nodes[i+1].highlight = true;
                            treeObj.selectNode(nodes[i+1]);
                            isSelect=true;
                            break;
                        }
                    }
                }
            }
            if(nodes.length>0&&!isSelect){
                treeObj.selectNode(nodes[0]);
                nodes[0].highlight = true;
            }
        }
    };
    return zTreeObj;
};

/**
 * 封装弹框的ztree
 * @param obj
 * @param opt
 * @param zNodes
 * @returns {*}
 */
_initLayOpenZtree=function(opt, zNodes,$callback){
    var layerOpen;
    layui.use(['layer'], function() {
        opt.url=opt.url||getRootPath()+"system/sysOpenTree";
        opt.width=opt.width||350;
        opt.height=opt.height||450;
        opt.title=opt.title||"请选择";
        var url=opt.url;
        var width=opt.width;
        var height=opt.height;
        var title=opt.title;
        delete opt.url;
        delete opt.width;
        delete opt.height;
        delete opt.title;
        layerOpen=_layerOpen({
            url:url,  //弹框自定义的url，会默认采取type=2
            width:width, //弹框自定义的宽度，方法会自动判断是否超过宽度
            height:height,  //弹框自定义的高度，方法会自动判断是否超过高度
            title:title, //弹框标题
            success: function(layero, index){
                var body = layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];//得到iframe页的窗口对象，执行iframe页的方法：
                iframeWin.initSysZtree(opt,zNodes);
            },
            done:function(index,iframeWin){
                /**
                 * 确定按钮的回调,说明：index是关闭弹框用的，iframeWin是操作子iframe窗口的变量，
                 * 利用iframeWin可以执行弹框的方法，比如save方法
                 */
                var ids = iframeWin.getCheckedList(
                    function success(data) {
                        typeof $callback=== 'function' && $callback(data);
                        layer.close(index); //如果设定了yes回调，需进行手工关闭
                    }
                );
            }
        })
    });
    return layerOpen;
};

function hideZtree(){
    _hideZreeDiv();
    //延迟执行，为的是table能自适应宽度
    setTimeout(function(){
        var table=layui.table;
        $.each(table.cache, function (key, value) {
            if(isNotEmpty(key)){
                table.resize(key);
            }
        });
    },300);
}

/**
 * 初始化左侧树
 * 参数：
 *  opt.title 树标题
 *  opt.width 树的div默认230
 *  opt.isHide 默认true 是否开启隐藏树
 *  opt.isSearch 默认true 是否开启搜索框
 *  opt.onSearch 搜索事件
 *
 */
function _initLeftZtree(obj, opt, zNodes){
    var treeId="treeDemo"; //树的节点
    //搜索事件
    var searchZtree=function(){
        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        treeObj.searchTree($("#tree_search_name").val());
    };
    opt.title=opt.title||''; //树的title
    opt.width=opt.width|| 230;//左侧的宽度
    opt.isHide = opt.isHide==undefined || opt.isHide==null?true:opt.isHide; //是否开启隐藏树
    opt.isSearch = opt.isSearch==undefined || opt.isSearch==null?true:opt.isSearch; //是否开启搜索框
    opt.onSearch = opt.onSearch||searchZtree; //是否开启搜索框
    var html="";
    html+='<div class="tree-side-title">'+
        '        <span>'+opt.title+(opt.isSearch?'<span class="fly-search LAY_search"><i class="layui-icon"></i></span>':'')+'</span>';
    //收缩按钮
    if(opt.isHide){
        html+='   <a href="javascript:;" onclick="hideZtree();">'+
            '        <i class="layui-icon tree-side-hide layui-icon-triangle-r"></i>'+
            '     </a>';
    }
    html+=' </div>';
    //ztree树
    html+='   <div class="layui-fluid">'+
        '        <div class="layui-card">'+
        '            <ul id="treeDemo" class="ztree"></ul>'+
        '        </div>'+
        '    </div>';
    obj.css("margin-top","55px");
    obj.addClass('layui-side');
    obj.addClass('tree-side-div');
    if($(window).width()<900){
        obj.addClass("tree-hide-div");
    }
    obj.width(opt.width);
    if(!obj.next().hasClass("layui-body")){
        obj.next().wrapAll('<div class="layui-body" style="left:'+(opt.width+15)+"px"+'"></div>');
    }
    obj.html(html);
    //搜索事件
    if(opt.isSearch){
        $(".fly-search").click(function(){
            _layerOpen({
                type: 1,
                width:350,
                height:80,
                closeBtn:0,
                shadeClose:true,
                fixed:false,
                scrollbar:false,
                title:false,
                maxmin:false,
                content:'<div class="layui-form" style="padding-top:20px;padding-left: 20px;">'+
                '        <div class="layui-inline">'+
                '            <input type="text" id="tree_search_name" placeholder="关键字搜索" autocomplete="off" class="layui-input" style="width: 250px;">'+
                '        </div>'+
                '        <button class="layui-btn" style="margin-left: -5px;" id="leftTree_search"><i class="layui-icon layui-icon-search"></i></button>'+
                '</div>',
                success:function(){
                    $("#leftTree_search").click(opt.onSearch);
                }
            });
        });
    }
    if(opt.isHide){
        $(window).resize(function(){
            setTimeout(function(){
                _hideZreeDiv("auto");
            }, 100);
        });
    }
    typeof opt.initZtree === 'function' && opt.initZtree($("#"+treeId));
}

/**
 * 封装搜索条件方法
 * @param opt
 * search:搜索事件
 * done：表单加载完回调事件
 */
_initSearch=function(opt){
    layui.use(['form','laydate'], function() {
        var form = layui.form;
        var laydate = layui.laydate;
        var limit=3;
        opt.elem=opt.elem||''; //elem: '#layui_form_condition' //指定搜索框表单的元素选择器（推荐id选择器）
        opt.filter=opt.filter||'';//指定的lay-filter
        var width=$(opt.elem).width(); //获取页面宽度,以便确定页面最多显示多少个条件
        if(width===0){
            width=$(window).width();
        }
        if(width>=1500){
            limit=4;
        }else if(width>=1200&&width<1500){
            limit=3;
        }else if(width>=900&&width<1200){
            limit=2;
        }else{
            limit=1;
        }
        if(isEmpty(opt.limit)){
            opt.limit=limit;
        }
        opt.conds=opt.conds||[];//condition的列表
        if(isNotEmpty(opt.elem)){
            var conditionArr=[]; //用于存储每个搜索条件的html字符串
            var timeArr=[]; //用于存储日期时间的集合
            var valueMap={}; //获取初始化值
            $.each(opt.conds,function(i,item){
                if(item==undefined||item==null){
                    return true;
                }
                var conditionStr="";
                item.field = item.field || ''; //必填，字段名字，用作于搜索条件返回的key
                item.title = item.title || ''; //搜索框名称
                item.placeholder = item.placeholder || ''; //搜索框提示
                item.type = item.type || 'input'; //搜索框类型，默认是input，还有select、time（时间）、radio
                item.templet = item.templet || ''; //搜索框条件模板
                item.data = item.data || []; //数据，只有select才有，格式{name:'选项名字',value:'选项值'}
                item.value = item.value || ''; //必填，字段名字，用作于搜索条件返回的key
                if(isNotEmpty(item.value)){
                    if(item.type==="date_range"||item.type==="datetime_range"||item.type==="time_range"){
                        //会有2个值
                        item.value1 = item.value1 || ''; //必填，字段名字，用作于搜索条件返回的key
                        valueMap[item.field+'_begin']=item.value; //存初始化值
                        valueMap[item.field+'_end']=item.value1; //存初始化值
                    }else{
                        valueMap[item.field]=item.value; //存初始化值
                    }
                }
                if(item.type==="checkbox"||item.type==="radio"
                    ||item.type==="date_range"||item.type==="datetime_range"||item.type==="time_range"){
                    conditionStr+=
                        '<div class="layui-inline">'+
                        '   <label class="layui-form-label">'+item.title+'</label>'+
                        '   <div class="layui-input-block">';
                }else{
                    conditionStr+=
                        '<div class="layui-inline" style="width:300px;">'+
                        '   <label class="layui-form-label">'+item.title+'</label>'+
                        '   <div class="layui-input-block">';
                }
                if(isNotEmpty(item.templet)){
                    //如果有模板，直接填充模板
                    conditionStr+=item.templet;
                }else{
                    if(item.type==="input"){
                        if(isEmpty(item.placeholder)){
                            item.placeholder='请输入';
                        }
                        conditionStr+= '<input type="text" name="'+item.field+'" placeholder="'+item.placeholder+'" ' +
                            'autocomplete="off" class="layui-input">';
                    }else if(item.type==="select"){
                        conditionStr+='<select name="'+item.field+'">';
                        $.each(item.data,function(i1,item1){
                            if(item1==undefined||item1==null){
                                return true;
                            }
                            conditionStr+='<option value="'+item1.value+'">'+item1.name+'</option>';
                        });
                        conditionStr+='</select>';
                    }else if(item.type==="date"||item.type==="datetime"||item.type==="time"){
                        item.format=item.format||'';
                        if(isEmpty(item.placeholder)){
                            if(item.type==="date"){
                                item.placeholder='yyyy-MM-dd';
                            }else if(item.type==="datetime"){
                                item.placeholder='yyyy-MM-dd HH:mm:ss';
                            }else if(item.type==="time"){
                                item.placeholder='HH:mm:ss';
                            }
                            if(isNotEmpty(item.format)){
                                item.placeholder=item.format;
                            }
                        }
                        conditionStr+= '<input type="text" name="'+item.field+'" placeholder="'+item.placeholder+'" ' +
                            'id="'+item.field+'" class="layui-input">';
                        var timeitem={
                            elem:'#'+item.field,
                            type:item.type
                        };
                        if(isNotEmpty(item.format)){
                            timeitem.format=item.format;
                        }
                        timeArr.push(timeitem);
                    }else if(item.type==="date_range"||item.type==="datetime_range"||item.type==="time_range"){
                        //时间范围选择
                        item.format=item.format||'';
                        if(isEmpty(item.placeholder)){
                            if(item.type==="date_range"){
                                item.placeholder='yyyy-MM-dd';
                            }else if(item.type==="datetime_range"){
                                item.placeholder='yyyy-MM-dd HH:mm:ss';
                            }else if(item.type==="time_range"){
                                item.placeholder='HH:mm:ss';
                            }
                            if(isNotEmpty(item.format)){
                                item.placeholder=item.format;
                            }
                        }
                        //开始时间
                        conditionStr+= '<div class="layui-input-inline">' +
                            '<input type="text" name="'+item.field+'_begin'+'" placeholder="'+item.placeholder+'" ' +
                            'id="'+item.field+'_begin'+'" class="layui-input">';
                        conditionStr+='</div>';
                        var time_begin={
                            elem:'#'+item.field+'_begin',
                            type:item.type.replace("_range","")
                        };
                        if(isNotEmpty(item.format)){
                            time_begin.format=item.format;
                        }
                        timeArr.push(time_begin);
                        //结束时间
                        conditionStr+= '<div class="layui-form-mid layui-word-aux"> - </div>';
                        conditionStr+= '<div class="layui-input-inline">' +
                             '<input type="text" name="'+item.field+'_end'+'" placeholder="'+item.placeholder+'" ' +
                            'id="'+item.field+'_end'+'" class="layui-input">';
                        conditionStr+='</div>';
                        var time_end={
                            elem:'#'+item.field+'_end',
                            type:item.type.replace("_range","")
                        };
                        if(isNotEmpty(item.format)){
                            time_end.format=item.format;
                        }
                        timeArr.push(time_end);
                    }else if(item.type==="checkbox"){
                        $.each(item.data,function(i1,item1){
                            if(item1==undefined||item1==null){
                                return true;
                            }
                            conditionStr+='<input type="checkbox" name="'+(item.field+'_'+item1.value)+'" lay-skin="primary" title="'+item1.name+'">';
                        });
                    }else if(item.type==="radio"){
                        $.each(item.data,function(i1,item1){
                            if(item1==undefined||item1==null){
                                return true;
                            }
                            conditionStr+='<input type="radio" name="'+item.field+'" value="'+item1.value+'" title="'+item1.name+'">';
                        });
                    }
                }
                conditionStr+='</div>'+
                    '</div>';
                conditionArr.push(conditionStr);
            });
            //开始处理筛选条件，根据limit是否出现收缩或者展开
            var htmlStr="";
            if(conditionArr.length>opt.limit&&opt.limit>0){
                htmlStr+='<div class="layui-form-item" style="margin-bottom: 0px;">';
                htmlStr+=(conditionArr.slice(0,opt.limit)).join("");
                htmlStr+=
                    '<div class="layui-inline">'+
                    '   <button class="layui-btn layuiadmin-btn-useradmin" lay-submit lay-filter="'+opt.filter+'_search">'+
                    '   <i class="layui-icon layui-icon-search layuiadmin-button-btn"></i>'+
                    '   </button>'+
                    '   <a href="javascript:;" class="pl-10" onclick="showMoreCondition(this)" id="moreCondit">' +
                    '       <cite>更多筛选条件</cite><span class="layui-icon layui-icon-down condition-icon"></span>' +
                    '   </a>'+
                    '</div>';
                htmlStr+='</div>';

                htmlStr+='<div class="layui-form-item" style="display:none;" id="condition_more">';
                htmlStr+=(conditionArr.slice(opt.limit)).join("");
                htmlStr+='</div>';
            }else{
                htmlStr+='<div class="layui-form-item">';
                htmlStr+=conditionArr.join("");
                htmlStr+=
                    '<div class="layui-inline">'+
                    '   <button class="layui-btn layuiadmin-btn-useradmin" lay-submit lay-filter="'+opt.filter+'_search">'+
                    '   <i class="layui-icon layui-icon-search layuiadmin-button-btn"></i>'+
                    '   </button>'+
                    '</div>';
                htmlStr+='</div>';
            }
            $(opt.elem).html(htmlStr);
        }
        //完成html载入，可进行一些插件方法的初始化事件
        typeof opt.done=== 'function' && opt.done(opt.filter,valueMap);
        //常规用法,填充时间
        $.each(timeArr,function(i,item){
            laydate.render(item);
        });
        //刷新表单
        form.render();
        //表单初始赋值
        form.val(opt.filter,valueMap);
        //监听搜索
        form.on('submit('+opt.filter+'_search)', function(data){
            //返回信息
            var field = data.field;
            $.each(opt.conds,function(i,item){
                if(item==undefined||item==null){
                    return true;
                }
                if(item.type==="checkbox"){
                    //复选框特殊处理一下，自动返回返回已选列表
                    var checkboxArr=[];
                    $.each(item.data,function(i1,item1){
                        if(item1==undefined||item1==null){
                            return true;
                        }
                        if(isNotEmpty(field[item.field+"_"+item1.value])&&field[item.field+"_"+item1.value]==="on"){
                            checkboxArr.push(item1.value);
                        }
                    });
                    if(checkboxArr.length>0){
                        field[item.field]=checkboxArr.join(",");
                    }
                }
            });
            typeof opt.search=== 'function' && opt.search(data);
        });
    });
};

/**
 * 重新封装jq的ajax方法
 * @param opt
 */
_ajax=function(opt){
    layui.use(['layer'], function() {
        var layer=layui.layer;
        opt.data = opt.data || {};
        opt.headers = opt.headers || {};
        opt.loading = opt.loading==undefined || opt.loading==null?true:opt.loading; //是否开启loading
        opt.loadMsg = opt.loadMsg||'数据请求中'; //是否开启loading的信息
        var loadlayer=null;
        showLoading=function(){
            loadlayer=layer.msg(opt.loadMsg, {icon: 16,shade: 0, time: 1000000});
        };
        hideLoading=function(){
            if(loadlayer){
                layer.close(loadlayer);
            }
        };
        var beforeSend = opt.beforeSend;
        var success = opt.success;
        var error = opt.error;
        delete opt.beforeSend;
        delete opt.success;
        delete opt.error;
        return $.ajax($.extend({
             dataType: 'json'
            ,beforeSend:function(xhr){
                if(opt.loading){
                    showLoading();
                }
                typeof beforeSend === 'function' && beforeSend(xhr);
            }
            ,success: function(res){
                if(opt.loading){
                    hideLoading();
                }
                //只有 response 的 code 一切正常才执行 done
                if(res.rtnCode == 0) {
                    typeof opt.done === 'function' && opt.done(res.bizData);
                }
                //登录状态失效，清除本地 access_token，并强制跳转到登入页
                else if(res.rtnCode == 100){
                    try {
                        //询问框
                        layer.confirm('未登录或登录超时。请重新登录，谢谢', {
                            btn: ['确定'] //按钮
                        }, function () {
                            if (window.top.location.href != location.href) {
                                window.top.location.href = getRootPath() + "logout.do";
                            } else {
                                window.location.href = getRootPath() + "logout.do";
                            }
                        });
                    } catch (err) {
                    }
                }
                //其它异常
                else {
                    var error = [
                        '<cite>Error：</cite> ' + (res.msg || '返回状态码异常')
                    ].join('');
                    errorToast(error);
                }
                //只要 http 状态码正常，无论 response 的 code 是否正常都执行 success
                typeof success === 'function' && success(res);
            }
            ,error: function(e, code){


                //console.log("异常的状态="+);
                if(opt.loading){
                    hideLoading();
                }
                var error = [
                    '请求异常，请重试<br><cite>错误信息：</cite>'+ code
                ].join('');
                errorToast(error);
                typeof error === 'function' && error(e);
            }
        }, opt));
    });
};
/**
 * 重新封装upload组件
 * @param opt
 */
//创建监听函数
var xhrOnProgress=function(fun) {
    xhrOnProgress.onprogress = fun; //绑定监听
    //使用闭包实现监听绑
    return function() {
        //通过$.ajaxSettings.xhr();获得XMLHttpRequest对象
        var xhr = $.ajaxSettings.xhr();
        //判断监听函数是否为函数
        if (typeof xhrOnProgress.onprogress !== 'function')
            return xhr;
        //如果有监听函数并且xhr对象支持绑定时就把监听函数绑定上去
        if (xhrOnProgress.onprogress && xhr.upload) {
            xhr.upload.onprogress = xhrOnProgress.onprogress;
        }
        return xhr;
    }
}
_layuiUpload=function (opt){
    var uploadObj;
    var tips;
    var layer=layui.layer;
    layui.use(['upload'], function() {

        opt.showImgDiv = opt.showImgDiv || ''; //自定义字段，可选，用来显示上传后的图片的div
        opt.showHttpPath = opt.showHttpPath || ''; //自定义字段，可选，用于拼接显示图片的http映射路径，比如http:192.168.1.126:8081
        opt.showImgSrc = opt.showImgSrc || ''; //自定义字段，可选，在div显示图片的src，通常用于编辑后的回显，相对路径，比如‘/upload/XXX/XXX.jpg’
        //显示图片
        var showImage = function (url) {
            $(opt.showImgDiv).html('<img  class="layui-upload-img"' +
                'src="' + opt.showHttpPath + url + '">' +
                '<i class="layui-icon layui-upload-close">&#x1006;</i>');
            //赋值
            $(opt.elem).prev().val(url);
            if (isNotEmpty(url)){
                $(opt.elem).find("i").removeClass("layui-icon-add-circle-fine").addClass("iconfont icon-newupload").text("重新选择");
            }
            //提示可以点击查看原图
            $(opt.showImgDiv + ' .layui-upload-img').hover(function () {
                //, {
                //                     tips: [1, '#3595CC'],
                //                     time: 1000
                //                 }
                tips=layer.tips('点击我查看原图',this);
            },function () {
                layer.close(tips);
            });
            //预览图片
            $(opt.showImgDiv + ' .layui-upload-img').on('click', function () {
                parent.imagesOpen(opt.showHttpPath + url);
            });
            //删除图片
            $(opt.showImgDiv + ' .layui-upload-close').on('click', function () {
                $(opt.elem).prev().val("");
                $(opt.showImgDiv).html("");
                $(opt.elem).find("i").removeClass("iconfont icon-newupload").addClass("layui-icon-add-circle-fine").text("上传图片");
            });
        };
        var loadlayer = null;
        var showLoading = function () {
            loadlayer = layer.msg("上传中...", {
                icon: 16,
                shade: 0,
                time: 1000000
            });
        };
        var hideLoading = function () {
            if (loadlayer) {
                layer.close(loadlayer);
            }
        };
        //上传时设置为禁用
        /**
         * i=1表示禁用，i=2表示可用
         * @param i
         */
        var setBtnDisabled=function (i) {
            var btn=$(opt.elem);
            if (i==1){
                btn.addClass("layui-btn-disabled").attr('disabled',true);
            }else {
                btn.removeClass("layui-btn-disabled").removeAttr('disabled',true);
            }
        };
        var done = opt.done;
        var before = opt.before;
        var error = opt.error;
        var progress=opt.progress;
        delete opt.done;
        delete opt.before;
        delete opt.error;
        var upload = layui.upload;
        uploadObj = upload.render($.extend({
            progress:function(value){//上传进度回调 value进度值
            //element.progress('demo', value+'%')//设置页面进度条
                typeof progress === 'function' && progress.call(value);
            },
            before: function (obj) {
                showLoading();
                setBtnDisabled(1);
                typeof before === 'function' && before.call(this, obj);
            },
            error: function (index, upload) {
                hideLoading();
                setBtnDisabled(2);
                typeof error === 'function' && error.call(this, index, upload);
            },
            done: function (res, index, upload) {
                hideLoading();
                setBtnDisabled(2);
                //只有 response 的 code 一切正常才执行 done
                if (res.rtnCode == 0) {
                    //正常返回
                    if (isNotEmpty(opt.showImgDiv) && isNotEmpty(res.bizData)) {
                        showImage(res.bizData.path);
                    }
                    typeof done === 'function' && done.call(this, res, index, upload);
                }
                //登录状态失效，清除本地 access_token，并强制跳转到登入页
                else if (res.rtnCode == 100) {
                    try {
                        //询问框
                        layer.confirm('未登录或登录超时。请重新登录，谢谢', {
                            btn: ['确定'] //按钮
                        }, function () {
                            if (window.top.location.href != location.href) {
                                window.top.location.href = getRootPath() + "logout.do";
                            } else {
                                window.location.href = getRootPath() + "logout.do";
                            }
                        });
                    } catch (err) {
                    }
                }
                //其它异常
                else {
                    var error = [
                        '<cite>Error：</cite> ' + (res.msg || '返回状态码异常')
                    ].join('');
                    errorToast(error);
                }
            }
        }, opt));
        if (isNotEmpty(opt.showImgDiv) && isNotEmpty(opt.showImgSrc)) {
            showImage(opt.showImgSrc);
        }
    });
    return uploadObj;
};

/**
 * 重新封装layui的table
 * @param opt
 * 目前重新定义,具体看layui的文档
 * render（填充table），
 * sort:（监听排序切换），
 * tool:（监听工具条），
 * edit:（监听单元格编辑），
 * checkbox:（监听复选框选择），
 */
_layuiTable=function(opt){
    layui.use(['table'], function() {
        var table = layui.table;
        opt.elem=opt.elem||''; //elem: '#layui_table' 必填，指定搜索框表单的元素选择器（推荐id选择器）
        opt.filter=opt.filter||'';//必填，指定的lay-filter的名字
        opt.render=opt.render||{};//执行渲染的参数
        opt.render.cols=opt.render.cols||[];
        $.each(opt.render.cols[0],function (i,item) {
            var align="center";
            if(isNotEmpty(item.align)){
                align=item.align;
            }
            var style="";
            if(isNotEmpty(item.style)){
                style=item.style;
            }
            item.align="center"; //表头全部居中
            item.style="text-align:"+align+";"+style;//内容自定义
        });
        //执行渲染table
        var done=opt.render.done;
        delete opt.render.done;
        table.render($.extend(true,{
            elem: opt.elem //指定原始表格元素选择器（推荐id选择器）
            ,page: true //开启分页
            ,limit:20 //每页显示的条数（默认：20）
            ,method:"POST"  //默认是post
            ,autoSort:false //若为 false，则需自主排序，通常由服务端直接返回排序好的数据。
            // ,contentType: 'application/json;charset=utf-8' //设置请求头信息
            ,request: {
                pageName: 'page.pageNum' //改变页码的参数名称，默认：page
                ,limitName: 'page.pageSize' //改变每页数据量的参数名，默认：limit
            }
            ,response: {
                statusName: 'rtnCode' //数据状态的字段名称，默认：code
                ,statusCode: 0 //成功的状态码，默认：0
                ,msgName: 'msg' //状态信息的字段名称，默认：msg
                ,countName: 'total' //数据总数的字段名称，默认：count
                ,dataName: 'bizData' //数据列表的字段名称，默认：data
            }
            ,done: function(res, curr, count){
                //如果是异步请求数据方式，res即为你接口返回的信息。
                if(res.rtnCode == 100){
                    try {
                        //询问框
                        layer.confirm('未登录或登录超时。请重新登录，谢谢', {
                            btn: ['确定'] //按钮
                        }, function () {
                            if (window.top.location.href != location.href) {
                                window.top.location.href = getRootPath() + "logout.do";
                            } else {
                                window.location.href = getRootPath() + "logout.do";
                            }
                        });
                    } catch (err) {
                    }
                }
                typeof done === 'function' && done(res, curr, count);
            }
        }, opt.render));
        //监听排序切换
        table.on('sort('+opt.filter+')', function(obj){
            if(opt.sort==undefined||!(typeof opt.sort === 'function')){
                //获取自定义的中的sort字段
                var field=obj.field;
                opt.render.cols=opt.render.cols||[];
                if(opt.render.cols.length>0){
                    $.each(opt.render.cols[0],function (i,item) {
                        if((isNotEmpty(item)&&isNotEmpty(item.field)&&item.field==field)){
                            if(isNotEmpty(item.sortField)){
                                field=item.sortField;
                                return true;
                            }
                        }
                    });
                }
                var orderBy="";
                if(isNotEmpty(field)&&isNotEmpty(obj.type)){
                    orderBy=field+" "+obj.type //排序字段
                }
                table.reload(opt.filter, {
                    initSort: obj //记录初始排序，如果不设的话，将无法标记表头的排序状态。 layui 2.1.1 新增参数
                    ,where: { //请求参数（注意：这里面的参数可任意定义，并非下面固定的格式）
                        'page.orderBy': orderBy //排序字段
                    }
                });
            }else{
                typeof opt.sort === 'function' && opt.sort(obj,opt.filter);
            }
        });
        //监听工具条
        table.on('tool('+opt.filter+')', function(obj){
            typeof opt.tool === 'function' && opt.tool(obj,opt.filter);
        });
        //监听单元格编辑
        table.on('edit('+opt.filter+')', function(obj){
            typeof opt.edit === 'function' && opt.edit(obj,opt.filter);
        });
        //监听复选框选择
        table.on('checkbox('+opt.filter+')', function(obj){
            typeof opt.checkbox === 'function' && opt.checkbox(obj,opt.filter);
        });
    });
};
/**
 * 重新封装layer的弹框
 * @param opt
 */
_layerOpen=function(opt){
    var layerOpen;
    var btns;
    var type;
    layui.use(['layer'], function() {
        var layer = layui.layer;
        opt.url = opt.url || ''; //自定义的弹窗的url
        opt.width = opt.width || ''; //自定义的弹窗的width宽度，会自动判断是否超过当前页面
        opt.height = opt.height || ''; //自定义的弹窗的height高度，会自动判断是否超过当前页面
        //弹框自定义参数，用于是否只能查看,默认是false，true代表只能查看
        opt.readonly=(opt.readonly === undefined || opt.readonly === null) ? false : opt.readonly;
        btns=opt.btns||'';
        type=opt.type||2;
        var win_width=$(window).width();
        var win_height=$(window).height();
        var w=win_width - 50;
        var h=win_height - 20;
        if (isNotEmpty(opt.width)&&(opt.width<win_width)) {
            w=opt.width;
        }
        if (isNotEmpty(opt.height)&&(opt.height<win_height)) {
            h=opt.height;
        }
        if(opt.readonly){
            opt.btn=["关闭"];
            opt.done=function(index,iframeWin){
                layer.close(index);
            };
        }
        var yes=opt.yes;
        var url=opt.url;
        var setting={
            type: type,
            area: [w+'px', h+'px'],
            fix: true, //固定
            maxmin: true,//最大化
            content: url,//url
            yes: function(index, layero) {
                if(isEmpty(opt.type)||opt.type=='2'){
                    var body = layer.getChildFrame('body', index);
                    var iframeWin = window[layero.find('iframe')[0]['name']];//得到iframe页的窗口对象，执行iframe页的方法：
                    typeof opt.done === 'function' && opt.done(index,iframeWin);
                }else{
                    typeof opt.done === 'function' && opt.done(index);
                }
                typeof yes === 'function' && yes(index, layero);
            }
        };
        opt.closeBtn=(opt.closeBtn === undefined || opt.closeBtn === null) ? 1 : opt.closeBtn;
        if(opt.closeBtn===1){
            if (isNotEmpty(btns)){
                setting.btn=btns;
            }else {
                setting.btn = ["确定", "取消"];
            }
        }
        delete opt.width;
        delete opt.height;
        delete opt.url;
        delete opt.yes;
        layerOpen=layer.open($.extend(setting, opt));
    });
    return layerOpen;
};


//截取url请求参数
function GetQueryString(name){
    var searchStr = decodeURI(window.location.search.substr(1));
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = searchStr.match(reg);
    if(r!=null)
        return  unescape(r[2]);
    return null;
}

//判断该字符串是否不为空，且长度大于0
function isNotEmpty(str){
    return str != null && str != undefined && $.trim(str).length > 0;
}
//判断该字符串是否不为空，且长度大于0
function isEmpty(str){
    return str == null || str == undefined || $.trim(str).length == 0;
}

//判断该字符串是否是数字
function isNumber(str){
    if(str == undefined || str.length <0) {
        return false;
    }
    //验证非零的正整数：^\+?[1-9][0-9]*$
    var myreg = /^[0-9]*$/;
    if(!myreg.test(str)){
        return false;
    }
    return true;
}

/**
 * 增加缓存
 * @param key
 * @param obj
 */
function addStorage(key, obj) {
    window.localStorage.setItem(key, obj);
}
/**
 * 获取Storage
 * @param key
 */
function getStorage(key) {
    return window.localStorage.getItem(key);
}
/**
 * 去除Storage
 * @param key
 */
function removeStorage(key) {
    window.localStorage.removeItem(key);
}

/**
 * 清除所有缓存
 * @param key
 */
function clearStorage() {
    window.localStorage.clear();
}




//格式化时间
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};


//处理键盘事件 禁止后退键（Backspace）密码或单行、多行文本框除外
function banBackSpace(e){
    var ev = e || window.event;//获取event对象
    var obj = ev.target || ev.srcElement;//获取事件源

    var t = obj.type || obj.getAttribute('type');//获取事件源类型

    //获取作为判断条件的事件类型
    var vReadOnly = obj.getAttribute('readonly');
    var vEnabled = obj.getAttribute('enabled');
    //处理null值情况
    vReadOnly = (vReadOnly == null) ? false : vReadOnly;
    vEnabled = (vEnabled == null) ? true : vEnabled;

    //当敲Backspace键时，事件源类型为密码或单行、多行文本的，
    //并且readonly属性为true或enabled属性为false的，则退格键失效
    var flag1=(ev.keyCode == 8 && (t=="password" || t=="text"|| t=="number" || t=="textarea")
    && (vReadOnly==true || vEnabled!=true))?true:false;

    //当敲Backspace键时，事件源类型非密码或单行、多行文本的，则退格键失效
    var flag2=(ev.keyCode == 8 && t != "password" && t != "text"&& t != "number" && t != "textarea")
        ?true:false;

    //判断
    if(flag2){
        return false;
    }
    if(flag1){
        return false;
    }
}
//禁止后退键 作用于Firefox、Opera
document.onkeypress=banBackSpace;
//禁止后退键  作用于IE、Chrome
document.onkeydown=banBackSpace;

/**
 * Created by huoquan on 2017/9/26.
 * 此文件用于公共业务的逻辑处理
 */


//定义框架基本方法,这样定义的话，avalon也能直接调用baseFuncInfo所有方法
var baseFuncInfo={
    courseId:"",
    courseType:"",
    courseStatus:"",
    courseOrderby:"1",
    courseKeyword:"",
    //获取个人信息
    userInfoData:{
        userId: getStorage("KEY_USERID")||"",
        loginid:getStorage("KEY_LOGINID")||"",
        username: getStorage("KEY_USERNAME")||""
    },
    //菜单权限方法
    authorityTag:function (code) {
        if(getStorage('KEY_ISADMIN')!=null&&getStorage('KEY_ISADMIN')==="true"){
            //如果为超级管理员
            return true;
        }
        var roles=eval(getStorage("KEY_ROLES"))||[];
        for(var i=0;i<roles.length;i++) {
            if(code == roles[i].keyCode){
                return true;
            }
        }
        return false;
    },
    //获取菜单
    getMenus:function () {
        var list=eval(getStorage("KEY_MENUS"))||[];
        return list;
    },
    //获取数据字典
    getSysDictByCode:function (code,addEmpty) {
        return getSysDictByCode(code,addEmpty);
    },

    // 获取数据字典的名称，专用于列表显示
    getSysDictName:function(code,value) {
        return getSysDictName(code,value);
    },

    getHttpPath:function(){
        return getHttpPath();
    },
    getCourseType:function () {
        var list=[];
        var p={
            "icon":"layui-icon-app",
            "value":"",
            "name":"全部"
        };
        list.push(p);
        var data=eval(getStorage("KEY_COURSE_TYPE"))||{};
        if (isNotEmpty(data)) {
            $.each(data, function (i, item) {
                var v = {
                    "icon": item.dictDataDesc,
                    "value": item.dictDataValue,
                    "name": item.dictDataName
                };
                list.push(v);
            });
        }
        //getStorage("Key_COURSE_TYPE");
        return list;
    },
    getDict:function ($callback) {
        //如果不缓存，每次都要查询字典，但是缓存又不能及时刷新
            getSysDict(function success() {
                typeof $callback === 'function' && $callback({});
            });
    }
    //可继续添加其他方法
    //……
};

//获取http路径
function getHttpPath(){
    var uploadHttpPath=$("input[id='uploadHttpPath']").val();
    return uploadHttpPath;
    //return getStorage("KEY_HTTPPATH")||""
}
function getUserId(){
    var userId=$("input[id='userId']").val();
    return userId;
    //return getStorage("KEY_HTTPPATH")||""
}

//获取对讲视频的路径
function getVideoPath() {
    return getStorage("KEY_VIDEOPATH")|""
}

/**
 * 获取全部组织列表
 */
function selectOrgList(obj,chkStyle,isJianshe,isParent){
    var opt={
        url:getRootPath()+"tOrg/getLists.do",
        chkStyle:chkStyle,
        isJianshe:isJianshe
    };
    if(isNotEmpty(isParent)){
        parent.selectOrgListByRole(obj,opt);
    }else{
        selectOrgListByRole(obj,opt);
    }
}

/**
 * 选择组织架构
 * @param obj
 * @param opt 参数
 * chkStyle：(checkbox 或 radio），默认radio
 * isJianshe: 默认是''：选择全部，1：只能选监舍，2：能选全部除了监舍
 * radioFilter:函数，可对radio类型的数据进行过滤处理
 */
function selectOrgListByRole(obj,opt){
    opt=opt||{};
    opt.chkStyle=opt.chkStyle||'radio'; //(checkbox 或 radio），默认radio
    opt.isJianshe = opt.isJianshe||'';  // 默认是''：选择全部，1：只能选监舍，2：能选全部除了监舍
    opt.url = opt.url||getRootPath()+"system/getOrgLists.do";  // 默认是''：选择全部，1：只能选监舍，2：能选全部除了监舍
    var param = {
    };
    // 渲染表格
    _ajax({
        type: "POST",
        //loading:true,  //是否ajax启用等待旋转框，默认是true
        url: opt.url,
        data: param,
        done:function(data){
            var setting={
                id: 'uuid', //新增自定义参数，同ztree的data.simpleData.idKey
                pId: 'parent', //新增自定义参数，同ztree的data.simpleData.pIdKey
                name:'orgName',  //新增自定义参数，同ztree的data.key.name
                done:function(treeObj){
                    //新增自定义函数,完成初始化加载后的事件，比如可做一些反勾选操作
                    var uuid=$(obj).prev().val();
                    if(isNotEmpty(uuid)){
                        $.each(data,function(i,item){
                            if(uuid.indexOf(item.uuid)>-1){
                                var node = treeObj.getNodeByParam("uuid",item.uuid,null);
                                if(node!=null){
                                    treeObj.checkNode(node, true, false);
                                    treeObj.selectNode(node);
                                }
                                return true;
                            }
                        });
                    }
                }
                //其它具体参数请参考ztree文档
            };
            if(opt.chkStyle==="radio"){
                setting.radio=true; //新增自定义参数，开启radio
                if(isNotEmpty(opt.isJianshe)&&opt.isJianshe==='1'){
                    //如果只选择监舍,其他禁用选择
                    $.each(data,function(i,item) {
                        if(item.type!="4"){
                            item.chkDisabled=true;
                        }
                    });
                }else if(isNotEmpty(opt.isJianshe)&&opt.isJianshe==='2'){
                    //能选全部除了监舍
                    $.each(data,function(i,item) {
                        if(item.type=="4"){
                            item.chkDisabled=true;
                        }
                    });
                }
                typeof opt.radioFilter === 'function' && opt.radioFilter(data);
            }else if(opt.chkStyle==="checkbox"){
                setting.checkbox=true; //新增自定义参数，开启checkbox
            }
            //加载ztree弹框树
            _initLayOpenZtree(setting,data,function(nodes){
                //确定事件，返回选择的nodes节点列表
                var name=[];
                var id=[];
                if(nodes.length>0){
                    $.each(nodes,function(i,item){
                        if(opt.chkStyle==="checkbox"){
                            if(isNotEmpty(opt.isJianshe)&&opt.isJianshe==='1'){
                                //如果只选择监舍
                                if(item.type!="4"){
                                    return true;
                                }
                            }else if(isNotEmpty(opt.isJianshe)&&opt.isJianshe==='2'){
                                //能选全部除了监舍
                                if(item.type=="4"){
                                    return true;
                                }
                            }
                        }
                        name.push(item.orgName);
                        id.push(item.uuid);
                    })
                }
                $(obj).val(name.join(","));
                $(obj).prev().val(id.join(","));
            });
        }
    });
}

/**
 *  获取监舍列表
 * @param obj
 * @param chkStyle (checkbox 或 radio），默认radio
 * @param isParent 是否父窗口打开，1代表是
 */
function getJianSheList(obj,chkStyle,isParent){
    var opt={
        chkStyle:chkStyle,
        isJianshe:'1'
    };
    if(isNotEmpty(isParent)){
        parent.selectOrgListByRole(obj,opt);
    }else{
        selectOrgListByRole(obj,opt);
    }
}


/**
 *  获取组织架构列表
 * @param obj
 * @param chkStyle (checkbox 或 radio），默认radio
 * @param isParent 是否父窗口打开，1代表是
 */
function getOrgsList(obj,chkStyle,isParent){
    var opt={
        chkStyle:chkStyle
    };
    if(isNotEmpty(isParent)){
        parent.selectOrgListByRole(obj,opt);
    }else{
        selectOrgListByRole(obj,opt);
    }
}

/**
 * 能选全部除了监舍
 * @param obj
 * @param chkStyle (checkbox 或 radio），默认radio
 * @param isParent 是否父窗口打开，1代表是
 */
function getOrgsListExcludeJianShe(obj,chkStyle,isParent){
    var opt={
        chkStyle:chkStyle,
        isJianshe:'2'
    };
    if(isNotEmpty(isParent)){
        parent.selectOrgListByRole(obj,opt);
    }else{
        selectOrgListByRole(obj,opt);
    }
}


//不显示搜索框
function noShowSearch() {
    $("#Main_search").remove();
}
//后台不显示前台后补
function noShowCusTop() {
    $("#cusTop").remove();
}

//添加搜索框
function addTopSearchHtml() {
    var str="<div class=\" layui-container\" id='searchcontainer'>\n" +
        "    <ul class=\" layui-row\">\n" +
        "        <li class=\"layui-col-md4  layui-col-sm6  layui-col-xs8 layui-col-md-offset3 layui-col-sm-offset2 layui-col-xs-offset1 \"\n" +
        "            style=\"margin-top: 10px;margin-bottom: 10px\">\n" +
        "            <input class=\"layui-input\" id='keyword' placeholder=\"请输入课程名称\" ></input>\n" +
        "        </li>\n" +
        "        <li class=\"layui-col-md1\" style=\"margin-top: 10px;margin-left: 10px\">\n" +
        "            <button class=\"layui-btn \" id='top_search'>\n" +
        "                <i class=\"layui-icon layui-icon-search\"></i>\n" +
        "            </button>\n" +
        "        </li>\n" +
        "    </ul>\n" +
        "</div>";
   /* var str='<div class="layui-form">\n' +
        '    <div class="layui-form-item"></div>\n' +
        '        <div class="layui-input-block layui-row">\n' +
        '            <div class="layui-col-lg3  layui-col-md4  layui-col-sm6  layui-col-xs8 layui-col-md-offset3 layui-col-sm-offset2 layui-col-xs-offset1">\n' +
        '            <select class="" id="keyword"  lay-search="" lay-filter="keyword">\n' +
        '                <option value="">直接选择或搜索选择</option>\n' +
        '                <option value="1">1111</option>\n' +
        '                <option value="2">222</option>\n' +
        '                <option value="3">333</option>\n' +
        '\n' +
        '            </select>\n' +
        '            </div>\n' +
        '            <div class=" layui-col-md1 layui-form-mid layui-word-aux " style="margin-left: 10px;">\n' +
        '                <button class="layui-btn " style="margin-top: -9px;" id="top_search">\n' +
        '                    <i class="layui-icon layui-icon-search"></i>\n' +
        '                </button>\n' +
        '            </div>\n' +
        '            </div>\n' +
        '\n' +
        '</div>';*/

    $("#cusTop").after(str);
}
//头部搜索事件
function topSearch() {
    $("#top_search").click(function () {
        //debugger;
        //console.log($("#keyword"));
        var val=$("#keyword").val();
        //var val2=$("#keyword")[0].find("input").value;
        console.log(val);
        //console.log('v2==='+val2);
        var path=window.location.pathname;
        console.log(path);
        if (path.indexOf("course/courseList")!=-1){
            getList();
        }else {
            window.location = getRootPath() + "course/courseList?keyword=" + val;
        }
    });
}

//隐藏头部


/*跳转到后台*/
function toAdminPage() {
    var param={};
    _ajax({
        type: "POST",
        url: getRootPath()+"sysUser/selectAdminData.do",
        data:param,  //必须字符串后台才能接收list,
        //loading:false,  //是否ajax启用等待旋转框，默认是true
        dataType: "json",
        done: function(data){
            removeStorage("KEY_MENUS");
            addStorage("KEY_MENUS",data.menus); //菜单
            window.location = getRootPath()+"admin/index";
        }
    });
}

/*
查询字典到缓存
 */
function getSysDict($callback) {
    var param={};
    _ajax({
        type: "POST",
        url: getRootPath()+"sysDict/getGsonLists.do",
        data:param,  //必须字符串后台才能接收list,
        loading:false,  //是否ajax启用等待旋转框，默认是true
        dataType: "json",
        done: function(data){
            //字典已存在时先清空
            if (isNotEmpty(getStorage("KEY_SYSDICT"))){
                removeStorage("KEY_SYSDICT");
            }
            //重新赋值
            addStorage("KEY_SYSDICT", data.sysDict); //数据字典
            typeof $callback === 'function' && $callback({});
        }
    });
}

//加载模板
/**
 *
 * @param TempName 模板的名字
 * @param id      装在模板的id
 * @param list    数据列表
 */
function loadTemp(TempName, id, list) {
    var laytpl = layui.laytpl;
    var temp = TempName.innerHTML;

    laytpl(temp).render({list: list}, function (html) {
        id.innerHTML='';
        id.innerHTML += html;
    });
}
//加载分页
/**
 *
 * @param id  容器id
 * @param total 数据总数量
 * @param funcName  获取数据的名称
 */
function loadPage(id, total, funcName) {
    var laypage = layui.laypage;
    laypage.render({
        elem: id
        , count: total || 0
        , layout: [
            'count',
            'prev',
            'page',
            'next',
            // 'limit',
            'refresh',
            'skip'
        ]
        , jump: function (obj, first) {
            // console.log(obj);
            // console.log("first==="+first);
            //首次不执行
            if (!first) {
                //do something
                funcName(obj.curr, obj.limit);
            }
        }
    });
}


/**
 * 验证登录
 * @param param
 */

function goLogin(param) {
    //console.log(param);

    //console.log(getRootPath());
    _ajax({
        type: "POST",
        url:getRootPath()+"sysUser/clearAndLogin.do",
        data: param,
        dataType:"json",
        loadMsg:"正在登录...",
        done:function(data){
            clearStorage();
            console.log(data);
            //addStorage("KEY_USERID", data.userId);//用户id
            //jump(2);
            successToast("登入成功,2秒后跳转",2000);
            setTimeout(function (){
                // var url=document.referrer;
                // console.log("==========上一次的路径"+url);
                // if (isEmpty(url)){
                //     url=getRootPath()+"mainPage.do";
                // }
                var url=getRootPath()+"mainPage.do";
                //location.href=url;
                location.href=url;
            },2000);

        }
    });
}

/**
 * 预览
 */
function preview(path,name) {
    var title='预览<span style="color: #0e97e2">'+name+"</span>";
    var url=getRootPath()+'video/preview?path='+path+'&name='+name;
    parent._layerOpen({
        url:url,  //弹框自定义的url，会默认采取type=2
        width:600, //弹框自定义的宽度，方法会自动判断是否超过宽度
        height:500,  //弹框自定义的高度，方法会自动判断是否超过高度
        readonly:true,  //弹框自定义参数，用于是否只能查看,默认是false，true代表只能查看,done事件不执行
        title:title, //弹框标题
        done:function(index,iframeWin){
            /**
             * 确定按钮的回调,说明：index是关闭弹框用的，iframeWin是操作子iframe窗口的变量，
             * 利用iframeWin可以执行弹框的方法，比如save方法
             */
        }
    });
}

//生成uuid,指定长度和基数(进制)
function getUuid(len, radix) {
    var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
    var uuid = [], i;
    radix = radix || chars.length;

    if (len) {
        // Compact form
        for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random()*radix];
    } else {
        // rfc4122, version 4 form
        var r;

        // rfc4122 requires these characters
        uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
        uuid[14] = '4';

        // Fill in random data.  At i==19 set the high bits of clock sequence as
        // per rfc4122, sec. 4.1.5
        for (i = 0; i < 36; i++) {
            if (!uuid[i]) {
                r = 0 | Math.random()*16;
                uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
            }
        }
    }

    return uuid.join('');
}


//查看驳回原因
function seeSeason(text,name) {
    var layer=layui.layer;
    var title="课程《"+name+"》的驳回原因";
    var str='<div class="layui-fluid layui-row" style="padding-top: 10px;">' +
        '<div class="layui-col-md2" >' +
        '   驳回原因:<br>' +
        '</div>' +
        '<div class="layui-col-md10">' +
        '   <textarea style="width: 375px;overflow: auto;padding:10px 10px 10px 10px">'+text+'</textarea>' +
        '</div>' +
        '</div>';
    layer.open({
        type:1,
        id:"xxxx",
        title:title,
        area: ['500px', '300px'],
        content:str
    });
}
//懒加载
function lazyImg() {
    var flow=layui.flow;
    flow.lazyimg();
}
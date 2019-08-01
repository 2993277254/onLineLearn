/**
 * Created by huoquan on 2017/9/28.
 */
var sysMenuEdit = avalon.define({
    $id: "sysMenuEdit"

});

layui.use(['index'],function(){
    avalon.ready(function () {
        //所有的入口事件写在这里
        var id=GetQueryString("id");  //获取变量
        var parentId=GetQueryString("parentId");  //获取变量
        var parentName=GetQueryString("parentName");  //获取变量
        var form=layui.form; //调用layui的form模块
        //表格赋值
        form.val('sysMenuEdit_form', {
            id:id,
            parentId:parentId,
            parentName:parentName
        });
        if(isNotEmpty(id)){
            getInfo(id); //获取实体信息
        }
        avalon.scan();
    });
});

//获取实体
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
                if($callBack!=undefined)
                    $callBack(data);
            }
        });
    });
}


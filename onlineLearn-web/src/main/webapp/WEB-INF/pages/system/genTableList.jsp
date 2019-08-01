<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8" %>
<%@ include file="/WEB-INF/base/adminCommon.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" type="text/css" href="${baseprefix}/layuiadmin/style/admin.css" media="all">
    <link rel="stylesheet" type="text/css" href="${baseprefix}/lib/zTree/v3/css/layuiStyle/layuiStyle.css">
</head>


<style>
    .layui-table-cell .layui-form-select .layui-input{
        height: 28px;
        line-height: 28px;
    }
    .layui-table-cell .layui-search-input{
        height: 28px;
        line-height: 28px;
    }
    .layui-table-cell .input-icon-right{
        position: absolute;
        top: 50%;
        right: 17px !important;
        margin-top: -13px;
    }
    .cell_overflow{
        overflow:visible;
    }
    .cell_overflow .layui-form-select dl{
        /*top: 30px;*/
    }
</style>
<body ms-controller="genTableList">
<!--左侧树的div-->
<div id="left_tree"></div>
<div class="layui-fluid">
    <div class="layui-card">
        <div class="layui-form layui-card-header layuiadmin-card-header-auto search-form" lay-filter="genTableList_search_form">
            <div class="layui-inline">
                <button class="layui-btn"  onclick="save(1)">保存</button>
            </div>
            <div class="layui-inline pl-10">
                <button class="layui-btn"  onclick="save(2)">一键生成</button>
            </div>
            <div class="layui-inline">
                <div class="layui-form-item">
                    <div class="layui-inline pl-10">
                        <input type="checkbox" lay-filter="isCover" name="isCover" lay-skin="primary" title="覆盖项目文件（会生成jsp页面，通常用于第一次生成）">
                        <%--<span class="edit-verify-span">此工具只用在公司内部，请勿拷贝发给别人。     --turkey</span>--%>
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-card-body">
            <div class="layui-card-header">表基本信息</div>
            <div class="layui-form" lay-filter="genTableList_form">
                <div class="layui-form-item">
                    <label class="layui-form-label"><span class="edit-verify-span">*</span>主表名</label>
                    <div class="layui-input-inline">
                        <input type="text" name="name" lay-verify="required" ms-duplex="@tableName" readonly class="layui-input">
                    </div>
                    <label class="layui-form-label"><span class="edit-verify-span">*</span>所属模块</label>
                    <div class="layui-input-inline">
                        <select name="module"  lay-verify="required">
                            <option  value=""></option>
                            <option  ms-for="($index1, el) in @moduleList" ms-attr="{value:el.value}">{{el.name}}</option>
                        </select>
                    </div>
                    <div class="layui-inline pl-5" style="float: left;">
                        <input type="checkbox" lay-filter="isLeftZtree" name="isLeftZtree" lay-skin="primary" title="添加左侧树">
                    </div>
                    <div class="layui-inline" style="float: left;" ms-if="@isLeftZtree==1">
                        <input type="text" lay-verify="required" name="leftZtreeName" id="leftZtreeName" placeholder="请选择树" readonly="" autocomplete="off" class="layui-input icon-input-search" onclick="getLeftZtree(this);">
                        <i class="layui-icon input-icon-right layui-icon-search" style="margin-top: -11px;"></i>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a ms-click="@addPk" style="color: dodgerblue;" href="javascript:;"><i class="layui-icon layui-icon-add-circle"></i>添加关联父表(可多个)
                        </a>
                        <i class="layui-icon layui-icon-tips" lay-tips="关联父表一般用作于列表查询时显示父表字段，不需要显示请勿添加。（提示：目前仅支持生成一对一的关联，一对多例子请参考数据字典）"></i>
                    </div>
                </div>
                <div class="layui-form-item" ms-for="($index, el) in @pkJson">
                    <label class="layui-form-label"><span class="edit-verify-span">*</span>父表表名</label>
                    <div class="layui-input-inline">
                        <select lay-verify="required" lay-filter="pkJson_tableName" ms-attr="{name:'tableName'+$index,'index':$index}">
                            <option  value=""></option>
                            <option  ms-for="($index1, el1) in @tableList" ms-attr="{value:el1.name}">{{el1.name}}</option>
                        </select>
                    </div>
                    <label class="layui-form-label"><span class="edit-verify-span">*</span>所属模块</label>
                    <div class="layui-input-inline">
                        <select   lay-verify="required" lay-filter="pkJson_module" ms-attr="{name:'module'+$index,'index':$index}">
                            <option  value=""></option>
                            <option  ms-for="($index1, el) in @moduleList" ms-attr="{value:el.value}">{{el.name}}</option>
                        </select>
                    </div>
                    <label class="layui-form-label"><span class="edit-verify-span">*</span>当前表外键</label>
                    <div class="layui-input-inline">
                        <select  lay-verify="required" lay-filter="pkJson_pk" ms-attr="{name:'pk'+$index,'index':$index}">
                            <option  value=""></option>
                            <optgroup  ms-attr="{label:tableName+'表字段:'}">
                                <option  ms-for="($index2, el2) in @tableColumnList" ms-attr="{value:el2.name}">{{el2.name}}</option>
                            </optgroup>
                        </select>
                    </div>
                    <div class="layui-form-mid layui-word-aux"><a href="javascript:;" style="color: red" ms-click="@delPk($index)"><i class="layui-icon layui-icon-close"></i>删除</a></div>
                </div>
                <div class="layui-form-item layui-hide">
                    <button class="layui-btn" lay-submit lay-filter="genTableList_submit" id="genTableList_submit">提交</button>
                </div>
            </div>
        </div>
        <div class="layui-card-body">
            <!--table定义-->
            <table id="genTableList_table" lay-filter="genTableList_table"></table>
        </div>
    </div>
</div>
<script type="text/javascript" src="${baseprefix}/lib/zTree/v3/js/jquery.ztree.all.min.js"></script>
<script type="text/javascript" src="${baseprefix}/js/system/genTableList.js?t=<%= System.currentTimeMillis()%>"></script>
<!-- 是否查询条件 -->
<script type="text/html" id="isQueryTpl">
    <input type="checkbox" name="isQuery" value="{{d.LAY_INDEX-1}}" lay-skin="switch" lay-text="是|否" lay-filter="isQuery" {{ d.isQuery == "1" ? 'checked' : '' }}>
</script>
<!-- 查询类型 -->
<script type="text/html" id="queryTypeTpl">
    <select name="queryType" lay-filter="queryType">
        <option value="{{d.LAY_INDEX-1}}|input" {{ d.queryType == "input" ? 'selected' : '' }}>文本框(input)</option>
        <option value="{{d.LAY_INDEX-1}}|select" {{ d.queryType == "select" ? 'selected' : '' }}>下拉框(select)</option>
        <option value="{{d.LAY_INDEX-1}}|checkbox" {{ d.queryType == "checkbox" ? 'selected' : '' }}>复选框(checkbox)</option>
        <option value="{{d.LAY_INDEX-1}}|radio" {{ d.queryType == "radio" ? 'selected' : '' }}>单选框(radio)</option>
        <option value="{{d.LAY_INDEX-1}}|date" {{ d.queryType == "date" ? 'selected' : '' }}>时间选择(date)</option>
        <option value="{{d.LAY_INDEX-1}}|datetime" {{ d.queryType == "datetime" ? 'selected' : '' }}>时间选择(datetime)</option>
        <option value="{{d.LAY_INDEX-1}}|time" {{ d.queryType == "time" ? 'selected' : '' }}>时间选择(time)</option>
        <option value="{{d.LAY_INDEX-1}}|date_range" {{ d.queryType == "date_range" ? 'selected' : '' }}>时间范围(date)</option>
        <option value="{{d.LAY_INDEX-1}}|datetime_range" {{ d.queryType == "datetime_range" ? 'selected' : '' }}>时间范围(datetime)</option>
        <option value="{{d.LAY_INDEX-1}}|time_range" {{ d.queryType == "time_range" ? 'selected' : '' }}>时间范围(time)</option>
    </select>
</script>
<!-- 查询条件 -->
<script type="text/html" id="queryConditionTpl">
    <select name="queryCondition" lay-filter="queryCondition">
        <option value="{{d.LAY_INDEX-1}}|=" {{ d.queryCondition == "=" ? 'selected' : '' }}>=</option>
        <option value="{{d.LAY_INDEX-1}}|like" {{ d.queryCondition == "like" ? 'selected' : '' }}>LIKE</option>
        <option value="{{d.LAY_INDEX-1}}|&gt;" {{ d.queryCondition == ">" ? 'selected' : '' }}>></option>
        <option value="{{d.LAY_INDEX-1}}|&gt;=" {{ d.queryCondition == ">=" ? 'selected' : '' }}>>=</option>
        <option value="{{d.LAY_INDEX-1}}|&lt;" {{ d.queryCondition == "<" ? 'selected' : '' }}><</option>
        <option value="{{d.LAY_INDEX-1}}|&lt;=" {{ d.queryCondition == "<=" ? 'selected' : '' }}><=</option>
        <option value="{{d.LAY_INDEX-1}}|!=" {{ d.queryCondition == "!=" ? 'selected' : '' }}>!=</option>
    </select>
</script>
<!-- 是否列表条件 -->
<script type="text/html" id="isListTpl">
    <input type="checkbox" name="isList" value="{{d.LAY_INDEX-1}}" lay-skin="switch" lay-text="是|否" lay-filter="isList" {{ d.isList == "1" ? 'checked' : '' }}>
</script>
<!-- 列表排列 -->
<script type="text/html" id="listAlignTpl">
    <select name="listAlign" lay-filter="listAlign">
        <option value="{{d.LAY_INDEX-1}}|left" {{ d.listAlign == "left" ? 'selected' : '' }}>向左</option>
        <option value="{{d.LAY_INDEX-1}}|center" {{ d.listAlign == "center" ? 'selected' : '' }}>居中</option>
        <option value="{{d.LAY_INDEX-1}}|right" {{ d.listAlign == "right" ? 'selected' : '' }}>向右</option>
    </select>
</script>
<!-- 是否排序 -->
<script type="text/html" id="isSortTpl">
    <input type="checkbox" name="isSort" value="{{d.LAY_INDEX-1}}" lay-skin="switch" lay-text="是|否" lay-filter="isSort" {{ d.isSort == "1" ? 'checked' : '' }}>
</script>
<!-- 列表显示值 -->
<script type="text/html" id="listFieldTpl">
    <select name="align" lay-filter="listField">
        <option value="{{d.LAY_INDEX-1}}|left" {{ d.listField == "" ? 'selected' : '' }}>本字段值</option>
    </select>
</script>
<!-- 编辑显示 -->
<script type="text/html" id="isEditTpl">
    <input type="checkbox" name="isEdit" value="{{d.LAY_INDEX-1}}" lay-skin="switch" lay-text="是|否" lay-filter="isEdit" {{ d.isEdit == "1" ? 'checked' : '' }}>
</script>
<!-- 是否必填 -->
<script type="text/html" id="isRequiredTpl">
    <input type="checkbox" name="isRequired" value="{{d.LAY_INDEX-1}}" lay-skin="switch" lay-text="是|否" lay-filter="isRequired" {{ d.isRequired == "1" ? 'checked' : '' }}>
</script>
<!-- 编辑类型 -->
<script type="text/html" id="editTypeTpl">
    <select name="editType" lay-filter="editType">
        <option value="{{d.LAY_INDEX-1}}|input" {{ d.editType == "input" ? 'selected' : '' }}>文本框(input)</option>
        <option value="{{d.LAY_INDEX-1}}|select" {{ d.editType == "select" ? 'selected' : '' }}>下拉框(select)</option>
        <option value="{{d.LAY_INDEX-1}}|checkbox" {{ d.editType == "checkbox" ? 'selected' : '' }}>复选框(checkbox)</option>
        <option value="{{d.LAY_INDEX-1}}|radio" {{ d.editType == "radio" ? 'selected' : '' }}>单选框(radio)</option>
        <option value="{{d.LAY_INDEX-1}}|textarea" {{ d.editType == "textarea" ? 'selected' : '' }}>文本域(textarea)</option>
        <option value="{{d.LAY_INDEX-1}}|date" {{ d.editType == "date" ? 'selected' : '' }}>时间选择(date)</option>
        <option value="{{d.LAY_INDEX-1}}|datetime" {{ d.editType == "datetime" ? 'selected' : '' }}>时间选择(datetime)</option>
        <option value="{{d.LAY_INDEX-1}}|time" {{ d.editType == "time" ? 'selected' : '' }}>时间选择(time)</option>
        <option value="{{d.LAY_INDEX-1}}|upload" {{ d.editType == "upload" ? 'selected' : '' }}>图片上传(upload)</option>
    </select>
</script>
<!-- 栅格排列 -->
<script type="text/html" id="field3Tpl">
    <select name="field3" lay-filter="field3">
        <option value="{{d.LAY_INDEX-1}}|1" {{ d.field3 == "1" ? 'selected' : '' }}>一行一个</option>
        <option value="{{d.LAY_INDEX-1}}|2" {{ d.field3 == "2" ? 'selected' : '' }}>一行两个</option>
        <option value="{{d.LAY_INDEX-1}}|3" {{ d.field3 == "3" ? 'selected' : '' }}>一行三个</option>
        <option value="{{d.LAY_INDEX-1}}|4" {{ d.field3 == "4" ? 'selected' : '' }}>一行四个</option>
    </select>
</script>
<!-- 验证方式 -->
<script type="text/html" id="editVerifyTpl">
    <select name="editVerify" lay-filter="editVerify">
        <option value="{{d.LAY_INDEX-1}}|" selected></option>
        <option value="{{d.LAY_INDEX-1}}|phone" {{ d.editVerify == "phone" ? 'selected' : '' }}>手机号</option>
        <option value="{{d.LAY_INDEX-1}}|email" {{ d.editVerify == "email" ? 'selected' : '' }}>邮箱</option>
        <option value="{{d.LAY_INDEX-1}}|url" {{ d.editVerify == "url" ? 'selected' : '' }}>网址</option>
        <option value="{{d.LAY_INDEX-1}}|number" {{ d.editVerify == "number" ? 'selected' : '' }}>数字</option>
        <option value="{{d.LAY_INDEX-1}}|date" {{ d.editVerify == "date" ? 'selected' : '' }}>日期</option>
        <option value="{{d.LAY_INDEX-1}}|identity" {{ d.editVerify == "identity" ? 'selected' : '' }}>身份证</option>
    </select>
</script>
</body>
</html>
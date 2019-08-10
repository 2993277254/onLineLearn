<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/WEB-INF/base/common.jsp" %>


<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>在线视频学习平台</title>
</head>
<body ms-controller="frontIndex">
<div class="layui-card" >
<div class="layui-container " style="padding-bottom: 20px;">
   <div class="layui-fluid">
       <div class="layui-row">

           <ul class="layui-col-md3 layui-nav layui-nav-tree" style="min-width:260px;max-height: 300px;overflow: auto;">
                <li class="layui-row layui-bg-black layui-nav-item layui-nav-itemed" style="text-align: center;padding: 10px 0px 10px 0px;color: white; "
                     >
                    <a href="javascript:;">所有课程</a>
                    <dl class="layui-nav-child" >
                        <dd ms-for="($index, el) in @typeData"><span  style="padding: 10px 0px 10px 0px; " class="courseType layui-col-md12 acursor"   :click="typeClick(el.value)" >{{el.name}}</span>
                        </dd>
                    </dl>
                    <%--<span :if="@baseFuncInfo.getSysDictByCode('course_type_').length>3" style="padding: 10px 0px 10px 0px; " class="courseType layui-col-md12 acursor"   :click="" >更多</span>--%>
                </li>

           </ul>
           <div class="layui-col-md9" >
               <div class="layui-carousel" id="carouselWheel" lay-filter="carouselWheel" >
                   <div carousel-item>
                       <div ms-for="($index, el) in @hotList">
                           <img  ms-attr="{src: @imgfix+@el.photoUrl}" style="width: 100%;height: 100%" :click="imgClick(el.uid)" class="layui-row acursor " alt="" >
                           <div class="layui-row layui-fluid acursor" :click="imgClick(el.uid)" style="text-align: center;position: absolute;top: 0px;font-size: 20px;font-weight: bolder">{{el.name}}</div>
                       </div>
                   </div>
               </div>
           </div >
       </div>

   </div>
</div>
 <div class="layui-container layui-card">
     <div class="layui-fluid">
         <div class="layui-row" ms-for="($index, el) in @categorizeList" style="padding-bottom: 20px;">
             <ul  class="layui-row hot-list" ><div class="layui-col-md11" style="font-size: 20px;font-weight: bolder">{{el.name}}</div><div class="layui-col-md1 acursor"  style="text-align: right" :click="typeClick(el.value)">查看全部</div></ul>
             <hr>
             <%--<div class="layui-row">{{el.content}}</div>--%>
             <%--:if="el.content!=null&&el.content.length>0"--%>
             <li class="layui-col-md3 " :if="el.content!=null&&el.content.length>0" :class="[@$index2!=0?'layui-col-md-offset1':'','addedcourseli']"  ms-for="($index2, e2) in @el.content" :click="imgClick(e2.uid)" style="padding: 10px 0px 10px 0px">
                 <div class="layui-row" style="height: 136px;"><img :click="imgClick(e2.uid)" class="img-box transition" :attr="{src:@imgfix+@e2.photoUrl}" ></div>
                 <div class="layui-row layui-fluid acursor" style="padding-top: 10px;" :click="imgClick(e2.uid)">{{e2.name}}</div>
                 <div class="layui-row layui-fluid acursor" style="padding-top: 10px;" :click="imgClick(e2.uid)"><i class="layui-icon layui-icon-username" title="学习人数">{{e2.personNum}}</i></div>
             </li>

             <div class="layui-row" :if="el.content.length==0" style="text-align: center">暂无数据</div>
         </div>
 </div>
</div>
</div>
<script type="text/javascript" src="${baseprefix}/js/index.js?t=<%= System.currentTimeMillis()%>"></script>
<%--<script type="text/javascript" src="${baseprefix}/js/footer.js?t=<%= System.currentTimeMillis()%>"></script>--%>

</body>
</html>

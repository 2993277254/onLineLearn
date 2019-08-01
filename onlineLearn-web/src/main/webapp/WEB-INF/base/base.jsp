<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/12/19
  Time: 17:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=utf-8" language="java" import="java.util.*" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%
    String path=request.getContextPath();

    String projectPath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<%--项目路径--%>
<c:set var="basePath" value="<%=projectPath%>"></c:set>
<%--静态资源路径--%>
<c:set var="baseprefix" value="${pageContext.request.contextPath}"></c:set>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en">
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>活动页面</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap/bootstrap-table.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/activity/list.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/plug-in/bootstrap-fileinput-master/css/fileinput.min.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/chosen/chosen.min.css"/>

  </head>
  <body>
<div>
<select class="chosenCategory chosen-select" multiple data-placeholder="请选择">
    <c:forEach var="category" items="${categorys}">
        <option value="${category.CODE}">${category.NAME}</option>
    </c:forEach>
</select>
</div>
    <script src="${pageContext.request.contextPath}/js/common/jquery.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/common/bootstrap.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/common/bootstrap-table.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/common/bootstrap-table-zh-CN.js"></script>
        <script src="${pageContext.request.contextPath}/js/common/modal.js""></script>
        <script src="${pageContext.request.contextPath}/js/laydate/laydate.js" charset="UTF-8"></script>
        <script src="${pageContext.request.contextPath}/js/common/bootbox.min.js" charset="UTF-8"></script>
        <script src="${pageContext.request.contextPath}/js/common/globa.js"></script>
        <script src="${pageContext.request.contextPath}/query/bootstrap-table-export.js"></script>
        <script src="${pageContext.request.contextPath}/plug-in/bootstrap-fileinput-master/js/fileinput.min.js"></script>
        <script src="${pageContext.request.contextPath}/plug-in/bootstrap-fileinput-master/js/locales/zh.js"></script>
        <script src="${pageContext.request.contextPath}/js/common/chosen.jquery.min.js"></script>
    <script type = "text/javascript">
        $(function(){

        $('.chosen-select').chosen();
        });
    </script>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>版本列表</title>
</head>
<body>
<table class="table table-striped table-bordered bootstrap-datatable datatable">
<thead>
		<tr>
				<th width="34%">版本号</th>
				<th width="33%">版本文件地址</th>
				<th width="33%">版本上传时间</th>
			</tr>
</thead>
<tbody>
	<c:forEach items="${versions}" var="version">
		<tr>
			<td>${version.apkVersion}</td>
			<td>${version.url}</td>
		    <%-- <td>${version.uploadTime}</td> --%>
		   <td><fmt:formatDate value="${version.uploadTime}"/></td>
		</tr>
		</c:forEach>
</tbody>
</table>
</body>
</html>
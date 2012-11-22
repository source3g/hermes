<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>集团商户详细信息</title>
</head>
<body>
		<div>
			<p><span>集团商户详细信息:     </span>${merchantGroup.name}</p>
		</div>
	<table class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="50%">商户名称</th>
			</tr>
		</thead>
		<c:forEach items="${merchants}" var="merchant">
		<tr>
				<td>${merchant.name}</td>
		</tr>
		</c:forEach>	
	</table>
</body>
</html>
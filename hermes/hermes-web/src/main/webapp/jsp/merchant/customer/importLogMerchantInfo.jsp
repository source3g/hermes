<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>导入日志商户信息</title>
</head>
<body>
	<table
		class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="9%">商户姓名</th>
				<th width="9%">性别</th>
				<th width="9%">生日</th>
				<th width="9%">电话号码</th>
				<th width="9%">住址</th>
				<th width="9%">QQ</th>
				<th width="9%">Email</th>
				<th width="9%">备注</th>
				<th width="9%">顾客组名</th>
				<th width="9%">导入结果</th>
				<th width="10%">失败原因</th>
			</tr>
		</thead>
		<c:forEach items="${customerImportItem}" var="customerImportItem">
			<tr>
			<td> ${customerImportItem.name }</td>
	 		<td> ${customerImportItem.sex }</td>
			<td> ${customerImportItem.birthday }</td>
			<td> ${customerImportItem.phone }</td>
			<td> ${customerImportItem.address }</td>
			<td> ${customerImportItem.qq }</td>
			<td> ${customerImportItem.email }</td>
			<td> ${customerImportItem.note }</td>
			<td> ${customerImportItem.customerGroupName }</td> 
			<td> ${customerImportItem.importStatus }</td> 
			<td> ${customerImportItem.failedReason }</td> 
			
			</tr>
			</c:forEach>
		</table>
</body>
</html>
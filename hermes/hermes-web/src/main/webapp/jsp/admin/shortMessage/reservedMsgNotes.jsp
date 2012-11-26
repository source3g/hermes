<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商户个人短信预存记录</title>
</head>
<body>
	<form id="msgForm" class="well form-inline " method="get">
			<select id="shortMessage" name="type"class="input-medium">
			<option value="allNotes">全部记录</option>
			<option value="reservedNotes">预存记录</option>
			<option value="sendNotes">发送记录</option>
			</select>
	</form>
	<table
		class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="30%">商户名称</th>
				<th width="35%">短信操作记录</th>
				<th width="35%">日期</th>
			</tr>
		</thead>
		
	</table>
</body>
</html>
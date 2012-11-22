<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>导入日志列表</title>
</head>
<body>
	<form id="queryForm" class="well form-inline " method="get">
		<label class="control-label" for="name">导入时间：</label> <input
			type="text" name="name" class="input-medium"
			placeholder="请选择导入日志的时间..."> <input type="submit"
			class="btn btn-primary" value="查询">
	</form>

	<table
		class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="10%">旧文件名</th>
				<th width="10%">新文件名</th>
				<th width="10%">导入状态</th>
				<th width="10%">总记录数</th>
				<th width="10%">导入记录数</th>
				<th width="10%">导入时间</th>
				<th width="10%">操作</th>
			</tr>
		</thead>
		<c:forEach items="${importLogs }" var="importLog">
			<tr>
				<td>${importLog.name }</td>
				<td>${importLog.newName }</td>
				<td>${importLog.status }</td>
				<td>${importLog.totalCount }</td>
				<td>${importLog.importCount }</td>
				<td>${importLog.importTime }</td>
				<td>详情</td>
			</tr>

		</c:forEach>
	</table>


</body>
</html>
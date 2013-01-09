<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>资源设置</title>
</head>
<body>
	<h3>顾客组操作</h3>

	<form id="addCustomerGroupForm" class="form-inline">
		<label class="control-label" for="prefix">前缀：</label> <input
			type="text" class="input-xlarge" placeholder="请输入短信前缀..." id="prefix"
			name="prefix"> <label class="control-label" for="suffix">后缀：</label>
		<input type="text" class="input-xlarge" placeholder="请输入短信后缀..."
			id="suffix" name="suffix">
		<input type="submit" class="btn btn-primary"  value="保存">
	</form>

	<form action="addResourceForm">
		<label class="control-label" for="name">资源名称：</label> <input
			type="text" class="input-xlarge" placeholder="请输入资源名称..." id="name"
			name="name"> <span class="help-inline"><font
			color="red">*</font></span> <input type="submit" id="addCustomerGroupBtn"
			data-loading-text="增加中..." class="btn btn-primary" value="增加">

	</form>

	<h3>资源列表</h3>

	<table class="table table-bordered" id="resourceTab">
		<thead>
			<tr>
				<th>名称</th>
				<th>操作</th>
			</tr>
		</thead>
	</table>
</body>
</html>
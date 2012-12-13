<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>帐号授权</title>
</head>
<body>
	<div>当前帐号${account.account },姓名${account.name } 所有角色列表</div>
	<div class="row-fluid">
		<div class="span4 well">
			<select multiple="multiple">
			</select>
		</div>
		<div class="span2 well">
			<button class="btn btn-primary">增加</button>
			<button class="btn btn-danger">删除</button>
		</div>
		<div class="span4 well">
			<select multiple="multiple">
			</select>
		</div>
	</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发送记录</title>
</head>
<body>
		<form id="queryForm" class="well form-inline " method="get">
	<label class="control-label" for="name">名称：</label>
	<input type="text" name="name" value="${merchant.name}" placeholder="请输入商户名称...">
	<input id="pageNo" name="pageNo" type="hidden">
	<input type="submit" class="btn btn-primary"
			value="查询">
	</form>
	<table class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="14%">顾客姓名</th>
				<th width="14%">顾客组别</th>
				<th width="14%">顾客手机号</th>
				<th width="14%">发送数量</th>
				<th width="14%">发送形式</th>
				<th width="16%">发送时间</th>
				<th width="14%">详情</th>
			</tr>
		</thead>
		<c:forEach items="${page.data}" var="merchant">
			<tr>
				<td>${merchant.name }</td>
				<td>${merchant.addr }</td>
				<td>
				<a class="btn btn-success" href="javascript:void();" onclick="toModify('${merchant.id}');">修改</a>
				<a class="btn btn-danger" href="javascript:void();" onclick="deleteById('${merchant.id}');">删除</a>
		
				</td>
			</tr>
		</c:forEach>
	</table>
	<div>
		<ul class="pagination">
			<li id="firstPage"><a href="javascript:void();">首页</a></li>
			<li id="frontPage"><a href="javascript:void();">前一页</a></li>
			<li id="nextPage"><a href="javascript:void();">后一页</a></li>
			<li id="lastPage"><a href="javascript:void();">尾页</a></li>
			<li >当前第${page.currentPage}/${page.totalPageCount}页共${page.totalRecords }条 转到第<input
			type="text" id="pageNoToGo" name="pageNo" class="input-mini">页<input
			type="button" id="pageOk" class="btn" value="确定"></input></li>
		</ul>
	</div>
</body>
</html>
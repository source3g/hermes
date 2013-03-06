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
	<form id="queryForm"  method="get">

		<input id="pageNo" name="pageNo" type="hidden"> 
	</form>
	<table
		class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="30%">商户名称</th>
				<th width="35%">预存记录</th>
				<th width="35%">日期</th>
			</tr>
		</thead>
		<c:forEach items="${page.data}" var="msgLog">
		<tr>
			<td>${merchant.name}</td>
			<td>${msgLog.count}</td>
			<td>${msgLog.chargeTime}</td>
		</tr>
		</c:forEach>
	</table>
	<div>
		<ul class="pagination">
			<li id="firstPage"><a href="javascript:void();">首页</a></li>
			<li id="frontPage"><a href="javascript:void();">前一页</a></li>
			<li id="nextPage"><a href="javascript:void();">后一页</a></li>
			<li id="lastPage"><a href="javascript:void();">尾页</a></li>
			<li>当前第${page.currentPage}/${page.totalPageCount}页共${page.totalRecords }条 转到第<input
				type="text" id="pageNoToGo" name="pageNo" class="input-mini">页<input
				type="button" id="pageOk" class="btn" value="确定"></input></li>
		</ul>
	</div>
	<script type="text/javascript">
	$(document).ready(function(){
		initPage(${page.currentPage},${page.totalPageCount});
});
	function goToPage(pageNo){
		$("#pageNo").attr("value",pageNo);
		var options={
			    url:"${pageContext.request.contextPath}/admin/merchant/reservedMsgLog/${merchant.id}/",
				success:showContentInfo,
				error:showError
		};
		$('#queryForm').ajaxSubmit(options);
		
	}
	function showError() {
		$("#resultMessage").html("操作失败，请重试");
		$("#errorModal").modal();
	}
	</script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>短信预存明细</title>
</head>
<body>
	<form id="queryForm" class="well form-inline " method="get" action="${pageContext.request.contextPath}/admin/merchant/messageInfo/list/">
		<label class="control-label" for="name">名称：</label>
		<input type="text" name="name" value="${merchant.name}" placeholder="请输入商户名称...">
		<input type="text" name="account" value="${merchant.account}" placeholder="请输入商户账号...">
		<input id="pageNo" name="pageNo" type="hidden">
		<input type="submit" class="btn btn-primary" value="查询">
	</form>
	<table class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="20%">商户名称</th>
				<th width="15%">预存短信数量</th>
				<th width="15%">已使用短信数量</th>
				<th width="15%">可用短信数量</th>
				<th width="35%">短信操作</th>
			</tr>
		</thead>
		<c:forEach items="${page.data}" var="merchant">
			<c:if test="${merchant.canceled==false }">
				<tr>
					<td>${merchant.name }</td>
					<td>${merchant.messageBalance.totalCount}</td>
					<td>${merchant.messageBalance.sentCount}</td>
					<td>${merchant.messageBalance.surplusMsgCount}</td>
					<td>
						<a class="btn btn-success" href="${pageContext.request.contextPath}/admin/merchant/toReservedMsg/${merchant.id}/">短信预存</a>
						<a class="btn btn-success" href="${pageContext.request.contextPath}/admin/merchant/reservedMsgLog/${merchant.id}/">预存记录</a>
						<a class="btn btn-success" href="${pageContext.request.contextPath}/admin/merchant/toUpdateQuota/${merchant.id}/" >额度调整</a>
					</td>
				</tr>
			</c:if>
		</c:forEach>
	</table>
	<div>
		<ul class="pagination">
			<li id="firstPage">
				<a href="javascript:void();">首页</a>
			</li>
			<li id="frontPage">
				<a href="javascript:void();">前一页</a>
			</li>
			<li id="nextPage">
				<a href="javascript:void();">后一页</a>
			</li>
			<li id="lastPage">
				<a href="javascript:void();">尾页</a>
			</li>
			<li>
				当前第${page.currentPage}/${page.totalPageCount}页 共${page.totalRecords }条转到第
				<input type="text" id="pageNoToGo" name="pageNo" class="input-mini">
				页
				<input type="button" id="pageOk" class="btn" value="确定"></input>
			</li>
		</ul>
	</div>
	<script type="text/javascript">
	$(document).ready(function(){
		activeMenu("shortMsgInfo");
    	initPage(${page.currentPage},${page.totalPageCount});
	});
	function goToPage(pageNo){
		$("#pageNo").attr("value",pageNo);
		$('#queryForm').submit();
	}
	</script>
</body>
</html>
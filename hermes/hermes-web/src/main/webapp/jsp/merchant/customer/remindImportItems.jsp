<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>导入日志商户信息</title>
</head>
<body>
	<table class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="20%">顾客电话</th>
				<th width="20%">提醒标题</th>
				<th width="20%">提醒时间</th>
				<th width="20%">导入状态</th>
				<th width="20%">失败原因</th>
			</tr>
		</thead>
		<c:forEach items="${page.data}" var="customerRemindImportItem">
			<tr>
				<td>${customerRemindImportItem.phone }</td>
				<td>${customerRemindImportItem.remindTitle }</td>
				<td>${customerRemindImportItem.remindTime }</td>
				<td>${customerRemindImportItem.importStatus }</td>
				<td>${customerRemindImportItem.reasion }</td>
			</tr>
		</c:forEach>
	</table>

	<div>
		<ul class="pagination">
			<li id="firstPage"><a href="javascript:void();">首页</a></li>
			<li id="frontPage"><a href="javascript:void();">前一页</a></li>
			<li id="nextPage"><a href="javascript:void();">后一页</a></li>
			<li id="lastPage"><a href="javascript:void();">尾页</a></li>
			<li>当前第${page.currentPage}/${page.totalPageCount}页 共${page.totalRecords }条 转到第<input type="text" id="pageNoToGo" name="pageNo" class="input-mini">页<input type="button" id="pageOk" class="btn" value="确定"></input>
			</li>
		</ul>
	</div>
	<script type="text/javascript">
		$(document).ready(function(){
			activeMenu("customerList");
			initPage(${page.currentPage},${page.totalPageCount});
		});
		
		function goToPage(pageNo){
			loadPage("${pageContext.request.contextPath}/merchant/customer/importLog/items/${logId}/?pageNo="+pageNo);
		}
	
	</script>

</body>
</html>
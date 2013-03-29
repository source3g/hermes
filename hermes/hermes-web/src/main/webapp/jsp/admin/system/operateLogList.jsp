<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Jms失败记录</title>
</head>
<form  id="findByTime" class="well form-inline " method="get">
<span>开始查询日期：</span><input type="text" class="input-medium" name="startTime"
					value="${startTime}" placeholder="请输入开始日期..."
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" />
	<span>结束查询日期：</span><input type="text" class="input-medium" name="endTime"
					value="${endTime}" placeholder="请输入结束日期..."
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" />	
					<input type="submit" value="查询" class="btn btn-primary"/>			
</form>
<form id="queryForm"  method="get">
		<input id="pageNo" name="pageNo" type="hidden"> 
		<c:if test="${not empty startTime}"><input name="startTime" type="hidden" value="${startTime}"> </c:if>
		<c:if test="${not empty endTime}"><input name="endTime" type="hidden" value="${endTime}"> </c:if>
	</form>
<body>
	<table class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="20%">API名称</th>
				<th width="20%">方法名</th>
				<th width="20%">请求参数</th>
				<th width="20%">操作时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.data}" var="operatorLog">
				<tr>
					<td>${operatorLog.className}</td><td>${operatorLog.methodName}</td><td>${operatorLog.args}</td><td>${operatorLog.operateTime}</td>
				</tr>
			</c:forEach>
		</tbody>
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
	
	$("#findByTime").submit(function (){
		var options={
				url:"${pageContext.request.contextPath}/admin/system/toLogList/",
				success:showContentInfo,
				error:showError
		};
		$('#findByTime').ajaxSubmit(options);
		return false;
	});
	});
	function goToPage(pageNo){
		$("#pageNo").attr("value",pageNo);
		var options={
			    url:"${pageContext.request.contextPath}/admin/system/toLogList/",
				success:showContentInfo,
				error:showError
		};
		$('#queryForm').ajaxSubmit(options);
		return false;
	}
	
	
	</script>
</body>
</html>
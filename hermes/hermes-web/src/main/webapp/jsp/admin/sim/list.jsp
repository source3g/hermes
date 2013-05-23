<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查询商户</title>
</head>
<body>
	<form id="queryForm" class="well form-inline " method="get">
	<label class="control-label" for="no">SIM卡号：</label>
	<input type="text" name="serviceNo" value="${sim.serviceNo}" placeholder="请输入SIM卡号...">
	<input type="text" name="imsiNo" value="${sim.imsiNo}" placeholder="请输入IMSI卡号...">
	<input id="pageNo" name="pageNo" type="hidden">
	<input type="submit" class="btn btn-primary"
			value="查询">
	</form>
	<table class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="25%">用户名</th>
				<th width="25%">SIM卡号</th>
				<th width="25%">simUimCard号</th>
				<th width="25%">imsi编码</th>
			</tr>
		</thead>
		<c:forEach items="${page.data}" var="simInfo">
			<tr>
				<td>${simInfo.username }</td>
				<td>${simInfo.serviceNo }</td>
				<td>${simInfo.simUimCardNo }</td>
				<td>${simInfo.imsiNo }</td>
			</tr>
		</c:forEach>
	</table>
	<div>
		<ul class="pagination">
			<li id="firstPage"><a href="javascript:void();">首页</a></li>
			<li id="frontPage"><a href="javascript:void();">前一页</a></li>
			<li id="nextPage"><a href="javascript:void();">后一页</a></li>
			<li id="lastPage"><a href="javascript:void();">尾页</a></li>
			<li >当前第${page.currentPage}/${page.totalPageCount}页 转到第<input
			type="text" id="pageNoToGo" name="pageNo" class="input-mini">页<input
			type="button" id="pageOk" class="btn" value="确定"></input></li>
		</ul>
	</div>
	<div id="errorModal" class="modal hide fade">
		<div class="modal-body">
			<p id="resultMessage"></p>
		</div>
		<div class="modal-footer">
			<a href="#" class="btn" data-dismiss="modal">确定</a>
		</div>
	</div>
	
	<script type="text/javascript">
	
		function goToPage(pageNo){
			$("#pageNo").attr("value",pageNo);
			var options={
					url:"${pageContext.request.contextPath}/admin/sim/list/",
					success:showContentInfo,
					error:showError
			};
			$('#queryForm').ajaxSubmit(options);
			
		}
		function showError() {
			$("#resultMessage").html("操作失败，请重试");
			$("#errorModal").modal();
		}
	    $(document).ready(function(){
	    	$('#queryForm').submit(function(){
	    		goToPage(1);
	    		return false;
	    	});
	    	initPage(${page.currentPage},${page.totalPageCount});
	});
	</script>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form id="queryForm" class="well form-inline " method="get">
		<label class="control-label" for="account">帐号名称：</label> <input
			type="text" name="account" class="input-medium"
			placeholder="请输入顾客名称..."> <label class="control-label"
			for="name">姓名：</label> <input type="text" name="name"
			class="input-medium" placeholder="请输入顾客电话..."> <input
			id="pageNo" name="pageNo" type="hidden"> <input type="submit"
			class="btn btn-primary" value="查询">
	</form>

	<table
		class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="30%">帐号名</th>
				<th width="30%">姓名</th>
				<th width="40%">操作</th>
			</tr>
		</thead>
		<c:forEach items="${page.data}" var="account">
			<tr>
				<td>${account.account }</td>
				<td>${account.name }</td>
				<td><a class="btn btn-success" href="javascript:void();"
					onclick="toModify('${account.id}');">修改</a> <a
					class="btn btn-danger" href="javascript:void();"
					onclick="deleteById('${account.id}');">删除</a>
					<a
					class="btn btn-danger" href="javascript:void();"
					onclick="grantById('${account.id}');">授权</a>
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
			<li>当前第${page.currentPage}/${page.totalPageCount}页
				共${page.totalRecords }条 转到第<input type="text" id="pageNoToGo"
				name="pageNo" class="input-mini">页<input type="button"
				id="pageOk" class="btn" value="确定"></input>
			</li>
		</ul>
	</div>



	<script type="text/javascript">
		function deleteById(id) {
		$.ajax({
			url:"${pageContext.request.contextPath}/admin/security/account/delete/"+id+"/",
			type:"get",
			success:showContentInfo		
		});	
	}
		function toModify(id){
		loadPage("${pageContext.request.contextPath}/admin/security/account/toUpdate/"+id+"/");
	}
		
		function goToPage(pageNo){
			$("#pageNo").attr("value",pageNo);
			var options={
					url:"${pageContext.request.contextPath}/admin/security/account/list/",
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
	    
	    function grantById(id){
	    	$.get("${pageContext.request.contextPath}/admin/security/account/grant/"+id+"/",showContentInfo);
	    }
	</script>
</body>
</html>
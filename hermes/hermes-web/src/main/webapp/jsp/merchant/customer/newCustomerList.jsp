<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>未编辑顾客列表</title>
</head>
<body>
	<form id="queryForm" class="well form-inline " method="get" action="${pageContext.request.contextPath}/merchant/customer/newCustomerList/">
		<label class="control-label" for="name">电话：</label> <input type="text"
			name="phone" value="${customer.phone}" placeholder="请输入电话号码..."> <input
			id="pageNo" name="pageNo" type="hidden"> <input type="submit"
			class="btn btn-primary" value="查询">
	</form>

	<table
		class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="40%">电话</th>
				<th width="40%">最后通话时间</th>
				<th width="20%">操作</th>
			</tr>
		</thead>
		<c:forEach items="${page.data}" var="customer">
			<tr>
				<td width="40%">${customer.phone }</td>
				<td width="40%">${customer.lastCallInTime }</td>
				<td width="20%"><a class="btn btn-success"
					href="javascript:void();" onclick="editById('${customer.id}');">编辑</a></td>
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
		$(document).ready(function(){
			activeMenu("customerList");
			initPage(${page.currentPage},${page.totalPageCount});
		});
		function editById(id) {
			loadPage("${pageContext.request.contextPath}/merchant/customer/toUpdate/" + id + "/?isNewCustomer=true");
		}
		
		function goToPage(pageNo){
			$("#pageNo").attr("value",pageNo);
			$('#queryForm').submit();
		}
	</script>
</body>
</html>
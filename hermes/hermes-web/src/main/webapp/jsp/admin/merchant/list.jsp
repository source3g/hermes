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
	<table class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="30%">名称</th>
				<th width="30%">地址</th>
				<th width="40%">操作</th>
			</tr>
		</thead>
		<c:forEach items="${merchants}" var="merchant">
			<tr>
				<td>${merchant.name }</td>
				<td>${merchant.addr }</td>
				<td>
				<a class="btn btn-success" href="javascript:void();" onclick="toModify('${merchant.id}');">修改</a>
				<a class="btn btn-danger" href="javascript:void();" onclick="deleteById('${merchant.id}');">删除</a></td>
				
			</tr>
		</c:forEach>
	</table>
	<script type="text/javascript">
		function deleteById(id) {
	
		$.ajax({
			url:"${pageContext.request.contextPath}/admin/merchant/delete/"+id+"/",
			type:"get",
			success:showList		
		});
		
	}
		function showList(data){
			loadPage("${pageContext.request.contextPath}/admin/merchant/list/");
		}
		function toModify(id){
		loadPage("${pageContext.request.contextPath}/admin/merchant/toModify/"+id+"/");
	}
	
		
	</script>
</body>
</html>
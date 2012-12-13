<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>角色管理</title>
</head>
<body>
	<h3>角色列表</h3>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th>名称</th>
				<th>操作</th>
			</tr>
		</thead>
		<c:forEach items="${roles }" var="role">
			<tr>
				<td>${role.name }</td>
				<td><a class="btn btn-success" href="javascript:void();"
					onclick="showResourceList('${role.id}');">查看资源列表</a> <a
					class="btn btn-primary" href="javascript:void();"
					onclick="updateById('${role.id}');">修改</a> <a
					class="btn btn-danger" href="javascript:void();"
					onclick="deleteById('${role.id}');">删除</a></td>
			</tr>
		</c:forEach>
	</table>



	<div id="myModal" class="modal hide fade">
		<div class="modal-header">
			<a class="close" data-dismiss="modal">&times;</a>
			<h3>角色资源列表</h3>
		</div>
		<div class="modal-body">
			<table id="resourceTable" class="table table-bordered table-striped">
				<thead>
					<tr>
						<th>名称</th>
						<th>代码</th>
					</tr>
				</thead>
			</table>
		</div>
		<div class="modal-footer"></div>
	</div>


	<script type="text/javascript">
		function showResourceList(id) {
			$("#resourceTable tr:gt(0)").each(function() {
				$(this).remove();
			});
			$.ajax({
				type : "get",
				url : "${pageContext.request.contextPath}/admin/security/role/get/" + id + "/",
				dataType : "json",
				success : drawTable,
				error : showError
			});
			$("#myModal").modal();
		}
		function drawTable(data) {
			for ( var i = 0; i < data.resources.length; i++) {
				var str = $("<tr><td>" + data.resources[i].name + "</td> <td>" + data.resources[i].code + "</td></tr>"); //注意拼接字符串前加上$
				$("#resourceTable").append(str);//添加
			}
		}
		
		function updateById(id){
			var url= "${pageContext.request.contextPath}/admin/security/role/toUpdate/" + id + "/";
			$.get(url,showContentInfo);
		}
	</script>
</body>
</html>
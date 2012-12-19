<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>帐号授权</title>
</head>
<body>
	<div>
		当前帐号${account.account },姓名${account.name } 所有角色列表 <a
			class="btn btn-success" href="javascript:void();"
			onclick="addRole('${account.id}');">增加角色</a>
	</div>
	<table
		class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="60%">角色名</th>
				<th width="40%">操作</th>
			</tr>
		</thead>
		<c:forEach items="${account.roles}" var="r">
			<tr>
				<td>${r.name }</td>
				<td><a class="btn btn-danger" href="javascript:void();"
					onclick="deleteById('${account.id }','${r.id}');">删除</a></td>
			</tr>
		</c:forEach>
	</table>

	<div id="myModal" class="modal hide fade">
		<div class="modal-header">
			<a class="close" data-dismiss="modal">&times;</a>
			<h3>角色资源列表</h3>
		</div>
		<div class="modal-body">
			<table id="roleTab" class="table table-bordered table-striped">
				<thead>
					<tr>
						<th>选择</th>
						<th>名称</th>
					</tr>
				</thead>
			</table>
		</div>
		<div class="modal-footer">
			<button class="btn btn-primary"
				onclick="grantRoles('${account.id}');">选择</button>
		</div>
	</div>


	<script type="text/javascript">
		function addRole(id) {
			$("#roleTab tr:gt(0)").each(function() {
				$(this).remove();
			});

			$.ajax({
				type : "get",
				url : "${pageContext.request.contextPath}/admin/security/role/notGrant/" + id + "/",
				dataType : "json",
				success : drawTable,
				error : showError
			});
			$("#myModal").modal();
		}

		function drawTable(data) {
			for ( var i = 0; i < data.length; i++) {
				var str = $("<tr><td><input name='roleCheckBox' type='checkbox' value='"+data[i].id+"' ></td><td>" + data[i].name + "</td> </tr>"); //注意拼接字符串前加上$
				$("#roleTab").append(str);//添加
			}
		}
		function grantRoles(id) {
			var data = {
				"roleIds" : function() {
					var ids = new Array();
					$("input:checked[name='roleCheckBox']").each(function() {
						ids.push($(this).val());
					});
					return ids;
				}
			};
			$.post("${pageContext.request.contextPath}/admin/security/account/grant/" + id + "/", data, callBack);
		}
		function callBack(data) {
			$("#myModal").modal("hide");
			showContentInfo(data);
		}
		function deleteById(accountId,id){
			$.get("${pageContext.request.contextPath}/admin/security/account/"+accountId+"/role/recover/"+id+"/",showContentInfo);
		}
	</script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>顾客组管理</title>
</head>
<body>
	<h3>顾客组操作</h3>
	<form id="addCustomerGroupForm" class="form-inline">
		<label class="control-label" for="name">名称：</label> <input type="text"
			class="input-xlarge" placeholder="请输入顾客组名称..." id="name" name="name">
		<span class="help-inline"><font color="red">*</font></span> <input
			class="btn btn-primary" type="submit" value="增加">
	</form>

	<h3>顾客组列表</h3>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th>名称</th>
				<th>操作</th>
			</tr>
		</thead>
		<c:forEach items="${customerGroups }" var="customerGroup">
			<tr>
				<td>${customerGroup.name }</td>
				<td><a class="btn btn-danger" href="javascript:void();"
					onclick="deleteById('${customerGroup.id}');">删除</a></td>
			</tr>
		</c:forEach>
	</table>
	<script type="text/javascript">
		$(document).ready(function() {
			if(${not empty error}==true){
				alert("${error}");
			}
			var submitOptions = {
				url : "${pageContext.request.contextPath}/merchant/customerGroup/add",
				type : "post",
				success : showList
			}

			$("#addCustomerGroupForm").validate({
				rules : {
					name : {
						required : true,
						remote:{
							type: "get",
							url:"${pageContext.request.contextPath}/merchant/customerGroup/nameValidate",
							data:{"name":function(){
												return $('#name').val();
											}
								}
						}
					}
				},
				messages : {
					name : {
						required : "请填写顾客组名称",
						remote:		"顾客组名称已存在"
					}
				}
			});

			$("#addCustomerGroupForm").submit(function() {
				if (!$("#addCustomerGroupForm").valid()) {
					return;
				}
				$(this).ajaxSubmit(submitOptions);
				return false;
			});
		});

		function deleteById(id) {
			$.ajax({
				url : "${pageContext.request.contextPath}/merchant/customerGroup/delete/" + id + "/",
				type : "get",
				success : showList
			});
		}
		
		function showList(data) {
			$("#pageContentFrame").html(data);
		}
	</script>
</body>
</html>
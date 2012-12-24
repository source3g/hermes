<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>角色添加</title>
</head>
<body>
	<form id="addRoleForm" method="post"
		action="${pageContext.request.contextPath}/admin/security/role/add/"
		class="form-horizontal">
		<div class="control-group">
			<label class="control-label" for="name">角色名称：</label>
			<div class="controls">
				<c:if test="${not empty role }">
					<input type="hidden" value="${role.id}" name="id">
				</c:if>
				<input type="text" class="input-xlarge" placeholder="请输入角色名称..."
					id="name" name="name" value="${role.name }"> <span
					class="help-inline"></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="resources">填写资源代码：</label>
			<div class="controls">
				<input type="text" class="input-xlarge" placeholder="请输入资源名称..."
					id="resourceCode" name="resourceCode" >
			<!-- 	<input type="button" class="btn btn-primary"
					onclick="addResource();" value="添加"> -->
				<button id="addResourceBtn" data-loading-text="角色增加中..." class="btn btn-primary" onclick="addResource();">
                    		增加
                 </button>	
					 <span
					class="help-inline"></span>
			</div>
		</div>
		<h3 class="caption">资源列表：</h3>

		<table
			class="table table-striped table-bordered bootstrap-datatable datatable"
			id="resourceTable">
			<thead>
				<tr>
					<th width="35%">资源名称</th>
					<th width="35%">资源代码</th>
					<th width="30%">操作</th>
				</tr>

			</thead>
			<c:forEach items="${role.resources}" var="resource">
				<tr>
					<td>${resource.name}</td>
					<td class="codeTd">${resource.code}</td>
					<td><input type='button' name='deleteDeviceSn'
						class='btn btn-danger' onclick='deleteDevice(this)' value='删除'>
						<input type='hidden' name='resourceIds' value='${resource.id}'></td>
				</tr>
			</c:forEach>
		</table>
		<div class="form-actions">
			<c:if test="${ empty role }">
				<input type="submit" class="btn btn-primary" value="增加">
			</c:if>
			<c:if test="${not empty role }">
				<input type="button" onclick="update();" class="btn btn-primary" value="修改">
			</c:if>
		</div>
	</form>
	<script type="text/javascript">
		$(document).ready(function() {
			if(${not empty error}==true){
				alert("${error}");
			}
 			var validateOptions = {
					rules :{
						name:{
							required : true,
			 		 		remote:{
								type: "get",
								url:"${pageContext.request.contextPath}/admin/security/nameValidate/",
								data:{"name":function(){
													return $('#name').val();
												}
									}
							}  
						}
					},
					messages:{
						name:{
							required : "角色名称不能为空",
						 	remote: "该账号已存在" 
						}
				}			
			}; 
			$('#addRoleForm').validate(validateOptions);
			$("#addRoleForm").submit(function() {
				if (!$('#addRoleForm').valid()) {
					return false;
				}
				$('#addResourceBtn').button('loading');
				$(this).ajaxSubmit({
					success : showContentInfo,
					error : showError
				});
				return false;
			});
		});
		function addResource() {
			var code = $("#resourceCode").val();
			if (inTable(code) == true) {
				alert("该资源已在列表中");
				return;
			}
			$.ajax({
						type : "get",
						url : "${pageContext.request.contextPath}/admin/security/resource/" + code,
						success : function(data) {
							if (data != null) {
								var td = $("<tr><td>"
										+ data.name
										+ "</td><td class='codeTd'>"
										+ data.code
										+ " </td> <td><input type='hidden' name='resourceIds' value='"+data.id+"'> <input type='button' value='删除' class='btn btn-primary' onclick='deleteResource(this);'> </td> </tr>");
								$("#resourceTable").append(td);
							} else {
								alert("资源不存在");
							}
						},
						dataType : "json",
						error : showError
					});
		}
		
		function update(){
			$("#addRoleForm").ajaxSubmit({
				url:"${pageContext.request.contextPath}/admin/security/role/update",
				type:"post",
				success : showContentInfo,
				error : showError
			});
		}

		function inTable(code) {
			var isInTable = false;
			$("#resourceTable tr:gt(0)").each(function(index) {
				var tdInfo = $(this).children(".codeTd").html();
				if (tdInfo.trim() == code.trim()) {
					isInTable = true;
				}
			});
			return isInTable;
		}
	</script>
</body>
</html>
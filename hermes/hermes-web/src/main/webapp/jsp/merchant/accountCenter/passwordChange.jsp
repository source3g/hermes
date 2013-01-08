<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../../include/import.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>短信发送</title>
</head>
<body>
	<form id="passworChangeForm" class="well ">
		<table class="table table-bordered">
		<thead>
			<tr>
				<th colspan="2"><center>
							<h4 >密码修改 </h4>
						</center></th>
				</tr>
		</thead>
		<tbody>
			  	<tr>
					<td width="20%"><label class="control-label">请输入原始密码：</label></td>
					<td colspan="4">
							<input id="password" name="password" type="password" >			
					</td>
				</tr>
					<tr>
					<td width="20%"><label class="control-label">请输入新密码密码：</label></td>
					<td colspan="4">
							<input id="newPassword" name="newPassword" type="password" >			
					</td>
				</tr>
					<tr>
					<td width="20%"><label class="control-label">请再输入一次新密码：</label></td>
					<td colspan="4">
							<input id="newPassword1" name="newPassword1" type="password" >			
					</td>
				</tr>
				<tr>
					<td colspan="4">
							<input  type="submit"  value="确定 " class="btn btn-primary"  >			
					</td>
				</tr>
			</tbody>
		</table>
	</form>
	<script type="text/javascript">
	$(document).ready(function() {
		var validateOptions = {
				rules : { 
					password:{
						 required : true
					},
					newPassword:{
						required : true,
						minlength : 6	
					}, 
					newPassword1:{
						required : true,
						minlength : 6,
						equalTo: "#newPassword" 
					}
				},
				messages : {
					password : {
						required : "请填写账号"
					},
					newPassword : {
						required : "请填写新密码",
						minlength : "至少输入六个字符"
					}, 
					newPassword1:{
						required : "请再次填写新密码",
						minlength : "至少输入六个字符",
						equalTo: "密码必须一致"
					}
				}	
		};
		$('#passworChangeForm').validate(validateOptions); 
		$('#passworChangeForm').submit(function() {
			var password=$('#password').val();
			var newPassword=$('#newPassword').val();
		 	if (!$("#passworChangeForm").valid()) {
		 		return false;
			 }
			 var options = {
					 url:"${pageContext.request.contextPath}/merchant/account/passwordChange/"+password+"/"+newPassword+"/",
					 type:"get",
				success : showContentInfo
			}; 
			$(this).ajaxSubmit(options);
			return false;
		});
	});
	</script>
</body>
</html>
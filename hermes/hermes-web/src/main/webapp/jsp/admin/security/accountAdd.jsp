<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>帐号管理</title>
</head>
<body>
	<form id="addAccountForm" method="post"
		action="${pageContext.request.contextPath}/admin/security/account/add/"
		class="form-horizontal">
		<div class="control-group">
			<label class="control-label" for="account">账号：</label>
			<div class="controls">
				<input type="text" class="input-xlarge" placeholder="请输入账号..."
					id="account" name="account"> <span class="help-inline"></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="password">密码：</label>
			<div class="controls">
				<input type="password" class="input-xlarge" placeholder="请输入密码..."
					id="password" name="password"> <span class="help-inline"></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="password1">重复密码：</label>
			<div class="controls">
				<input type="password" class="input-xlarge" placeholder="请再次输入密码..."
					id="password1" name="password1"> <span class="help-inline"></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="name">性名：</label>
			<div class="controls">
				<input type="text" class="input-xlarge" placeholder="请再次输入密码..."
					id="name" name="name"> <span class="help-inline"></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="note">备注：</label>
			<div class="controls">
				<input type="text" class="input-xlarge" placeholder="请再次输入密码..."
					id="note" name="note"> <span class="help-inline"></span>
			</div>
		</div>

		<div class="form-actions">
			<button id="addAccountBtn" data-loading-text="账号增加中..." class="btn btn-primary">
                    		增加
                 </button>
		</div>
		
		<div id="errorModal" class="modal hide fade">
		<div class="modal-body">
			<p id="resultMessage"></p>
		</div>
		<div class="modal-footer">
			<a href="#" class="btn" data-dismiss="modal">确定</a>
		</div>
		</div>
	</form>
	<script type="text/javascript">
		$(document).ready(function() {
			initDialog();
			if(${not empty error}==true){
				alert("${error}");
			}
			var validateOptions = {
				rules : {
					account : {
						required : true,
						minlength : 5,
			 	 		remote:{
							type: "get",
							url:"${pageContext.request.contextPath}/admin/security/accountValidate/",
							data:{"account":function(){
												return $('#account').val();
											}
								}
						}  
					},
					
					password : {
						required : true,
						minlength : 5
					},
					password1 : {
						required : true,
						minlength : 5,
						equalTo : "#password"
					},
					name : {
						required : true,
						minlength : 2
					}
				},
				messages : {
					account : {
						required : "请填写账号",
						minlength : "至少输入五个字符",
					 	 remote:"该账号已存在"  
					},
					password : {
						required : "请填写密码",
						minlength : "至少输入五个字符"
					},
					password1 : {
						required : "请填写密码",
						minlength : "至少输入五个字符",
						equalTo : "密码必须一致"
					},
					name : {
						required : "请填写姓名",
						minlength : "至少输入两个字符"
					}
				}
			};
			$('#addAccountForm').validate(validateOptions);

			$("#addAccountForm").submit(function() {
				if (!$('#addAccountForm').valid()) {
					return false;
				}
				$('#addAccountBtn').button('loading')
				$("#addAccountForm").ajaxSubmit({
					success:showContentInfo
				});
				
				return false;
			});
		});

		function initDialog(){
			if(${not empty success }==true){
				$('#addAccountForm').clearForm();
				$("#resultMessage").html("操作成功！");
				$("#errorModal").modal({
					backdrop:true,
				    keyboard:true,
				    show:true
				});
			}
		} 
	</script>
</body>
</html>
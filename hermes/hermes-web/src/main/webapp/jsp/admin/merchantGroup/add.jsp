<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加集团商户</title>

</head>
<body>
	<form id="addMerchantForm"
		<c:if test="${empty update }">
			action="${pageContext.request.contextPath}/admin/merchantGroup/add/"
		</c:if>
		<c:if test="${not empty update }">
			action="${pageContext.request.contextPath}/admin/merchantGroup/update/"
		</c:if>
		method="post" class="form-horizontal">

		<div class="control-group">
			<label class="control-label" for="name">名称：</label>
			<div class="controls">
				<input type="text" class="input-xlarge " placeholder="请输入集团商户名称..."
					id="name" name="name" value="${merchantGroup.name }"><font
					color="red">*</font>
				<c:if test="${not empty update }">
					<input type="hidden" id="strId" name="strId"
						value="${merchantGroup.id }">
				</c:if>
			</div>
		</div>

		<div class="form-actions">
			<c:if test="${not empty update }">
				<input class="btn btn-primary" type="button" onclick="modify();"
					value="修改">
			</c:if>

			<c:if test="${ empty update}">
				<input type="submit" class="btn btn-primary" value="增加">
			</c:if>
		</div>
	</form>

	<c:if test="${not empty success }">

	</c:if>

	<c:if test="${not empty errors }">
		<div class="alert alert-error">
			<ul>
				<c:forEach items="${errors }" var="error">
					<li>${error.defaultMessage }</li>
				</c:forEach>
			</ul>
		</div>
	</c:if>

	<div id="errorModal" class="modal hide fade">
		<div class="modal-body">
			<p id="resultMessage"></p>
		</div>
		<div class="modal-footer">
			<a href="#" class="btn" data-dismiss="modal">确定</a>
		</div>
	</div>

	<script type="text/javascript">
		$(document).ready(function() {
			var options = {
				success : toList, // post-submit callback
				error : showError
			};

			$('#addMerchantForm').submit(function() {
				if (!$("#addMerchantForm").valid()) {
					return false;
				}
				$(this).ajaxSubmit(options);
				return false;
			});
			var validateOptions = {
				rules : {
					name : {
						required : true,
						minlength : 2
					}
				},
				messages : {
					name : {
						required : '请填写集团商户名称',
						minlength : '至少输入两个字符'
					}
				}
			};

			$("#addMerchantForm").validate(validateOptions);

			function showError() {
				$("#resultMessage").html("操作失败，请重试");
				$("#errorModal").modal();
			}
			
			initDialog();
			
		});

		function modify() {
			if (!$("#addMerchantForm").valid()) {
				return false;
			}
			$('#addMerchantForm').ajaxSubmit({
				success : toList
			});
		}
		function toList(data) {
			$("#pageContentFrame").html(data)
		}
		function initDialog(){
			if(${not empty success }==true){
				$('#addMerchantForm').clearForm();
				$("#resultMessage").html("操作成功！");
				$("#errorModal").modal();
			}
		}
	</script>
</body>
</html>
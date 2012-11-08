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
				<input type="text" class="input-xlarge" placeholder="请输入集团商户名称..."
					id="name" name="name" value="${merchantGroup.name }">
				<c:if test="${not empty update }">
					<input type="hidden" id="strId" name="strId"
						value="${merchantGroup.id }">
				</c:if>
				<span class="help-inline"><font color="red">*</font></span>
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
		<div class="alert alert-success">操作成功</div>
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
				// target:        '#output2',   // target element(s) to be updated with server response 
				// beforeSubmit:  showRequest,  // pre-submit callback 
				success : toList, // post-submit callback
				error : showError

			// other available options: 
			//url:       url         // override for form's 'action' attribute 
			//type:      type        // 'get' or 'post', override for form's 'method' attribute 
			//dataType:  null        // 'xml', 'script', or 'json' (expected server response type) 
			//clearForm: true        // clear all form fields after successful submit 
			//resetForm: true        // reset the form after successful submit 

			// $.ajax options can be used here too, for example: 
			//timeout:   3000 
			};

			// bind to the form's submit event 
			$('#addMerchantForm').submit(function() {
				// inside event callbacks 'this' is the DOM element so we first 
				// wrap it in a jQuery object and then invoke ajaxSubmit 
				$(this).ajaxSubmit(options);
				// !!! Important !!! 
				// always return false to prevent standard browser submit and page navigation 
				return false;
			});

			function showError() {
				$("#resultMessage").html("操作失败，请重试");
				$("#errorModal").modal();
			}
		});

		function modify() {
			$('#addMerchantForm').ajaxSubmit({
				success : toList
			});
		}
		function toList(data) {
			$("#pageContentFrame").html(data)
		}
	</script>
</body>
</html>
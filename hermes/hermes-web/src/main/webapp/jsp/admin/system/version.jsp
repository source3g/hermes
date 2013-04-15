<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>版本更新</title>
</head>
<body>
	<form id="importForm"
		action="${pageContext.request.contextPath}/admin/version/upload/"
		method="post" enctype="multipart/form-data" class="form-horizontal">
		<div class="control-group">
			<label class="control-label" for="fileUpload">请输入版本号：</label>
			<div class="controls">
				<input type="text" name="version" id="version">
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="fileUpload">请选择要上传的文件版本：</label>
			<div class="controls">
				<input type="file" name="file" id="fileUpload">
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">上传进度：</label>
			<div class="progress progress-striped progress-success active"
				style="width: 40%;">
				<div class="bar" style="width: 0%;">
					<font color="black" class="percent">0%</font>
				</div>
			</div>
		</div>

		<div class="form-actions">
			<input type="submit" class="btn btn-primary" value="上传"> <input
				id="backToList" type="button"
				onclick="loadPage('${pageContext.request.contextPath}/merchant/customer/list/');"
				class="btn btn-primary" value="返回" />
		</div>
	</form>

	<script type="text/javascript">
		$(document).ready(function() {
			initForm();
		});
		function initForm() {
			var bar = $('.bar');
			var percent = $('.percent');
			var status = $('#uploadStatus');
			$('#importForm').ajaxForm({
				beforeSend : function() {
					//status.empty();
					var percentVal = '0%';
					bar.css("width", percentVal);
					percent.html(percentVal);
				},
				uploadProgress : function(event, position, total, percentComplete) {
					var percentVal = percentComplete + '%';
					bar.css("width", percentVal);
					percent.html(percentVal);
				},
				complete : function(xhr) {
					if ("\"success\"" == xhr.responseText) {
						alert("上传成功");
						percent.html("上传成功");
					} else {
						alert("上传失败");
						percent.html("上传失败");
					}
				}
			});
		}
	</script>
</body>
</html>

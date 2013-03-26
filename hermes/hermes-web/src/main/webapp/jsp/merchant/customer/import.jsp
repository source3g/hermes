<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC
"-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>导入顾客信息</title>
</head>
<body>
	<form id="importForm"
		action="${pageContext.request.contextPath}/merchant/customer/import/"
		method="post" enctype="multipart/form-data" class="form-horizontal">
		<div class="control-group">
			<label class="control-label" for="fileUpload">请选择要上传的文件：</label>
			<div class="controls">
				<input id="file" type="file" name="file" id="fileUpload">
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">状态：</label>
			<!-- <div class="progress progress-striped progress-success active"
				style="width: 40%;"> -->
			<!-- <div class="bar" style="width: 0%;" > -->
			 <!-- <font color="black" id="status" class="percent"></font>  -->
			<img id="loader" alt="" style="display: none;"
				src="${pageContext.request.contextPath}/img/ajax-loader-7.gif">
			<!-- </div> -->
			<!-- </div> -->
		</div>

		<div class="form-actions">
			<input type="submit" id="uploadBtn" class="btn btn-primary"
				value="上传"> <input id="backToList" type="button"
				onclick="loadPage('${pageContext.request.contextPath}/merchant/customer/list/');"
				class="btn btn-primary" value="返回" />
		</div>
	</form>
	<script type="text/javascript">
		$("#importForm").submit(function() {
			$("#loader").css("display","");
			$(this).ajaxSubmit({
				success : showContentInfo
			});
			return false;
		});
	</script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>备忘录新增</title>
</head>
<body>

	<form id="addNote"
		action="${pageContext.request.contextPath}/merchant/note/add/"
		method="post" class="form-horizontal">
		<div class="control-group">
			<label class="control-label" for="content">提醒内容：</label>
			<div class="controls">
				<textarea rows="16" id="content" name="content" class="span8"></textarea>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="remindTime">提醒时间：</label>
			<div class="controls">
				<input type="text" id="remindTime" name="remindTime"
					class="input-xlarge"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" />
			</div>
		</div>

		<div class="form-actions">
			<input type="submit" class="btn btn-primary" value="增加" />
		</div>
	</form>

	<script type="text/javascript">
		$(document).ready(function() {
			$("#addNote").submit(function() {
				$(this).ajaxSubmit({
					success : showContentInfo,
					error : showError
				});
				return false;
			});

		});
	</script>
</body>
</html>
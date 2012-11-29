<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>短信模板</title>
</head>
<body>
	<div class="well">
		<div>
			选择模板： <select></select>
		</div>
	</div>
	<div>
		<form id="messageTemplateForm" method="post" class="form-horizontal">
			<table class="table table-bordered">
				<thead>
					<tr>
						<th colspan="2"><center>
								<h4>短信模板设置</h4>
							</center></th>
					</tr>
				</thead>
				<tr>
					<td width="20%"><label class="control-label">标题：</label></td>
					<td width="80%"><input type="text" name="title"
						class="input-xlarge" placeholder="请输入模板标题..." /></td>
				</tr>
				<tr>
					<td width="20%"><label class="control-label">内容：</label></td>
					<td width="80%"><textarea rows="16" class="span8"
							name="content"></textarea></td>
				</tr>
				<tr>
					<td width="20%"><label class="control-label">字数统计：</label></td>
					<td width="80%"><label>当前0个字，以1条短信发送</label></td>
				</tr>
				<tr>
					<td width="20%"><label class="control-label">操作：</label></td>
					<td width="80%"><a class="btn btn-success"
						href="javascript:void();" onclick="add();">保存</a>&nbsp; <a
						class="btn btn-danger" href="javascript:void();">删除</a>
				</tr>
			</table>
		</form>
	</div>
	<script type="text/javascript">
		function add() {
			alert("aaaa");
			$("#messageTemplateForm").ajaxSubmit({
				url : "${pageContext.request.contextPath}/merchant/message/template/add",
				success : showInfo,
				error : showError
			});
		}
	</script>
</body>
</html>
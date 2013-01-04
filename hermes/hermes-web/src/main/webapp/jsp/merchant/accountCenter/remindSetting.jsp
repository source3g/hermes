<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>提醒设置</title>
</head>
<body>
	<div>
		<form id="remindSettingForm" method="post" class="form-horizontal">
			<table class="table table-bordered">
				<thead>
					<tr>
						<th colspan="2"><center>
								<h4>定时提醒设置</h4>
							</center></th>
					</tr>
				</thead>
				<tr>
					<td width="20%"><label class="control-label">选择提醒：</label></td>
					<td width="80%"><input type="hidden" id="id" name="id" />
						<div>
							<select id="sel">
								<option>请选择</option>
								<c:forEach items="${merchantRemindTemplates}"
									var="merchantRemindTemplate">
									<option value="${merchantRemindTemplate.id}">${merchantRemindTemplate.remindTemplate.title}</option>
								</c:forEach>
							</select>

							<c:forEach items="${merchantRemindTemplates}"
								var="merchantRemindTemplate">
								<span id="${merchantRemindTemplate.id}" style="display: none;">${merchantRemindTemplate.messageContent}</span>
								<span id="content${merchantRemindTemplate.id}"
									style="display: none;">${merchantRemindTemplate.remindTemplate.messageContent}</span>
								<span id="advancedTime${merchantRemindTemplate.id}"
									style="display: none;">${merchantRemindTemplate.advancedTime}</span>
							</c:forEach>

						</div></td>
				</tr>
				<tr>
					<td width="20%"><label class="control-label">提前天数：</label></td>
					<td width="80%"> <input
						type="text" id="advancedTime" name="advancedTime"
						class="input-xlarge" placeholder="请输入提醒的提前天数..." /></td>
				</tr>
				<tr>
					<td width="20%"><label class="control-label">提醒内容：</label></td>
					<td width="80%"><textarea id="messageContent" rows="16"
							class="span8" name="messageContent"></textarea></td>
				</tr>
				<tr>
					<td width="20%"><label class="control-label">字数统计：</label></td>
					<td width="80%"><label>当前0个字，以1条短信发送</label></td>
				</tr>
				<tr>
					<td width="20%"><label class="control-label">操作：</label></td>
					<td width="80%"><a class="btn btn-success"
						href="javascript:void();" onclick="save();">保存</a>&nbsp; <a
						class="btn btn-success" href="javascript:void();"
						onclick="recover();">恢复</a>&nbsp;
				</tr>
			</table>
		</form>
		<c:if test="${not empty errors }">
			<div class="alert alert-error">
				<ul>
					<c:forEach items="${errors }" var="error">
						<li>${error.defaultMessage }</li>
					</c:forEach>
				</ul>
			</div>
		</c:if>
		<c:if test="${not empty success }">
			<div class="alert alert-success">操作成功</div>
		</c:if>
	</div>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#sel").change(function() {
				var messageContent = $("#" + $('#sel').val()).text();
				var advancedTime=$("#advancedTime"+$("#sel").val()).text();
				var title = $("#sel").find("option:selected").text();
				if (title == '请选择') {
					$("#id").html("");
					$("#title").attr("value", "");
					$("#advancedTime").attr("value","");
					$("#messageContent").val(messageContent);
					return;
				}
				$("#id").attr("value", $(this).val());
				$("#title").attr("value", title);
				$("#advancedTime").attr("value",advancedTime);
				$("#messageContent").val(messageContent);
			});
		});
		function recover() {
			var messageContent = $("#content" + $('#sel').val()).text();
			$("#messageContent").val(messageContent);
		}
		function save() {
			$("#remindSettingForm").ajaxSubmit({
				url : "${pageContext.request.contextPath}/merchant/account/remindSave",
				success : showContentInfo,
				error : showError
			});
		}
	</script>
</body>
</html>

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
	<div class="well">
		<div>
			选择提醒： <select id="sel">
				<option>请选择</option>
				<%-- <c:forEach items="${templates}" var="template">
					<option value="${template.id}">${template.title }</option>
				</c:forEach> --%>
			</select>
			<%-- <c:forEach items="${templates}" var="template">
				<span id="${template.id}" style="display: none;">${template.content}</span>
			</c:forEach> --%>

		</div>
	</div>
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
					<td width="20%"><label class="control-label">标题：</label></td>
					<td width="80%"><input type="hidden" id="id" name="id" /> <input
						type="text" id="title" name="title" class="input-xlarge"
						placeholder="请输入模板标题..." /></td>
				</tr>
				<tr>
					<td width="20%"><label class="control-label">短信内容：</label></td>
					<td width="80%"><textarea id="content" rows="16" class="span8"
							name="content"></textarea></td>
				</tr>
				<tr>
					<td width="20%"><label class="control-label">字数统计：</label></td>
					<td width="80%"><label>当前0个字，以1条短信发送</label></td>
				</tr>
				<tr>
					<td width="20%"><label class="control-label">操作：</label></td>
					<td width="80%"><a class="btn btn-success"
						href="javascript:void();" onclick="save();">保存</a>&nbsp; <a
						class="btn btn-success" href="javascript:void();" onclick="add();">新增</a>&nbsp;
						<a class="btn btn-danger" href="javascript:void();"
						onclick="deleteById();">删除</a>
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
			$.ajax({
				url : "${pageContext.request.contextPath}/merchant/message/template/listJson/",
				dataType : "json",
				success : initSel
			});

			$("#sel").change(function() {
				var content = $("#" + $(this).val()).text();
				var title = $("#sel").find("option:selected").text();
				if (title == '请选择') {
					$("#id").html("");
					$("#title").attr("value", "");
					$("#content").html("");
					return;
				}
				$("#id").attr("value", $(this).val());
				$("#title").attr("value", title);
				$("#content").html(content);
			});
		});
		function add() {
			$("#messageTemplateForm").ajaxSubmit({
				url : "${pageContext.request.contextPath}/merchant/message/template/add",
				success : showContentInfo,
				error : showError
			});
		}

		function save() {
			$("#messageTemplateForm").ajaxSubmit({
				url : "${pageContext.request.contextPath}/merchant/message/template/save",
				success : showContentInfo,
				error : showError
			});
		}

		function initSel(data) {
			for ( var i = 0; i < data.length; i++) {
				$("#sel").append("<option value='"+data[i].id+"'>" + data[i].title + "</option>");
				$("#sel").after("<span id="+data[i].id+" style='display: none;'>" + data[i].content + "</span>");
			}
		}

		function deleteById() {
			var title = $("#sel").find("option:selected").text();
			if (title == '请选择') {
				$("#id").html("");
				$("#title").attr("value", "");
				$("#content").html("");
				return;
			}
			var id = $("#id").val();
			$.ajax({
				url : "${pageContext.request.contextPath}/merchant/message/template/delete/" + id + "/",
				type : "get",
				success : function(data) {
					alert("删除成功");
					showContentInfo(data);
				}
			});
		}
	</script>
</body>
</html>
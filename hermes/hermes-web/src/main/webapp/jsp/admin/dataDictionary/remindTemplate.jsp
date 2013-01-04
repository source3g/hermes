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
				<c:forEach items="${remindTemplate}" var="template">
					<option value="${template.id}">${template.title }</option>
				</c:forEach>
			</select>
		<%-- 	<c:forEach items="${remindTemplate}" var="template">
				<span id="${template.id}" style="display: none;">${template.messageContent}</span>
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
						placeholder="请输入提醒标题..." /></td>
				</tr>
				<tr>
					<td width="20%"><label class="control-label">提前天数：</label></td>
					<td width="80%"><input type="text" id="advancedTime"
						name="advancedTime" class="input-xlarge"
						placeholder="请输入提醒提前的天数..." /></td>
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
 			$('#remindSettingForm').validate({
				rules : {
					title : {
						required : true
					},
					advancedTime:{
						required : true,
						number:true,
						digits:true
					}
				},
				messages : {
					title : {
						required : "[标题不能为空]"
					},
					advancedTime:{
						required : "[提前天数不能为空]",
						number:"[请输入数字]",
						digits:"[请输入整数]" 
					}
				}
			});   
			if(${not empty error}==true){
				alert("${error}");	
				}
			
			$("#sel").change(selectRemind);
		});
		function add() {
			 if (!$("#remindSettingForm").valid()) {
					return false;
				} 
			$("#remindSettingForm").ajaxSubmit({
				url : "${pageContext.request.contextPath}/admin/dictionary/remindAdd",
				success : showContentInfo,
				error : showError
			});
		}

		function save() {
			if (!$("#remindSettingForm").valid()) {
				return false;
			} 
			$("#remindSettingForm").ajaxSubmit({
				url : "${pageContext.request.contextPath}/admin/dictionary/remindSave",
				success : showContentInfo,
				error : showError
			});
		}

		function deleteById() {
			var title = $("#sel").find("option:selected").text();
			if (title == '请选择') {
				return;
			}
			var id = $("#id").val();
			$.ajax({
				url : "${pageContext.request.contextPath}/admin/dictionary/remindDelete/" + id + "/",
				type : "get",
				success : function(data) {
					showContentInfo(data);
				}
			});
		}
		
		function selectRemind(){
			var title = $("#sel").find("option:selected").text();
			if (title == '请选择') {
				$("#id").attr("value","");
				$("#title").attr("value", "");
				$("#advancedTime").attr("value","");
				$("#messageContent").html("");
				return;
			}
			var remindId=$("#sel").val();
			$("#id").attr("value",remindId);
			$.get("${pageContext.request.contextPath}/admin/dictionary/remind/"+remindId+"/",showRemind);
		}
		
		function showRemind(data){
			$("#title").attr("value", data.title);
			$("#messageContent").html(data.messageContent);
			$("#advancedTime").attr("value",data.advancedTime);
		}
	</script>
</body>
</html>

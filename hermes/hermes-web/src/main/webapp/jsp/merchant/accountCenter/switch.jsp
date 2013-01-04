<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../../include/import.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商户开关设置</title>
</head>
<body>
	<form id="switchForm"
		action="${pageContext.request.contextPath}/merchant/account/switch/"
		method="post" class="form-horizontal">

		<div class="control-group">
			<label class="control-label" for="autoSend">挂机短信开关：</label>
			<div class="controls">
				<input type="radio" name="autoSend" id="autoSend" value="true"
					<c:if test="${ merchant.setting.autoSend eq true }"> checked="checked" </c:if> />开
				<input type="radio" name="autoSend" id="autoSend" value="false"
					<c:if test="${merchant.setting.autoSend eq false }"> checked="checked" </c:if> />关
				<span class="help-inline">(注：选择关时，将不会进行挂机短信发送) </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="nameMatch">性别匹配开关：</label>
			<div class="controls">
				<input type="radio" name="nameMatch" id="nameMatch" value="true"
					<c:if test="${ merchant.setting.nameMatch eq true }"> checked="checked" </c:if> />开
				<input type="radio" name="nameMatch" id="nameMatch" value="false"
					<c:if test="${ merchant.setting.nameMatch eq false }"> checked="checked" </c:if> />关
				<span class="help-inline">(注：选择关时，发送短信将不会进行自动匹配性别 )</span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="salerMatch">匹配销售开关：</label>
			<div class="controls">
				<input type="radio" name="salerMatch" id="salerMatch" value="true"
					<c:if test="${ merchant.setting.salerMatch eq true }"> checked="checked" </c:if> />开
				<input type="radio" name="salerMatch" id="salerMatch" value="false"
					<c:if test="${ merchant.setting.salerMatch eq false }"> checked="checked" </c:if> />关
				<span class="help-inline">(注：打开开关，群发短信自动加上对应的销售信息)</span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="birthdayRemind">生日提醒开关：</label>
			<div class="controls">
				<input type="radio" name="birthdayRemind" id="birthdayRemind"
					value="true"
					<c:if test="${ merchant.setting.birthdayRemind eq true }"> checked="checked" </c:if> />开
				<input type="radio" name="birthdayRemind" id="birthdayRemind"
					value="false"
					<c:if test="${ merchant.setting.birthdayRemind eq false }"> checked="checked" </c:if> />关

				选择模板 <select id="sel" name="birthdayRemindTemplate.id">
				</select> <span class="help-inline">(注：打开开关，生日提醒将出现在提醒列表中)</span>
			</div>
		</div>

		<div class="controls">
			<input type="submit" class="btn btn-primary" value="提交">
		</div>
	</form>
	<script type="text/javascript">
		$(document).ready(function() {
			$.get("${pageContext.request.contextPath}/merchant/account/remindTemplate/get/", showTemplates);
			var validateoptions = {
				rules : {
					autoSend : {
						required : true
					},
					nameMatch : {
						required : true
					},
					salerMatch : {
						required : true
					},
					birthdayRemind : {
						required : true
					}

				},
				messages : {
					autoSend : {
						required : "选项不能为空"
					},
					nameMatch : {
						required : "选项不能为空"
					},
					salerMatch : {
						required : "选项不能为空"
					},
					birthdayRemind : {
						required : "选项不能为空"
					}

				}
			};
			$('#switchForm').validate(validateoptions);

			$('#switchForm').submit(function() {
				if (!$('#switchForm').valid()) {
					return false;
				}
				var options = {
					success : toSwitch
				};
				$(this).ajaxSubmit(options);
				return false;
			});

			function showTemplates(data) {
				for ( var i = 0; i < data.length; i++) {
					var option ="<option value='"+data[i].id+"'";
					if(data[i].id=="${merchant.setting.birthdayRemindTemplate.id}"){
						option+="  selected='selected'";
					}
					option +=" >";
					option+=data[i].remindTemplate.title+"</option>";
					$("#sel").append(option);
				}
			}

		});

		function toSwitch(data) {
			$("#pageContentFrame").html(data);
		}
	</script>
</body>
</html>
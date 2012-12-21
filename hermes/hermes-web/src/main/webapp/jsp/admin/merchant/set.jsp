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
			选择提醒内容： <select id="sel">
				<option value="chose">请选择</option>
			</select>
<%-- 			<c:forEach items="${remindTemplate}" var="template">
				<span id="${template.id}" style="display: none;">${template.messageContent}</span>
			</c:forEach>  --%>
			<input type="button" class="btn btn-primary "  value="增加" onclick="remindAdd('${merchantId}')"/>
		</div>
	</div>
	<div>
		<form id="remindSettingForm"  class="form-horizontal">
			<table class="table table-bordered">
				<thead>
					<tr>
						<th colspan="2"><center>
								<h4>定时提醒列表</h4>
							</center></th>
					</tr>
				</thead>
				<tr>
					<td width="35%">定时提醒标题</td>
					<td width="45%">定时提醒内容</td>
					<td width="20%">操作</td>
				</tr>
				
				<c:forEach items="${merchantRemindTemplates}" var="merchantRemindTemplate">
				<tr>
				<td>${merchantRemindTemplate.remindTemplate.title}</td>
				<td>${merchantRemindTemplate.messageContent}</td>
				<td><input type='button' class='btn btn-danger' value='删除' onclick="remindDelete('${merchantId}','${merchantRemindTemplate.id}')"/></td>
				</tr>
				</c:forEach>
				
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
	</div>
	<script type="text/javascript">
	$(document).ready(function() {
		$.get("${pageContext.request.contextPath}/admin/merchant/dataDictionaryList/",showList);
	});
		
	function showList(data){
		if(data!=null){
			for(var i=0;i<data.length;i++){
				var str=$("<option value=\""+data[i].id+"\">"+data[i].title+"</option>");
				$('#sel').append(str);
			}
		}
	}
	function remindAdd(merchantId){
		var templateId=$("#sel").val();
		if(templateId=="chose"){
			return false;
		}
		$.get("${pageContext.request.contextPath}/admin/merchant/remindAdd/"+merchantId+"/"+templateId+"/",showContentInfo);
	}
	function remindDelete(merchantId,merchantRemindTemplateId){
		$.get("${pageContext.request.contextPath}/admin/merchant/remindDelete/"+merchantId+"/"+merchantRemindTemplateId+"/",showContentInfo);
	}
	
	</script>
</body>
</html>
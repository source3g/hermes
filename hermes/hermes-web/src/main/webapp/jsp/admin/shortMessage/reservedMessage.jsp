<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>短信预存</title>
</head>
<body>
	<form id="reservedMsgForm">
	<table>
		<tr>
			<td>商户名称：              <td>
			
			<span>${merchant.name}</span>
			<input type="hidden" id="merchantId" name="id" value="${merchant.id}">
		</tr>
		<tr>
			<td>预存类型：</td>
			<td>
			<select id="shortMessage" name="type"class="input-medium">
			<option value="add">递增</option>
			</select>
			</td>
		</tr>
		<tr>
			<td>预存条数 ：</td>
			<td>
			<input type="text" name="count" value="100">
			</td>
		</tr>
		<tr>
			<td>
                <input id="addMessageBtn" data-loading-text="短信预存中..." class="btn btn-primary" type="submit" value="确定" >
                 
			</td>
		</tr>
		</table>
	</form>
	<script type="text/javascript">
	$(document).ready(function() {
		var validateoptions={
					rules: {
						count:{
												rangelength: [0,6],
												 	 number: true
						}
				},
			   	 messages: {
			   			count:{
			   									rangelength: "请输入6位有效数字",
												 	 number: "请输入有效数字"
			   		}
		        }	
			}
		$('#reservedMsgForm').validate(validateoptions);
		
		$('#reservedMsgForm').submit(function(){
			if(!$('#reservedMsgForm').valid()){
				return false;
			}
			var merchantId=$('#merchantId').val();
			 var options = {
					 		url:"${pageContext.request.contextPath}/admin/merchant/reservedMsg/"+merchantId+"/",
							type:"post",
							success : toList
					};
			 $('#addMessageBtn').button('loading')
					$(this).ajaxSubmit(options);
					
					return false;
				
		});
	});
		function toList(data){
			$("#pageContentFrame").html(data);
		}
	</script>
</body>
</html>
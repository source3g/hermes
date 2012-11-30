<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../../include/import.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>短信发送</title>
</head>
<body>
	<form id="messageSendForm" class="well ">
		<table class="table table-bordered">
		<thead>
			<tr>
				<th colspan="4"><center>
							<h4>短信录入 </h4>
						</center></th>
				</tr>
		</thead>
		<tbody>
			<tr>
			<td width="20%" ><label class="control-label">商户短信数据 :</label></td>
			<td width="26%" >短信预存数量：${merchant.shortMessage.totalCount}</td>
			<td width="27%" >短信可用数量：${merchant.shortMessage.surplusMsgCount}</td>
			<td width="27%" >短信已发送数量：</td>
				
			</tr>
			
			 <tr>
				 <td>
					<label class="control-label">输入客户电话号码已封号隔开：</label> </td>
					<td colspan="4"><textarea class="span8" rows="5" name="customerPhone" ></textarea>
				 </td>
			  </tr>
			<tr>
			<td>
				<label class="control-label">编辑短信内容：</label> </td>
				<td colspan="4"><textarea class="span8" rows="5" name="messageInfo" ></textarea>
			 </td>
			 </tr>
			 
			<tr>
			<td colspan="4">
				<input type="submit" class="btn btn-primary" value="发送" >
				<td>
		    </tr>
		
	</tbody>
		</table>
	</form>
	<script type="text/javascript">
	$(document).ready(function() {
		var validateOptions = {
				rules : { 
					messageInfo:{
						required : true
					}
				},
				messages:{
					messageInfo	:{
						required : "短信输入不能为空"
					}
				}
		};
		$('#messageSendForm').validate(validateOptions); 
	 $('#messageSendForm').submit(function() {
		 if (!$('#messageSendForm').valid()) {
				return false;
			}
		 var options = {
				 url:"${pageContext.request.contextPath}/merchant/message/messageSend/",
				 type:"post",
			success : showList
		}; 
		
		$(this).ajaxSubmit(options);
		return false;

		}); 
	});
		function showList(data){
			$("#pageContentFrame").html(data);
		}
		</script>
</body>
</html>
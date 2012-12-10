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
			<td width="27%" >短信已发送数量：${merchant.shortMessage.sentCount}</td>
				
			</tr>
			
			 <tr>
			 <td><label class="control-label">选择客户组:：</label></td>
			 <td colspan="3"> 
			<c:if test="${not empty customerGroups }">
				<c:forEach items="${customerGroups}" var="customerGroup">
					 <input type=checkbox name="ids" value="${customerGroup.id}">${customerGroup.name}
				</c:forEach>
			</c:if>
			
			  </td>	
			  </tr>
			  <tr>
			  <td>
			  		<label class="control-label">选择短信模板：</label> </td>
			  		<td colspan="4"> <select id="sel">
								<option>请选择</option>
									</select>
			  	</td>
			  </tr>
			<tr>
			<td>
				<label class="control-label">编辑短信内容：</label> </td>
				<td colspan="4"><textarea class="span8" rows="5" name="content" id="content"></textarea>
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
	<div id="errorModal" class="modal hide fade">
		<div class="modal-body">
			<p id="resultMessage"></p>
		</div>
		<div class="modal-footer">
			<a href="#" class="btn" data-dismiss="modal">确定</a>
		</div>
	</div>
	<script type="text/javascript">
	$(document).ready(function() {
		//initDialog();
		
		var validateOptions = {
				rules : { 
					content:{
						required : true
					}
				},
				messages:{
					content	:{
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
			success : function (data){
				if(${merchant.shortMessage.surplusMsgCount}==0){
					alert("短信可用数量不足,请充值");
				}else{
				alert("短信息已向后台发送,请在短信列表中查看");
				}
				showList(data);
			}
		}; 
		
		$(this).ajaxSubmit(options);
		return false;

		}); 
	 
	 //短信模板
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
	
		function initSel(data) {
			for ( var i = 0; i < data.length; i++) {
				$("#sel").append("<option value='"+data[i].id+"'>" + data[i].title + "</option>");
				$("#sel").after("<span id="+data[i].id+" style='display: none;'>" + data[i].content + "</span>");
			}
		}
	
		function showList(data){
			$("#pageContentFrame").html(data);
		}
		</script>
</body>
</html>
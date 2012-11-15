<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加盒子</title>
<%-- <%@ include file="../../include/header.jsp"%> --%>
</head>
<body>
	<form id="addDeviceForm"
		action="${pageContext.request.contextPath}/admin/device/add/"
		method="post" class="form-horizontal">

		<div class="control-group">
			<label class="control-label" for="sn">SN:</label>
			<div class="controls">
				<input type="text" class="input-xlarge" placeholder="请输入盒子SN..."
					id="sn" name="sn" value="${device.sn}"> <span
					class="help-inline"><font color="red">*</font></span>
			</div>
		</div>
		<div class="form-actions">
			<input type="submit" class="btn btn-primary" input="增加">

		</div>
		<c:if test="${not empty errors }">
			<div class="alert alert-error">
				<ul>
					<c:forEach items="${errors }" var="error">
						<li>${error.defaultMessage }</li>
					</c:forEach>
				</ul>
			</div>
		</c:if>
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
	$(document).ready(function(){
		$('#addDeviceForm').validate({
			rules : {
				sn : {
					required : true
				}
			},
			messages : {
				sn : {
					required : "请填写SN名称"
				}
			}
		});
		
		 var options={
			success:toAdd,
			error:showError
		 };
		 
		$('#addDeviceForm').submit(function(){
			if (!$("#addDeviceForm").valid()) {
				return false;
			}
			$(this).ajaxSubmit(options);
			return false;
		});
		
		initDialog();
	});
	
		function toAdd(data){
			$("#pageContentFrame").html(data);
			
		}
		
		function showError(){
			alert("出错了");
		}
		
		function initDialog(){
			if(${not empty success }==true){
				$('#addDeviceForm').clearForm();
				$("#resultMessage").html("操作成功！");
				$("#errorModal").modal({
					backdrop:true,
				    keyboard:true,
				    show:true
				});
			}
		}
		
	</script>
</body>
<%-- <%@include file="../../include/footer.jsp"%> --%>

</html>

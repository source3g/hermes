<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>盒子详细信息</title>
</head>
<body>
	<form id="deviceInfo" class="form-horizontal">
		<div class="control-group">
			<label class="control-label">盒子:</label>
			<div class="controls">
				${device.sn}<input type="hidden" name="sn" value="${device.sn}">
				<input type="hidden" name="id" value="${device.id}">
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="simId">请输入SIM卡号:</label>
			<div class="controls">
				<input type="text" class="input-xlarge" placeholder="请输入SIM卡号..."
					id="no" name="no" value="${sim.no}"> <span
					class="help-inline"> <font color="red">*</font></span>
				<c:if test="${ empty device.simId }">
					<input type="submit" class="btn btn-primary" value="绑定">
				</c:if>
				<c:if test="${ not empty device.simId }">
					<input type="submit" class="btn btn-primary" value="修改">
			
				</c:if>

			</div>
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
			
		<div id="errorModal" class="modal hide fade">
			<div class="modal-body">
				<p id="resultMessage"></p>
			</div>
			<div class="modal-footer">
				<a href="#" class="btn" data-dismiss="modal">确定</a>
			</div>
		</div>
	</form>

	<script type="text/javascript">
		$(document).ready(function(){
			 var validateOptions = {
					rules : { 
						no : {
							required : true,
							minlength : 2
						}
					},
					messages : {
						no : {
							required : "请填写SIM卡号",
							minlength : "至少输入两个字符"
						}
					}
				};
		
		$('#deviceInfo').validate(validateOptions); 
		 $('#deviceInfo').submit(function() { 
		 	 if(!$('#deviceInfo').valid()){
				return false;
			} 
			var options = {
			url : "${pageContext.request.contextPath}/admin/device/update/",
			type : "post",
			success : toList // post-submit callback
		};
			
			$('#deviceInfo').ajaxSubmit(options);
		
			return false;
		});
		
		initDialog(); 

		
		
 	});
		function toList(data) {
			$("#pageContentFrame").html(data);
		}
		 function initDialog() {
			if (${not empty success }== true) {
				$("#resultMessage").html("绑定成功！");
				$("#errorModal").modal();
			}
		}   
	</script>

</body>
</html>
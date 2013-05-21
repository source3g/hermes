<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加SIM卡</title>
<%-- <%@ include file="../../include/header.jsp"%> --%>
</head>
<body>
	<form id="addSimForm" method="post" class="form-horizontal">

		<div class="control-group">
			<label class="control-label"> excel文件:</label> <span
				id="spanButtonPlaceholder"></span>
			<!-- <input type="text"
				name="uploadFile" style="border: none; width: 3px;"> -->
			<span>&nbsp;&nbsp;&nbsp;(每个文件最大10M)</span>
			<div id="divFileProgressContainer" style="width: 200;"></div>
		</div>
		<div class="form-actions" onclick="importSim();">
			<input id="importBtn" type="button" data-loading-text="SIM卡导入中..."
				class="btn btn-primary" value="导入">
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

	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/swfupload/swfupload.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/swfupload/picHandlers.js"></script>

	<script type="text/javascript">
	$(document).ready(function(){
		 var options={
			success:toAdd,
			error:showError
		 };
		 
		$("#importBtn").click(importSim);
		
		function importSim(){
			swfu.startUpload();
			return false;
		}
		
		
		var swfu = new SWFUpload({
			// Backend Settings
			upload_url : "${pageContext.request.contextPath}/admin/sim/import/",
			file_size_limit : "2 MB", // 2MB
			file_types : "*.xls;*.xlsx",
			file_types_description : "Images",
			file_upload_limit : "1",

			// Event Handler Settings - these functions as defined in Handlers.js
			//  The handlers are not part of SWFUpload but are part of my website and control how
			//  my website reacts to the SWFUpload events.
			file_queue_error_handler : fileQueueError,
			file_queued_handler : fileQueued,
			file_dialog_complete_handler : fileDialogComplete,
			upload_progress_handler : uploadProgress,
			upload_error_handler : uploadError,
			upload_success_handler : function (file, server_data){
				$.get("${pageContext.request.contextPath}/merchant/account/electricMenu",showContentInfo);
			},
			upload_complete_handler : uploadComplete,

			// Button Settings
			button_image_url : "${pageContext.request.contextPath}/js/swfupload/XPButtonNoText_61x22.png",
			button_placeholder_id : "spanButtonPlaceholder",
			button_width : 61,
			button_height : 22,
			button_text : '<span class="button">选择xls</span>',
			button_text_style : '.button { font-family: Helvetica, Arial, sans-serif; font-size: 12pt; } .buttonSmall { font-size: 10pt; }',
			button_text_top_padding : 0,
			button_text_left_padding : 3,
			button_window_mode : SWFUpload.WINDOW_MODE.TRANSPARENT,
			button_cursor : SWFUpload.CURSOR.HAND,
			// Flash Settings
			flash_url : "${pageContext.request.contextPath}/js/swfupload/swfupload.swf",
			custom_settings : {
				upload_target : "divFileProgressContainer"
			},
			// Debug Settings
			debug : false
		});
		
		
		
	});
	
		
	
		function toAdd(data){
			$("#pageContentFrame").html(data);
			
		}
		
		function showError(){
			alert("出错了");
		}
		
	</script>
</body>
<%-- <%@include file="../../include/footer.jsp"%> --%>

</html>

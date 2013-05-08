<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC
"-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>导入顾客信息</title>
</head>
<body>
	<span id="spanButtonPlaceholder"></span>
	<span>(每个文件最大10M)</span>
	<div id="divFileProgressContainer" style="width: 200; display: none;"></div>
	<div id="thumbnails">
		<table id="infoTable" border="0" width="50%"
			style="border: solid 1px #7FAAFF; background-color: #C5D9FF; padding: 2px; margin-top: 8px;">
		</table>
	</div>
	<br>
	<br>
<p style="color:red">提示:请不要在旺财宝使用高峰期导入大量顾客信息,这样会影响旺财宝的正常使用</p>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/swfupload/swfupload.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/swfupload/handlers.js"></script>


	<script type="text/javascript">
		var swfu;
		$(document).ready(function() {
			swfu = new SWFUpload({
				upload_url : "${pageContext.request.contextPath}/merchant/customer/import/",

				// File Upload Settings
				file_size_limit : "10MB", // 1000MB
				file_types : "*.xls;*.xlsx",//设置可上传的类型
				file_types_description : "excel",
				//file_upload_limit : "1",

				file_queue_error_handler : fileQueueError,//选择文件后出错
				file_dialog_complete_handler : fileDialogComplete,//选择好文件后提交
				file_queued_handler : fileQueued,
				upload_progress_handler : uploadProgress,
				upload_error_handler : uploadError,
				upload_success_handler : uploadSuccess,
				upload_complete_handler : uploadComplete,

				// Button Settings
				button_image_url : "${pageContext.request.contextPath}/js/swfupload/XPButtonNoText_61x22.png",
			button_placeholder_id : "spanButtonPlaceholder",
			button_width: 61,
			button_height: 22,
			button_text :  '<span class="button">上传excel</span>', 
			button_text_style : '.button { font-family: Helvetica, Arial, sans-serif; font-size: 12pt; } .buttonSmall { font-size: 10pt; }',
			button_text_top_padding : 0,
			button_text_left_padding : 2,
				button_window_mode : SWFUpload.WINDOW_MODE.TRANSPARENT,
				button_cursor : SWFUpload.CURSOR.HAND,

				// Flash Settings
				flash_url : "${pageContext.request.contextPath}/js/swfupload/swfupload.swf",

				custom_settings : {
					upload_target : "divFileProgressContainer"
				},
				// Debug Settings
				debug : false
			//是否显示调试窗口
			});
		});
		//window.onload =
		function startUploadFile() {
			swfu.startUpload();
		}
	</script>
	<script type="text/javascript">
		$("#importForm").submit(function() {
			$("#loader").css("display", "");
			$(this).ajaxSubmit({
				success : showContentInfo
			});
			return false;
		});
	</script>
</body>
</html>

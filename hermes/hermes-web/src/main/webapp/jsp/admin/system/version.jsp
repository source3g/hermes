<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>版本更新</title>
</head>
<body>
	<form id="addVersionForm">
		<div class="control-group">
			<label class="control-label" for="fileUpload">请先输入版本号：</label>
			<div class="controls">
				<input type="text" name="version" id="version"></input><span id="versionValidateMsg" style="color:red"></span>
			</div>
		</div>

		<span id="spanButtonPlaceholder"></span><input type="text" name="uploadFile" style="border:none;width:3px;"><span>&nbsp;&nbsp;&nbsp;(每个文件最大10M)</span><span id="fileValidateMsg" style="color:red"></span>
		<div id="divFileProgressContainer" style="width: 200; display: none;"></div>
		<div id="thumbnails">
			<table id="infoTable" border="0" width="50%"
				style="border: solid 1px #7FAAFF; background-color: #C5D9FF; padding: 2px; margin-top: 8px;">
			</table>
		</div>
		<br> <br> <input class="btn btn-primary" value="上传"
			id="uploadVersion" type="button" />
		<!-- onclick="uploadVersion();" -->
		<br>
	</form>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/swfupload/swfupload.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/swfupload/handlers.js"></script>

	<script type="text/javascript">
		var swfu;
		swfu = new SWFUpload({
			upload_url : "${pageContext.request.contextPath}/admin/version/upload/",
			// File Upload Settings
			file_size_limit : "50MB", // 1000 MB
			file_types : "*.apk",//设置可上传的类型
			file_types_description : "apk文件",
			//file_upload_limit : "1",
			file_queue_error_handler : fileQueueError,//选择文件后出错
			file_dialog_complete_handler : apkSelDialogComplete,//选择好文件后提交
			file_queued_handler : apkQueued,
			upload_progress_handler : uploadProgress,
			upload_error_handler : uploadError,
			upload_success_handler : uploadSuccess,
			upload_complete_handler : uploadComplete,
			// Button Settings
			button_image_url : "${pageContext.request.contextPath}/js/swfupload/XPButtonNoText_61x22.png",
			button_placeholder_id : "spanButtonPlaceholder",
			button_width: 61,
			button_height: 22,
			button_text :  '<span class="button">选择apk</span>', 
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
		//window.onload =
		/* function startUploadFile() {
			swfu.startUpload();
		} */
		function apkQueued(file, version) {
			addReadyFileInfo(file.id, file.name, "准备上传");
		}
		function apkSelDialogComplete(numFilesSelected, numFilesQueued) {
		}

		$(document).ready(function() {
			$('#addVersionForm').validate({
				rules : {
					version : {
						required : true,
						remote : {
							type : "get",
							url : "${pageContext.request.contextPath}/admin/version/versionValidate",
							data : {
								"version" : function() {
									return $('#version').val();
								}
							}
						}
					
					},
				 	uploadFile:{
						validateFile : true
					} 
			
				},
				messages : {
					version : {
						required : "请填写版本号",
						remote : "版本号不能重复"
					
					},
					
					uploadFile:{
						validateFile : "请选择上传文件"
					} 
				}
			});

			$.validator.addMethod(
					"validateFile",
					function(value, element, params) {
						if(swfu.getStats().files_queued==0){
							return false;
						} else{
							return true;
						}
					}, "输入数字有误");
			
 			$("#uploadVersion").click(function() {
				if (!$("#addVersionForm").valid()) {
			 		return false;
				 } 
				var version = $("#version").val();
				swfu.addPostParam("version", version);
				swfu.startUpload();
				return false;
				});
		});
	</script>
</body>
</html>

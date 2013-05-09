<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加电子菜单</title>
</head>
<body>
	增加电子菜单项
	<form id="addElectricMenuItemForm"
		<%-- action="${pageContext.request.contextPath}/merchant/account/electricMenu/addItem/"
		method="post" --%> class="form-horizontal">
		<div class="control-group">
			<label class="control-label"> 菜品类别:</label> <select name="menuId">
				<c:forEach var="menu" items="${electricMenus }">
					<option value="${menu.id }">${menu.name }</option>
				</c:forEach>
			</select>
		</div>
		<div class="control-group">
			<label class="control-label"> 名称: </label><input type="text"
				name="title" id="title"></input>
		</div>
		<div class="control-group">
			<label class="control-label"> 图片:</label> <span
				id="spanButtonPlaceholder"></span><span>&nbsp;&nbsp;&nbsp;(每个文件最大10M)</span>
			<div id="divFileProgressContainer" style="width: 200;"></div>
		</div>
		<div class="control-group">
			<label class="control-label"> 单位:</label><input type="text"
				name="unit" id="unit"></input>
		</div>
		<div class="form-actions">
			<input type="button" id="addElectricMenuBtn" class="btn btn-primary"
				value="增加" /> <input type="button" id="backToList"
				class="btn btn-primary" value="返回" />
		</div>
	</form>

	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/swfupload/swfupload.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/swfupload/picHandlers.js"></script>


	<script type="text/javascript">
		$(document).ready(function() {
			$("#backToList").click(function() {
				loadPage("${pageContext.request.contextPath}/merchant/account/electricMenu/");
			});
			$("#addElectricMenuBtn").click(function() {
				//	swfu.addPostParam("menuId", "menuId");
				//swfu.addPostParam("title", $("#title").val());
				//swfu.addPostParam("unit", $("#unit").val());
				var post_params = {
					"menuId" : "menuId",
					"title" : $("#title").val(),
					"unit" : $("#unit").val()
				};
				swfu.setPostParams(post_params);
				swfu.startUpload();
			});
			var swfu = new SWFUpload({
				// Backend Settings
				upload_url : "${pageContext.request.contextPath}/merchant/account/electricMenu/addItem/",
				post_params : {
					"title" : "哇哈哈"
				},
				// File Upload Settings
				file_size_limit : "2 MB", // 2MB
				file_types : "*.jpg;*.png",
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
				upload_success_handler : uploadSuccess,
				upload_complete_handler : uploadComplete,

				// Button Settings
				button_image_url : "${pageContext.request.contextPath}/js/swfupload/XPButtonNoText_61x22.png",
				button_placeholder_id : "spanButtonPlaceholder",
				button_width : 61,
				button_height : 22,
				button_text : '<span class="button">选择图片</span>',
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
	</script>
</body>
</html>
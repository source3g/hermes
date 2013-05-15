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
			<label class="control-label"> 菜品类别:</label> <select id="menuSel"
				name="menuId">
				<c:forEach var="menu" items="${electricMenus }">
					<option value="${menu.id }"
						<c:if test="${menu.id eq menuId }"> selected</c:if>>
						${menu.name }</option>
				</c:forEach>
			</select>
		</div>
		<div class="control-group">
			<label class="control-label"> 名称: </label><input type="text" <c:if test="${detail eq true }">readonly="readonly"</c:if> 
				name="title" id="title" value="${electricMenuItem.title }"></input>
		</div>
		<div class="control-group">
			<label class="control-label">价格:</label><input type="text" <c:if test="${detail eq true }">readonly="readonly"</c:if>
				name="price" id="price" value="${electricMenuItem.price}"></input><label
				class="help-inline">(单位:元)</label>
		</div>
		<div class="control-group">
			<label class="control-label"> 单位:</label><input type="text" <c:if test="${detail eq true }">readonly="readonly"</c:if>
				name="unit" id="unit" value="${electricMenuItem.unit}"></input><label
				class="help-inline">(例如:盘,份,例,碗等)</label>
		</div>
		<div class="control-group">
			<label class="control-label"> 图片:</label> <span
				id="spanButtonPlaceholder"></span><input type="text" name="uploadFile" style="border:none;width:3px;"><span>&nbsp;&nbsp;&nbsp;(每个文件最大10M)</span>
			<div id="divFileProgressContainer" style="width: 200;"></div>
			<c:if test="${ not empty update }">
				<input type="hidden" id="id" name="id" value="${electricMenuItem.id }" />
				<img style="display:inline;" alt="" src="${electricMenuItem.picPath }" width="800"
					height="600">
			</c:if>
		</div>
		<c:if test="${detail ne true }">
		<div class="form-actions">
			<c:choose>
				<c:when test="${ empty update }">
					<input type="button" id="addElectricMenuItemBtn"
						class="btn btn-primary" value="增加" />
				</c:when>
				<c:otherwise>
					<input type="button" id="updateElectricMenuItemBtn"
						class="btn btn-primary" value="修改" />
				</c:otherwise>
			</c:choose>
			<input type="button" id="backToList" class="btn btn-primary"
				value="返回" />
		</div>
		</c:if>
	</form>

	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/swfupload/swfupload.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/swfupload/picHandlers.js"></script>


	<script type="text/javascript">
		$(document).ready(function() {
			if(${empty update}){
				$("#menuSel").change(function (){
					$('#title').attr("value","");
				});	
			}
				var validateOptions={
						rules : {
							title : {
								required : true,
							remote : {
									type : "get",
									url : "${pageContext.request.contextPath}/merchant/account/titleValidate",
									data : {
										"title" : function() {
											return $('#title').val();
										},
									"menuId":function() {
										return $('#menuSel').val();
									}
									}
								} 
							},
							uploadFile:{
								validateFile : true
							},
							price:{
								required : true,
								number :true
							},
							unit:{
								required : true
							}
					
						},
						messages : {
							title : {
								required : "请填写有效菜名",
								remote : "菜单名称不能重复"
							},
							uploadFile:{
								validateFile : "请选择上传文件"
							},
							price:{
								required : "请填写有效价格 ",
								number :"请输入有效数字"
							},
							unit:{
								required : "请填写有效数量"
							}
						
						}
					};
						
				if(${not empty update}){
					validateOptions.rules.title.remote.data={
							"title" : function() {
								return $('#title').val();
							},
							"menuId":function() {
								return $('#menuSel').val();
							},
							"oldTitle":"${electricMenuItem.title}"
					};
				}
					$.validator.addMethod(
							"validateFile",
							function(value, element, params) {
								if(${not empty update}){
									return true;
								}
								if(swfu.getStats().files_queued==0){
									return false;
								} else{
									return true;
								}
							}, "请选择上传文件");
			
					$('#addElectricMenuItemForm').validate(validateOptions);

			$("#backToList").click(function() {
				loadPage("${pageContext.request.contextPath}/merchant/account/electricMenu/");
			});
			$("#addElectricMenuItemBtn").click(function() {
				if (!$("#addElectricMenuItemForm").valid()) {
			 		return false;
				 } 
				//	swfu.addPostParam("menuId", "menuId");
				//swfu.addPostParam("title", $("#title").val());
				//swfu.addPostParam("unit", $("#unit").val());
				var post_params = {
					"menuId" : $("#menuSel").val(),
					"title" : $("#title").val(),
					"unit" : $("#unit").val(),
					"price" : $("#price").val()
				};
				swfu.setPostParams(post_params);
				swfu.startUpload();
			});

			$("#updateElectricMenuItemBtn").click(function() {
				if (!$("#addElectricMenuItemForm").valid()) {
			 		return false;
				 } 
				var newFileName = $("#divFileProgressContainer").text();
				var post_params = {
					"title" : $("#title").val(),
					"unit" : $("#unit").val(),
					"price" : $("#price").val()
				};
				
				if (newFileName == null || newFileName == '') {
					var url="${pageContext.request.contextPath}/merchant/account/electricMenu/updateItemNoPic/"+ $("#menuSel").val()+"/"+$("#id").val()+"/";
					$.post(url, post_params);
				} else {
					var updateUrl = "${pageContext.request.contextPath}/merchant/account/electricMenu/updateItem/"+ $("#menuSel").val()+"/"+$("#id").val()+"/";
					swfu.setUploadURL(updateUrl);
					swfu.setPostParams(post_params);
					swfu.startUpload();
				}
				alert("!");
				$.get("${pageContext.request.contextPath}/merchant/account/electricMenu",showContentInfo);
				return false;
			});

			var swfu = new SWFUpload({
				// Backend Settings
				upload_url : "${pageContext.request.contextPath}/merchant/account/electricMenu/addItem/",
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

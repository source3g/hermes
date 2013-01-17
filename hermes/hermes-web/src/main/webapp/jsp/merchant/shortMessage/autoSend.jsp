<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>挂机短信</title>
</head>
<body>
	<form id="autoSendForm" class="well ">
		<table class="table table-bordered">
		<thead>
				<tr>
					<th colspan="4"><center>
							<h4>挂机短信内容设置</h4>
						</center></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td width="20%"><label class="control-label">新顾客挂机短信内容：</label></td>
					<td colspan="4">
						<input type="hidden" value="${messageAutoSend.id }" name="id">
						<textarea class="span8" rows="7"
							name="newMessageCotent" id="newMessageCotent">${messageAutoSend.newMessageCotent }</textarea> 
					</td>
				</tr>
				<tr>
					<td ><label class="control-label">字数统计：</label></td>
					<td colspan="4"><span  id="newContentLength"></span></td>
				</tr>
				<tr>
					<td width="20%"><label class="control-label">老顾客挂机短信内容：</label></td>
					<td colspan="4">
						<textarea class="span8" rows="7"
							name="oldMessageCotent" id="oldMessageCotent">${messageAutoSend.oldMessageCotent }</textarea> 
							 <input type="submit" class="btn btn-primary" value="确定">
					</td>
					<tr>
					<td ><label class="control-label">字数统计：</label></td>
					<td colspan="4"><span  id="oldContentLength"></span></td>
				</tr>
			</tbody>
		</table>
	</form>
	<script type="text/javascript">
	$(document).ready(function() {
		var i=1;
		$("#oldMessageCotent").keyup(function(){
			var length=$("#oldMessageCotent").val().length;
	 		if((length%70)==0){
				i=(length/70);
				$("#oldContentLength").text("当前"+length+"个字，以"+i+"条短信发送");
			} else{
				i=Math.floor(length/70)+1;
				$("#oldContentLength").text("当前"+length+"个字，以"+i+"条短信发送");
			}
		});
		var k=1;
		$("#newMessageCotent").keyup(function(){
			var length=$("#newMessageCotent").val().length;
	 		if((length%70)==0){
				k=(length/70);
				$("#newContentLength").text("当前"+length+"个字，以"+k+"条短信发送");
			} else{
				k=Math.floor(length/70)+1;
				$("#newContentLength").text("当前"+length+"个字，以"+k+"条短信发送");
			}
		});
		var validateOptions = {
				rules : {
					newMessageCotent : {
						required : true
					},	
					oldMessageCotent : {
						required : true
				}		
				},
				messages : {
					newMessageCotent : {
						required : "短信输入不能为空"
					},
					oldMessageCotent : {
						required : "短信输入不能为空"
					}
				}
			};
			$('#autoSendForm').validate(validateOptions);
		$('#autoSendForm').submit(function(){
			if (!$('#autoSendForm').valid()) {
				return false;
			}
			var options = {
					 url:"${pageContext.request.contextPath}/merchant/message/autoSend/",
					 type:"post",
					success : function (data){
						alert("短信内容设置成功");
						showList(data)
					}
				
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
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>资源设置</title>
</head>
<body>
	<h3>操作说明</h3>
	<p>1.{}里填写的内容表示旺财宝电话在编辑短信时所被替换的内容.<br>2.{时间}和{资源}分别表示旺财宝编辑短信时需要选择的时间和资源.<br>3.{}里所填写的内容可以在编辑短信内容里任意变更位置.</p>
	<h4>例1</h4>
	<p>资源操作填写的内容：您于{时间}预定了{资源}房间.</p>
	<p>实际发送效果：尊敬的xxx先生:您于2012-04-12预定了101房间.</p>
	<h4>例2</h4>
	<p>资源操作填写的内容：{时间}您在XXX酒楼预定了{资源}包间</p>
	<p>实际发送效果：尊敬的xxx先生:2013-04-12您在XXX酒店预定了101包间.</p>
	<br>
	
	<form id="addMerchantResourceForm1" class="form-inline">
		<h3>编辑短信内容</h3>
		 <textarea rows="8" class="span6"  id="messageContent" name="messageContent" ></textarea><br>
		<input type="submit" class="btn btn-primary"  value="保存">
	</form>
	
	<h3>资源操作</h3>
	<form id="addMerchantResourceForm">
		<label class="control-label" for="name">资源名称：</label> <input 
			type="text" class="input-xlarge" placeholder="请输入资源名称..." id="name"
			name="name"> <span class="help-inline"><font
			color="red">*</font></span> <input type="submit" id="addMerchantResource"
			data-loading-text="增加中..." class="btn btn-primary" value="增加">
	</form>
	<h3>资源列表</h3>

	<table class="table table-bordered" id="resourceTab">
		<thead>
			<tr>
				<th>名称</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
	<script type="text/javascript">
	  $(document).ready(function() {
		  if(${not empty error}){
			  alert("${error}");		 
			  }
			$.get("${pageContext.request.contextPath}/merchant/account/merchantResource",drawTable);
 		 	function drawTable (data){
		  		for(var i=0;i<data.list.length;i++){
		  			var str="<tr><td>"+data.list[i]+"</td><td><input type='button' value='删除' class='btn btn-danger' onclick=\"deletemerchantResource('"+data.list[i]+"')\"></td></tr>";
		  			$("#resourceTab").append(str);
		 		} 
		  		var messageContent=data.messageContent;
		  		$("#messageContent").attr("value",messageContent);
			}  
 		 	
 			$('#addMerchantResourceForm').validate({
				rules : {
					name:{
						required:true
					}
				},
				messages : {
					name:{
						required:"名称不能为空"
					}
				}
			});   
	  });
	  function deletemerchantResource(name){
			if(!confirm("确定该删除吗")){
				return;
			}
		  $.get("${pageContext.request.contextPath}/merchant/account/deletemerchantResource/"+name+"/",showContentInfo);
	  }
	  $("#addMerchantResourceForm").submit(function(){
 		  if (!$('#addMerchantResourceForm').valid()) {
				return false;
			}  
		  options={
			url:"${pageContext.request.contextPath}/merchant/account/addMerchantResource/",
			type:"get",
			success:showContentInfo
		  };
		  $(this).ajaxSubmit(options);
			return false;
	  });
	  $("#addMerchantResourceForm1").submit(function(){
		  options={
			url:"${pageContext.request.contextPath}/merchant/account/updateMerchantResource",
			type:"get",
			success:showContentInfo
		  };
		  $(this).ajaxSubmit(options);
			return false;
	  });
	</script>
</body>
</html>

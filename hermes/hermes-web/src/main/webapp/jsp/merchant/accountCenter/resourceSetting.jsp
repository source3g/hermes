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
	<h3>资源操作</h3>

	<form id="addMerchantResourceForm1" class="form-inline">
		<%-- <label class="control-label" for="prefix">尊敬的某某先生(女士)：</label> <input
			type="text" class="input-xlarge" placeholder="请输入短信前缀..." id="prefix"
			name="prefix" value="${merchant.merchantResource.prefix }"> <label class="control-label" for="suffix">后缀：</label>
		<input type="text" class="input-xlarge" placeholder="请输入短信后缀..."
			id="suffix" name="suffix" value="${merchant.merchantResource.suffix }"> --%>
		<input type="text" value="${merchant.merchantResource.messageContent}" id="messageContent" name="messageContent" style="border:0 solid 1px; border-top:none; border-left:none; border-right:none;width:500px">
		<input type="submit" class="btn btn-primary"  value="保存">
	</form>

	<h3>范例</h3>
	<p>操作说明:{}里填写的内容表示旺财宝电话里编辑短信时需要填写的内容，{时间}根据实际情况填写例如“XXX先生，您于2013年4月12日预定....”，{资源}填写资源列表里的数据信息，例如具体房间号“101”</p>
	<p>资源操作填写的内容：您于{时间}预定了{资源}房间.</p>
	<p>实际发送效果：尊敬的xxx先生:您于2012-04-12预定了101房间.</p>
	<br>
	
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

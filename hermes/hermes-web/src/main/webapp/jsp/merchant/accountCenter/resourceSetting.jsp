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
		<label class="control-label" for="prefix">前缀：</label> <input
			type="text" class="input-xlarge" placeholder="请输入短信前缀..." id="prefix"
			name="prefix" value="${merchant.merchantResource.prefix }"> <label class="control-label" for="suffix">后缀：</label>
		<input type="text" class="input-xlarge" placeholder="请输入短信后缀..."
			id="suffix" name="suffix" value="${merchant.merchantResource.suffix }">
		<input type="submit" class="btn btn-primary"  value="保存">
	</form>

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
			$.get("${pageContext.request.contextPath}/admin/merchant/merchantResourceList",drawTable);
 		 	function drawTable (data){
		  		for(var i=0;i<data.merchantResource.list.length;i++){
		  			var str="<tr><td>"+data.merchantResource.list[i]+"</td><td><input type='button' value='删除' class='btn btn-danger' onclick=\"deletemerchantResource('"+data.merchantResource.list[i]+"')\"></td></tr>";
		  			$("#resourceTab").append(str);
		 		} 
		  		var prefix=data.merchantResource.prefix;
		  		var suffix=data.merchantResource.suffix;
		  		$("#prefix").attr("value",prefix);
		  		$("#suffix").attr("value",suffix);
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
		  $.get("${pageContext.request.contextPath}/admin/merchant/deletemerchantResource/"+name+"/",showContentInfo);
	  }
	  $("#addMerchantResourceForm").submit(function(){
 		  if (!$('#addMerchantResourceForm').valid()) {
				return false;
			}  
		  options={
			url:"${pageContext.request.contextPath}/admin/merchant/addMerchantResource",
			type:"get",
			success:showContentInfo
		  };
		  $(this).ajaxSubmit(options);
			return false;
	  });
	  $("#addMerchantResourceForm1").submit(function(){
		  options={
			url:"${pageContext.request.contextPath}/admin/merchant/updateMerchantResource",
			type:"get",
			success:showContentInfo
		  };
		  $(this).ajaxSubmit(options);
			return false;
	  });
	  
	
	</script>
</body>
</html>
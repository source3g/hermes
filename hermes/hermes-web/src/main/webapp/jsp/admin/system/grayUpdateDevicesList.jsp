<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>版本列表</title>
</head>
<body>
	<form id="queryForm" method="get">
		<input id="pageNo" name="pageNo" type="hidden">

		<div class="well">
			<input type="text" name="sn" id="sn" placeholder="填写盒子编码...">
			<input type="button" id="addDevice" class="btn btn-primary"
				value="增加设备" onclick="add()" />
		</div>
	</form>
	<table
		class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="33%">设备名称</th>
				<th width="33%">版本</th>
				<th width="34%">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.data}" var="device">
				<tr>
					<td>${device.sn}</td>
					<td>${device.apkVersion}</td>
					<td><input type="button" value="删除" class="btn btn-danger"
						onclick="deleteThis('${device.sn}')"></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div>
		<ul class="pagination">
			<li id="firstPage"><a href="javascript:void();">首页</a></li>
			<li id="frontPage"><a href="javascript:void();">前一页</a></li>
			<li id="nextPage"><a href="javascript:void();">后一页</a></li>
			<li id="lastPage"><a href="javascript:void();">尾页</a></li>
			<li>当前第${page.currentPage}/${page.totalPageCount}页共${page.totalRecords
				}条 转到第<input type="text" id="pageNoToGo" name="pageNo"
				class="input-mini">页<input type="button" id="pageOk"
				class="btn" value="确定"></input>
			</li>
		</ul>
	</div>
	<script type="text/javascript">
    $(document).ready(function(){
     	$('#queryForm').submit(function(){
    		goToPage(1);
    		return false;
    	});
    	
    	initPage(${page.currentPage},${page.totalPageCount}); 
    	
    	$('#queryForm').validate({
			rules : {
				sn : {
					required : true,
		 			 remote:{
						type: "get",
						url:"${pageContext.request.contextPath}/admin/version/snValidate",
						data:{"sn":function(){
											return $('#sn').val();
										}
							}
					} 
				
				}
			},
			messages : {
				sn : {
					required : "请填写SN名称",
				 	remote:"盒子名称不存在" 
				}
			}
 
		});  
    	
}); 
   	function goToPage(pageNo){
		$("#pageNo").attr("value",pageNo);
		var options={
				url:"${pageContext.request.contextPath}/admin/version/toGrayUpdateDevicesList/",
				success:showContentInfo,
				error:showError
		};
		$('#queryForm').ajaxSubmit(options);
	} 
    function add(){
    	var sn=$("#sn").val();
		if (!$("#queryForm").valid()) {
			return false;
		} 
  	 	$.post("${pageContext.request.contextPath}/admin/version/addToGrayUpdateDevicesList",{"sn":sn},showInfo);
    }
    
	function deleteThis(sn){
		if(confirm("确定要删除设备"+sn+"么？")){
	 	 $.get("${pageContext.request.contextPath}/admin/version/deleteGrayUpdateDevice/"+sn+"/",showInfo);
		}
	}
	function showInfo(data){
		 $.get("${pageContext.request.contextPath}/admin/version/toGrayUpdateDevicesList/",showContentInfo);
	}
	</script>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>盒子列表</title>
</head>
<body>

	<form id="queryForm" class="well form-inline " method="get">
		<label class="control-label" for="sn">编码：</label> <input id="sn"
			type="text" name="sn" value="${device.sn}" placeholder="请输入盒子SN..." />
		<input type="text" name="merchantName" id="merchantName"
			value="${merchantName}" placeholder="请输入商户名称" />
			<input id="pageNo" name="pageNo" type="hidden"> 
			<input type="submit" class="btn btn-primary" value="查询" />
			<input type="button" class="btn btn-primary" onclick="exportCustomer();" value="导出" />
	</form>

	<table
		class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="20%">编码</th>
				<th width="20%">卡号</th>
				<th width="20%">imsi号</th>
				<th width="20%">软件版本</th>
				<th width="20%">操作</th>
			</tr>
		</thead>

		<c:forEach items="${page.data}" var="device">
			<tr>
				<td>${device.sn}</td>
				<td>${device.simInfo.serviceNo}</td>
				<td>${device.simInfo.imsiNo}</td>
				<td><c:choose>
						<c:when test="${not empty device.apkVersion }">
						${device.apkVersion }
					</c:when>
						<c:otherwise>
						未知
					</c:otherwise>
					</c:choose></td>
				<td><a class="btn btn-danger" href="javascript:void();"
					onclick="deleteById('${device.id}');">删除</a> <a
					class="btn btn-success" href="javascript:void();"
					onclick="deviceDetail('${device.id}');">详细信息</a></td>
			</tr>
		</c:forEach>

	</table>
	<div>
		<ul class="pagination">
			<li id="firstPage"><a href="javascript:void();">首页</a></li>
			<li id="frontPage"><a href="javascript:void();">前一页</a></li>
			<li id="nextPage"><a href="javascript:void();">后一页</a></li>
			<li id="lastPage"><a href="javascript:void();">尾页</a></li>
			<li>当前第${page.currentPage}/${page.totalPageCount}页
				共${page.totalRecords }条记录 转到第 <input type="text" id="pageNoToGo"
				name="pageNo" class="input-mini">页 <input type="button"
				id="pageOk" class="btn" value="确定"></input>
			</li>
		</ul>
	</div>

	<div id="errorModal" class="modal hide fade">
		<div class="modal-body">
			<p id="resultMessage"></p>
		</div>
		<div class="modal-footer">
			<a href="#" class="btn" data-dismiss="modal">确定</a>
		</div>
	</div>

	<script type="text/javascript">
		$(document).ready(function() {
			initPage(${page.currentPage},${page.totalPageCount});
			$("#queryForm").submit(function(){
				goToPage(1);
				return false;
			});
		});
		function exportCustomer(){
			var sn=$("#sn").val();
			var merchantName=$("#merchantName").val();
			if(sn||merchantName){
				$.get("${pageContext.request.contextPath}/admin/device/export/?sn="+sn+"&merchantName="+merchantName,function(data){
					window.open(data);
				});
			}else{
				alert("必须有一个条件才能导出");
			}
		}
		function goToPage(pageNo) {
			$("#pageNo").attr("value",pageNo);
			var options = {
				url : "${pageContext.request.contextPath}/admin/device/list/",
				success : showContentInfo, // post-submit callback
				error : showError
			};
			$('#queryForm').ajaxSubmit(options);
			return false;
		}

		function deleteById(id) {
			if(confirm("确定删除？")){
			$.ajax({
				url : "${pageContext.request.contextPath}/admin/device/delete/" + id + "/",
				type : "get",
				success : showContentInfo
			});
			}
		}
		function showError() {
			$("#resultMessage").html("操作失败，请重试");
			$("#errorModal").modal();
		}
		function deviceDetail(id){
			$.ajax({
				url:"${pageContext.request.contextPath}/admin/device/detail/"+id+"/",
				type:"get",
				success:showContentInfo
			});
		}
	</script>
</body>
</html>

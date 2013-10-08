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

	<form id="queryForm" class="well form-inline " method="get" action="${pageContext.request.contextPath}/admin/device/list/">
		<label class="control-label" for="sn">编码：</label> <input id="sn"
			type="text" class="input-medium " name="sn" value="${device.sn}"
			placeholder="请输入盒子SN..." /> <label class="control-label" for="sn">商户账号：</label>
		<input type="text" name="merchantAccount" id="merchantAccount"
			value="${merchantAccount}" class="input-medium "
			placeholder="请输入商户账号" /> <input id="pageNo" name="pageNo"
			type="hidden"> <input type="submit" class="btn btn-primary"
			value="查询" /> <input type="button" class="btn btn-primary"
			onclick="exportCustomer();" value="导出" />
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
				<td><a class="btn btn-danger" href="${pageContext.request.contextPath}/admin/device/delete/${device.id}/"
					onclick="return confirm('确定删除${device.sn}?')">删除</a> <a
					class="btn btn-success" href="javascript:void();"
					onclick="return showDeviceDetail('${device.id}');">详细信息</a></td>
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

	<div id="deviceDetailModal" class="modal hide fade">
		<div class="modal-header">
			<a class="close" data-dismiss="modal">&times;</a>
			<h3>设备详细信息</h3>
		</div>
		<div class="modal-body" id="deviceDetailContent">载入中</div>
		<div class="modal-footer">
			<a href="#" class="btn" data-dismiss="modal">确定</a>
		</div>
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
			activeMenu("listDevice");
			initPage(${page.currentPage},${page.totalPageCount});
			//$("#queryForm").submit(function(){
			//	goToPage(1);
			//	return false;
			//});
		});
		
		function showDeviceDetail(id){
			var url = "${pageContext.request.contextPath}/admin/device/detail/"+id+"/";
			$.get(url, function(data) {
				$("#deviceDetailContent").html(data);
				$("#deviceDetailModal").modal("show");
			});
			return false;
		}
		function exportCustomer(){
			var sn=$("#sn").val();
			var merchantAccount=$("#merchantAccount").val();
			$.get("${pageContext.request.contextPath}/admin/device/export/?sn="+sn+"&merchantAccount="+merchantAccount,function(data){
				window.open(data);
			});
			
		}
		function goToPage(pageNo) {
			$("#pageNo").attr("value",pageNo);
			$("#queryForm").submit();
		}

		function showError() {
			$("#resultMessage").html("操作失败，请重试");
			$("#errorModal").modal();
		}
	</script>
</body>
</html>

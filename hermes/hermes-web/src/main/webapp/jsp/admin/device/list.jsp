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
		<label class="control-label" for="sn">名称：</label> <input type="text"
			name="sn" value="${device.sn}" placeholder="请输入盒子SN..." /> <input
			id="pageNo" name="pageNo" type="hidden"> <input type="submit"
			class="btn btn-primary" value="查询" />
	</form>

	<table
		class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="25%">名称</th>
				<th width="25%">绑定SIM卡号</th>
				<th width="25%">绑定状态</th>
				<th width="25%">操作</th>
			</tr>
		</thead>

		 <c:forEach items="${page.data}" var="deviceVo">
			<tr>
				<td>${deviceVo.device.sn}</td>
				<td>${deviceVo.device.sim.no}</td>
				<td><c:if test="${not empty deviceVo.merchant.name}">[已绑定商户 : ${deviceVo.merchant.name}]</c:if>
				<c:if test="${empty deviceVo.merchant.name}">[未绑定商户]</c:if></td>
				<td><a class="btn btn-danger" href="javascript:void();"
					onclick="deleteById('${deviceVo.device.id}');">删除</a>
				<a class="btn btn-success" href="javascript:void();"
					onclick="findById('${deviceVo.device.id}');">详细信息</a></td>	
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
			if(${not empty error}==true){
				alert('${error}');
			} 
			initPage(${page.currentPage},${page.totalPageCount});
			$("#queryForm").submit(function(){
				goToPage(1);
				return false;
			});
		});
		
		function goToPage(pageNo) {
			$("#pageNo").attr("value",pageNo);
			var options = {
				url : "${pageContext.request.contextPath}/admin/device/list/",
				success : showList, // post-submit callback
				error : showError
			};
			$('#queryForm').ajaxSubmit(options);
			return false;
		}

		function deleteById(id) {
			$.ajax({
				url : "${pageContext.request.contextPath}/admin/device/delete/" + id + "/",
				type : "get",
				success : showList
			});
		}
		function showList(data) {
			$("#pageContentFrame").html(data);
			
		}
		function showError() {
			$("#resultMessage").html("操作失败，请重试");
			$("#errorModal").modal();
		}
		function findById(id){
			$.ajax({
				url:"${pageContext.request.contextPath}/admin/device/findById/"+id+"/",
				type:"get",
				success:toDeviceInformation
			});
		}
		function toDeviceInformation(data){
			$("#pageContentFrame").html(data);
		}
	</script>
</body>
</html>

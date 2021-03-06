<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发送记录</title>
</head>
<body>
	<form id="queryForm" class="well form-inline " method="get" action="${pageContext.request.contextPath}/merchant/message/toMessageList/">
		<label class="control-label" for="name">电话号码：</label> 
			<input type="text"name="phone" value="${phone}" placeholder="请输电话号码...">
			<label class="control-label" for="name">日期：</label>
			<input type="text" class="input-medium" name="startTime"placeholder="起始日期..." onclick="WdatePicker();" value="${startTime}"/> 
			<input type="text" class="input-medium" name="endTime"placeholder="结束日期..." onclick="WdatePicker();" value="${endTime}"/>
			<label class="control-label" for="name">顾客组：</label>
			<select id="customerGroupName" name="customerGroupName" class="input-medium">
				<option value="">请选择</option>
			</select>
		<input id="pageNo" name="pageNo" type="hidden"> <input
			type="submit" class="btn btn-primary" value="查询">
	</form>
	<table
		class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="11%">顾客姓名</th>
				<th width="11%">顾客组别</th>
				<th width="12%">顾客手机号</th>
				<th width="8%">发送数量</th>
				<th width="10%">发送状态</th>
				<th width="16%">发送内容</th>
				<th width="12%">发送形式</th>
				<th width="20%">发送时间</th>
				
				
			</tr>
		</thead>
		<c:forEach items="${page.data}" var="messageSendLog">
			<tr>
				<td width="11%">${messageSendLog.customerName }</td>
				<td width="11%">${messageSendLog.customerGroup.name }</td>
				<td width="14%">${messageSendLog.phone }</td>
				<td width="8%">${messageSendLog.sendCount }</td>
				<td width="8%">${messageSendLog.status }</td>
				<td width="18%" id="content" title="${messageSendLog.content }">
				<div style="width:200px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis; ">${messageSendLog.content }</div></td>
				<td width="14%">${messageSendLog.type }</td>
				<td width="20%">${messageSendLog.sendTime }</td>
		</c:forEach>
	</table>
	
	<div>
		<ul class="pagination">
			<li id="firstPage"><a href="javascript:void();">首页</a></li>
			<li id="frontPage"><a href="javascript:void();">前一页</a></li>
			<li id="nextPage"><a href="javascript:void();">后一页</a></li>
			<li id="lastPage"><a href="javascript:void();">尾页</a></li>
			<li>当前第${page.currentPage}/${page.totalPageCount}页共${page.totalRecords }条 转到第
			<input type="text" id="pageNoToGo" name="pageNo"
				class="input-mini">页<input type="button" id="pageOk"
				class="btn" value="确定"></input>
			</li>
		</ul>
	</div>
	<script type="text/javascript">
	 $(document).ready(function(){
		 activeMenu("messageList");
		 initPage(${page.currentPage},${page.totalPageCount});
		initCustomerGroupList()
	});
		function goToPage(pageNo){
			$("#pageNo").attr("value",pageNo);
			$('#queryForm').submit();
			
		}
		function initCustomerGroupList() {
			$.ajax({
				url : "${pageContext.request.contextPath}/merchant/customerGroup/listAllJson",
				type : "get",
				dataType : "json",
				success : initCustomerSelection,
				error : error
			});
		}
		function error() {
			alert("顾客组初始化失败，请重新进入该页面");
		}
		function initCustomerSelection(data) {
			for ( var i = 0; i < data.length; i++) {
				if(data[i].name=='${customerGroupName  }'){
					$("#customerGroupName").append("<option value='"+data[i].name+"' selected>" + data[i].name + "</option>");	
				}else{
					$("#customerGroupName").append("<option value='"+data[i].name+"'>" + data[i].name + "</option>");
				}
			}
			
		} 
	</script>
</body>
</html>
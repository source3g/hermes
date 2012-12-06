<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>导入日志列表</title>
</head>
<body>
	<form id="queryForm" class="well form-inline " method="get">
		<label class="control-label" for="name">导入时间：</label> <input
			type="text" class="input-medium" name="startTime"
			placeholder="起始日期..." onclick="WdatePicker();"
			value="${startTime}"/> 
		<input type="text" class="input-medium" name="endTime"
			placeholder="结束日期..." onclick="WdatePicker();"
			value="${endTime}"/> <input
			type="hidden" name="pageNo" id="pageNo"> <input type="submit"
			class="btn btn-primary" value="查询">
	</form>

	<table
		class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="10%">旧文件名</th>
				<th width="10%">新文件名</th>
				<th width="10%">导入状态</th>
				<th width="10%">总记录数</th>
				<th width="10%">导入记录数</th>
				<th width="10%">导入时间</th>
				<th width="10%">操作</th>
			</tr>
		</thead>
		<c:forEach items="${page.data }" var="importLog">
			<tr>
				<td>${importLog.name }</td>
				<td>${importLog.newName }</td>
				<td>${importLog.status }</td>
				<td>${importLog.totalCount }</td>
				<td>${importLog.totalCount - importLog.failedCount }</td>
				<td>${importLog.importTime }</td>
				<td><input type="button" class="btn btn-success" onclick="importLogInfo('${importLog.id }')" value="详情"></td>
			</tr>

		</c:forEach>
	</table>

	<div>
		<ul class="pagination">
			<li id="firstPage"><a href="javascript:void();">首页</a></li>
			<li id="frontPage"><a href="javascript:void();">前一页</a></li>
			<li id="nextPage"><a href="javascript:void();">后一页</a></li>
			<li id="lastPage"><a href="javascript:void();">尾页</a></li>
			<li >当前第${page.currentPage}/${page.totalPageCount}页共${page.totalRecords }条 转到第<input
			type="text" id="pageNoToGo" name="pageNo" class="input-mini">页<input
			type="button" id="pageOk" class="btn" value="确定"></input></li>
		</ul>
	</div>

	<script type="text/javascript">
$(document).ready(function(){
	$('#queryForm').submit(function(){
		goToPage(1);
		return false;
	});
	initPage();
});

function goToPage(pageNo){
	$("#pageNo").attr("value",pageNo);
	var options={
			url:"${pageContext.request.contextPath}/merchant/customer/importLog/",
			success:showList,
			error:showError
	};
	$('#queryForm').ajaxSubmit(options);
	
}
function showError() {
	$("#resultMessage").html("操作失败，请重试");
	$("#errorModal").modal();
}
function showList(data){
	$("#pageContentFrame").html(data);
}
function initPage(){
	$('#pageOk').click(function(){
		var pageNoToGo=$('#pageNoToGo').val();
		goToPage(pageNoToGo);
	});
	
	if(${page.totalPageCount}==1||${page.totalPageCount}==0){
		$("#firstPage").addClass("active");
		$("#frontPage").addClass("active");
		$("#nextPage").addClass("active");
		$("#lastPage").addClass("active");
	}else if(${page.currentPage}==1){
		$("#firstPage").addClass("active");
		$("#frontPage").addClass("active");
		$("#nextPage").removeClass("active");
		$("#lastPage").removeClass("active");
		
		$('#nextPage').click(function(){
			goToPage(${page.nextPageNo});
		});
		$("#lastPage").click(function (){
			goToPage(${page.lastPageNo});
		});		
		
	}else if(${page.currentPage}==${page.totalPageCount}){
		$("#firstPage").removeClass("active");
		$("#frontPage").removeClass("active");
		$("#nextPage").addClass("active");
		$("#lastPage").addClass("active");
		
		$("#firstPage").click(function (){
			goToPage(${page.firstPageNo});
		});
		$("#frontPage").click(function (){
			goToPage(${page.previousPageNo});
		});
	}else{
		$("#firstPage").click(function (){
			goToPage(${page.firstPageNo});				
			});
		$("#frontPage").click(function (){
			goToPage(${page.previousPageNo});				
			});
		$("#nextPage").click(function (){
			goToPage(${page.nextPageNo});			
			});
		$("#lastPage").click(function (){
			goToPage(${page.lastPageNo});			
			});
	}
}
	function importLogInfo(id){
		$.ajax({
			url:"${pageContext.request.contextPath}/merchant/customer/importLog/merchantInfo/"+id+"/",
			type:"get",
		success:showList
		});
	}
</script>

</body>
</html>
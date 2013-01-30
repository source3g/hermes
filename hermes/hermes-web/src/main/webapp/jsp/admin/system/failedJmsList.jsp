<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Jms失败记录</title>
</head>
<body>
<form id="queryForm"  method="get">
		<input id="pageNo" name="pageNo" type="hidden"> 
	</form>
	<table
		class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="25%">目标网址</th>
				<th width="25%">Jsm消息内容</th>
				<th width="25%">发送类型 </th>
				<th width="25%">失败日期</th>
			</tr>
		</thead>
		<c:forEach items="${page.data}" var="failedMessage">
		<tr>
			<td>${failedMessage.destination}</td>
			<td>${failedMessage.message}</td>
			<td>${failedMessage.properties}</td>
			<td>${failedMessage.date}</td>
		</tr>
		</c:forEach>
	</table>
	<div>
		<ul class="pagination">
			<li id="firstPage"><a href="javascript:void();">首页</a></li>
			<li id="frontPage"><a href="javascript:void();">前一页</a></li>
			<li id="nextPage"><a href="javascript:void();">后一页</a></li>
			<li id="lastPage"><a href="javascript:void();">尾页</a></li>
			<li>当前第${page.currentPage}/${page.totalPageCount}页共${page.totalRecords }条 转到第<input
				type="text" id="pageNoToGo" name="pageNo" class="input-mini">页<input
				type="button" id="pageOk" class="btn" value="确定"></input></li>
		</ul>
	</div>
	<script type="text/javascript">
	$(document).ready(function(){
		initPage();
});
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
	function goToPage(pageNo){
		$("#pageNo").attr("value",pageNo);
		var options={
			    url:"${pageContext.request.contextPath}/admin/system/monitor/failedJms",
				success:showList,
				error:showError
		};
		$('#queryForm').ajaxSubmit(options);
		
	}
	function showList(data){
		$("#pageContentFrame").html(data);
	}
	</script>
</body>
</html>
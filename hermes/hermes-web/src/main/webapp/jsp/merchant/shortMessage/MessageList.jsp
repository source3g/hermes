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
	<form id="queryForm" class="well form-inline " method="get">
		<label class="control-label" for="name">名称：</label> <input type="text"
			name="name" value="${merchant.name}" placeholder="请输入商户名称...">
		<input id="pageNo" name="pageNo" type="hidden"> <input
			type="submit" class="btn btn-primary" value="查询">
	</form>
	<table
		class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="14%">顾客姓名</th>
				<th width="14%">顾客组别</th>
				<th width="14%">顾客手机号</th>
				<th width="8%">发送数量</th>
				<th width="18%">发送内容</th>
				<th width="14%">发送形式</th>
				<th width="18%">发送时间</th>
				
				
			</tr>
		</thead>
		<c:forEach items="${page.data}" var="messageSendLog">
			<tr>
				<td width="14%">${messageSendLog.customerName }</td>
				<td width="14%">${messageSendLog.customerGroupName }</td>
				<td width="14%">${messageSendLog.pnone }</td>
				<td width="8%">${messageSendLog.sendCount }</td>
				<td width="18%" id="content" title="${messageSendLog.content }"  ><div style="width:200px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis; ">${messageSendLog.content }</div></td>
				<td width="14%">${messageSendLog.type }</td>
				<td width="18%">${messageSendLog.date }</td>
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
		// $('#content').popover(show);
			initPage();
	});
		function goToPage(pageNo){
			$("#pageNo").attr("value",pageNo);
			var options={
					url:"${pageContext.request.contextPath}/merchant/message/toMessageList/",
					success:showList,
					error:showError
			};
			$('#queryForm').ajaxSubmit(options);
			
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
		function showList(data){
			$("#pageContentFrame").html(data);
		}
		function showError() {
			$("#resultMessage").html("操作失败，请重试");
			$("#errorModal").modal();
		}
	</script>
</body>
</html>
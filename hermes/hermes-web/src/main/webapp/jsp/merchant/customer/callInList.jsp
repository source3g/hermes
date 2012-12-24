<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>来电列表</title>
</head>
<body>
	<form id="queryForm" class="well form-inline " method="get">
		<label class="control-label" for="name">电话：</label> <input type="text"
			name="phone" value="${customer.phone}" placeholder="请输入电话号码...">
		<input id="pageNo" name="pageNo" type="hidden"> <label>顾客类型</label>
		<select name="customerType" id="customerTypeSel">
			<option value="allCustomer"
				<c:if test="${customerType eq 'allCustomer' }"> selected="selected" </c:if>>全部顾客</option>
			<option value="newCustomer"
				<c:if test="${customerType eq 'newCustomer' }"> selected="selected" </c:if>>新顾客</option>
			<option value="oldCustomer"
				<c:if test="${customerType eq 'oldCustomer' }"> selected="selected" </c:if>>老顾客</option>
		</select>
		 <input type="submit" class="btn btn-primary" value="查询">
	</form>

	<table
		class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="20%">姓名</th>
				<th width="20%">电话</th>
				<th width="20%">最后来电时间</th>
				<th width="20%">今日来电次数</th>
				<th width="20%">操作</th>
			</tr>
		</thead>
		<c:forEach items="${page.data}" var="customer">
			<tr>
				<td width="20%">${customer.name }</td>
				<td width="20%">${customer.phone }</td>
				<td width="20%">${customer.lastCallInTime }</td>
				<td width="20%">${customer.callInCountToday }</td>
				<td width="20%"><a class="btn btn-success"
					href="javascript:void();" onclick="editById('${customer.id}');">编辑</a></td>
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
				共${page.totalRecords }条 转到第<input type="text" id="pageNoToGo"
				name="pageNo" class="input-mini">页<input type="button"
				id="pageOk" class="btn" value="确定"></input>
			</li>
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
		function editById(id) {
			loadPage("${pageContext.request.contextPath}/merchant/customer/toUpdate/" + id + "/?isNewCustomer=true");
		}
		
		function goToPage(pageNo){
			$("#pageNo").attr("value",pageNo);
			var options={
					url:"${pageContext.request.contextPath}/merchant/customer/callInList/",
					success:showList
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
	</script>
</body>
</html>
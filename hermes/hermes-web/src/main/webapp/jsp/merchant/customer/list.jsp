<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>顾客列表</title>
</head>
<body>
	<form id="queryForm" class="well form-inline " method="get">
		<label class="control-label" for="name">姓名：</label> <input type="text"
			name="name" class="input-medium" value="${customer.name}"
			placeholder="请输入顾客名称..."> <label class="control-label"
			for="phone">电话：</label> <input type="text" name="phone"
			class="input-medium" value="${customer.phone}"
			placeholder="请输入顾客电话..."> <input id="pageNo" name="pageNo"
			type="hidden"> 
			 <input id="sortType" name="sortType" value="${sortType }"
			type="hidden"> 
			 <input id="phoneSortType" name="phoneSortType" value="${phoneSortType }"
			type="hidden"> 
			 <input id="property" name="property" value="${property }"
			type="hidden">
			
			<select name="type" class="input-small">
				<option value="newCustomer" <c:if test="${type eq 'newCustomer' }"> selected="selected"</c:if>>未编辑</option>
				<option value="oldCustomer" <c:if test="${type eq 'oldCustomer' }"> selected="selected"</c:if>>已编辑</option>
				<option value="allCustomer" <c:if test="${type eq 'allCustomer' }"> selected="selected"</c:if>>全部</option>
			</select>
			<input type="submit" class="btn btn-primary"
			value="查询">
			<input  type="button" class="btn btn-primary"
			value="新增"  onclick="loadPage('${pageContext.request.contextPath}/merchant/customer/add/');">
			<span><input type="button" onclick="exportCustomer();" class="btn btn-primary" value="导出" data-loading-text="导出中..." id="exportCustomerBtn">
			<input class="btn btn-primary" type="button"onclick="loadPage('${pageContext.request.contextPath}/merchant/customer/import/');" value="导入">
			<input class="btn btn-primary" type="button" onclick="loadPage('${pageContext.request.contextPath}/merchant/customer/importLog/');" value="查看导入日志">
			<input class="btn btn-primary" type="button" onclick="window.open('${pageContext.request.contextPath}/jsp/merchant/template.xls');" value="导入日志模板下载"></span>
	</form>
	<table
		class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="25%"><a href="javascript:void();" id="sortByName" onclick="sortByName()" >姓名<c:if test="${sortType eq 'asc'and empty phoneSortType   }">↑</c:if><c:if test="${sortType eq 'desc'and  empty phoneSortType  }">↓</c:if> </a></th>
				<th width="25%"><a href="javascript:void();" id="sortByPhone" onclick="sortByPhone()" >电话<c:if test="${phoneSortType eq 'asc'and  empty sortType  }">↑</c:if><c:if test="${phoneSortType eq 'desc'and  empty sortType }">↓</c:if></a></th>
				<th width="25%">所属顾客组</th>
				<th width="25%">操作</th>
			</tr>
		</thead>
		<c:forEach items="${page.data}" var="customer">
			<tr>
				<td>${customer.name }</td>
				<td>${customer.phone }</td>
				<td>${customer.customerGroup.name }</td>
				<td><a class="btn btn-success" href="javascript:void();"
					onclick="toModify('${customer.id}');">修改</a> <a
					class="btn btn-danger" href="javascript:void();"
					onclick="deleteById('${customer.id}');">删除</a></td>

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
	<div id="errorModal" class="modal hide fade">
		<div class="modal-body">
			<p id="resultMessage"></p>
		</div>
		<div class="modal-footer">
			<a href="#" class="btn" data-dismiss="modal">确定</a>
		</div>
	</div>

	<script type="text/javascript">
	function sortByName(){
		if(${empty sortType}){
			$("#sortType").attr("value",'desc');
			$("#phoneSortType").attr("value",null);
		}
		
		if(${sortType eq 'desc' }){
		$("#sortType").attr("value",'asc');
		$("#phoneSortType").attr("value",null);
		}
		if(${sortType eq 'asc' }){
			$("#sortType").attr("value",'desc');
			$("#phoneSortType").attr("value",null);
		}
		
		$("#property").attr("value","name");
		goToPage(1);
	}
	function sortByPhone(){
		if(${empty phoneSortType}){
			$("#phoneSortType").attr("value",'desc');
			$("#sortType").attr("value",null);
		}
		
		if(${phoneSortType eq 'desc' }){
		$("#phoneSortType").attr("value",'asc');
		$("#sortType").attr("value",null);
		}
		if(${phoneSortType eq 'asc' }){
			$("#phoneSortType").attr("value",'desc');
			$("#sortType").attr("value",null);
		}
		
		$("#property").attr("value","phone");
		goToPage(1);
	}
		function deleteById(id) {
			if(!confirm("确定该删除吗")){
				return;
			}
		$.ajax({
			url:"${pageContext.request.contextPath}/merchant/customer/delete/"+id+"/",
			type:"get",
			success:showList		
		});	
	}
		function showList(data){
			$("#pageContentFrame").html(data);
		}
		function toModify(id){
		loadPage("${pageContext.request.contextPath}/merchant/customer/toUpdate/"+id+"/");
		$.history.load("${pageContext.request.contextPath}/merchant/customer/toUpdate/"+id+"/");
		}
		
		function goToPage(pageNo){
			$("#pageNo").attr("value",pageNo);
			var options={
					url:"${pageContext.request.contextPath}/merchant/customer/list/",
					success:showList,
					error:showError
			};
			$('#queryForm').ajaxSubmit(options);
		}
		function showError() {
			$("#resultMessage").html("操作失败，请重试");
			$("#errorModal").modal();
		}
	    $(document).ready(function(){
	    	$('#queryForm').submit(function(){
	    		goToPage(1);
	    		return false;
	    	});
	    	initPage(${page.currentPage},${page.totalPageCount});
	});
	    
	    function exportCustomer(){
	    	$("#pageNo").attr("value",pageNo);
	    	$('#exportCustomerBtn').button('loading');
			var options={
					url:"${pageContext.request.contextPath}/merchant/customer/export/",
					dataType:'json',
					success:function (data){
						window.open(data);
					},
					error:showError
			};
			$('#queryForm').ajaxSubmit(options);
	    }
	</script>
</body>
</html>

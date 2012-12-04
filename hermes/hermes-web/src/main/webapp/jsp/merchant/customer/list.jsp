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
			type="hidden"> <input type="submit" class="btn btn-primary"
			value="查询">
	</form>

	<table
		class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="30%">姓名</th>
				<th width="30%">电话</th>
				<th width="40%">操作</th>
			</tr>
		</thead>
		<c:forEach items="${page.data}" var="customer">
			<tr>
				<td>${customer.name }</td>
				<td>${customer.phone }</td>
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
			<li><button onclick="exportCustomer();">导出</button></li>
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
	
		function deleteById(id) {
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
	    
	    function exportCustomer(){
	    	$("#pageNo").attr("value",pageNo);
			var options={
					url:"${pageContext.request.contextPath}/merchant/customer/export/",
					dataType:'json',
					success:function (data){
						//alert(data);
						window.open(data);
					},
					error:showError
			};
			$('#queryForm').ajaxSubmit(options);
	    }
	</script>
</body>
</html>

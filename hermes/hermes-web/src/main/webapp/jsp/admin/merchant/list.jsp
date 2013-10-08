<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查询商户</title>
</head>
<body>
	<form action="${pageContext.request.contextPath}/admin/merchant/list/" id="queryForm" class="well form-inline " method="get">
		<label class="control-label" for="name">名称：</label>
		<input type="text" name="name" class="input-medium" value="${merchant.name}" placeholder="请输入商户名称...">
		<label class="control-label" for="name">账号：</label>
		<input type="text" name="account" class="input-medium" value="${merchant.account}" placeholder="请输入商户账号...">
		<input id="pageNo" name="pageNo" type="hidden">
		<input type="submit" class="btn btn-primary" value="查询">
	</form>
	<table class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="25%">账号</th>
				<th width="25%">名称</th>
				<th width="25%">地址</th>
				<th width="25%">操作</th>
			</tr>
		</thead>
		<c:forEach items="${page.data}" var="merchant">
			<tr>
				<td>${merchant.account }</td>
				<td>
					${merchant.name }
					<c:if test="${merchant.canceled==true }">  [已删除]</c:if>
				</td>
				<td>${merchant.addr }</td>
				<td>
					<a class="btn btn-success" href="${pageContext.request.contextPath}/admin/merchant/detail/${merchant.id}/">详细信息</a>
					<a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/merchant/toModify/${merchant.id}/">修改</a>
					<c:if test="${merchant.canceled==false}">
						<a class="btn btn-danger" href="${pageContext.request.contextPath}/admin/merchant/cancel/${merchant.id}/"
							onclick="return confirm('确定该删除${merchant.name}吗')">删除</a>
					</c:if>
					<c:if test="${merchant.canceled==true}">
						<a class="btn btn-danger" href="${pageContext.request.contextPath}/admin/merchant/recover/${merchant.id}/">恢复</a>
					</c:if>
					<a class="btn btn-success" href="${pageContext.request.contextPath}/admin/merchant/toSetDictionary/${merchant.id}/">设置</a>
				</td>
			</tr>
		</c:forEach>
	</table>
	<div>
		<ul class="pagination">
			<li id="firstPage">
				<a href="javascript:void();">首页</a>
			</li>
			<li id="frontPage">
				<a href="javascript:void();">前一页</a>
			</li>
			<li id="nextPage">
				<a href="javascript:void();">后一页</a>
			</li>
			<li id="lastPage">
				<a href="javascript:void();">尾页</a>
			</li>
			<li>
				当前第${page.currentPage}/${page.totalPageCount}页共${page.totalRecords }条 转到第
				<input type="text" id="pageNoToGo" name="pageNo" class="input-mini">
				页
				<input type="button" id="pageOk" class="btn" value="确定"></input>
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
		function goToPage(pageNo){
			$("#pageNo").attr("value",pageNo);
			$('#queryForm').submit();
		}
	    $(document).ready(function(){
	    	activeMenu("listMerchant");
	    	initPage(${page.currentPage},${page.totalPageCount});
	});
	    
	</script>
</body>
</html>

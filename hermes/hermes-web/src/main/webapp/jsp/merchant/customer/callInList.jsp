<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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
				<c:if test="${customerType eq 'newCustomer' }"> selected="selected" </c:if>>未编辑顾客</option>
			<option value="oldCustomer"
				<c:if test="${customerType eq 'oldCustomer' }"> selected="selected" </c:if>>已编辑顾客</option>
		</select> <input type="submit" class="btn btn-primary" value="查询">
	</form>

	<table
		class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="20%">姓名</th>
				<th width="20%">电话</th>
				<th width="20%">最后来电时间</th>
				<th width="20%">通话次数</th>
				<th width="20%">操作</th>
			</tr>
		</thead>
		<c:forEach items="${page.data}" var="customer">
			<tr>
				<td width="20%">${customer.name }</td>
				<td width="20%">${customer.phone }</td>
				<td width="20%">${customer.lastCallInTime }</td>
				<td width="20%">${fn:length(customer.callRecords) }</td>
				<td width="20%"><a class="btn btn-success"
					href="javascript:void();"
					onclick="showCallRecords('${customer.id}');">详情</a> <a
					class="btn btn-success" href="#"
					onclick="return editCustomer('${customer.id}');">编辑</a> <a
					class="btn btn-success" href="#"
					onclick="return sendMessage('${customer.id}');">发短信</a></td>
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

	<div id="sendMessageModal" class="modal hide fade">
		<div>短信内容:</div>
		<div>
			<textarea name="messageContent"></textarea>
		</div>
	</div>

	<div id="myModal" class="modal hide fade">
		<div class="modal-header">
			<a class="close" data-dismiss="modal">&times;</a>
			<h3>顾客来电详情</h3>
		</div>
		<div class="modal-body">
			<table id="customersTab" class="table table-bordered table-striped">
				<thead>
					<tr>
						<td>来电时间</td>
						<td>通话时长</td>
						<td>通话状态</td>
					</tr>
				</thead>
				<tbody id="callInTb">
				</tbody>
			</table>
			<div>
				<input type="button" class="btn btn-primary" id="customersFormBtn"
					value="确定" onclick="closeModal();"></input>
			</div>
		</div>
		<div class="modal-footer"></div>
	</div>


	<script type="text/javascript">
		$(document).ready(function (){
			$('#queryForm').submit(function(){
	    		goToPage(1);
	    		return false;
	    	});
			initPage(${page.currentPage},${page.totalPageCount});
		});
		
		function editCustomer(id){
			loadPage("${pageContext.request.contextPath}/merchant/customer/toUpdate/"+id+"/");
			return false;
		}
		function showCallRecords(id){
			$.get("${pageContext.request.contextPath}/merchant/customer/get/"+id+"/",function callback(data){
				$("#callInTb").html("");
				for (var i=0;i<data.callRecords.length;i++){
					var callRecords=data.callRecords;
					var status="未知";
					if(callRecords[i].callStatus==0){
						if(callRecords[i].callDuration==0){
							status="打入未接通";
						}else{
							status="打入";
						}
					}
					if(callRecords[i].callStatus==1){
						if(callRecords[i].callDuration==0){
							status="拨出未接通";
						}else{
							status="拨出";
						}
					}
					var tr="<tr> <td>"+callRecords[i].callTime+"</td> <td>"+callRecords[i].callDuration+"</td><td>"+status+"</td> </tr>";
					$("#customersTab").append(tr);
				}
				$("#myModal").modal();
			});
		}
		function goToPage(pageNo){
			$("#pageNo").attr("value",pageNo);
			var options={
					url:"${pageContext.request.contextPath}/merchant/customer/callInList/",
					success:showContentInfo
			};
			$('#queryForm').ajaxSubmit(options);
		}
		
		function closeModal(){
			$("#myModal").modal("hide");
		}
		function sendMessage(id){
			
		}
	</script>
</body>
</html>

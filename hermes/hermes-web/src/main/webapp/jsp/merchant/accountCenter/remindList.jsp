<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>提醒列表</title>
</head>
<body>
	<form id="remindListForm" method="post" class="form-horizontal">
		<table class="table table-bordered" id="customerRemindDtos">
			<thead>
				<tr>
					<th width="18%">提醒标题</th>
					<th width="18%">提醒内容</th>
					<th width="18%">提前天数</th>
					<th width="28%">顾客信息</th>
					<th width="18%">操作</th>
				</tr>
			</thead>
			<c:forEach items="${reminds }" var="remind" varStatus="status">
				<tr>
					<td width="18%">${remind.title}</td>
					<td width="18%" title="${remind.content }"><div
							style="width: 200px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
							${remind.content }</div></td>
					<td width="18%">${remind.advancedTime }</td>
					<td width="28%"><input type="button" value="详细信息" onclick="showCustomerList('${status.index}')"
						class="btn btn-success"><span>[共有
							${fn:length(remind.customers)}位客户]</span>
						<div id="customerModal${status.index}" class="modal hide fade">
							<div class="modal-body">
								<table id="customerInfo"
									class="table table-bordered table-striped">
									<thead>
										<tr>
											<th>顾客姓名</th>
											<th>顾客电话</th>
											<th>提醒日期</th>
										</tr>
										<c:forEach items="${remind.customers }" var="customer">
											<tr><td>${customer.customerName }</td><td>${customer.phone}</td><td>
												<fmt:formatDate value="${customer.remindTime }" pattern="yyyy年MM月dd日"/></td></tr>
										</c:forEach>
									</thead>
								</table>
								<div>
									<input type="button" class=" btn btn-primary "
										data-dismiss="modal" id="customersFormBtn" value="关闭 "></input>
								</div>
							</div>
							<div class="modal-footer"></div>
						</div>
						</td>
					<td width="18%"><input type="button" class="btn btn-success"
						value="一键发送" onclick="sendMessages('${remind.title}')"><input
						type="button" class="btn btn-danger" value="忽略发送"
						onclick="ignoreSendMessages('${remind.title}')"></td>
				</tr>
			</c:forEach>
		</table>
	</form>

	<script type="text/javascript">
	  $(document).ready(function() {
		  activeMenu("merchantRemind");
			if(${not empty error}){
				alert("${error}");
			}
			if(${not empty success}){
				alert("发送成功");
			}
			initRemind();
		});
	  function showCustomerList(index){
		  $("#customerModal"+index).modal();
	  }
		function sendMessages(title){
			loadPage("${pageContext.request.contextPath}/merchant/account/sendMessages/"+title+"/");
		}
		function ignoreSendMessages(title){
			if(!confirm("忽略以后将不能再发送此条短信,确定该忽略吗?")){
				return;
			}
			loadPage("${pageContext.request.contextPath}/merchant/account/ignoreSendMessages/"+title+"/");
		}
	</script>
</body>
</html>

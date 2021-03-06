<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>短信发送失败记录</title>
</head>
<body>
<form id="queryForm" class="well form-inline " method="get" action="${pageContext.request.contextPath}/admin/message/failed/list/">
			<label class="control-label" for="name">日期：</label>
			<input type="text" class="input-medium" name="startTime"placeholder="起始日期..." onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" value="${startTime}"/> 
			<input type="text" class="input-medium" name="endTime"placeholder="结束日期..." onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" value="${endTime}"/>
			<label class="control-label" for="name">发送状态：</label>
			<select id="status" name="status" class="input-medium">
				<option value="">请选择</option>
				<option value="已发送"
				<c:if test="${status eq '已发送' }"> selected="selected"</c:if>>已发送</option>
				<option value="提交失败"
				<c:if test="${status eq '提交失败' }"> selected="selected"</c:if>>提交失败</option>
				<option value="发送失败"
				<c:if test="${status eq '发送失败' }"> selected="selected"</c:if>>发送失败</option>
				<option value="商户不存在"
				<c:if test="${status eq '商户不存在' }"> selected="selected"</c:if>>商户不存在</option>
				<option value="余额不足发送失败"
				<c:if test="${status eq '余额不足发送失败' }"> selected="selected"</c:if>>余额不足发送失败</option>
			</select>
		<input id="pageNo" name="pageNo" type="hidden"/> 
		<input type="submit" class="btn btn-primary" value="查询"/>
	</form>
	<table
		class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="16%">电话号码</th>
				<th width="18%">短信内容</th>
				<th width="16%">发送类别 </th>
				<th width="16%">发送状态</th>
				<th width="18%">发送时间</th>
				<th width="16%">操作</th>
			</tr>
		</thead>
		<c:forEach items="${page.data}" var="ShortMessageRecord">
		<tr>
			<td>${ShortMessageRecord.phone}</td>
			<td  title="${ShortMessageRecord.content }">
			<div style="width:200px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis; ">${ShortMessageRecord.content }</div>
			</td>
			<td>${ShortMessageRecord.messageType}</td>
			<td>${ShortMessageRecord.status}</td>
			<td>${ShortMessageRecord.sendTime}</td>
			<td><input type="button" value="重新发送" class="btn btn-primary" id="${ShortMessageRecord.id}" data-loading-text="发送中..." onclick="failedMessageSendAgain('${ShortMessageRecord.id}')"></td>
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
				type="button" id="pageOk" class="btn" value="确定"></input>
				<input type="button" class="btn btn-primary" id="oneKeyAllSend" data-loading-text="发送中..." value="一键发送" onclick='oneKeyAllSend()'></li>
		</ul>
	</div>
	<script type="text/javascript">
	$(document).ready(function(){
		activeMenu("failedMessageList");
		initPage(${page.currentPage},${page.totalPageCount});
	});
	function goToPage(pageNo){
		$("#pageNo").attr("value",pageNo);
		$('#queryForm').submit();
	}
	
	function failedMessageSendAgain(id){
		 $('#'+id).button('loading')
		$.get("${pageContext.request.contextPath}/admin/message/failed/resend/"+id+"/",showInfo);
	 function showInfo(data){
		  if(data!=null){
			 alert(data);
		 } 
		  loadPage("${pageContext.request.contextPath}/admin/message/failed/list");
	 }
	
	}
	function oneKeyAllSend(){
		var str=prompt("请输入密码");
		if(str!="12345"){
			return;
		}
		 $('#oneKeyAllSend').button('loading')
		var options={
			    url:"${pageContext.request.contextPath}/admin/message/failed/resendAll/",
				success:showAllInfo,
				error:showError
		};
		$('#queryForm').ajaxSubmit(options);
		
		 function showAllInfo(data){
			  if(data!=null){
				 alert(data);
			 } 
			  loadPage("${pageContext.request.contextPath}/admin/message/failed/list");
		 }
	}
	</script>
</body>
</html>
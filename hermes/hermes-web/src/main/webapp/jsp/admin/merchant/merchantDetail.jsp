<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商户详情</title>
</head>
<body>
	<form id="addMerchantForm" class="form-horizontal">
		<div class="control-group">
			<label class="control-label" for="name">名称：</label>
			<div class="controls">
				<input type="text" readonly="readonly" class="input-xlarge"
					placeholder="请输入商户名称..." id="name" name="name"
					value="${merchant.name}">
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="addr">地址：</label>
			<div class="controls">
				<input type="text" readonly="readonly" class="input-xlarge"
					placeholder="请输入商户地址..." id="addr" name="addr"
					value="${merchant.addr}"> <span class="help-inline"></span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="tag"> 标签： </label>
			<div class="controls">
				<span id="tagName"><c:forEach
						items="${merchant.merchantTagNodes}" var="merchantTagNode"
						varStatus="status">
						<input type="hidden" class="tagId"
							name="merchantTagNodes[${status.index }].id"
							value="${merchantTagNode.id}">
						<span>${merchantTagNode.name}</span>
					</c:forEach> </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="tag"> 商户所属销售： </label>
			<div class="controls">
				<span id="salerName">${merchant.saler.name}</span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label" for="merchantGroup">集团商户：</label>
			<div class="controls">
				<input type="text" class="input-xlarge" readonly="readonly"
					placeholder="请选择集团商户，如果没有则不选..." id="merchantGroupSel"
					readonly="readonly" value="${merchantGroup.name }">
			</div>
		</div>
		<hr>
		<div class="control-group">
			<label class="control-label" for="account">账号：</label>
			<div class="controls">
				<input type="text" class="input-xlarge" placeholder="请输入商户账号..."
					id="account" name="account" value="${merchant.account}"
					readonly="readonly"> <span class="help-inline"></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="password">密码：</label>
			<div class="controls">
				<input type="text" class="input-xlarge" placeholder="请输入商户账号..."
					id="password" name="password" value="${merchant.password}"
					readonly="readonly"> <span class="help-inline"></span>
			</div>
		</div>
		<h2>盒子SN列表</h2>
		<table
			class="table table-striped table-bordered bootstrap-datatable datatable"
			id="deviceTable">
			<thead>
				<tr>
					<th width="25%">盒子SN编码</th>
					<th width="25%">上次成功获取任务时间</th>
					<th width="25%">上次成功获取的任务ID</th>
					<th width="25%">任务堆积数量</th>
				</tr>

			</thead>
			<c:forEach items="${deviceStatusDtos}" var="deviceStatusDto">
				<tr>
					<td class='deviceSnTd'>${deviceStatusDto.sn}</td>
					<c:choose>
						<c:when test="${not empty deviceStatusDto.lastUpdateTime}">
							<td><fmt:formatDate pattern="yyyy年MM月dd日 HH时mm分ss秒" value="${deviceStatusDto.lastUpdateTime}"/></td>
						</c:when>
						<c:otherwise>
							<td>无</td>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${not empty deviceStatusDto.lastTaskId}">
							<td>${deviceStatusDto.lastTaskId}</td>
						</c:when>
						<c:otherwise>
							<td>无</td>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${not empty deviceStatusDto.restTaskCount}">
							<td>${deviceStatusDto.restTaskCount}</td>
						</c:when>
						<c:otherwise>
							<td>无</td>
						</c:otherwise>
					</c:choose>

				</tr>
			</c:forEach>
		</table>

	</form>

	<script type="text/javascript">
		$(document).ready(function() {
			//查出标签分类信息
			$.get("${pageContext.request.contextPath}/admin/dictionary/tag/list", drawmerchantTagNodeList);
		});

		function drawmerchantTagNodeList(data) {
			for ( var i = 0; i < data.length; i++) {
				var str = "<ul class=\"tagUl\"><li  id='li"+data[i].id+"' ><input name='tagName' type='hidden' value='"+data[i].id+"'><input type='hidden' value='"+data[i].parentId+"'><input type=\"checkbox\"  class=\"tag\" value="+data[i].name+">"
						+ data[i].name + "</li></ul>";
				$("#modalBody").append(str);
				if (data[i].children != null && data[i].children.length >= 0) {
					drawByParent(data[i]);
				}
			}
		}
		$(".tag").css("margin", "8px");
		function drawByParent(node) {
			var children = node.children;
			if (children == null || children.length <= 0) {
				return;
			}
			//获取子标签的ul
			var ul = $("#ul" + node.id);
			//如果没有再父级的后面创建一个空的ul
			if (ul == null || ul.length == 0) {
				var li = $("#li" + node.id);
				$("#li" + node.id).after("<ul class='tagUl' id='ul"+node.id+"'></ul>");
				ul = $("#ul" + node.id);
			}
			//将li放进ul里
			for ( var i = 0; i < children.length; i++) {
				var str = "<li id='li"+children[i].id+"'><input name='tagName' type='hidden' value='"+children[i].id+"'><input type='hidden' value='"+children[i].parentId+"'><input type=\"checkbox\"  class=\"tag\"  value="+children[i].name+">"
						+ children[i].name + "</li>";
				$(ul).append(str);
				drawByParent(children[i]);
			}
		}
	</script>
</body>
<%-- <%@include file="../../include/footer.jsp"%> --%>

</html>

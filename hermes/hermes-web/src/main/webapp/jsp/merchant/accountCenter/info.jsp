<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商户信息</title>
</head>
<body>
	<form id="addMerchantForm" class="form-horizontal">
		<div class="control-group">
			<label class="control-label" for="name">名称：</label>
			<div class="controls">
				<input type="text" readonly="readonly" class="input-xlarge"
					id="name" name="name" value="${merchant.name}">
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="addr">地址：</label>
			<div class="controls">
				<input type="text" readonly="readonly" class="input-xlarge"
					id="addr" name="addr" value="${merchant.addr}"> <span
					class="help-inline"></span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="tag"> 商户所属销售： </label>
			<div class="controls">
				<span id="salerName">${saler.name}</span>
			</div>
		</div>
		<hr>
		<div class="control-group">
			<label class="control-label" for="account">账号：</label>
			<div class="controls">
				<input type="text" class="input-xlarge" id="account" name="account"
					value="${merchant.account}" readonly="readonly"> <span
					class="help-inline"></span>
			</div>
		</div>

		<h2>旺财宝列表</h2>
		<table
			class="table table-striped table-bordered bootstrap-datatable datatable"
			id="deviceTable">
			<!-- <thead>
				<tr>
					<th width="100%">旺财宝列表</th>
				</tr>

			</thead> -->
			<c:forEach items="${devices}" var="device">
				<tr>
					<td class='deviceSnTd'>${merchant.name}${fn:substring(device.sn, fn:length(device.sn)-4, fn:length(device.sn))}</td>
				</tr>
			</c:forEach>
		</table>

	</form>
</body>
</html>
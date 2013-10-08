<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>盒子详细信息</title>
</head>
<body>
	<div class="well ">
		<!-- span8 -->
		<table class="table table-bordered">
			<thead>
				<tr>
					<th colspan="2"><center>
							<h4>设备详细信息</h4>
						</center></th>
				</tr>
			</thead>
			<tr>
				<td width="30%">盒子名称:</td>
				<td width="70%">${device.device.sn}</td>
			</tr>
			<tr>
				<td>所属商户:</td>
				<td>${device.merchant.name}</td>
			</tr>
			<tr>
				<td>所属商户账号:</td>
				<td>${device.merchant.account}</td>
			</tr>
			<tr>
				<td>所属商户销售:</td>
				<td>${device.saler.name}</td>
			</tr>
			<tr>
				<td>sim卡号:</td>
				<td>${device.device.simInfo.serviceNo}</td>
			</tr>
			<tr>
				<td>用户名:</td>
				<td>${device.device.simInfo.username}</td>
			</tr>
			<tr>
				<td>uim卡号:</td>
				<td>${device.device.simInfo.simUimCardNo}</td>
			</tr>
			<tr>
				<td>imsiNo号:</td>
				<td>${device.device.simInfo.imsiNo}</td>
			</tr>
			<tr>
				<td>软件版本:</td>
				<td>${device.device.apkVersion}</td>
			</tr>
			<tr>
				<td>上次心跳时间</td>
				<td>
					<fmt:formatDate pattern="yyyy年MM月dd日 HH时mm分ss秒" value="${device.deviceStatus.lastAskTime}" />
				</td>
			</tr>

			<tr>
				<td>上次成功获取任务时间</td>
				<c:choose>
					<c:when test="${not empty device.deviceStatus.lastUpdateTime}">
						<td>
							<fmt:formatDate pattern="yyyy年MM月dd日 HH时mm分ss秒" value="${device.deviceStatus.lastUpdateTime}" />
						</td>
					</c:when>
					<c:otherwise>
						<td>无</td>
					</c:otherwise>
				</c:choose>
			</tr>
			<tr>
				<td>上次成功获取任务id</td>
				<c:choose>
					<c:when test="${not empty device.deviceStatus.lastTaskId}">
						<td>${device.deviceStatus.lastTaskId}</td>
					</c:when>
					<c:otherwise>
						<td>无</td>
					</c:otherwise>
				</c:choose>
			</tr>
			<tr>
				<td>任务堆积数量:</td>
				<c:choose>
					<c:when test="${not empty device.restTaskCount}">
						<td>${device.restTaskCount}</td>
					</c:when>
					<c:otherwise>
						<td>无</td>
					</c:otherwise>
				</c:choose>
			</tr>
			<tr>
				<td colspan="2">转卡记录:</td>
			</tr>
			<c:forEach items="${device.device.simChangeRecords }" var="changeRecord" varStatus="status">
				<tr>
					<td>
						<fmt:formatDate pattern="yyyy年MM月dd日 HH时mm分ss秒" value="${changeRecord.changeTime}" />
					</td>
					<td>从${changeRecord.oldSim.serviceNo} 转到${changeRecord.newSim.serviceNo }</td>
				</tr>
			</c:forEach>
		</table>
	</div>
	<script type="text/javascript">
		$(document).ready(function() {
			activeMenu("listDevice");
		});
	</script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>统计信息</title>
<%@include file="../../include/header.jsp"%>
<%@include file="../../include/footer.jsp"%>
</head>
<body>
		<div id="dayChartContainer"></div>

	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/highcharts.js">
		
	</script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/exporting.js">
		
	</script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/chartPieApi.js">
		
	</script>

	<script type="text/javascript">
		$(document).ready(function() {
			$.get("${pageContext.request.contextPath}/merchant/customer/callInStatistics/today/", drawPieChart, "json");
		});
	</script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div class="caption">
		<h3>来电次数</h3>
	</div>
	<div id="dayChartContainer"></div>

	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/highcharts.js">
		
	</script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/exporting.js">
		
	</script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/chart.js">
		
	</script>
	<script type="text/javascript">
		$(document).ready(function() {
			$.get("${pageContext.request.contextPath}/merchant/customer/callInStatistics/today/", drawPieChart, "json");
		});
	</script>
</body>
</html>
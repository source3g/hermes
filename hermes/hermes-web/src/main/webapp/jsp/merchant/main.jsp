<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>统计信息</title>

</head>
<body>
	<div class="thumbnail span10">
		<div class="caption well">
			<h3>最新数据</h3>
		</div>
		<div class="span5">
			<ul class="unstyled " id="customerCount">
			</ul>
		</div>
		<div class="span5">
			<ul class="unstyled inline" id="messageSentCount">
			</ul>
		</div>
	</div>
	<div class="thumbnail span10">
		<div class="caption">
			<h3>来电次数</h3>
		</div>
		<div id="dayChartContainer"></div>
	</div>

	<div class="thumbnail span10">
		<div class="caption">
			<h3>月度趋势</h3>
		</div>
		<div id="chartContainer"></div>
	</div>
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
			$.ajax({
				url : "${pageContext.request.contextPath}/merchant/customer/callInStatisticsJson/",
				dataType : "json",
				success : reDraw
			});
			$.get("${pageContext.request.contextPath}/merchant/customer/callInStatistics/today/", drawPieChart, "json");

			$.get("${pageContext.request.contextPath}/merchant/statistics/", showStatistics);
			function showStatistics(data) {
				for(var i=0;i<data.customerStatistics.length;i++){
					for (var j in data.customerStatistics[i]){
						var str="<li>"+data.customerStatistics[i][j].displayName+"<span>"+data.customerStatistics[i][j].value+"</span></li>";
						$("#customerCount").append(str);
					}
				}
				for(var g=0;g<data.messageStatistics.length;g++){
					for (var k in data.messageStatistics[g]){
						var str="<li>"+data.messageStatistics[g][k].displayName+"<span>"+data.messageStatistics[g][k].value+"</span></li>";
						$("#messageSentCount").append(str);
					}
				}
			}
		});
	</script>
</body>
</html>
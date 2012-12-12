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
			<ul class="unstyled ">
				<li>我们已保存的老顾客数量：</li>
				<li>最近三天手机来电总数量：</li>
				<li>最近三天新顾客手机来电数量：</li>
				<li>最近三天老顾客预订数量：</li>
				<li>最近一周老顾客预订数量：</li>
				<li>最近三天挂机短信发送数量：</li>
				<li>最近一周客户群发短信数量：</li>
			</ul>
		</div>
		<div class="span5">
			<ul class="unstyled inline">
				<li>占来电总数的比例：</li>
				<li>占来电数量的比例：</li>
				<li>占来电总数的比例：</li>
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
		});
	</script>
</body>
</html>
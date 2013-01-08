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
				<li>已编辑顾客数量：<span id="editedCustomerCount">载入中</span></li>
				<li>未编辑顾客数量：<span id="uneditedCustomerCount">载入中</span></li>
				<li>最近三天老顾客来电数量：<span id="editedCallInCountThreeDay">载入中</span></li>
				<li>最近三天新顾客来电数量：<span id="uneditedCallInCountThreeDay">载入中</span></li>
				<li>最近一周老顾客来电数量：<span id="editedCallInCountAWeek">载入中</span></li>
				<li>最近一周新顾客来电数量：<span id="uneditedCallInCountAWeek">载入中</span></li>
			</ul>
		</div>
		<div class="span5">
			<ul class="unstyled inline">
				<li>最近三天挂机短信发送数量：<span id="handUpMessageSentCountThreeDay">载入中</span></li>
				<li>最近一周挂机短信发送数量：<span id="handUpMessageSentCountAWeek">载入中</span></li>
				<li>最近三天客户群发短信数量：<span id="messageGroupSentCountThreeDay">载入中</span></li>
				<li>最近一周客户群发短信数量：<span id="messageGroupSentCountAWeek">载入中</span></li>
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
				$("#editedCustomerCount").html(data.customerStatistics.editedCustomerCount);
				$("#uneditedCustomerCount").html(data.customerStatistics.uneditedCustomerCount);
				$("#editedCallInCountThreeDay").html(data.customerStatistics.editedCallInCountThreeDay);
				$("#uneditedCallInCountThreeDay").html(data.customerStatistics.uneditedCallInCountThreeDay);
				$("#editedCallInCountAWeek").html(data.customerStatistics.editedCallInCountAWeek);
				$("#uneditedCallInCountAWeek").html(data.customerStatistics.uneditedCallInCountAWeek);
				$("#handUpMessageSentCountThreeDay").html(data.messageStatistics.handUpMessageSentCountThreeDay);
				$("#handUpMessageSentCountAWeek").html(data.messageStatistics.handUpMessageSentCountAWeek);
				$("#messageGroupSentCountThreeDay").html(data.messageStatistics.messageGroupSentCountThreeDay);
				$("#messageGroupSentCountAWeek").html(data.messageStatistics.messageGroupSentCountAWeek);
			}
		});
	</script>
</body>
</html>
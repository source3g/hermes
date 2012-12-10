<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>来电顾客列表</title>
</head>
<body>
	<div class="well">
		<div class="caption">
			<h3>查询条件</h3>
		</div>
		<form id="queryForm" class="form-inline " method="get">
			<label class="control-label" for="startTime">时间：</label> <input
				type="text" id="startTime" name="startTime" onclick="WdatePicker();"
				class="input-medium" placeholder="请选择起始时间...">&nbsp;- <input
				type="text" id="endTime" name="endTime" class="input-medium"
				onclick="WdatePicker();" placeholder="请选择结束时间..."> <input
				type="submit" class="btn btn-primary" value="查询">
		</form>
	</div>
	<div id="chartContainer"></div>

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
			
			$("#queryForm").submit(function() {
				$("#queryForm").ajaxSubmit({
					url : "${pageContext.request.contextPath}/merchant/customer/callInStatisticsJson/",
					dataType : "json",
					success : reDraw
				});
				return false;
			});
		});

		/* function callInStatisticsJson(){
			var startTime=("#startTime").val();
			var endTime=("#endTime").val();
			$.ajax({
				url:"${pageContext.request.contextPath}/merchant/customer/callInStatisticsJson/",
				type:"get",
				dataType:"json",
				success:reDraw(data)
			});
		} */
	</script>
</body>
</html>
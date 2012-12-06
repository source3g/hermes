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
		<form class="form-inline " method="get">
			<label class="control-label" for="startTime">时间：</label> <input
				type="text" name="startTime" onclick="WdatePicker();" class="input-medium"
				placeholder="请选择起始时间...">&nbsp;- <input type="text"
				name="endTime" class="input-medium"  onclick="WdatePicker();" placeholder="请选择结束时间...">
				<input type="button" class="btn btn-primary" value="查询">
		</form>
		<button  onclick='reDraw(this);' >测试</button>
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
</body>
</html>
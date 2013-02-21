<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@include file="../jsp/include/header.jsp"%>
<%@include file="../jsp/include/footer.jsp"%>
</head>
<body>
	<div class="caption">
		<h3>月度趋势</h3>
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
	 <% String sn=request.getParameter("sn");
	 if(sn==null) sn="";
	%> 
	<script type="text/javascript">
		$(document).ready(function(){
			$.ajax({
				url : "${pageContext.request.contextPath}/merchant/customer/callInStatisticsJson/?sn="<%=sn%>,
				dataType : "json",
				success : reDraw
			});
		});
	</script>
</body>
</html>
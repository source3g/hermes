<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	${result }
	<c:if test="${type eq 'customer'}">
	<input id="backToList" type="button"
		onclick="loadPage('/hermes/merchant/customer/importLog/');"
		class="btn btn-primary" value="查看导入日志" />
	</c:if>
	
	<c:if test="${type eq 'remind'}">
	<input id="backToList" type="button"
		onclick="loadPage('${pageContext.request.contextPath}/merchant/customer/remind/importLog/');"
		class="btn btn-primary" value="查看导入日志" />
	</c:if>
</body>
</html>
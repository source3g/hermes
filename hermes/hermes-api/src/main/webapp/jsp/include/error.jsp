<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${not empty errors }">
	<div class="alert alert-error">
		<ul>
			<c:forEach items="${errors }" var="error">
				<li>${error.defaultMessage }</li>
			</c:forEach>
		</ul>
	</div>
</c:if>
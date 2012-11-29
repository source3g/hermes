<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../../include/import.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>短信发送</title>
</head>
<body>
	<form class="well ">
		<table class="table table-bordered">
		<thead>
			<tr>
				<th colspan="4"><center>
							<h4>短信录入 </h4>
						</center></th>
				</tr>
		</thead>
		<tbody>
			<tr>
			<td width="20%" ><label class="control-label">商户短信数据 :</label></td>
			<td width="26%" >短信预存数量：${merchant.shortMessage.totalCount}</td>
			<td width="27%" >短信可用数量：${merchant.shortMessage.surplusMsgCount}</td>
			<td width="27%" >短信已发送数量：</td>
				
			</tr>
			
			 <tr>
			 <td><label class="control-label">选择客户组:：</label></td>
			 <td colspan="3"> 
			<c:if test="${not empty customerGroups }">
				<c:forEach items="${customerGroups}" var="customerGroup">
					 <input type=checkbox >${customerGroup.name}
				</c:forEach>
			</c:if>
			
			  </td>	
			  </tr>
			<tr>
			<td>
				<label class="control-label">编辑短信内容：</label> </td>
				<td colspan="4"><textarea class="span8" rows="5" name=""></textarea>
			 </td>
			 </tr>
			 
			<tr>
			<td colspan="4">
				<input type="submit" class="btn btn-primary" value="发送">
				<td>
		    </tr>
		
	</tbody>
		</table>
	</form>
</body>
</html>
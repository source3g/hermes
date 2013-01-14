<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商户标签</title>
</head>
<body>
	<table class="table">
		<thead>
			<tr>
				<td>分类设置</td>
			</tr>
		</thead>

		<tbody>
			<tr>
				<td><input class="input-small" type="text" name="type1" value="默认"> <a href="javascript:void();" onclick="addChild(this);">增加子类</a></td>
			</tr>
			<tr>
				<td>22</td>
			</tr>
			<tr>
				<td>33</td>
			</tr>
		</tbody>
		
		<tbody>
			<tr><td>增加分类</td></tr>
		</tbody>

	</table>
	
	<script type="text/javascript">
		$(document).ready(function(){
			
		});
		
		function addChild(el){
			var length=$(el).parents("tr").find(".subNode").length;
			var tr="<tr><td><span>";
			for (var i=0;i<length;i++){
				tr+="<image class=\"subNode\" src=\"${pageContext.request.contextPath}/img/subNodeWhite.gif\"> </image>"
			}
			tr+="<image class=\"subNode\" src=\"${pageContext.request.contextPath}/img/subNode.gif\"></image></span><input class=\"input-small\" type=\"text\" name=\"type1\" value=\"默认\">"+
			"<a href=\"javascript:void();\" onclick=\"addChild(this);\">增加子类</a></td></tr>";
			$(el).parents("tr").after(tr);
		}
	
	</script>
</body>
</html>
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
				<td class="node"><input class="input-small" type="text" name="name"
					value="默认"> <input type="hidden" name="id" value="123">
					<input type="hidden" name="parentId"> <a
					href="javascript:void();" onclick="addChild(this);">增加子类</a></td>
			</tr>
		</tbody>

		<tbody>
			<tr>
				<td><a href="javascript:void();" onclick="addTag(this);">增加分类</a></td>
			</tr>
		</tbody>
		<tfoot>
			<tr>
				<td><input type="button" class="btn btn-primary" value="提交" onclick="commitTree();"/></td>
			</tr>
		</tfoot>
	</table>

	<script type="text/javascript">
		
		$(document).ready(function() {
			
		});
		function commitTree(){
			var tags=new Array();
			$("td[class='node']").each(function (index){
				var tag=new Object();
				tag.id=$(this).children("input[name='id']").val();
				tag.parentId=$(this).children("input[name='parentId']").val();
				tags.push(tag);
			});
			$.post("${pageContext.request.contextPath}/admin/dictionary);
		}
		function addChild(el) {
			var parentId = $(el).prevAll("input[name='id']").val();
			if (parentId == null || parentId == "") {
				alert("请先提交");
				return;
			}
			var length = $(el).parents("tr").find(".subNode").length;
			var tr = "<tr><td  class='node'><span>";
			for ( var i = 0; i < length; i++) {
				tr += "<image class=\"subNode\" src=\"${pageContext.request.contextPath}/img/subNodeWhite.gif\"> </image>"
			}
			tr += "<image class=\"subNode\" src=\"${pageContext.request.contextPath}/img/subNode.gif\"></image></span><input class=\"input-small\" type=\"text\" name=\"type1\" value=\"默认\">";
			tr += "<input type='hidden' name='parentId' value='"+parentId+"'>";
			tr += "<a href=\"javascript:void();\" onclick=\"addChild(this);\">增加子类</a></td></tr>";
			$(el).parents("tr").after(tr);
		}

		function addTag(el) {
			var tbody = "<tbody> <tr> <td class='node'><input class=\"input-small\" type=\"text\" name=\"name\" value=\"默认\"> <input type=\"hidden\" name=\"id\">";
			tbody += "<input type=\"hidden\" name=\"parentId\">";
			tbody += "<a href=\"javascript:void();\" onclick=\"addChild(this);\">增加子类</a></td>";
			tbody += "</tr></tbody>";
			$(el).parents("tbody").before(tbody);
		}
	</script>
</body>
</html>
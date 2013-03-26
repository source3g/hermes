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
				<td><a href="javascript:void();" id="addTag"
					onclick="addTag(this);">增加分类</a></td>
			</tr>
		</tbody>
		<tfoot>
			<tr>
				<td><input type="button" class="btn btn-primary" value="提交"
					onclick="commitTree();" /></td>
			</tr>
		</tfoot>
	</table>
	<script type="text/javascript">
		$(document).ready(function() {
			$.get("${pageContext.request.contextPath}/admin/dictionary/tag/list/", callback);
			function callback(data) {
				for ( var i = 0; i < data.length; i++) {
					initTag(data[i]);
					if (data[i].children != null && data[i].children.length > 0) {
						initChildren(data[i]);
					}
				}
			}
		});
		function initChildren(node) {
			if (node.children == null || node.children.length <= 0) {
				return;
			}
			var length = $("#" + node.id).find(".subNode").length;
			var children = node.children;
			for ( var i = 0; i < children.length; i++) {
				var tr = "<tr id='"+children[i].id+"'><td  class='node'><span>";
				for ( var j = 0; j < length; j++) {
					tr += "<image class='subNode' src='${pageContext.request.contextPath}/img/subNodeWhite.gif' > </image>"
				}
				tr += "<image class='subNode' src='${pageContext.request.contextPath}/img/subNode.gif'></image></span><input class='input-small' type='text' name='name' value='"+children[i].name+"'>";
				tr += "<input type='hidden' name='parentId' value='"+node.id+"'>";
				tr += "<input type='hidden' name='id' value='"+children[i].id+"'>";
				tr += "<a href=\"javascript:void();\" onclick=\"addChild(this);\">增加子类</a>";
				tr += "<a href=\"javascript:void();\" onclick=\"deleteSelf(this);\">          删除本栏</a></td></tr>";
				$("#" + node.id).after(tr);
				initChildren(children[i]);
			}
		}
		function deleteSelf(del) {
			//alert($("#"+node.id+"[class='subNode']").length);
			//$("#"+node.id+"[class='subNode']").remove();
			$(del).parents("tr").remove();
		}

		function initTag(node) {
			var tbody = "<tbody><tr id='"+node.id+"'> <td class='node'> <input class='input-small' type='text' name='name' value='"+node.name+"'> <input type='hidden' name='id' value='"+node.id+"' >";
			if (node.parentId == null) {
				tbody += " <input name='parentId' type='hidden' value= ''>";
			} else {
				tbody += " <input name='parentId' type='hidden' value= '"+node.parentId+"'>";
			}
			tbody += " <a href='javascript:void();' onclick='addChild(this);'>增加子类</a></td> </tr></tbody> ";
			$("#addTag").parents("tbody").before(tbody);
		}
		function commitTree() {
			var tags = new Array();
			$("td[class='node']").each(function(index) {
				var tag = new Object();
				tag.id = $(this).children("input[name='id']").val();
				tag.parentId = $(this).children("input[name='parentId']").val();
				tag.name = $(this).children("input[name='name']").val();
				tags.push(tag);
			});
			var strJson = "{";
			//	alert(tags.length);
			for ( var tagsIndex = 0; tagsIndex < tags.length; tagsIndex++) {
				strJson += "\"nodes[" + tagsIndex + "].id\":\"";
				strJson += tags[tagsIndex].id + "\",";
				strJson += "\"nodes[" + tagsIndex + "].parentId\":\"";
				strJson += tags[tagsIndex].parentId + "\",";
				strJson += "\"nodes[" + tagsIndex + "].name\":\"";
				strJson += tags[tagsIndex].name + "\",";
			}
			if (strJson.length > 2) {
				strJson = strJson.substring(0, strJson.length - 1);
			}
			strJson += "}";

			var dataJson = eval('(' + strJson + ')');
			$.post("${pageContext.request.contextPath}/admin/dictionary/tag/add/", dataJson, showContentInfo);
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
			tr += "<image class=\"subNode\" src=\"${pageContext.request.contextPath}/img/subNode.gif\"></image></span><input class=\"input-small\" type=\"text\" name=\"name\" value=\"默认\">";
			tr += "<input type='hidden' name='parentId' value='"+parentId+"'>";
			tr += "<input type='hidden' name='id' value=''>";
			tr += "<a href=\"javascript:void();\" onclick=\"addChild(this);\">增加子类</a></td></tr>";
			$(el).parents("tr").after(tr);
		}

		function addTag(el) {
			var tbody = "<tbody> <tr> <td class='node'><input class=\"input-small\" type=\"text\" name=\"name\" value=\"默认\"> <input type=\"hidden\" name=\"id\">";
			tbody += "<input type=\"hidden\" name=\"parentId\">";
			tbody += "<input type='hidden' name='id' value=''>";
			tbody += "<a href=\"javascript:void();\" onclick=\"addChild(this);\">增加子类</a></td>";
			tbody += "</tr></tbody>";
			$(el).parents("tbody").before(tbody);
		}
	</script>
</body>
</html>

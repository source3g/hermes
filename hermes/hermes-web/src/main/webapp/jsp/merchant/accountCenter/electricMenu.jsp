<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>电子菜单</title>
</head>
<body>
	<table class="table">
		<thead>
			<tr>
				<td>设置</td>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="menu" items="${menus }">
				<tr>
					<td class='node'><a href="#" onclick="return expand(this);"
						class="help-inline" style="width: 150px;">${menu.name}</a> <input
						type='hidden' name='id' value='${menu.id }'> <a href="#"
						class="btn btn-primary updateA" onclick="return updateMenu(this);">修改</a>
						 <a href="#"
						class="btn btn-danger" onclick="return deleteMenu(this);">删除</a></td>
				</tr>
				<c:forEach items="${menu.items }" var="item" varStatus="status">
					<c:if test="${status.index%4 eq 0 }">
					<tr class="itemTr" style="display: none">
						<td>
					</c:if>
							 <span class="span3" style="background-color: #eee;"><img
								alt="" src="" width="120" height="120"></img><br> <label>${item.title }&nbsp;单位${item.unit} &nbsp;(${item.price }元) <a href="#" class="btn btn-primary">详情</a><a href="#" class="btn btn-danger">删除</a></label></span>
					<c:if test="${status.index%4 eq 0 }">
							</td>
						</tr>
					</c:if>
					
					<c:if test="${status.index%4 ne 0 and status.last }">
							</td>
						</tr>
					</c:if>
				</c:forEach>
			</c:forEach>
			<tr>
				<td><a href="javascript:void();" id="addTag" class="btn btn-success"
					onclick="addTag(this);">增加分类</a>
					<a href="#" class="btn btn-success"
						onclick="return addChild(this);">增加菜品</a>
					</td>
			</tr>
		</tbody>
		<tfoot>
			<tr>
				<td><input type="button" class="btn btn-primary" value="批量增加"
					onclick="commitTree();" /></td>
			</tr>
		</tfoot>
	</table>

	<script type="text/javascript">
		$(document).ready(function() {
		});

		function expand(el) {
			$(el).parents("tr").next(".itemTr").toggle("slow");
			return false;
		}
		
		function deleteMenu(el){
			var label = $(el).prevAll(".help-inline");
			var name=label.text();
			if(confirm("是否要删除:"+name+"?")){
				var idInput=$(el).prevAll("input[name='id']");
				$.get("${pageContext.request.contextPath}/merchant/account/electricMenu/delete/"+$(idInput).val()+"/");
			}
			return false;
		}

		function updateMenu(el) {
			var label = $(el).prevAll(".help-inline");
			$(label).after("<input type='text' class='input-small' name='name' value='" + $(label).text() + "'/>");
			$(label).css("display", "none");
			$(el).after("<input type='button' value='确定' onclick='doUpdate(this);' class='btn btn-primary updateBtn'><input type='button' value='取消' onclick='cancelUpdate(this);' class='btn btn-primary updateBtn'>");
			$(el).css("display", "none");
			return false;
		}

		function doUpdate(el) {
			var label = $(el).prevAll(".help-inline");
			var input = $(el).prevAll("input[name='name']");
			var name=$(input).val();
			var idInput=$(el).prevAll("input[name='id']");
			$.post("${pageContext.request.contextPath}/merchant/account/electricMenu/update",{"name":name,"id":$(idInput).val()});
			$(label).html($(input).val());
			$(input).remove();
			$(label).css("display", "");
			$(el).prevAll(".updateA").css("display", "");
			$(el).nextAll(".updateBtn").remove();
			$(el).remove();
			return false;
		}

		function cancelUpdate(el) {
			var label = $(el).prevAll(".help-inline");
			$(label).css("display", "");
			$(el).prevAll(".updateA").css("display", "");
			$(el).prevAll(".updateBtn").remove();
			$(el).prevAll("input[name='name']").remove();
			$(el).remove();
			return false;
		}

		function deleteSelf(del) {
			$(del).parents("tr").remove();
		}

		function commitTree() {
			var menus = new Array();
			$("td[class='node']").each(function(index) {
				var id = $(this).children("input[name='id']").val();
				if (!id) {
					var electricMenu = new Object();
					electricMenu.name = $(this).children("input[name='name']").val();
					menus.push(electricMenu);
				}
			});
			var strJson = "{";
			for ( var tagsIndex = 0; tagsIndex < menus.length; tagsIndex++) {
				strJson += "\"menus[" + tagsIndex + "].name\":\"";
				strJson += menus[tagsIndex].name + "\",";
			}
			if (strJson.length > 2) {
				strJson = strJson.substring(0, strJson.length - 1);
			}
			strJson += "}";
			alert(strJson);
			var dataJson = eval('(' + strJson + ')');
			$.post("${pageContext.request.contextPath}/merchant/account/electricMenu/add/", dataJson, showContentInfo);
		}

		function addChild(el) {
			/* var parentId = $(el).prevAll("input[name='id']").val();
			if (parentId == null || parentId == "") {
			 	alert("请先提交");
			 	return;
			 } */
			loadPage("${pageContext.request.contextPath}/merchant/account/electricMenu/addItem");
			return false;
		}
		
		function addTag(el) {
			var tr = "<tr> <td class='node'><input class=\"input-small\" type=\"text\" name=\"name\" value=\"默认\">";
			tr += "</td>";
			tr += "</tr>";
			$(el).parents("tr").before(tr);
		}
	</script>
</body>
</html>
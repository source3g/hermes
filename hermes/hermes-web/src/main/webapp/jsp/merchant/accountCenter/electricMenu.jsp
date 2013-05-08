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
						<a href="#" class="btn btn-success"
						onclick="return addChild(this);">增加菜品</a> <a href="#"
						class="btn btn-danger" onclick="return addChild(this);">删除</a></td>
				</tr>
				<tr class="itemTr" style="display: none">
					<td><table>
							<%-- <c:forEach items="${menu.items }" var="item">
								<tr>
									<td>${item.title }</td>
								</tr>
							</c:forEach> --%>
							<tr>
									<td>fdasfafafdas</td>
								</tr>
						</table></td>
				</tr>
			</c:forEach>
			<tr>
				<td><a href="javascript:void();" id="addTag"
					onclick="addTag(this);">增加分类</a></td>
			</tr>
		</tbody>
		<tfoot>
			<tr>
				<td><input type="button" class="btn btn-primary" value="批量增加"
					onclick="commitTree();" /></td>
			</tr>
		</tfoot>
	</table>

	<!-- <table class="table">
		<thead>
			<tr>
				<td>设置</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td class="node"><input class="input-small" type="text"
					name="name" value="默认"> <input type="hidden" name="id"><input
					type="hidden" name="parentId"><input type="hidden"
					name="id" value=""><a href="#"
					onclick="return addChild(this);">增加菜品</a></td>
			</tr>
			<tr>
				<td class="node" ><span
					style="background-color: #fc0; display: -moz-inline-box; display: inline-block; width: 40px; height: 40px;"></span>  
					
					 <span style=" position: relative;  background-color: #fc0; display: -moz-inline-box; display: inline-block; width: 40px; height: 40px;">
				<span style="position: absolute; z-index: 2; left: 1px; top: 10px">未上传</span>
				</span> 
				<span id="spanButtonPlaceholder"></span><input type="text" name="uploadFile" style="border:none;width:0px;"></span>
					<div 
					style="clear:both;background-color: #fc0; margin:0;padding:0;  display: -moz-inline-box; display: inline-block;">
						<div >	 <input class="input-small"
						type="text" name="name" value="默认"><input type="hidden"
						name="parentId" value=""><input type="hidden" name="id"
						value=""> </div>
				</div></td>
			</tr>
		</tbody>
		<tfoot>
			<tr>
				<td><input type="button" class="btn btn-primary" value="提交"
					onclick="commitTree();"></td>
			</tr>
		</tfoot>
	</table> -->



	<script type="text/javascript">
		$(document).ready(function() {
		});

		function expand(el) {

			$(el).next(".itemTr").css("display","");

			//$(el).parents("tr").after();
			return false;
		}

		function updateMenu(el) {
			var label = $(el).prevAll(".help-inline");
			$(label).after("<input type='text' class='input-small' name='name' value='" + $(label).text() + "'/>");
			$(label).css("display", "none");
			$(el)
					.after(
							"<input type='button' value='确定' onclick='doUpdate(this);' class='btn btn-primary updateBtn'><input type='button' value='取消' onclick='cancelUpdate(this);' class='btn btn-primary updateBtn'>");
			$(el).css("display", "none");
			return false;
		}

		function doUpdate(el) {
			var label = $(el).prevAll(".help-inline");
			var input = $(el).prevAll("input[name='name']");
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
				if (id == null || id == "") {
					var electricMenu = new Object();
					electricMenu.id = id;
					electricMenu.name = $(this).children("input[name='name']").val();
					menus.push(electricMenu);
				}
			});
			var strJson = "{";
			for ( var tagsIndex = 0; tagsIndex < menus.length; tagsIndex++) {
				strJson += "\"menus[" + tagsIndex + "].id\":\"";
				strJson += menus[tagsIndex].id + "\",";
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
			var parentId = $(el).prevAll("input[name='id']").val();
			//	if (parentId == null || parentId == "") {
			//		alert("请先提交");
			//		return;
			//	}
			loadPage("${pageContext.request.contextPath}/merchant/account/electricMenu/addItem");
			return false;
		}
		function addTag(el) {
			var tr = "<tr> <td class='node'><input class=\"input-small\" type=\"text\" name=\"name\" value=\"默认\">";
			tr += "<input type='hidden' name='id'>";
			tr += "</td>";
			tr += "</tr>";
			$(el).parents("tr").before(tr);
		}
	</script>
</body>
</html>
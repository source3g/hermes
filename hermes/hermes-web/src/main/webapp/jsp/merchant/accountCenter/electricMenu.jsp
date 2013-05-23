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
	<table width="100%">
		<thead>
			<tr>
				<td><h1>类别列表</h1></td>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="menu" items="${menus }">
				<tr>
					<td class='node'><a href="#" onclick="return expand(this);"
						 style="display:inline-block;width: 150px;font-size: 20px;font-weight: bold;" id="menuName">${menu.name}</a> <input
						type='hidden' name='id' value='${menu.id }'> <a href="#"
						class="btn btn-primary updateA" onclick="return updateMenu(this);">修改</a>
						 <a href="#"
						class="btn btn-danger" onclick="return deleteMenu(this);">删除</a></td>
				</tr>
				
				<tr class="itemTr" style="display: none">
					<td>
					<table  width="100%">
						<tr>
							<td >
							<c:forEach items="${menu.items }" var="item" varStatus="status">
							<c:if test="${status.count%4 eq 0 }">
							</td>
							</tr>
							<tr>
								<td>
							</c:if>
								 <span class="span4" > <a href="#"  onclick="return menuDetailDialog('${item.title}','${menu.id }');"><img style="display:inline;margin-left: 10px;margin-right: 10px;margin-top: 10px;margin-bottom: 10px;"
									alt="" src="${item.picPath }" width="400" height="300"></img></a><br> <label style="text-align: center ;margin-left: 10px; font-size: 15px;">菜名:<span style="font-size: 20px;  font-weight:bold; color:  red;">${item.title }</span>&nbsp;单位:<span style="font-size: 20px; font-weight:bold; color: red;">${item.unit}</span> &nbsp;(${item.price }元)<br/> 操作: <a href="#"  onclick="return menuDetail('${item.title}','${menu.id }');">修改</a>&nbsp;&nbsp;<a href="#"  onclick="return deleteItem('${item.title }','${item.id}','${menu.id }',this);">删除</a></label></span>
										<c:if test="${status.count%4 ne 0 and status.last }">
									</td>
								</tr>
							</c:if>
						</c:forEach>
					</table>
					</td>
				</tr>
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
					onclick="return commitTree();" />
					<input type="button" class="btn btn-danger" value="与旺财宝同步"
					onclick="return sync();" /></td>
			</tr>
		</tfoot>
	</table>
	
	<div id="detailModal" class="modal hide fade">
		<div class="modal-header">
			<a class="close" data-dismiss="modal">&times;</a>
			<h3>菜品详情</h3>
		</div>
		<div id="detailContent" class="modal-body">
			
		</div>
		<div class="modal-footer"></div>
	</div>
	

	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		
		function sync(){
			$.get("${pageContext.request.contextPath}/merchant/account/electricMenu/sync",function (data){
			alert("同步成功，两小时后会出现在王财宝中，请耐心等候，不要频繁点击");
			});
			return false;
		}

		function expand(el) {
			$(el).parents("tr").next(".itemTr").toggle("slow");
			return false;
		}
		
		function deleteItem(title,itemId,menuId,el){
			if(confirm("是否确定要删除:"+title+"?")){
				var url="${pageContext.request.contextPath}/merchant/account/electricMenu/deleteItem/"+itemId+"/"+menuId+"/";
				$.get(url,function (data){
					$(el).parents("span").remove();
				});
				return false;
			}
			return false;
		}
		function menuDetail(title,id){
			var url="${pageContext.request.contextPath}/merchant/account/electricMenu/updateItem/"+id+"/"+title+"/";
			$.get(url,showContentInfo);
			return false;
		}
		function menuDetailDialog(title,id){
			var url="${pageContext.request.contextPath}/merchant/account/electricMenu/updateItem/"+id+"/"+title+"/?detail=true";
			$.get(url,function(data){
				$("#detailContent").html(data);
				$("#detailModal").modal("show");
			});
			return false;
		}
		
		function deleteMenu(el){
			var label = $(el).prevAll("#menuName");
			var name=label.text();
			if(confirm("注意!删除后该类别下的所有菜品将被删除!是否要删除菜单:"+name+"?")){
				var idInput=$(el).prevAll("input[name='id']");
				$.get("${pageContext.request.contextPath}/merchant/account/electricMenu/delete/"+$(idInput).val()+"/",menusList);
			}
			return false;
		}

		function menusList(data){
			if(data!=null){
				$.get("${pageContext.request.contextPath}/merchant/account/electricMenu",showContentInfo);
			}
		}
		
		function updateMenu(el) {
			var label = $(el).prevAll("#menuName");
			$(label).after("<input type='text' class='input-small' name='name' value='" + $(label).text() + "'/>");
			$(label).css("display", "none");
			$(el).after("<input type='button' value='确定' onclick='doUpdate(this);' class='btn btn-primary updateBtn'><input type='button' value='取消' onclick='cancelUpdate(this);' class='btn btn-primary updateBtn'>");
			$(el).css("display", "none");
			return false;
		}

		function doUpdate(el) {
			var label = $(el).prevAll("#menuName");
			var input = $(el).prevAll("input[name='name']");
			var menus=new Array();
			if($(input).val()==""){
				alert("类别名称不能为空");
				return ;
			}
			$("td[class='node']").each(function(index) {
				menus.push($(this).children("a[id='menuName']").html());
			});
			if($(input).val()!=$(label).html()){
				for(var i=0;i<menus.length;i++){
					 if(menus[i]==$(input).val()){
						alert("类别名称已存在 ");
						return;
					} 
				}
			}
			var name=$(input).val();
			var idInput=$(el).prevAll("input[name='id']");
			$.post("${pageContext.request.contextPath}/merchant/account/electricMenu/update",{"name":name,"id":$(idInput).val()});
			$(label).html($(input).val());
			$(input).remove();
			$(label).css("display", "inline-block");
			$(el).prevAll(".updateA").css("display", "");
			$(el).nextAll(".updateBtn").remove();
			$(el).remove();
			return false;
		}

		function cancelUpdate(el) {
			var label = $(el).prevAll("#menuName");
			$(label).css("display", "inline-block");
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
			var validatamenus=new Array();
			$("td[class='node']").each(function(index) {
				var id = $(this).children("input[name='id']").val();
				validatamenus.push($(this).children("a[id='menuName']").html());
				if (!id) {
					var electricMenu = new Object();
					electricMenu.name = $(this).children("input[name='name']").val();
					if(electricMenu.name==""){
						return ;
					} 
					menus.push(electricMenu);
				}
			});
			
			//验证类别名是否重复
	 		for(var i=0;i<menus.length;i++){
			 	for(var j=i+1;j<menus.length;j++){
					if(menus[i].name==menus[j].name){
						alert("类别名称重复");
						return false;
					}  
				} 
			} 
 	 		for(var g=0;g<menus.length;g++){
	 			for(var h=0;h<validatamenus.length;h++){
					if(menus[g].name==validatamenus[h]){
						alert("类别名称重复");
						return false;
					} 
				}
	 		} 
			//验证类别是否为空
			var itemTrs=$("tr[class='itemTr']").length;
			var tds=$("td[class='node']").length;
			if(itemTrs+menus.length!=tds){
				alert("类别不能为空");
				return false;
			} 
			
			var strJson = "{";
			for ( var tagsIndex = 0; tagsIndex < menus.length; tagsIndex++) {
				strJson += "\"menus[" + tagsIndex + "].name\":\"";
				strJson += menus[tagsIndex].name + "\",";
			}
			if (strJson.length > 2) {
				strJson = strJson.substring(0, strJson.length - 1);
			}
			strJson += "}";
			var dataJson = eval('(' + strJson + ')');
			$.post("${pageContext.request.contextPath}/merchant/account/electricMenu/add/", dataJson, showInfo);
		}

		function showInfo(data){
			if(data!=null){
				alert(data);
			}
			$.get("${pageContext.request.contextPath}/merchant/account/electricMenu",showContentInfo);
		}
		
		function addChild(el) {
			loadPage("${pageContext.request.contextPath}/merchant/account/electricMenu/addItem");
			return false;
		}
		function removeTr(el){
			$(el).parents("tr").remove();
		}
		function addTag(el) {
			var tr = "<tr> <td class='node'><input class=\"input-small\" type=\"text\" name=\"name\" value=\"默认\"> <input type='button' class='btn btn-danger' value='删除' onclick='removeTr(this)'>";
			tr += "</td>";
			tr += "</tr>";
			$(el).parents("tr").before(tr);
		}
	</script>
</body>
</html>

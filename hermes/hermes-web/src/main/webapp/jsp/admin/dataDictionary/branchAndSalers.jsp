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
	<table>
		<thead>
			<tr>
				<td><a href="javascript:void();" id="addBranch"
					onclick="addBranch();">增加分公司</a></td>
			</tr>
		</thead>
		<c:forEach items="${BranchCompanys}" var="BranchCompany">
			<tbody>
				<tr id="${BranchCompany.id}">
					<td><span style="font-size:15px">${BranchCompany.name}</span><a href="javascript:void();" onclick="showSalers('${BranchCompany.id}')">展开 </a><a href="javascript:void();" onclick="add(this);">增加销售</a><a href="javascript:void();" onclick="deleteBranch('${BranchCompany.id}');"> 删除</a></td>
				</tr>
			</tbody>
		</c:forEach>
	</table>
	<script type="text/javascript">
		function addBranch() {
			var branch = "<tbody><tr> <td class='info'><input class=\"input-small\" type=\"text\" name=\"name\" value=\"分公司名字\"><a href=\"javascript:void();\" onclick=\"addBranchCompany(this)\">保存 </a><a href=\"javascript:void();\" onclick=\"add(this);\">增加销售</a><a href=\"javascript:void();\" onclick=\"deleteThis(this)\"> 删除</a></td></tr></tbody>"
			$('table').append(branch);
		}
		function deleteThis(el){
			$(el).parents('tr').remove();
		}
		function addBranchCompany(el) {
			var branchCompanyName = $(el).prev().val();
			$.get("${pageContext.request.contextPath}/admin/BranchAndSalers/addBranchCompany/"+ branchCompanyName + "/",showContentInfo);
		}
		function add(el) {
			var saler = "<tr><td class='info'><image class=\"subNode\" src=\"${pageContext.request.contextPath}/img/subNode.gif\"></image>"
					+ "<input type=\"text\" class=\"input-small\" name=\salerName\" value=\"销售员\"></input><a href=\"javascript:void();\" onclick=\"addSaler(this)\">增加 </a><a href=\"javascript:void();\" onclick=\"deleteThis(this)\">删除</a></td></tr>";
			$(el).parents('tr').after(saler);
		}
		function deleteSaler(id) {
			$("#"+id).remove();
			$.get("${pageContext.request.contextPath}/admin/BranchAndSalers/deleteSaler/"+ id + "/");
		
		}
		function deleteBranch(id){
			$("#"+id).parent().remove();
			$.get("${pageContext.request.contextPath}/admin/BranchAndSalers/deleteBranch/"+ id + "/");
		}
		function addSaler(el) {
			var branchCompanyId = $(el).parents('tr').prev().attr("id");
			var salerName = $(el).prev().val();
			var salerId=$(el).parents('tr').attr('id');
			if(branchCompanyId==null){
				alert("请先保存分公司名字");
				return ;
			}
			$.get("${pageContext.request.contextPath}/admin/BranchAndSalers/addSaler/"+ salerName + "/" + branchCompanyId + "/",salerInfo);
			function salerInfo(data){
				var saler = "<tr id=\""+data.id+"\"><td class='info'><image class=\"subNode\" src=\"${pageContext.request.contextPath}/img/subNode.gif\"></image><span style=\"font-size:15px\">销售："+$(el).prev().val()+"</span><a href=\"javascript:void();\" onclick=\"deleteSaler('"+data.id+"')\"> 删除</a></td></tr>";
				$(el).parents('tbody').append(saler);
				$(el).parents('tr').remove();
			}
		} 
		
		function showSalers(id) {
 		 	 if($("#"+id).children().children("span").next().text()=="收回 "){
 			 	$("#"+id).nextAll().attr("style","display:none"); 
 			 	$("#"+id).children().children("span").next().text("展开 ");
			}  else{
				if (typeof($("#"+id).next().attr("id"))=="undefined"){
					$.get("${pageContext.request.contextPath}/admin/BranchAndSalers/showSalers/"+ id + "/", showSaler);
					function showSaler(data) {
				  		for(var i=0;i<data.length;i++){
							var str="<tr id="+data[i].id+"><td><image class=\"subNode\" src=\"${pageContext.request.contextPath}/img/subNode.gif\"></image><span style=\"font-size:15px\">销售："+data[i].name+"</span><a href=\"javascript:void();\" onclick=\"deleteSaler('"+data[i].id+"')\"> 删除</a></td></tr>";
							$("#"+id).after(str);
						}  
				  		$("#"+id).children().children("span").next().text("收回 ");
					} 
					}else{
						$("#"+id).nextAll().attr("style","display:block"); 
						$("#"+id).children().children("span").next().text("收回 ");
					} 
			}
		}
	
	</script>
</body>
</html>
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
		<c:forEach items="${branchCompanys}" var="branchCompany">
			<tbody>
				<tr id="${branchCompany.id}">
					<td><span style="font-size:16px">${branchCompany.name}</span><a href="javascript:void();" onclick="showSalers('${branchCompany.id}')">展开 </a><a href="javascript:void();" onclick="add(this);">增加销售</a><a href="javascript:void();" onclick="deleteBranch('${branchCompany.id}');"> 删除</a></td>
				</tr>
			</tbody>
		</c:forEach>
	</table>
	<script type="text/javascript">
 		function addBranch() {
			var branch = "<tbody><tr> <td class='info'><input class=\"input-small\"  placeholder=\"请输入分公司名字...\" type=\"text\" name=\"name\"><a href=\"javascript:void();\" onclick=\"addBranchCompany(this)\">保存 </a><a href=\"javascript:void();\" onclick=\"add(this);\">增加销售</a><a href=\"javascript:void();\" onclick=\"deleteThis(this)\"> 删除</a></td></tr></tbody>"
			$('table').append(branch);
		}
		function deleteThis(el){
			$(el).parents('tbody').remove();
		}
		function deleteMySelf(el){
			$(el).parents('tr').remove();
		}
		function addBranchCompany(el) {
			var branchCompanyName = $(el).prev().val();
			if(branchCompanyName==""){
				alert("请输入分公司名字 ");
				return;
			}
			$.get("${pageContext.request.contextPath}/admin/dictionary/addBranchCompany/"+ branchCompanyName + "/",branchCompanyInfo);
			function branchCompanyInfo(data){
				$(el).parents('tbody').remove();
				var branchCompany="<tbody><tr id=\""+data.id+"\"> <td class='info'><span style=\"font-size:16px\">"+data.name+"</span><a href=\"javascript:void();\" onclick=\"showSalers('"+data.id+"')\">展开 </a><a href=\"javascript:void();\" onclick=\"add(this);\">增加销售</a><a href=\"javascript:void();\" onclick=\"deleteThis(this)\"> 删除</a></td></tr></tbody>";
				$('table').append(branchCompany);
			}
		}
	 	function add(el) {
			var branchCompanyId = $(el).parents('tr').attr("id");
			var status=$(el).prev().text();
			var saler = "<tr id=\""+branchCompanyId+"\"><td class=\""+status+"\"><image class=\"subNode\" src=\"${pageContext.request.contextPath}/img/subNode.gif\"></image>"
					+ "<input type=\"text\" class=\"input-small\" name=\salerName\" placeholder=\"请输入销售员名字...\"></input><a href=\"javascript:void();\" onclick=\"addSaler(this)\">增加 </a><a href=\"javascript:void();\" onclick=\"deleteMySelf(this)\">删除</a></td></tr>";
			$(el).parents('tr').after(saler);
		}
		function deleteSaler(id) {
			$("#"+id).remove();
			$.get("${pageContext.request.contextPath}/admin/dictionary/deleteSaler/"+ id + "/");
		}
		function deleteBranch(id){
			$("#"+id).parent().remove();
			$.get("${pageContext.request.contextPath}/admin/dictionary/deleteBranch/"+ id + "/");
		} 
		function addSaler(el) {
			var salerName = $(el).prev().val();
			var branchCompanyId=$(el).parents('tr').attr('id');
			//alert($(el).parents('tr').nextAll().attr("id"));
			if(salerName==""){
				alert("请填写销售名字 ");
				return ;
			}
			if(branchCompanyId=="undefined"){
				alert("请先保存分公司名字");
				return;
			}
			$("span[style='font-size:15px']").each(function (){
			 	if(salerName==$(this).text()&&branchCompanyId==$(this).parents('tr').attr('id')){
					alert("销售名已存在");
					return;
				} 
			});
	 	 	$.get("${pageContext.request.contextPath}/admin/dictionary/addSaler/"+ salerName + "/" + branchCompanyId + "/",salerInfo);
			function salerInfo(data){
				if(data==""||$(el).parents('td').attr('class')=="展开 "){
					$(el).parents('tr').remove();
					return;
				}else{
					var saler = "<tr id=\""+branchCompanyId+"\"><td class='info'><image class=\"subNode\" src=\"${pageContext.request.contextPath}/img/subNode.gif\"></image><span style=\"font-size:15px\">"+$(el).prev().val()+"</span><a href=\"javascript:void();\" onclick=\"deleteSaler('"+data.id+"')\"> 删除</a></td></tr>";
					$(el).parents('tr').first().after(saler);
					$(el).parents('tr').remove();
				}
			}  
		}  
		function showSalers(id) {
 		 	 if($("#"+id).children().children("span").next().text()=="收回 "){
 			 	$("#"+id).nextAll().attr("style","display:none"); 
 			 	$("#"+id).children().children("span").next().text("展开 ");
			}  else{
					$("#"+id).nextAll().html("");
					$.get("${pageContext.request.contextPath}/admin/dictionary/showSalers/"+ id + "/", showSaler);
					function showSaler(data) {
				  		for(var i=0;i<data.length;i++){
							var str="<tr id="+id+"><td><image class=\"subNode\" src=\"${pageContext.request.contextPath}/img/subNode.gif\"></image><span style=\"font-size:15px\">"+data[i].name+"</span><a href=\"javascript:void();\" onclick=\"deleteSaler('"+data[i].id+"')\"> 删除</a></td></tr>";
							$("#"+id).after(str);
						}  
					} 
						$("#"+id).nextAll().attr("style","display:block"); 
						$("#"+id).children().children("span").next().text("收回 ");
			}
		}
	
	</script>
</body>
</html>
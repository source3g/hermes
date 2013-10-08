<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>顾客组管理</title>
</head>
<body>
	<h3>顾客组操作</h3>
	<form id="addCustomerGroupForm" method="post" class="form-inline" action="${pageContext.request.contextPath}/merchant/customerGroup/add">
		<label class="control-label" for="name">名称：</label> <input type="text"
			class="input-xlarge" placeholder="请输入顾客组名称..." id="name" name="name">
		<span class="help-inline"><font color="red">*</font></span>
		<input type="submit" id="addCustomerGroupBtn" data-loading-text="增加中..." class="btn btn-primary" value="增加">
	</form>

	<h3>顾客组列表</h3>
	<table class="table table-bordered" id="groupTab">
		<thead>
			<tr>
				<th>名称</th>
				<th>操作</th>
			</tr>
		</thead>
		<c:forEach items="${customerGroups }" var="customerGroup">
			<tr>
				<td>${customerGroup.name }</td>
				<td><a class="btn btn-danger" href="javascript:void();"
					onclick="deleteById('${customerGroup.id}');">删除</a>
			
				</td>
			</tr>
		</c:forEach>
	</table>
			<div id="myModal" class="modal hide fade">
		<div class="modal-header">
			<a class="close" data-dismiss="modal">&times;</a>
			<h3>请选择该顾客组顾客的新顾客组</h3>
		</div>
		<div class="modal-body">
			<input type="hidden" id="groupIdToDel">
			<div>
			<select id="sel">
			<option value="chose">请选择</option>
			</select>
			<input type="button" class="btn btn-primary" id="customersFormBtn" value="确定" onclick="choseGroup()"></input>
			</div>
		</div>
		<div class="modal-footer"></div>
	</div>
	<script type="text/javascript">
		$(document).ready(function() {
			activeMenu("customerGroup");
			if(${not empty error}==true){
				alert("${error}");
			}
			$("#addCustomerGroupForm").validate({
				rules : {
					name : {
						required : true,
						remote:{
							type: "get",
							url:"${pageContext.request.contextPath}/merchant/customerGroup/nameValidate",
							data:{"name":function(){
												return $('#name').val();
											}
								}
						}
					}
				},
				messages : {
					name : {
						required : "请填写顾客组名称",
						remote:		"顾客组名称已存在"
					}
				}
			});

			$("#addCustomerGroupForm").submit(function() {
				if (!$("#addCustomerGroupForm").valid()) {
					return false;
				}
				$('#addCustomerGroupBtn').button('loading')
				return true;
			});
		});

		function deleteById(id) {
			if($("#groupTab tr:gt(0)").length<=1){
				alert("只剩一个了，不能删了");
				return ;
			}
			if(!confirm("确定该删除吗")){
				return;
			}
			$("#groupIdToDel").attr("value",id);
			$.get("${pageContext.request.contextPath}/merchant/customerGroup/listAllJson/",drawGroupToSel);
			$("#myModal").modal();
		}
		function drawGroupToSel(data){
			$('#sel').empty();
			for(var i=0;i<data.length;i++){
				$('#sel').append("<option value="+data[i].id+">"+data[i].name+"</option>");
			}
		}
		function choseGroup(){
			var customerGroupId=$('#groupIdToDel').val();
			var selector=$('#sel').val();
			loadPage("${pageContext.request.contextPath}/merchant/customerGroup/update/"+customerGroupId+"/"+selector+"/");
			 
			$("#myModal").modal('hide');
		}
	</script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加商户</title>
<%-- <%@ include file="../../include/header.jsp"%> --%>
</head>
<body>
	<form id="addMerchantForm"
		<c:if test="${empty update }">
			action="${pageContext.request.contextPath}/admin/merchant/add/"
		</c:if>
		<c:if test="${not empty update}">
			action="${pageContext.request.contextPath}/admin/merchant/update/"
		</c:if>
		method="post" class="form-horizontal">
		
		<div class="control-group">
			<label class="control-label" for="name">名称：</label>
			<div class="controls">
				<input type="text" class="input-xlarge" placeholder="请输入商户名称..."
					id="name" name="name" value="${merchant.name}"> <span class="help-inline"><font
					color="red">*</font></span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="addr">地址：</label>
			<div class="controls">
				<input type="text" class="input-xlarge" placeholder="请输入商户地址..."
					id="addr" name="addr" value="${merchant.addr}"> <span class="help-inline"></span>
			</div>
		</div>



		<div class="control-group">
			<label class="control-label" for="addr">集团商户：</label>
			<div class="controls">
				<input type="text" class="input-xlarge"
					placeholder="请选择集团商户，如果没有则不选..." id="addr" readonly="readonly"
					name="merchantGroupId"> <span >
					<c:if test="${not empty update }">
					 <input
					type="hidden"
					id="strId" name="strId" value="${merchant.id }">
					</c:if>
					 <a
					data-toggle="modal" href="#myModal"
					class="btn btn-primary btn-small">选择集团商户</a></span>
			</div>
		</div>


		<div class="form-actions">
			
			<c:if test="${not empty update }">
				<input class="btn btn-primary" type="button" onclick="modify();" value="修改">
			</c:if>

			<c:if test="${ empty update }">
				<input type="submit" class="btn btn-primary"  input="增加">
			</c:if>
			
			<c:if test="${ not empty success }">
				${success }
			</c:if>
			<c:if test="${not empty errors}">
				<ul>
					<c:forEach items="${errors}" var="error">
						<li>${error.defaultMessage}</li>
					</c:forEach>
				</ul>
			</c:if>
		</div>
	</form>



	<div id="myModal" class="modal hide fade">
		<div class="modal-header">
			<a class="close" data-dismiss="modal">&times;</a>
			<h3>选择集团商户</h3>
		</div>
		<div class="modal-body">
			<div class="well form-inline">
				<label for="merchantGroup">商户名称：</label> <input id="merchantGroup"
					type="text" class="input-small" placeholder="商户名称">
				<button class="btn" id="queryMerchantGroupBtn">查询</button>
			</div>
			<table id="merchantGroupTab"
				class="table table-bordered table-striped">
				<thead>
					<tr>
						<th>名称</th>
						<th>操作</th>
					</tr>
				</thead>
			</table>
		</div>
		<div class="modal-footer">
			
		</div>
	</div>


	<script type="text/javascript">
		$(document).ready()
		{
			$("#queryMerchantGroupBtn").click(queryMerchantGroup);
		}
		function queryMerchantGroup() {
			/* $.ajax(
			 {
			 type:"post",
			 url:${pageContext.request.contextPath}/admin/merchantGroup/list.html,
			 dataType : "json",
			 success : drawTable,
			 error:error
			 }
			 );  */
			$("#merchantGroupTab tr:gt(1)").each(function() {
				$(this).remove();
			});

			for ( var i = 0; i < 200; i++) {
				var str = $("<tr><td>" + "1111" + "</td><td>" + "2222" + "</td></tr>"); //注意拼接字符串前加上$
				$("#merchantGroupTab").append(str);//添加
			}

		}
		function drawTable() {
			alert("成功");
		}
		
		function modify(){
			if (!$("#addMerchantForm").valid()) {
				return false;
			}
			$('#addMerchantForm').ajaxSubmit({
				success:toList
			});
		}
		
		 $(document).ready(function() {
		 $('#addMerchantForm').submit(function(){
			 if (!$("#addMerchantForm").valid()) {
					return false;
				}
			var options = {
				success : toList
				
			};
			$(this).ajaxSubmit(options);
			return false;
			
		}); 
		 
		 var validateOptions={
					rules:{
						name:{
							required:true,
							minlength:2
						},
						addr:{
						required:true,
						minlength:2
						}
					},
					messages:{
						name:{
							required:"请填写商户名称",
							minlength:"至少输入两个字符"
						},
						addr:{
							required:"请输入地址",
							minlength:"至少输入两 个字符"
						}
					}
			};
			$('#addMerchantForm').validate(validateOptions);
		} );
		
		 
		function toList(data){
			$("#pageContentFrame").html(data)
		}

	</script>
</body>
<%-- <%@include file="../../include/footer.jsp"%> --%>

</html>
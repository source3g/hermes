<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加商户</title>
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
					id="name" name="name" value="${merchant.name}"> <span
					class="help-inline"><font color="red">*</font></span>
				<c:if test="${not empty update }">
					<input type="hidden" id="strId" name="strId"
						value="${merchant.id }">
				</c:if>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="addr">地址：</label>
			<div class="controls">
				<input type="text" class="input-xlarge" placeholder="请输入商户地址..."
					id="addr" name="addr" value="${merchant.addr}"> <span
					class="help-inline"></span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="addr">集团商户：</label>
			<div class="controls">
				<input type="text" class="input-xlarge"
					placeholder="请选择集团商户，如果没有则不选..." id="merchantGroupSel"
					readonly="readonly" value="${merchantGroup.name }"> <span>
					<input type="hidden" id="merchantGroupId" name="merchantGroupId"
					value="${merchant.merchantGroupId }"> <a
					data-toggle="modal" href="#myModal"
					class="btn btn-primary btn-small">选择集团商户</a>
				</span>
			</div>
		</div>
		<div class="form-actions">
			<c:if test="${not empty update }">
				<input class="btn btn-primary" type="button" onclick="modify();"
					value="修改">
			</c:if>

			<c:if test="${ empty update }">
				<input type="submit" class="btn btn-primary" value="增加">

			</c:if>
			<c:if test="${not empty errors }">
				<div class="alert alert-error">
					<ul>
						<c:forEach items="${errors }" var="error">
							<li>${error.defaultMessage }</li>
						</c:forEach>
					</ul>
				</div>
			</c:if>
		</div>
		<div class="control-group">
			<label class="control-label" for="addr">盒子SN：</label>
			<div class="controls">
				<input type="text" class="input-xlarge" placeholder="请输入盒子SN编码..."
					id="deviceSn" name="sn"> <input type="button"
					class="btn btn-primary " id="deviceId" onclick="findDevice()"
					value="增加">
			</div>
		</div>
		<table
			class="table table-striped table-bordered bootstrap-datatable datatable"
			id="deviceTable">
			<thead>
				<tr>
					<th width="100%">盒子SN编码</th>
				</tr>
				
			</thead>
			<c:if test="${not empty update }">
			<c:forEach items="${devices}" var="device">
				<tr><td class='deviceSnTd'>${device.sn}</td>
				<td><input type='button' name='deleteDeviceSn' class='btn btn-danger' onclick='deleteDevice(this)' value='删除'>
				<input type='hidden' name='deviceIds' value='${device.id}'></td>
			</tr>
			</c:forEach>
			</c:if>
		</table>
	</form>



	<div id="myModal" class="modal hide fade">
		<div class="modal-header">
			<a class="close" data-dismiss="modal">&times;</a>
			<h3>选择集团商户</h3>
		</div>
		<div class="modal-body">
			<form class="well form-inline" id="queryMerchantGroupForm">
				<label for="merchantGroup">集团商户名称：</label> <input
					id="merchantGroupName" name="merchantGroupName" type="text"
					class="input-small" placeholder="商户名称"> <input
					type="submit" class="btn" id="queryMerchantGroupBtn" value="查询"></input>
			</form>
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
		<div class="modal-footer"></div>
	</div>
	<div id="errorModal" class="modal hide fade">
		<div class="modal-body">
			<p id="resultMessage"></p>
		</div>
		<div class="modal-footer">
			<a href="#" class="btn" data-dismiss="modal">确定</a>
		</div>
	</div>

	<script type="text/javascript">
	
	function findDevice(){
		var sn=$("#deviceSn").val();
		var isInTable=false;
		$("#deviceTable tr:gt(0)").each(function (index){
			var tdInfo=$(this).children(".deviceSnTd").html();
			if(tdInfo.trim()==sn.trim()){
				isInTable=true;
			
			}
		});
		
		if(isInTable==true){
			return;
		}
		
		$.ajax({
				type: "get",
				url:"${pageContext.request.contextPath}/admin/device/sn/"+sn,
				success: showDevices,
				dataType: "json",
				error: showError
		});
	}
	
	function showError(){
		alert("该盒子已被绑定");
	}
	
		function select(id, name) {
			$("#myModal").modal("toggle");
			$("#merchantGroupId").attr("value", id);
			$("#merchantGroupSel").attr("value", name);
			
		}

		function queryMerchantGroup() {
			$("#merchantGroupTab tr:gt(0)").each(function() {
				$(this).remove();
			});
			var name = $("#merchantGroupName").val();
			$.ajax({
				type : "get",
				data : {
					"name" : name
				},
				url : "${pageContext.request.contextPath}/admin/merchantGroup/listAll/",
				dataType : "json",
				success : drawTable,
				error : error
			});

		}
		function drawTable(data) {
			for ( var i = 0; i < data.length; i++) {
				var str = $("<tr><td>" + data[i].name + "</td> <td> <a class='btn btn-success' href='javascript:void();' onclick='select(\"" + data[i].id + "\",\"" + data[i].name
						+ "\");' >选择</a> </td></tr>"); //注意拼接字符串前加上$
				$("#merchantGroupTab").append(str);//添加
			}
		}
		function error() {
			alert("出错了");
		}

		function modify() {
			if (!$("#addMerchantForm").valid()) {
				return false;
			}
			$('#addMerchantForm').ajaxSubmit({
				success : toList
			});
		}

		$(document).ready(function() {
			initDialog();
			
			$("#queryMerchantGroupForm").submit(function() {
				if (!$('#queryMerchantGroupForm').valid()) {
					return false;
				}
				queryMerchantGroup();
				return false;
			});

			$('#addMerchantForm').submit(function() {
				if (!$("#addMerchantForm").valid()) {
					return false;
				}
				var options = {
					success : toList

				};
				$(this).ajaxSubmit(options);
				return false;

			});

			var validateOptions = {
				rules : {
					name : {
						required : true,
						minlength : 2
					},
					addr : {
						required : true,
						minlength : 2
					}
				},
				messages : {
					name : {
						required : "请填写商户名称",
						minlength : "至少输入两个字符"
					},
					addr : {
						required : "请输入地址",
						minlength : "至少输入两 个字符"
					}
				}
			};
			$('#addMerchantForm').validate(validateOptions);

			$('#queryMerchantGroupForm').validate({
				rules : {
					merchantGroupName : {
						required : true
					}
				},
				messages : {
					merchantGroupName : {
						required : "请填写集团商户名称"
					}
				}
			});

		});
		function showDevices(data){
			var str = $("<tr><td class='deviceSnTd'>"+data.sn+ "</td><td><input type='button' name='deleteDeviceSn' class='btn btn-danger' onclick='deleteDevice(this)' value='删除'><input type='hidden' name='deviceIds' value='"+data.id+"'></td></tr>")
			$('#deviceTable').append(str);
		}
		function deleteDevice(deleteBtn){
			$(deleteBtn).parents("tr").remove();
		}
		function toList(data) {
			$("#pageContentFrame").html(data);
		}
		function initDialog(){
			if(${not empty success }==true){
				$('#addMerchantForm').clearForm();
				$("#resultMessage").html("操作成功！");
				$("#errorModal").modal();
			}
		}
		
	</script>
</body>
<%-- <%@include file="../../include/footer.jsp"%> --%>

</html>

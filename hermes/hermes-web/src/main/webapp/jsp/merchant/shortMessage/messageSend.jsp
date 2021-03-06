<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../include/import.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>短信发送</title>
</head>
<body>
	<form id="messageSendForm" class="well "  method="post" action="${pageContext.request.contextPath}/merchant/message/messageSend/">
		<table class="table table-bordered">
			<thead>
				<tr>
					<th colspan="4"><center>
							<h4>短信群发</h4>
						</center></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td width="20%"><label class="control-label">商户短信数据 :</label></td>
					<td width="26%">短信可用余额总数：${merchant.messageBalance.totalCount}</td>
					<td width="27%">短信可用数量：${merchant.messageBalance.surplusMsgCount}</td>
					<td width="27%">短信已发送数量：${merchant.messageBalance.sentCount}</td>

				</tr>

				<tr>
					<td><label class="control-label">选择客户组:</label></td>
					<td colspan="3"><c:if test="${not empty customerGroups }">
							<c:forEach items="${customerGroups}" var="customerGroup">
								<input type=checkbox name="ids" value="${customerGroup.id}">
								<a href="javascript:void();" onclick="customerListBycustomerGroupId('${customerGroup.id}')"> ${customerGroup.name}</a>
							</c:forEach>
						</c:if></td>
				</tr>

				<tr>
					<td><label class="control-label">输入客户电话号码(电话号码以分号或换行分隔)：</label></td>
					<td colspan="4"><textarea class="span8" rows="10" name="customerPhones" id="customerPhones"></textarea> <!--  <input id="customerPhonesInput" name="customerPhones" type="hidden"> -->
						<!-- <div style="background-color: white; height: 150px; width: 500px;" id="customerPhones" contentEditable="true"></div> --></td>
				</tr>

				<tr>
					<td><label class="control-label">选择短信模板：</label></td>
					<td colspan="4"><select id="sel">
							<option>请选择</option>
						</select></td>
				</tr>
				<tr>
					<td><label class="control-label">编辑短信内容：</label></td>
					<td colspan="4"><textarea class="span8" rows="5" name="content" id="content"></textarea></td>
				</tr>
				<tr>
					<td><label class="control-label">字数统计：</label></td>
					<td colspan="4"><span id="contentLength"></span></td>
				</tr>
				<tr>
					<td colspan="4"><input id="sendBtn" type="submit" data-loading-text="发送中..." class="btn btn-primary" value="发送"></td>
				</tr>
			</tbody>
		</table>
	</form>
	<table class="table table-bordered" id="groupSendLog">
		<thead>
			<tr>
				<th colspan="4">
					<h4>最近短信群发记录</h4>
				</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td width="30%">发送时间</td>
				<td width="30%">发送内容</td>
				<td width="40%">发送数量/成功数量</td>
			</tr>
		</tbody>
	</table>
	<div id="myModal" class="modal hide fade">
		<div class="modal-header">
			<a class="close" data-dismiss="modal">&times;</a>
			<h3>顾客组信息明细</h3>
		</div>
		<div class="modal-body">
			<div class="well" id="customersForm">
				<table id="customersTab" class="table table-bordered table-striped">
					<tbody>
						<tr>
							<td id="customer"></td>
						</tr>
						<tr>
							<td id="allList"></td>
						</tr>
					</tbody>
				</table>
				<div>
					<input type="button" class="btn btn-primary" id="customersFormBtn" value="确定" onclick="chosePhones()"></input>
				</div>
			</div>
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
	$(document).ready(function() {
		activeMenu("messageAdd");
			var i=1;
		$("#content").keyup(function(){
			var length=$("#content").val().length;
	 		if((length%70)==0){
				i=(length/70);
				$("#contentLength").text("当前"+length+"个字，以"+i+"条短信发送");
			} else{
				i=Math.floor(length/70)+1;
				$("#contentLength").text("当前"+length+"个字，以"+i+"条短信发送");
			}
		});
		$.get("${pageContext.request.contextPath}/merchant/message/groupSendLogList/",drawGroupSendLogList);
		
		if(${not empty success}==true){
		
			alert("短信已提交后台,请在短信列表查看");
		}
		if(${not empty error}==true){
			
			alert("${error}");
		}
		var validateOptions = {
				rules : { 
					content:{
						required : true
					}
				},
				messages:{
					content	:{
						required : "短信输入不能为空"
					}
				}
		};
		$('#messageSendForm').validate(validateOptions); 
	 $('#messageSendForm').submit(function() {
		 var usfulCount=getusfulCount();
		 if(usfulCount>2000){
			 alert("最多发2000条");
			 return false;
		 }
		 if($("input:checked[name='ids']").length==0&&usfulCount==0){
			 alert("请填写有效电话号码");
			 return false;
		 }
		 if (!$('#messageSendForm').valid()) {
				return false;
			}
		$('#sendBtn').button('loading')
		return true;
		}); 
	 
	 //短信模板
		$.ajax({
			url : "${pageContext.request.contextPath}/merchant/message/template/listJson/",
			dataType : "json",
			success : initSel
		});

		$("#sel").change(function() {
			var content = $("#" + $(this).val()).text();
			var title = $("#sel").find("option:selected").text();
			if (title == '请选择') {
				$("#id").html("");
				$("#title").attr("value", "");
				$("#content").html("");
				return;
			}
			$("#id").attr("value", $(this).val());
			$("#title").attr("value", title);
			$("#content").html(content);
		});
	});
		function drawGroupSendLogList(data){
			for(var i=0;i<data.length;i++){
				var str="<tr><td width=\"30%\">"+data[i].sendTime+"</td><td width=\"40%\" title="+data[i].content+" ><div style=\"width:200px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis; \">"+data[i].content+"</div></td><td width=\"30%\">"+data[i].sendCount+"/"+data[i].successCount+"</td></tr>";
				$('#groupSendLog').append(str);
			}
		}
		function initSel(data) {
			for ( var i = 0; i < data.length; i++) {
				$("#sel").append("<option value='"+data[i].id+"'>" + data[i].title + "</option>");
				$("#sel").after("<span id="+data[i].id+" style='display: none;'>" + data[i].content + "</span>");
			}
		}
		 function getusfulCount() {
			var customerPhones = $('#customerPhones').val();
			var phoneArray=customerPhones.split(/;|\n/);
			var usfulCount=0;
			for (var i=0;i<phoneArray.length;i++){
				if(phoneArray[i].match(/[0-9]{11}/)){
					usfulCount++;
				}
			}
			/*var startPos=0;
			for (var i=0;i<phoneArray.length;i++){
				alert(i);
				if(i==2){
					alert($("#customerPhones"));
					alert(startPos);
					alert(phoneArray[i].length);
					$("#customerPhones").selectRange(startPos, phoneArray[i].length);
				}
				startPos+=phoneArray[i].length;
			}*/
			return usfulCount;
		} 
		function customerListBycustomerGroupId(id){
			$("#customer").html("");
			$("#allList").html("");
			$.ajax({
				url: "${pageContext.request.contextPath}/merchant/customer/customerListBycustomerGroupId/"+id+"/",
				type:"get",
				success:drawTable
			});
			
			$("#myModal").modal();
		}
		
		function drawTable(data){
			if(typeof(data)=="string"){
				alert("客户组数据过多,最多显示5000位顾客信息");
				return;
			}
			for(var i=0;i<data.length;i++){
				var str="<input type=checkbox name=\"customerName\" onchange='selectOne(this);' value="+data[i].phone+">"+data[i].name;
			$("#customer").append(str);//添加
			}
			var allList="<input type=checkbox id=\"allCustomersList\" name=\"allList\" value=\"allCustomers\" onchange=\"change()\">全选"
			$("#allList").append(allList);//添加
		}
		function selectOne(checkBox){
			if($(checkBox).attr('checked')!='checked'){
				$("#allCustomersList").attr("checked",false);
			}
		}
		function change(){
	 		if($('#allCustomersList').attr('checked')=='checked'){
				$("input:checkbox[name='customerName']").attr("checked",'checked');
			} else {
				$("input:checkbox[name='customerName']").attr("checked",false);
	 		} 
		}
	
		function chosePhones(){
			$("input[name='customerName']:checked").each(function(){
				$('#customerPhones').append($(this).val()+";");
			});
			$("#myModal").modal("hide");
		}
	</script>
</body>
</html>

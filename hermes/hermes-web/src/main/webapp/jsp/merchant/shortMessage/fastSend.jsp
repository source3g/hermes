<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../../include/import.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>短信发送</title>
</head>
<body>
	<form id="fastSendForm" class="well ">
		<table class="table table-bordered">
			<thead>
				<tr>
					<th colspan="4"><center>
							<h4>短信录入</h4>
						</center></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td width="20%"><label class="control-label">商户短信数据 :</label></td>
					<td width="26%">短信预存数量：${merchant.shortMessage.totalCount}</td>
					<td width="27%" id="surplusMsgCount">短信可用数量：${merchant.shortMessage.surplusMsgCount}</td>
					<td width="27%">短信已发送数量：${merchant.shortMessage.sentCount}</td>

				</tr>

				<tr>
					<td>客户电话号码分隔类型：</td>
					<td colspan="3" style="color:red">
						电话号码以分号分隔
					</td>
				</tr>

				<tr>
					<td><label class="control-label">输入客户电话号码：</label></td>
					<td colspan="4">
						<!-- <textarea class="span8" rows="5"
							name="customerPhones" id="customerPhones"></textarea> -->
							<input id="customerPhonesInput" name="customerPhones" type="hidden" >
						<div style="background-color: white; height: 150px; width: 500px;"
							id="customerPhones" contentEditable="true" ></div>
					</td>
				</tr>

				<tr>
					<td><label class="control-label">选择短信模板：</label></td>
					<td colspan="4"><select id="sel">
							<option>请选择</option>
					</select></td>
				</tr>
				<tr>
					<td><label class="control-label">编辑短信内容：</label></td>
					<td colspan="4"><textarea class="span8" rows="5"
							name="content" id="content"></textarea></td>
				</tr>

				<tr>
					<td colspan="4"><input type="submit" class="btn btn-primary"
						value="发送"> 
					<td>
				</tr>

			</tbody>
		</table>
	</form>

	<script type="text/javascript">
		$(document).ready(function() {
			
			
			var validateOptions = {
					rules : {
						content : {
							required : true
						}					
					},
					messages : {
						content : {
							required : "短信输入不能为空"
						}
					}
				};
				$('#fastSendForm').validate(validateOptions);
							$('#fastSendForm').submit(function() {
									if ((!$('#fastSendForm').valid())||(!testCustomerPhones())||(!fastSend())) {
										return false;
									}
									$("#customerPhonesInput").attr("value",$('#customerPhones').text());
												var options = {
													url : "${pageContext.request.contextPath}/merchant/message/fastSend/",
													type : "post",
													success : function (data){
														if(${merchant.shortMessage.surplusMsgCount}==0){
															alert("短信可用数量不足,请充值");
														}else{
														alert("短信息已向后台发送,请在短信列表中查看");
														}
														showList(data);
													}
												};
												$(this).ajaxSubmit(options);
												return false;

											});

							//短信模板
							$.ajax({
										url : "${pageContext.request.contextPath}/merchant/message/template/listJson/",
										dataType : "json",
										success : initSel
									});

							$("#sel").change(
									function() {
										var content = $("#" + $(this).val())
												.text();
										var title = $("#sel").find(
												"option:selected").text();
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

		function initSel(data) {
			for ( var i = 0; i < data.length; i++) {
				$("#sel").append(
						"<option value='"+data[i].id+"'>" + data[i].title
								+ "</option>");
				$("#sel").after(
						"<span id="+data[i].id+" style='display: none;'>"
								+ data[i].content + "</span>");
			}
		}
		
		function showList(data) {
			$("#pageContentFrame").html(data);
		}

		function testCustomerPhones() {
			var j = 1;
			var customerPhones = $('#customerPhones').text();
			customerPhones.replace("<span id=\"wrongPhone\" >","");
			customerPhones.replace("</span>","");
			var re = /^[0-9]*$/;
			if(customerPhones.length==0){
				alert("请输入有效手机号码");
				return false;
			}
			if(customerPhones[customerPhones.length-1]!= ';'){
				customerPhones=customerPhones+";";
				$('#customerPhones').text(customerPhones);
			}
			
			for ( var i = 0; i < customerPhones.length; i++) {
				if (customerPhones[i] == ';') {
					if (i == j * 12 - 1
							&& re.test(customerPhones.substring(i - 11, i - 1))) {
						j++;
					} else {
						var str1 = customerPhones.substring(0, (j - 1) * 12);
						var str2 = customerPhones.substring((j - 1) * 12, i);
						var str3 = customerPhones.substring(i,
								customerPhones.length);
						var str = str1 + "<span id=\"wrongPhone\" >" + str2
								+ "</span>" + str3;

						$('#customerPhones').html(str);
						$('#wrongPhone').css('color', 'red');
						alert("电话号码不合法");
						return false;
					}
				}
			}
			return true;
		}
 		function fastSend() {
			var phones=$('#customerPhones').text();
			var phone=phones.split(";");
	 		if(phone.length-1>${merchant.shortMessage.surplusMsgCount}){
				alert("余额不足，请充值");
				return false;
			} 
	 		return true;
		} 
	</script>
</body>
</html>

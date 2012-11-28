<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>短信可用数量额度调整</title>
</head>
<body>
	<form id="updateQuotaForm">
		<table>
			<tr>
				<td><label class="control-label">商户名称： </label>
				<td><span>${merchant.name}</span> <input type="hidden"
					id="merchantId" name="id" value="${merchant.id}">
			</tr>
			<tr>
				<td>短信预存总数：
				<td><span id="totalCount">${merchant.shortMessage.totalCount}</span>
			</tr>
			<tr>
				<td>已使用短信数量：
				<td><span id="sentCount">${merchant.shortMessage.sentCount}</span>
			</tr>
			<tr>
				<td>可用发送数量:：
				<td><span id="surplusMsgCount">${merchant.shortMessage.surplusMsgCount}</span>
			</tr>
			<tr>
				<td>可用短信调整类型：</td>
				<td><select id="shortMessage" name="type" class="input-medium">
						<option value="add">递增</option>
						<option value="cut">递减</option>
				</select></td>
			</tr>
			<tr>
				<td>可用短信数量 ：</td>
				<td><input type="text" id="count" name="count" value="100">
				</td>
			</tr>
			<tr>
				<td><input type="submit" class="btn btn-primary" value="确定">
				</td>
			</tr>
		</table>
	</form>
	<script type="text/javascript">
		$(document)
				.ready(
						function() {
							var validateoptions = {
								rules : {
									count : {
										rangelength : [ 0, 6 ],
										number : true,
										digits : true,
										validateCount : true
									//	validateCount1 : true
									}
								},
								messages : {
									count : {
										rangelength : "请输入6位有效数字",
										number : "请输入有效数字",
										digits : "请输入整数",
										validateCount : "输入数量有误"
									//	validateCount1 : "输入数量不符合规范"
									}
								}
							};
							$('#updateQuotaForm').validate(validateoptions);

							$.validator.addMethod(
											"validateCount",
											function(value, element, params) {
												var select = $('#shortMessage').val();
												if (select == 'add') {
													return (parseInt(value) + parseInt($(
															"#surplusMsgCount")
															.text())) <= parseInt($(
															"#totalCount")
															.text());
												} else if (select=='cut') {
													return (parseInt($(
															"#surplusMsgCount")
															.text())-parseInt(value)) >= 0;
												}

												return false;

											}, "输入数字有误"); 
							
		
							$('#updateQuotaForm')
									.submit(
											function() {
												if (!$('#updateQuotaForm')
														.valid()) {
													return false;
												}
												var merchantId = $(
														'#merchantId').val();
												var options = {
													url : "${pageContext.request.contextPath}/admin/merchant/UpdateQuota/"
															+ merchantId + "/",
													type : "post",
													success : toList

												};

												$(this).ajaxSubmit(options);

												return false;

											});

							function toList(data) {
								$("#pageContentFrame").html(data);
							}
						});
	</script>
</body>
</html>
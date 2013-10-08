<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="sitemesh"
	uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@include file="../include/import.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>旺财宝精准营销管理系统-<sitemesh:title /></title>
<%@include file="../include/header.jsp"%>
<%@include file="../include/footer.jsp"%>
<style type="text/css">
body {
	padding-bottom: 40px;
}
</style>
<sitemesh:head />
</head>
<body>
	<%@include file="top.jsp"%>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span2">
				<%@include file="left.jsp"%>
			</div>
			<!-- span2 -->

			<div class="span10" id="pageContentFrame"><sitemesh:body></sitemesh:body></div>
		</div>
		<div id="remindTipAlert" class="alert alert-error"
			style="width: 200px; height: 20px; position: fixed; bottom: 5px; right: 5px; display: none;">
			<a class="close" data-dismiss="alert">×</a><strong
				id="remindTipContent" style="cursor: pointer;"> 提醒！</strong>
		</div>
		<%@include file="../include/copyright.jsp"%>
		<!-- row  -->
	</div>
	<!--/.fluid-container-->

	<script
		src="http://s25.cnzz.com/stat.php?id=5487211&web_id=5487211&show=pic1"
		language="JavaScript"></script>

</body>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						//loadPage("${pageContext.request.contextPath}/merchant/main/");

						//$("#collapseOne").collapse("show");
						//$("#customerList").addClass("active");
						initRemind();
						$("#remindTipContent")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/merchant/account/remind/toList");
											return false;
										});

						/*$("#customerList")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/merchant/customer/list/");
											return false;
										});
						*/
						$("#customerAdd")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/merchant/customer/add/");
											return false;
										});

						$("#customerGroup")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/merchant/customerGroup/");
											return false;
										});

						$("#newCustomerList")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/merchant/customer/newCustomerList/");
											return false;
										});
						$("#callInList")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/merchant/customer/callInList/");
											return false;
										});

						$("#callInStatistics")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/merchant/customer/callInStatistics/");
											return false;
										});

						$("#importCustomer")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/merchant/customer/import/");
											return false;
										});
						$("#importLog")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/merchant/customer/importLog/");
											return false;
										});
						$("#messageLog")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/merchant/message/reservedMsgLog/");
											return false;
										});
						$("#messageTemplate")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/merchant/message/template/");
											return false;
										});
						$("#messageAdd")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/merchant/message/toMessageSend/");
											return false;
										});

						$("#messageList")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/merchant/message/toMessageList/");
											return false;
										});
						$("#noteAdd")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/merchant/note/add/");
											return false;
										});
						$("#messageAutoSend")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/merchant/message/toAutoSend/");
											return false;
										});

						//商户中心
						$("#merchantSwitch")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/merchant/account/toSwitch/");
											return false;
										});
						$("#merchantRemindSetting")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/merchant/account/remindSetting/");
											return false;
										});
						$("#merchantRemind")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/merchant/account/remind/toList");
											return false;
										});
						$("#passwordChange")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/merchant/account/toPasswordChange/");
											return false;
										});

						$("#merchantResourceSetting")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/merchant/account/toResourceSetting/");
											return false;
										});
						$("#info")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/merchant/account/info/");
											return false;
										});
						$("#electricMenu")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/merchant/account/electricMenu/");
											return false;
										});
					});

	function toPasswordChange() {
		loadPage("${pageContext.request.contextPath}/merchant/account/toPasswordChange/");
	}
	function aabb() {
		loadPage("${pageContext.request.contextPath}/merchant/account/toResourceSetting/");
		return false;
	}
	function initRemind() {
		$
				.get(
						"${pageContext.request.contextPath}/merchant/account/remind/list",
						function callback(data) {
							var remindCount = data.length;
							if (remindCount == 0 || typeof (data) == "string") {
								$("#merchantRemind").html("提醒");
								$("#merchantRemind").css("color", "");
								$("#remindTipContent").html(
										"有" + remindCount + "个提醒 点击查看");
								$("#remindTipAlert").css("display", "none");
								return;
							} else {
								$("#remindTipContent").html(
										"有" + remindCount + "个提醒 点击查看");
								$("#remindTipAlert").css("display", "");
								$("#merchantRemind").html(
										"提醒" + "(" + remindCount + ")");
								$("#merchantRemind").css("color", "red");
							}
						});
	}
</script>
</html>

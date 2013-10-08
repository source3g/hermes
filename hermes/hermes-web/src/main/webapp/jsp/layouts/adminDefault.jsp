<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="sitemesh"
	uri="http://www.opensymphony.com/sitemesh/decorator"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>旺财宝精准营销管理系统后台-<sitemesh:title /></title>
<%@include file="../include/header.jsp"%>
<%@include file="../include/footer.jsp"%>
<style type="text/css">
body {
	padding-bottom: 40px;
}
</style>

</head>
<body>
	<%@include file="adminTop.jsp"%>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span2">
				<%@include file="adminLeft.jsp"%>
			</div>
			<!-- span2 -->
			<div class="span10">
				<sitemesh:body></sitemesh:body>
			</div>
		</div>
		<%@include file="../include/copyright.jsp"%>
	</div>
	<!--/.fluid-container-->
</body>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$("#addMerchant")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/admin/merchant/add/");
											return false;
										});
						$("#listMerchant")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/admin/merchant/list/");
											return false;
										});
						$("#addMerchantGroup")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/admin/merchantGroup/add/");
											return false;
										});

						$("#listMerchantGroup")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/admin/merchantGroup/list/");
											return false;
										});

						$("#addDevice")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/admin/device/add/");
											return false;
										});
						$("#listDevice")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/admin/device/list/");
											return false;
										});
						$("#importSim")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/admin/sim/import/");
											return false;
										});
						$("#listSim")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/admin/sim/list/");
											return false;
										});
						//短信管理 
						$("#shortMsgInfo")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/admin/merchant/messageInfo/list/");
											return false;
										});

						//角色管理

						$("#accountAdd")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/admin/security/account/add/");
											return false;
										});
						$("#accountManage")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/admin/security/account/list/");
											return false;
										});
						$("#roleAdd")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/admin/security/role/add/");
											return false;
										});
						$("#roleManage")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/admin/security/role/list/");
											return false;
										});
						$("#resourceManage")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/admin/security/resource/list/");
											return false;
										});

						//提醒设置 
						$("#remindDictionary")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/admin/dictionary/toRemindTemplate/");
											return false;
										});
						$("#merchantTagDictionary")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/admin/dictionary/tag/toTagSetting/");
											return false;
										});
						$("#branchAndSalers")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/admin/dictionary/toBranchAndSalers/");
											return false;
										});
						//系统管理
						$("#failedJms")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/admin/system/monitor/failedJms/");
											return false;
										});

						$("#failedMessageList")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/admin/message/failed/list/");
											return false;
										});
						$("#versionUpdate")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/admin/version/toVersion/");
											return false;
										});

						$("#versionList")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/admin/version/versionList/");
											return false;
										});
						/*
						$("#operateLog").click(function() {
						loadPage("${pageContext.request.contextPath}/admin/system/operateLog/");
						}); */
						$("#logList")
								.click(
										function() {
											loadPage("${pageContext.request.contextPath}/admin/system/toLogList/");
											return false;
										});
					});

</script>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>旺财宝精准营销管理系统-首页</title>
<%@include file="../include/header.jsp"%>
<%@include file="../include/footer.jsp"%>
<style type="text/css">
body {
	padding-bottom: 40px;
}
</style>

</head>
<body>
	<!-- topbar starts -->
	<div class="navbar">
		<div class="navbar-inner">
			<div class="container-fluid">
				<a class="btn btn-navbar" data-toggle="collapse"
					data-target=".top-nav.nav-collapse,.sidebar-nav.nav-collapse">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
				</a> <a class="brand"
					href="${pageContext.request.contextPath}/admin/index/"> <img
					alt="旺财宝" src="${pageContext.request.contextPath}/img/logo.png" />
					<!-- <span>旺财宝</span> --></a>

				<!-- theme selector ends -->

				<!-- user dropdown starts -->
				<div class="btn-group pull-right">
					<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
						<i class="icon-user"></i><span> admin</span> <span class="caret"></span>
					</a>
					<ul class="dropdown-menu">
						<li><a href="#">个人中心</a></li>
						<li class="divider"></li>
						<li><a href="login.html">退出</a></li>
					</ul>
				</div>
				<!-- user dropdown ends -->

				<!--/.nav-collapse -->
			</div>
		</div>
	</div>
	<!-- topbar ends -->


	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span2">
				<div class="accordion" id="accordion2">
					<div class="accordion-group">
						<div class="accordion-heading" style="background-color: #EEE;">
							<a class="accordion-toggle" data-toggle="collapse"
								data-parent="#accordion2" href="#collapseOne"> 商户管理 </a>
						</div>
						<div id="collapseOne" class="accordion-body collapse">
							<!-- <div class="accordion-inner"> -->
							<ul id="merchant-menu"
								class="nav nav-list nav-tabs  nav-stacked  main-menu">
								<!-- class="nav nav-list nav-tabs  nav-stacked collapse main-menu" -->
								<li><a href="#" id="addMerchant">添加商户</a></li>
								<li><a href="#" id="listMerchant">商户列表</a></li>
								<li><a href="#" id="addMerchantGroup">添加集团商户</a></li>
								<li><a href="#" id="listMerchantGroup">集团商户列表</a></li>
							</ul>
							<!-- </div> -->
						</div>
					</div>
					<div class="accordion-group">
						<div class="accordion-heading" style="background-color: #EEE;">
							<a class="accordion-toggle" data-toggle="collapse"
								data-parent="#accordion2" href="#collapseTwo"> 盒子 </a>
						</div>
						<div id="collapseTwo" class="accordion-body collapse">
							<!-- <div class="accordion-inner"> -->
							<ul id="device-menu"
								class="nav nav-list nav-tabs  nav-stacked  main-menu">
								<!-- class="nav nav-list nav-tabs  nav-stacked collapse main-menu" -->
								<li><a href="#" id="addDevice">添加盒子</a></li>
								<li><a href="#" id="listDevice">盒子列表</a></li>
							</ul>
							<!-- </div> -->
						</div>
					</div>
					<div class="accordion-group">
						<div class="accordion-heading" style="background-color: #EEE;">
							<a class="accordion-toggle" data-toggle="collapse"
								data-parent="#accordion2" href="#collapseThree"> SIM卡 </a>
						</div>
						<div id="collapseThree" class="accordion-body collapse">
							<!-- <div class="accordion-inner"> -->
							<ul id="sim-menu"
								class="nav nav-list nav-tabs  nav-stacked  main-menu">
								<!-- class="nav nav-list nav-tabs  nav-stacked collapse main-menu" -->
								<li><a href="#" id="addSim">添加SIM</a></li>
								<li><a href="#" id="listSim">SIM卡列表</a></li>
							</ul>
							<!-- </div> -->
						</div>
					</div>
					<div class="accordion-group">
						<div class="accordion-heading" style="background-color: #EEE;">
							<a class="accordion-toggle" data-toggle="collapse"
								data-parent="#accordion2" href="#collapseFour">短信管理 </a>
						</div>
						<div id="collapseFour" class="accordion-body collapse">
							<!-- <div class="accordion-inner"> -->
							<ul id="sim-menu"
								class="nav nav-list nav-tabs  nav-stacked  main-menu">
								<!-- class="nav nav-list nav-tabs  nav-stacked collapse main-menu" -->
								<li><a href="#" id="shortMsgInfo">预存明细</a></li>
							</ul>
							<!-- </div> -->
						</div>
					</div>
					<div class="accordion-group">
						<div class="accordion-heading" style="background-color: #EEE;">
							<a class="accordion-toggle" data-toggle="collapse"
								data-parent="#accordion2" href="#collapseFive">权限管理 </a>
						</div>
						<div id="collapseFive" class="accordion-body collapse">
							<!-- <div class="accordion-inner"> -->
							<ul id="security-menu"
								class="nav nav-list nav-tabs  nav-stacked  main-menu">
								<!-- class="nav nav-list nav-tabs  nav-stacked collapse main-menu" -->
								<li><a href="#" id="accountAdd">帐号添加</a></li>
								<li><a href="#" id="accountManage">帐号管理</a></li>
								<li><a href="#" id="roleAdd">角色新增</a></li>
								<li><a href="#" id="roleManage">角色管理</a></li>
								<li><a href="#" id="resourceManage">资源管理</a></li>
							</ul>
							<!-- </div> -->
						</div>
					</div>
					<div class="accordion-group">
						<div class="accordion-heading" style="background-color: #EEE;">
							<a class="accordion-toggle" data-toggle="collapse"
								data-parent="#accordion2" href="#collapseSix">数据字典 </a>
						</div>
						<div id="collapseSix" class="accordion-body collapse">
							<!-- <div class="accordion-inner"> -->
							<ul id="security-menu"
								class="nav nav-list nav-tabs  nav-stacked  main-menu">
								<!-- class="nav nav-list nav-tabs  nav-stacked collapse main-menu" -->
								<li><a href="#" id="remindDictionary">提醒设置</a></li>
								<li><a href="#" id="merchantTagDictionary">商户标签</a></li>
								<li><a href="#" id="branchAndSalers">销售管理</a></li>
							</ul>
							<!-- </div> -->
						</div>
					</div>
					<div class="accordion-group">
						<div class="accordion-heading" style="background-color: #EEE;">
							<a class="accordion-toggle" data-toggle="collapse"
								data-parent="#accordion2" href="#collapseSeven">系统管理</a>
						</div>
						<div id="collapseSeven" class="accordion-body collapse">
							<!-- <div class="accordion-inner"> -->
								<ul id="security-menu"
									class="nav nav-list nav-tabs  nav-stacked  main-menu">
									<!-- class="nav nav-list nav-tabs  nav-stacked collapse main-menu" -->
									<li><a href="#" id="failedJms">失败消息</a></li>
									<li><a href="#" id="failedMessageList">失败短信列表</a></li>
									<!-- <li><a href="javascript:void();" id="operateLog">操作日志</a></li> -->
									<li><a href="#" id="versionUpdate">版本更新</a></li>
								</ul>
							<!-- </div> -->
						</div>
					</div>
				</div>
			</div>

			<!-- span2 -->

			<noscript>
				<div class="alert alert-block span10">
					<h4 class="alert-heading">Warning!</h4>
					<p>
						You need to have <a href="http://en.wikipedia.org/wiki/JavaScript"
							target="_blank">JavaScript</a> enabled to use this site.
					</p>
				</div>
			</noscript>

			<div class="span10" id="pageContentFrame">
				<!-- <iframe id="pageContentFrame"  class="span10" frameborder="0"
					scrolling="no" style="margin-top: 10px"></iframe> -->
			</div>
		</div>
		<%@include file="../include/copyright.jsp"%>
		<!-- row  -->
	</div>
	<!--/.fluid-container-->
</body>
<script type="text/javascript">
	$(document).ready(function() {
		$("#addMerchant").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/merchant/add/");
			return false;
		});
		$("#listMerchant").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/merchant/list/");
			return false;
		});
		$("#addMerchantGroup").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/merchantGroup/add/");
			return false;
		});

		$("#listMerchantGroup").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/merchantGroup/list/");
			return false;
		});

		$("#addDevice").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/device/add/");
			return false;
		});
		$("#listDevice").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/device/list/");
			return false;
		});
		$("#addSim").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/sim/add/");
			return false;
		});
		$("#listSim").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/sim/list/");
			return false;
		});
		//短信管理 
		$("#shortMsgInfo").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/merchant/messageInfo/list/");
			return false;
		});
		
		//角色管理

		$("#accountAdd").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/security/account/add/");
			return false;
		});
		$("#accountManage").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/security/account/list/");
			return false;
		});
		$("#roleAdd").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/security/role/add/");
			return false;
		});
		$("#roleManage").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/security/role/list/");
			return false;
		});
		$("#resourceManage").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/security/resource/list/");
			return false;
		});

		//提醒设置 
		$("#remindDictionary").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/dictionary/toRemindTemplate/");
			return false;
		});
		$("#merchantTagDictionary").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/dictionary/tag/toTagSetting/");
			return false;
		});
		$("#branchAndSalers").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/dictionary/toBranchAndSalers/");
			return false;
		});
		//系统管理
		$("#failedJms").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/system/monitor/failedJms/");
			return false;
		});
		
		$("#failedMessageList").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/message/failed/list/");
			return false;
		});
		$("#versionUpdate").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/version/toVersion/");
			return false;
		});
		/*
		$("#operateLog").click(function() {
		loadPage("${pageContext.request.contextPath}/admin/system/operateLog/");
		}); */
	});

	function loadPage(url) {
		$("#pageContentFrame").load(url);
	}
</script>
</html>
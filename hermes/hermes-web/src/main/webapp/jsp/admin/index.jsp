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
				</a> <a class="brand" href="index/"> <img alt="旺财宝"
					src="${pageContext.request.contextPath}/img/logo.png" /> <!-- <span>旺财宝</span> --></a>

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
								<li><a href="javascript:void();" id="addMerchant">添加商户</a></li>
								<li><a href="javascript:void();" id="listMerchant">商户列表</a></li>
								<li><a href="javascript:void();" id="addMerchantGroup">添加集团商户</a></li>
								<li><a href="javascript:void();" id="listMerchantGroup">集团商户列表</a></li>
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
								<li><a href="javascript:void();" id="addDevice">添加盒子</a></li>
								<li><a href="javascript:void();" id="listDevice">盒子列表</a></li>
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
								<li><a href="javascript:void();" id="addSim">添加SIM</a></li>
								<li><a href="javascript:void();" id="listSim">SIM卡列表</a></li>
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
								<li><a href="javascript:void();" id="shortMsgInfo">预存明细</a></li>
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
								<li><a href="javascript:void();" id="accountAdd">帐号添加</a></li>
								<li><a href="javascript:void();" id="accountManage">帐号管理</a></li>
								<li><a href="javascript:void();" id="roleAdd">角色新增</a></li>
								<li><a href="javascript:void();" id="roleManage">角色管理</a></li>
								<li><a href="javascript:void();" id="resourceManage">资源管理</a></li>
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
								<li><a href="javascript:void();" id="dataDictionary">提醒设置</a></li>
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
		});
		$("#listMerchant").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/merchant/list/");
		});
		$("#addMerchantGroup").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/merchantGroup/add/");
		});

		$("#listMerchantGroup").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/merchantGroup/list/");
		});

		$("#addDevice").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/device/add/");
		});
		$("#listDevice").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/device/list/");
		});
		$("#addSim").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/sim/add/");
		});
		$("#listSim").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/sim/list/");
		});
		$("#shortMsgInfo").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/merchant/messageInfo/list/");
		});

		//角色管理

		$("#accountAdd").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/security/account/add/");
		});
		$("#accountManage").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/security/account/list/");
		});
		$("#roleAdd").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/security/role/add/");
		});
		$("#roleManage").click(function() { 
			loadPage("${pageContext.request.contextPath}/admin/security/role/list/");
		});
		$("#resourceManage").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/security/resource/list/");
		});
		
		//提醒设置 
		$("#dataDictionary").click(function() {
			loadPage("${pageContext.request.contextPath}/admin/dictionary/toRemindTemplate/");
		});
	});

	function loadPage(url) {
		/*  	 $.get(url, function(data) {
				$("#pageContentFrame").html(data)
			}); 
		 */
		//$("#pageContentFrame").attr("src", url);
		$("#pageContentFrame").load(url);
		//		$("#pageContentFrame").html(url);
	}
</script>
</html>
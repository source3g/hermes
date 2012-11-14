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
					src="${pageContext.request.contextPath}/img/logo.png" /> <span>旺财宝</span></a>

				<!-- theme selector starts -->
				<div class="btn-group pull-right theme-container">
					<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
						<i class="icon-tint"></i><span class="hidden-phone"> Change</span>
						<span class="caret"></span>
					</a>
					<ul class="dropdown-menu" id="themes">
						<li><a data-value="classic" href="#"><i
								class="icon-blank"></i> Classic</a></li>
					</ul>
				</div>
				<!-- theme selector ends -->

				<!-- user dropdown starts -->
				<div class="btn-group pull-right">
					<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
						<i class="icon-user"></i><span> ${merchant.name }</span> <span
						class="caret"></span>
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
								data-parent="#accordion2" href="#collapseOne"> 顾客管理 </a>
						</div>
						<div id="collapseOne" class="accordion-body collapse">
							<!-- <div class="accordion-inner"> -->
							<ul id="merchant-menu"
								class="nav nav-list nav-tabs  nav-stacked  main-menu">
								<li><a href="javascript:void();" id="customerList">顾客列表</a></li>
								<li><a href="javascript:void();" id="customerAdd">顾客新增</a></li>
								<li><a href="javascript:void();" id="customerGroup">顾客组管理</a></li>
								<li><a href="javascript:void();" id="callInList">来电顾客列表</a></li>
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

			<div class="span10" id="pageContentFrame"></div>
		</div>
		<%@include file="../include/copyright.jsp"%>
		<!-- row  -->
	</div>
	<!--/.fluid-container-->
</body>
<script type="text/javascript">
	$(document).ready(function() {
		$("#customerList").click(function() {
			loadPage("${pageContext.request.contextPath}/merchant/customer/list/");
		});
		$("#customerAdd").click(function() {
			loadPage("${pageContext.request.contextPath}/merchant/customer/add/");
		});
		$("#customerGroup").click(function() {
			loadPage("${pageContext.request.contextPath}/merchant/customerGroup/");
		});

		$("#callInList").click(function() {
			loadPage("${pageContext.request.contextPath}/merchant/customer/callIn/list/");
		});

	});

	function loadPage(url) {
		$("#pageContentFrame").load(url);
	}
</script>
</html>
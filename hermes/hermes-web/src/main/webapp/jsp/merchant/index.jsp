<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="${pageContext.request.contextPath}/css/adminIndex.css"
	rel="stylesheet" type="text/css" />
<title>首页</title>
<%@include file="../include/header.jsp"%>
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
				</a> <a class="brand" href="index.html"> <img alt="旺财宝"
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
				<div class="sidebar-nav">
					<a href="#accounts-menu" class="nav-header" data-toggle="collapse"><i
						class="icon-briefcase"></i>顾客管理<!-- <span class="label label-info">+3</span> --></a>
					<ul id="accounts-menu"
						class="nav nav-list nav-tabs  nav-stacked collapse main-menu">
						<li><a href="javascript:void();" id="addMerchant">添加</a></li>
						<li><a href="javascript:void();" id="listMerchant">查询</a></li>
					</ul>
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

			<div class="span10" id="pageContent"></div>
		</div>
		<%@include file="../include/copyright.jsp"%>
		<!-- row  -->
	</div>
	<!--/.fluid-container-->
</body>
<%@include file="../include/footer.jsp"%>
<script type="text/javascript">
	$(document).ready(function() {
		$("#addMerchant").click(function() {
			$("#pageContent").load("${pageContext.request.contextPath}/admin/merchant/add/");
		});
		$("#listMerchant").click(function() {
			$("#pageContent").load("${pageContext.request.contextPath}/admin/merchant/list/");
		});
	});
</script>
</html>
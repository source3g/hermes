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
						<i class="icon-user"></i><span> ${loginUser.name }</span> <span
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
								<li><a href="javascript:void();" id="newCustomerList">新顾客列表</a></li>
								<li><a href="javascript:void();" id="callInList">来电顾客列表</a></li>
								<li><a href="javascript:void();" id="importCustomer">导入顾客信息</a></li>
								<li><a href="javascript:void();" id="importLog">导入日志</a></li>
							</ul>
							<!-- </div> -->
						</div>
					</div>


					<div class="accordion-group">
						<div class="accordion-heading" style="background-color: #EEE;">
							<a class="accordion-toggle" data-toggle="collapse"
								data-parent="#accordion2" href="#collapseTwo"> 短信管理 </a>
						</div>
						<div id="collapseTwo" class="accordion-body collapse">
							<!-- <div class="accordion-inner"> -->
							<ul id="message-menu"
								class="nav nav-list nav-tabs  nav-stacked  main-menu">
								<li><a href="javascript:void();" id="messageList">短信列表</a></li>
								<li><a href="javascript:void();" id="messageAdd">短信发送</a></li>
								<li><a href="javascript:void();" id="messageFastSend">快捷发送</a></li>
								<li><a href="javascript:void();" id="messageSentLog">发送记录</a></li>
								<li><a href="javascript:void();" id="messageLog">预存记录</a></li>
								<li><a href="javascript:void();" id="messageAutoSend">挂机短信</a></li>
								<li><a href="javascript:void();" id="messageTemplate">短信模板</a></li>
							</ul>
							<!-- </div> -->
						</div>
					</div>
					<div class="accordion-group">
						<div class="accordion-heading" style="background-color: #EEE;">
							<a class="accordion-toggle" data-toggle="collapse"
								data-parent="#accordion2" href="#collapseThree"> 商户销售管理 </a>
						</div>
						<div id="collapseThree" class="accordion-body collapse">
							<!-- <div class="accordion-inner"> -->
							<ul id="message-menu"
								class="nav nav-list nav-tabs  nav-stacked  main-menu">
								<li><a href="javascript:void();" id="customerList">销售列表</a></li>
								<li><a href="javascript:void();" id="customerAdd">销售新增</a></li>
							</ul>
							<!-- </div> -->
						</div>
					</div>

					<div class="accordion-group">
						<div class="accordion-heading" style="background-color: #EEE;">
							<a class="accordion-toggle" data-toggle="collapse"
								data-parent="#accordion2" href="#collapseFour"> 备忘录 </a>
						</div>
						<div id="collapseFour" class="accordion-body collapse">
							<!-- <div class="accordion-inner"> -->
							<ul id="message-menu"
								class="nav nav-list nav-tabs  nav-stacked  main-menu">
								<li><a href="javascript:void();" id="passwordChange">备忘录列表</a></li>
								<li><a href="javascript:void();" id="merchantSwitch">新增</a></li>
								<li><a href="javascript:void();" id="merchantRemind">历史记录</a></li>
							</ul>
							<!-- </div> -->
						</div>
					</div>


					<div class="accordion-group">
						<div class="accordion-heading" style="background-color: #EEE;">
							<a class="accordion-toggle" data-toggle="collapse"
								data-parent="#accordion2" href="#collapseFive"> 商户中心 </a>
						</div>
						<div id="collapseFive" class="accordion-body collapse">
							<!-- <div class="accordion-inner"> -->
							<ul id="message-menu"
								class="nav nav-list nav-tabs  nav-stacked  main-menu">
								<li><a href="javascript:void();" id="passwordChange">密码修改</a></li>
								<li><a href="javascript:void();" id="infoChange">信息修改</a></li>
								<li><a href="javascript:void();" id="merchantSwitch">商户开关</a></li>
								<li><a href="javascript:void();" id="merchantRemind">提醒</a></li>
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

		$("#newCustomerList").click(function() {
			loadPage("${pageContext.request.contextPath}/merchant/customer/newCustomerList/");
		});
		$("#callInList").click(function() {
			loadPage("${pageContext.request.contextPath}/merchant/customer/callIn/list/");
		});

		$("#importCustomer").click(function() {
			loadPage("${pageContext.request.contextPath}/merchant/customer/import/");
		});
		$("#importLog").click(function() {
			loadPage("${pageContext.request.contextPath}/merchant/customer/importLog/");
		});
		$("#messageLog").click(function() {
			loadPage("${pageContext.request.contextPath}/merchant/message/reservedMsgLog/");
		});
		$("#messageTemplate").click(function() {
			loadPage("${pageContext.request.contextPath}/merchant/message/template/");
		});
		$("#messageAdd").click(function() {
			loadPage("${pageContext.request.contextPath}/merchant/message/toMessageSend/");
		});
		$("#messageFastSend").click(function() {
			loadPage("${pageContext.request.contextPath}/merchant/message/fastSend/");
		});
	});
		
	function loadPage(url) {
		$("#pageContentFrame").load(url);
	}
	
</script>
</html>

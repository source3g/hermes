<%@ page language="java" 
    pageEncoding="utf-8"%>
<!-- topbar starts -->
	<div class="navbar">
		<div class="navbar-inner">
			<div class="container-fluid">
				<a class="btn btn-navbar" data-toggle="collapse"
					data-target=".top-nav.nav-collapse,.sidebar-nav.nav-collapse">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
				</a> <a class="brand" target="_self"
					href="${pageContext.request.contextPath}/index/"> <img
					alt="旺财宝" src="${pageContext.request.contextPath}/img/logo.png" /></a>
				<span class="brand" style="padding-top: 25px;"> <span
					style="font-size: 30px; color: #eee;">${loginUser.name } </span> <span>客户来电和情感管理系统</span>
				</span>
				<!-- user dropdown starts -->
				<div class="btn-group pull-right">
					<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
						<i class="icon-user"></i><span> ${loginUser.account }</span> <span
						class="caret"></span>
					</a>
					<ul class="dropdown-menu">
						<li><a href="javascript:void(0)" onclick="toPasswordChange()">修改密码</a></li>
						<li class="divider"></li>
						<li><a href="${pageContext.request.contextPath}/logout/">退出</a></li>
					</ul>
				</div>
				<!-- user dropdown ends -->

				<!--/.nav-collapse -->
			</div>
		</div>
	</div>
	<!-- topbar ends -->
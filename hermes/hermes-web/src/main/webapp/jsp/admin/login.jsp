<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录</title>
<%@include file="../include/header.jsp"%>
<%@include file="../include/footer.jsp"%>
</head>
<body>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="row-fluid">
				<div class="span12">
					<center>
						<h2>旺财宝客户来电和情感管理系统</h2>
						<br> <br> <br> <br>
					</center>
					<hr>
				</div>
				<!--/span-->
			</div>
			<!--/row-->
			<div class="row-fluid">
				<div class=" span2 center login-box"></div>
				<div class=" span4 center login-box">
					<ul>
						<li>来电信息同步</li>
						<li>提高店员服务效率，增加顾客的尊贵感、贴心感和忠诚度。</li>
						<li>挂机短信发布</li>
						<li>顾客来电后，旺财宝会发出短信，便于顾客预留或转发。</li>
						<li>情感短信维护</li>
						<li>节庆日贴心的情感短信，增进顾客的亲切感和忠诚度。</li>
						<li>客户资料存储</li>
						<li>顾客信息是商家的宝贵资源，旺财宝帮您分享权限存储与管理。</li>
						<li>数据分析管理</li>
						<li>为老板和管理人员提供顾客消费行为和情感指数分析工具。</li>
					</ul>

				</div>
				<div class="well span3 center login-box">
					<div class="alert alert-info">请输入帐号密码</div>
					<form class="form-horizontal" action="${pageContext.request.contextPath}/adminLogin/login" method="post">
						<fieldset>
							<div  title="用户名" data-rel="tooltip">
								<span class="add-on"><i class="icon-user"></i></span><input
									autofocus class="input-large span10" name="username"
									id="username" type="text" />
							</div>
							<div class="clearfix"></div>

							<div title="密码" data-rel="tooltip">
								<span class="add-on"><i class="icon-lock"></i></span><input
									class="input-large span10" name="password" id="password"
									type="password"  />
							</div>
							<div class="clearfix"></div>

							<div class="input-prepend">
								<label class="remember" for="rememberMe"><input
									type="checkbox" name="rememberMe" id="rememberMe" />记住密码</label>
							</div>
							<div class="clearfix"></div>

							<p class="center span5">
								<button type="submit" class="btn btn-primary">登录</button>
							</p>
						</fieldset>
					</form>
				</div>
				<div class=" span3 center login-box"></div>

			</div>
			<!--/row-->
		</div>
		<!--/fluid-row-->

	</div>
	<!--/.fluid-container-->
	<%@include file="../include/copyright.jsp"%>
</body>
</html>
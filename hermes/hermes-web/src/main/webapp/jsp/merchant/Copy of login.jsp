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
				<div class="span12 center ">
					<h2>Welcome to Charisma</h2>
				</div>
				<!--/span-->
			</div>
			<!--/row-->
			<div class="row-fluid">
				<div class="well span5 center login-box">
					<div class="alert alert-info">Please login with your Username
						and Password.</div>
					<form class="form-horizontal" action="index.html" method="post">
						<fieldset>
							<div class="input-prepend" title="Username" data-rel="tooltip">
								<span class="add-on"><i class="icon-user"></i></span><input
									autofocus class="input-large span10" name="username"
									id="username" type="text" value="admin" />
							</div>
							<div class="clearfix"></div>

							<div class="input-prepend" title="Password" data-rel="tooltip">
								<span class="add-on"><i class="icon-lock"></i></span><input
									class="input-large span10" name="password" id="password"
									type="password" value="admin123456" />
							</div>
							<div class="clearfix"></div>

							<div class="input-prepend">
								<label class="remember" for="remember"><input
									type="checkbox" id="remember" />Remember me</label>
							</div>
							<div class="clearfix"></div>

							<p class="center span5">
								<button type="submit" class="btn btn-primary">Login</button>
							</p>
						</fieldset>
					</form>
				</div>
				<!--/span-->
			</div>
			<!--/row-->
		</div>
		<!--/fluid-row-->

	</div>
	<!--/.fluid-container-->
	<%@include file="../include/copyright.jsp"%>
</body>
</html>
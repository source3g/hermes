<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录</title>
<script type='text/javascript'
	src='${pageContext.request.contextPath}/js/jquery/jquery.js'></script>
<script type='text/javascript'
	src='${pageContext.request.contextPath}/css/bootstrap/bootstrap.js'></script>
<script type='text/javascript'
	src='${pageContext.request.contextPath}/js/jquery/jquery.history.js'></script>
<script type='text/javascript'
	src='${pageContext.request.contextPath}/js/jquery/jquery.form.js'></script>

<script type='text/javascript'
	src='${pageContext.request.contextPath}/js/jquery/jquery.validate.js'></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$.ajaxSetup({
				cache : false
			});
			addCssOrJs('${pageContext.request.contextPath}/css/bootstrap/bootstrap-cerulean.css');
			function addCssOrJs(file) {
				if ($("link[href$=" + file + "]").length == 0) {
					var css_href = file;
					var styleTag = document.createElement("link");
					styleTag.setAttribute('type', 'text/css');
					styleTag.setAttribute('rel', 'stylesheet');
					styleTag.setAttribute('href', css_href);
					$("head")[0].appendChild(styleTag);
				}
			}
		});
	</script>


</head>
<style type="text/css">
li {
	list-style: none;
}
</style>
<body>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="row-fluid">
				<div class="span12" style="height: 71px;witdh:1370px">
					<!-- <h2> -->
						<img alt="旺财宝"
							src="${pageContext.request.contextPath}/img/top1.jpg" />
				<!-- 	</h2> -->

					<!-- <hr> -->
				</div>
				<!--/span-->

			</div>
			<!--/row-->
			<div class="row-fluid" style="height: 500px;">
				<div class=" span2 center login-box"></div>
				<div class=" span4 center login-box" >  <!-- style="background-color:#FAFAD2 " -->
					<ul style="margin-left:-30px">
						<li style="color:#696969 ;margin-top:40px;">
						<div><img alt="来电信息同步" src="${pageContext.request.contextPath}/img/dianhua.jpg"/ style="margin-top:10px">
						<strong style="font-size:18px;">来电信息同步</strong>
						<p style="font-size:13px;margin-left:70px;margin-top:-20px">提高店员服务效率，增加顾客的尊贵感、贴心感和忠诚度。</p></div>
						</li>
						<!-- <li style="margin-bottom:8px;color:black"></li> -->
						<li style="color:#696969 ;">
						<div><img alt="挂机短信发布" src="${pageContext.request.contextPath}/img/duanxin.jpg"/ style="margin-top:10px">
						<strong style="font-size:18px;">挂机短信发布</strong>
						<p style="font-size:13px;margin-left:70px;margin-top:-20px">顾客来电后，旺财宝会发出短信，便于顾客预留或转发。</p></div>
						</li>
						<li style="color:#696969 ;">
						<div><img alt="情感短信维护" src="${pageContext.request.contextPath}/img/qinggan.jpg"/ style="margin-top:10px">
						<strong style="font-size:18px;">情感短信维护</strong>
						<p style="font-size:13px;margin-left:70px;margin-top:-20px">节庆日贴心的情感短信，增进顾客的亲切感和忠诚度。</p></div>
						</li>
						<li style="color:#696969 ;">
						<div><img alt="客户资料存储" src="${pageContext.request.contextPath}/img/kehu.jpg"/ style="margin-top:10px">
						<strong style="font-size:18px;">客户资料存储</strong>
						<p style="font-size:13px;margin-left:70px;margin-top:-20px">顾客信息是商家的宝贵资源，旺财宝帮您分享权限存储与管理。</p></div>
						</li>
						<li style="color:#696969 ;">
						<div><img alt="数据分析管理" src="${pageContext.request.contextPath}/img/shuju.jpg"/ style="margin-top:10px">
						<strong style="font-size:18px;">数据分析管理</strong>
						<p style="font-size:13px;margin-left:70px;margin-top:-20px">为老板和管理人员提供顾客消费行为和情感指数分析工具。</p></div>
						</li>
					</ul>
				</div>
				<div class="span4 center login-box"
					style="height: 330px; width: 350px; margin-left: 100px; margin-top: 60px; background-color:#F5F5F5; border: 1px solid #CFCFCF;">
					<!-- <div class="alert alert-info">请输入帐号密码</div>  -->
					<form class="form-horizontal"
						action="${pageContext.request.contextPath}/login"
						method="post">
						<fieldset>
							<!-- <i class="icon-user"></i> -->
							<!--  <div style="background-color:#CFCFCF;height:40px;">  -->
							<center style="margin-top: 10px;">
								<strong style="font-size: 20px; margin-top: 10px;">登录</strong>
							</center>
							<!-- </div> -->
							<div title="用户名" data-rel="tooltip" class="control-group"
								style="margin-top: 25px; margin-left: 25px">
								<strong>用户名：</strong><input class="input-large span10"
									name="username" placeholder="请输入用户名.." id="username"
									type="text" />
							</div>
							<div class="clearfix" style="margin-top: 10px"></div>

							<div title="密码" data-rel="tooltip" class="control-group"
								style="margin-left: 25px">
								<strong>登录密码：</strong>
								<!-- <i class="icon-lock"></i> -->
								<input class="input-large span10" name="password" id="password"
									placeholder="请输入密码.." type="password" />
							</div>
							<div class="clearfix"></div>

							<div class="input-prepend" style="margin-left: 25px">
								<label class="remember" for="rememberMe"><input
									type="checkbox" name="rememberMe" id="rememberMe" />记住密码</label>
							</div>
							<div class="clearfix"></div>

							<div class="center">
								<button type="submit" class="btn btn-large btn-primary"
									style="margin-left: 25px">登录</button>
							</div>
						</fieldset>
					</form>
				</div>
				<br>
				<div class=" span1 center login-box"></div>
			</div>
			<!--/row-->
		</div>
		<!--/fluid-row-->
	</div>
	<!--/.fluid-container-->
	<%@include file="../include/copyright.jsp"%>

</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商户开关设置</title>
</head>
<body>
	<form id="switchForm"
		action="${pageContext.request.contextPath}/merchant/account/switch/"
		method="post" class="form-horizontal">

		<div class="control-group">
			<label class="control-label" for="autoSend">挂机短信开关：</label>
			<div class="controls">
				<input type="radio" name="autoSend" id="autoSend">开
				<input type="radio" name="autoSend" id="autoSend">关
				<span class="help-inline">(注：选择关时，将不会进行挂机短信发送)
				</span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label" for="genderMatch">性别匹配开关：</label>
			<div class="controls">
				<input type="radio" name="genderMatch" id="genderMatch">开
				<input type="radio" name="genderMatch" id="genderMatch">关
				<span class="help-inline">(注：选择关时，发送短信将不会进行自动匹配姓名)</span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label" for="salerMatch">匹配销售开关：</label>
			<div class="controls">
				<input type="radio" name="salerMatch" id="salerMatch">开
				<input type="radio" name="salerMatch" id="salerMatch">关
				<span class="help-inline">(注：打开开关，群发短信自动加上对应的销售信息)</span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label" for="birthdayRemind">生日短信开关：</label>
			<div class="controls">
				<input type="radio" name="birthdayRemind" id="birthdayRemind">开
				<input type="radio" name="birthdayRemind" id="birthdayRemind">关
				<span class="help-inline">(注：打开开关，群发短信自动加上对应的销售信息)</span>
			</div>
		</div>


	</form>
</body>
</html>
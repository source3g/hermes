<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../../include/import.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加顾客信息</title>
</head>
<body>
	<form id="addCustomerForm" class="well " method="post">
		<table class="table table-bordered">
			<thead>
				<tr>
					<th colspan="3"><center>
							<h4>顾客信息录入</h4>
						</center></th>
				</tr>
			</thead>
			<tr>

				<td width="10%"><c:if test="${not empty update }">
						<input type="hidden" name="id" value="${customer.id }">
					</c:if> <label class="control-label">姓名：</label></td>
				<td width="40%"><input type="text" name="name"
					value="${customer.name }" class="input-medium"
					placeholder="请输入姓名..." /><span class="help-inline"><font
						color="red">*</font></span></td>
				<td width="10%"><label class="control-label" for="name">性别：</label></td>
				<td width="40%"><input type="radio" name="sex" value="MALE"
					<c:if test="${( empty customer) or customer.sex eq 'MALE' }">checked="checked"</c:if> />男
					<input type="radio" name="sex" value="FEMALE"
					<c:if test="${ customer.sex eq 'FEMALE' }">checked="checked"</c:if> />女</td>
			</tr>
			<tr>
				<td><label class="control-label">生日：</label></td>
				<td><input type="text" class="input-medium" name="birthday"
					value="${customer.birthday }" placeholder="请选择生日..."
					onclick="WdatePicker({dateFmt:'MM-dd'});" /></td>
				<td><label class="control-label">移动电话：</label></td>
				<td><input type="text" class="input-medium" name="phone"
					id="phone" placeholder="请输入移动电话..." value="${customer.phone }" /><span
					class="help-inline"><font color="red">*</font></span></td>
			</tr>
			<tr>
				<td><label class="control-label">顾客组：</label></td>
				<td><select id="customerGroupSel" name="customerGroup.id"
					class="input-medium">

				</select></td>
				<td><label class="control-label">黑名单：</label></td>
				<td><input type="checkbox" name="blackList"
					<c:if test="${customer.blackList eq true}">checked="checked"</c:if> />是
					&nbsp;&nbsp; 常用联系人  <input type="checkbox" name="favorite" <c:if test="${customer.favorite eq true}">checked="checked"</c:if> >是
					</td>
			</tr>
			<tr>
				<td><label class="control-label">家庭地址：</label></td>
				<td colspan="3"><input type="text" class="span8" name="address"
					placeholder="请输入地址..." value="${customer.address }" /></td>
			</tr>

			<tr>
				<c:forEach items="${customer.otherPhones }" var="otherPhone"
					varStatus="status">
					<td><label class="control-label">电话${status.count }：</label></td>
					<td><input type="text" class="span8" name="otherPhones"
						value="${otherPhone }" /></td>
				</c:forEach>
			</tr>

			<tr>
				<td><label class="control-label">QQ：</label></td>
				<td><input type="text" class="input-medium" name="qq"
					value="${customer.qq }" /></td>
				<td><label class="control-label">email：</label></td>
				<td><input type="text" class="input-medium" name="email"
					value="${customer.email }" /></td>
			</tr>
			<tr>
				<td><label class="control-label">备注：</label></td>
				<td colspan="3"><textarea class="span8" rows="3" name="note">${customer.note }</textarea></td>
			</tr>
			<tr>
				<td><label class="control-label">定时提醒：</label></td>
				<td colspan="3"><c:forEach items="${customer.reminds}"
						var="remind" varStatus="status">
						<div class="remindItem">
								<input type=hidden name="reminds[${status.index }].alreadyRemind" value="${remind.alreadyRemind }">
							事项：<input type="text" readonly="readonly"
								name="reminds[${status.index }].merchantRemindTemplate.title"
								class="input-medium"
								value="${remind.merchantRemindTemplate.title }"></input>
							<input type="hidden"
								name="reminds[${status.index }].merchantRemindTemplate.id"
								class="input-medium" value="${remind.merchantRemindTemplate.id}"></input>
							时间：<input type="text" name="reminds[${status.index }].remindTime"
								class="input-medium"
								value='<fmt:formatDate value="${remind.remindTime }" pattern="yyyy-MM-dd" />'
								onclick="WdatePicker();"> <span><c:if test="${remind.alreadyRemind eq true}">[已发送提醒短信]</c:if></span>
								<c:if test="${remind.alreadyRemind eq false}"><input type='button' class='btn' value='删除' onclick='deleteRemind(this);' /></c:if>
						</div>
					</c:forEach> <input type="button" id="addRemindBtn" class="btn"
					onclick="addRemind();" value="增加一个" /></td>
			</tr>
		</table>
		<div class="form-actions">
			<c:if test="${not empty update }">
				<input class="btn btn-primary" type="button" id="modifyBtn" value="修改">
			</c:if>

			<c:if test="${ empty update }">
					<input type="submit" id="addCustomer" data-loading-text="顾客增加中..." class="btn btn-primary" value="增加"/>
			</c:if>
			<input id="backToList" type="button"
				onclick="loadPage('${pageContext.request.contextPath}/merchant/customer/list/');"
				class="btn btn-primary" value="返回" />

			<div id="errorModal" class="modal hide fade">
				<div class="modal-body">
					<p id="resultMessage"></p>
				</div>
				<div class="modal-footer">
					<a href="#" class="btn" data-dismiss="modal">确定</a>
				</div>
			</div>

			<%@include file="../../include/error.jsp"%>
		</div>
	</form>
	<script type="text/javascript">
		var remindIndex = $(".remindItem").length; //初始化为1,第0个下边的方法直接添加，从第1个开始
		var remindOptions=null;
		$(document).ready(function() {
			activeMenu("customerList");
			if(${not empty error}){
				alert("${error}");
			}
			var validateoptions={
					rules: {
						name:{
							required : true,
							rangelength:[0,50]
						},
			 			phone:{
							required : true,
							number:true,
							digits:true ,
							remote:{
								type: "get",
								url:"${pageContext.request.contextPath}/merchant/customer/phoneValidate",
								data:{"phone":function(){
													return $('#phone').val();
												}
									}
							}
						},
						"customerGroup.id":{
							required : true
						},
						address:{
							rangelength:[0,50]
						},
						qq:{
							number:true,
							digits:true 
						}, 
						email:{
							email:true
						},
						note:{
							rangelength:[0,50]
						}
					},
					messages: {
						name:{
							required : "姓名不能为空",
							rangelength:"姓名不得超过50字"
						},
				 		phone:{
				 			required : "电话号码不能为空",
							number:"请输入合法的数字",
							digits:"请输入整数",
							remote:"该电话号码已存在"
						},
						"customerGroup.id":{
							required : "客户组不能为空"
						},
						address:{
							rangelength:"地址不得超过50字"
						},
						qq:{
							number:"请输入合法的数字",
							digits:"请输入整数"
						}, 
						email:{
							email:"请输入正确的email格式"
						},
						note:{
							rangelength:"备注不得超过50字"
						}
					}
			};
			
			if(${not empty update }==true){
				validateoptions.rules.phone.remote.data={"phone":function(){
								return $('#phone').val();
						},
					 "oldPhone":"${customer.phone}"
					};
			}
			
			$('#addCustomerForm').validate(validateoptions);
			initCustomerGroupList();
			initDialog();
			
			$("#modifyBtn").click(function(){
				if(!$('#addCustomerForm').valid()){
					return false;
				}
				//$('#modifyBtn').button('loading');
				var options={
						url : "${pageContext.request.contextPath}${action}",
						type : "post",
						success : function(data){
							alert("修改成功，重新查询查看结果");
							history.go(-1);
						}
				};
				$("#addCustomerForm").ajaxSubmit(options);
				return false;
			});
			
			
			$("#addCustomerForm").submit(function() {
				if(!$('#addCustomerForm').valid()){
					return false;
				}
				$('#addCustomer').button('loading');
				$('#addCustomerForm').attr("action","${pageContext.request.contextPath}/merchant/customer/add/");
				if(${not empty update }==true){
					$('#addCustomerForm').attr("action","${pageContext.request.contextPath}${action}");
				}
				$(this).submit();
				return false;
			});
		});	
		function initCustomerGroupList() {
			$.ajax({
				url : "${pageContext.request.contextPath}/merchant/customerGroup/listAllJson",
				type : "get",
				dataType : "json",
				success : initCustomerGroupSelection,
				error : error
			});
		}
		function error() {
			alert("顾客组初始化失败，请重新进入该页面");
		}
		function initCustomerGroupSelection(data) {
			for ( var i = 0; i < data.length; i++) {
				if(data[i].id=='${customer.customerGroup.id}'){
					$("#customerGroupSel").append("<option value='"+data[i].id+"' selected>" + data[i].name + "</option>");	
				}else{
					$("#customerGroupSel").append("<option value='"+data[i].id+"'>" + data[i].name + "</option>");
				}
			}
		} 	
		function addRemind() {
			function initRemindList() {
				$.ajax({
					url : "${pageContext.request.contextPath}/merchant/account/remindSetting/json/",
					type : "get",
					//dataType : "json",
					success : initRemingSelection,
					error : showError
				});
			}
			function initRemingSelection(data){
				remindOptions="<option value=''>请选择</option>"+remindOptions;
				for(var i=0;i<data.length;i++){
					remindOptions+="<option value='"+data[i].id+"'>"+data[i].title+"</option>"
				}
				 addRemindItem();
			}
			if(remindOptions==null){
				initRemindList();
			}else{
				addRemindItem();
			}
			
		}
		function showError(){
			alert("商户提醒为空");
		}
		function addRemindItem(){
			if ($(".remindItem").length == 0) {
				$("#addRemindBtn")
						.before(
								"<div  class=\"remindItem\">事项：<select name='reminds[0].merchantRemindTemplate.id'>"+remindOptions+"</select> 时间：<input type=\"text\" name=\"reminds[0].remindTime\"  class=\"input-medium\" onclick=\"WdatePicker();\"> <input  type='button' class='btn' value='删除' onclick='deleteRemind(this);'/> </div> ");
			} else {
				$(".remindItem:last")
						.after(
								"<div  class=\"remindItem\">事项：<select name='reminds["+remindIndex+"].merchantRemindTemplate.id'>"+remindOptions+"</select>  时间：<input type=\"text\" name=\"reminds["
										+ remindIndex
										+ "].remindTime\"  class=\"input-medium\" onclick=\"WdatePicker();\">  <input  type='button' class='btn' value='删除' onclick='deleteRemind(this);'/>  </div> ");
			}
			remindIndex++;
		}
		function deleteRemind(element) {
			$(element).parent(".remindItem").remove();
		}
		
		function initDialog(){
			if(${not empty success}==true){
				$('#addCustomerForm').clearForm();
				$("#resultMessage").html("操作成功！");
				$("#errorModal").modal();
			}
		}
	</script>
</body>
</html>

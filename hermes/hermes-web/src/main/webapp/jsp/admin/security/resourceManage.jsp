<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>资源管理</title>
</head>
<body>
	<h3>资源操作</h3>
	<form id="addResourceForm" class="form-inline">
		<label class="control-label" for="name">名称：</label> <input type="text"
			class="input-xlarge" placeholder="请输入资源名称..." id="name" name="name"
			value="${name }"> <span class="help-inline"><font
			color="red">*</font></span> <label class="control-label" for="code">代码：</label>
		<input type="text" class="input-xlarge" placeholder="请输入资源代码..."
			id="code" value="${code }" name="code"> <span
			class="help-inline"><font color="red">*</font></span><input
			id="pageNo" name="pageNo" type="hidden"> <input
			class="btn btn-primary" type="submit" value="增加"> <input
			class="btn btn-primary" type="button" onclick="query();" value="查询">
	</form>

	<h3>资源列表</h3>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th>名称</th>
				<th>代码</th>
				<th>操作</th>
			</tr>
		</thead>
		<c:forEach items="${page.data }" var="resource">
			<tr>
				<td>${resource.name }</td>
				<td>${resource.code }</td>
				<td><a class="btn btn-danger" href="javascript:void();"
					onclick="deleteById('${resource.id}');">删除</a></td>
			</tr>
		</c:forEach>
	</table>
	<div>
		<ul class="pagination">
			<li id="firstPage"><a href="javascript:void();">首页</a></li>
			<li id="frontPage"><a href="javascript:void();">前一页</a></li>
			<li id="nextPage"><a href="javascript:void();">后一页</a></li>
			<li id="lastPage"><a href="javascript:void();">尾页</a></li>
			<li>当前第${page.currentPage}/${page.totalPageCount}页
				共${page.totalRecords }条 转到第<input type="text" id="pageNoToGo"
				name="pageNo" class="input-mini">页<input type="button"
				id="pageOk" class="btn" value="确定"></input>
			</li>
		</ul>
	</div>
	<c:if test="${not empty errors }">
		<div class="alert alert-error">
			<ul>
				<c:forEach items="${errors }" var="error">
					<li>${error.defaultMessage }</li>
				</c:forEach>
			</ul>
		</div>
	</c:if>

	<script type="text/javascript">
		$(document).ready(function() {
			initPage();
			var submitOptions = {
				url : "${pageContext.request.contextPath}/admin/security/resource/add",
				type : "post",
				success : showContentInfo
			};

			$("#addResourceForm").validate({
				rules : {
					name : {
						required : true,
						remote:{
							type: "get",
							url:"${pageContext.request.contextPath}/admin/security/resourceValidate/",
							data:{"name":function(){
												return $('#name').val();
											}
								}
						} 
					},
					code : {
						required : true,
						remote:{
							type: "get",
							url:"${pageContext.request.contextPath}/admin/security/resourceValidate/",
							data:{"code":function(){
												return $('#code').val();
											}
								}
						} 
					}
				},
				messages : {
					name : {
						required : "请填写名称",
							remote:"名称已存在"
					},
					code : {
						required : "请填写代码",
						remote:"代码已存在"
					}
				}
			});

			$("#addResourceForm").submit(function() {
				if (!$("#addResourceForm").valid()) {
					return;
				}
				$(this).ajaxSubmit(submitOptions);
				return false;
			});
		});
		function query() {
			goToPage(1);
    		return false;
		}
		function deleteById(id) {
			$.ajax({
				url : "${pageContext.request.contextPath}/admin/security/resource/delete/" + id + "/",
				type : "get",
				success : showContentInfo
			});
		}
		
		
		function goToPage(pageNo){
			$("#pageNo").attr("value",pageNo);
			var options = {
					url : "${pageContext.request.contextPath}/admin/security/resource/list",
					type : "get",
					success : showContentInfo
				};
				$("#addResourceForm").ajaxSubmit(options);
		}
		
		
		 function initPage(){
		    	$('#pageOk').click(function(){
		    		var pageNoToGo=$('#pageNoToGo').val();
		    		goToPage(pageNoToGo);
		    	});
		    
		    	
		    	if(${page.totalPageCount}==1||${page.totalPageCount}==0){
		    		$("#firstPage").addClass("active");
					$("#frontPage").addClass("active");
					$("#nextPage").addClass("active");
					$("#lastPage").addClass("active");
		    	}else if(${page.currentPage}==1){
		    		$("#firstPage").addClass("active");
					$("#frontPage").addClass("active");
					$("#nextPage").removeClass("active");
					$("#lastPage").removeClass("active");
					
					$('#nextPage').click(function(){
						goToPage(${page.nextPageNo});
					});
					$("#lastPage").click(function (){
						goToPage(${page.lastPageNo});
					});		
					
		    	}else if(${page.currentPage}==${page.totalPageCount}){
		    		$("#firstPage").removeClass("active");
					$("#frontPage").removeClass("active");
					$("#nextPage").addClass("active");
					$("#lastPage").addClass("active");
					
					$("#firstPage").click(function (){
						goToPage(${page.firstPageNo});
					});
					$("#frontPage").click(function (){
						goToPage(${page.previousPageNo});
					});
		    	}else{
					$("#firstPage").click(function (){
						goToPage(${page.firstPageNo});				
						});
					$("#frontPage").click(function (){
						goToPage(${page.previousPageNo});				
						});
					$("#nextPage").click(function (){
						goToPage(${page.nextPageNo});			
						});
					$("#lastPage").click(function (){
						goToPage(${page.lastPageNo});			
						});
				}
		    }
	</script>
</body>
</html>
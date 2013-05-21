<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>版本列表</title>
</head>
<body>
	<form id="queryForm" method="get">
		<input id="pageNo" name="pageNo" type="hidden">
	</form>
	<table
		class="table table-striped table-bordered bootstrap-datatable datatable">
		<thead>
			<tr>
				<th width="14%">版本号(测试版)</th>
				<th width="14%">版本号(正式版)</th>
				<th width="10%">编码号</th>
				<th width="14%">版本文件地址</th>
				<th width="14%">版本上传时间</th>
				<th width="17%">md5</th>
				<th width="17%">版本描述</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.data}" var="version">
				<tr>
					<td><input type="radio" name="testVersionRadio"
						value="" onclick="return seltest(this);"></td>
					<td><input type="radio" name="onlineVersionRadio"
						value="${version.apkVersion }" onclick="return selOnline(this);"
						<c:if test="${onlineVersion.apkVersion eq version.apkVersion }">checked="checked"</c:if>>${version.apkVersion}</td>
					<td>${version.code}</td>
					<td>${version.url}</td>
					<td>${version.uploadTime}</td>
					<td>${version.md5}</td>
					<td title="${version.describe}">
					<div style="width:200px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis; ">${version.describe}</div></td>
					<%--  <td><fmt:formatDate value="${version.uploadTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td> --%>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div>
		<ul class="pagination">
			<li id="firstPage"><a href="javascript:void();">首页</a></li>
			<li id="frontPage"><a href="javascript:void();">前一页</a></li>
			<li id="nextPage"><a href="javascript:void();">后一页</a></li>
			<li id="lastPage"><a href="javascript:void();">尾页</a></li>
			<li>当前第${page.currentPage}/${page.totalPageCount}页共${page.totalRecords
				}条 转到第<input type="text" id="pageNoToGo" name="pageNo"
				class="input-mini">页<input type="button" id="pageOk"
				class="btn" value="确定"></input>
			</li>
		</ul>
	</div>
	<script type="text/javascript">
    $(document).ready(function(){
    	$('#queryForm').submit(function(){
    		goToPage(1);
    		return false;
    	});
    	initPage(${page.currentPage},${page.totalPageCount});
})
	function goToPage(pageNo){
		$("#pageNo").attr("value",pageNo);
		var options={
				url:"${pageContext.request.contextPath}/admin/version/versionList/",
				success:showContentInfo,
				error:showError
		};
		$('#queryForm').ajaxSubmit(options);
	}
    
    var test=$("input[name='onlineVersionRadio']:checked").val();
    function selOnline(el){
    	 if($(el).parent().prev().children().attr("checked")=="checked"){
     		alert("测试版与正式版不能共存");
     		$(el).removeAttr("checked");
     		$("input[value='"+test+"'][name='onlineVersionRadio']").attr("checked","checked");
     		return ;
     	}
    	var code=$(el).parent().next().html();
    	if(confirm("你确定更换版本?"))
    	{
    		$.post("${pageContext.request.contextPath}/admin/version/changeOnline/",{"version":version},function (data,status){
    			alert("更换成功");
    			showContentInfo(data);
    		});
    		return true;
    	}else{
    		return false;
    	}
    }
    
    function seltest(el){
    	if($(el).parent().next().children().attr("checked")=="checked"){
     		alert("测试版与正式版不能共存");
     		$(el).removeAttr("checked","false");
     		return ;
     	}
    	  	if(confirm("你确定更换版本?"))
        	{
    	  		
        	alert("更换成功");
        		return true;
        	}else{
        		return false;
        	}
    }
	</script>
</body>
</html>

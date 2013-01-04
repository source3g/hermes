<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>提醒列表</title>
</head>
<body>
		<form id="remindListForm" method="post" class="form-horizontal">
			<table class="table table-bordered">
				<thead>
					<tr>
						<th width="18%">提醒标题</th>
						<th width="18%">提醒内容</th>
						<th width="18%">提醒时间</th>
						<th width="28%">顾客信息</th>
						<th width="18%" >操作</th>
					</tr>
				</thead>
				<tr id="customerRemindDtos">
				</tr>
			</table>
		</form>
	<div id="myModal" class="modal hide fade">
		<div class="modal-body">
			<table id="customerInfo" class="table table-bordered table-striped">
				<thead>
					<tr>
						<th >顾客姓名</th>
						<th >顾客电话</th>
						<th >提醒日期</th>
					</tr>
				</thead>
			</table>
				<div>
					<input type="button" class=" btn btn-primary " data-dismiss="modal" id="customersFormBtn" value="关闭 " ></input>
				</div>
		</div>
			<div class="modal-footer"></div>
		</div>
		<script type="text/javascript">
	  $(document).ready(function() {
			$.get("${pageContext.request.contextPath}/merchant/account/remind/list",drawTable);
			function drawTable(data){
				for(var i=0;i<data.length;i++){
					var str="<td>"+data[i].title+"</td><td>"+data[i].content+"</td><td>"+data[i].advancedTime+"</td><td><input type=\"button\" value=\"详细信息\" class=\"btn btn-success\" id=\"customer"+i+"\"><span>  [共有"+data[i].customers.length+"位客户]</span></td><td><input type=\"button\" class=\"btn btn-success\" value=\"一键发送\" onclick=\"sendMessages('"+data[i].title+"')\"></td>";
					$("#customerRemindDtos").append(str);
					var index=i;
					$("#customer"+index).click(function (){
						$('#customerInfo').html("");
						$("#myModal").modal();
						var customers=data[index].customers;
						for(var k=0;k<customers.length;k++){
							var str="<tr><td>"+customers[k].customerName+"</td><td>"+customers[k].phone+"</td><td>"+customers[k].remindTime+"</td></tr>";
							$('#customerInfo').append(str);
						} 
					});
				}
			}
		});
		function sendMessages(title){
			$.get("${pageContext.request.contextPath}/merchant/account/sendMessages/"+title+"/",showContentInfo);
		}
	</script>
</body>
</html>
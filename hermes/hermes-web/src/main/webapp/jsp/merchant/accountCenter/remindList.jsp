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
			<table class="table table-bordered" id="customerRemindDtos">
				<thead>
					<tr>
						<th width="18%">提醒标题</th>
						<th width="18%">提醒内容</th>
						<th width="18%">提醒时间</th>
						<th width="28%">顾客信息</th>
						<th width="18%" >操作</th>
					</tr>
				</thead>
				
				<!-- <tr id="customerRemindDtos">
				</tr> -->
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
					var str="<tr><td width=\"18%\">"+data[i].title+"</td><td width=\"18%\">"+data[i].content+"</td><td width=\"18%\">"+data[i].advancedTime+"</td><td width=\"28%\"><input type=\"button\" value=\"详细信息\" class=\"btn btn-success\" id=\"customer"+i+"\"><span>  [共有"+data[i].customers.length+"位客户]</span></td><td width=\"18%\"><input type=\"button\" class=\"btn btn-success\" value=\"一键发送\" onclick=\"sendMessages('"+data[i].title+"')\"></td></tr>";
					$("#customerRemindDtos").append(str);
					$("#customer"+i).bind("click",{"remindInfo":data[i]},function (events){
						$('#customerInfo').html("");
						$("#myModal").modal();
						var customers=events.data.remindInfo.customers;
						for(var k=0;k<customers.length;k++){
							var str="<tr><td>"+customers[k].customerName+"</td><td>"+customers[k].phone+"</td><td>"+customers[k].remindTime+"</td></tr>";
							$('#customerInfo').append(str);
						}
					});
				}
			}
			if(${not empty error}){
				alert("${error}");
			}
			if(${not empty success}){
				alert("发送成功");
			}
			
			$.get("${pageContext.request.contextPath}/merchant/account/remind/list",function callback(data){
				var remindCount=data.length;
				if(remindCount==0||typeof(data)=="string"){
					$("#merchantRemind").html("提醒");
					$("#merchantRemind").css("color","");
					return;
				}else{
					$("#remindTipContent").html("有"+remindCount+"个提醒 点击查看");
					$("#remindTipContent").click(function(){
						loadPage("${pageContext.request.contextPath}/merchant/account/remind/toList");
					});
					$("#remindTipAlert").css("display","");
					$("#merchantRemind").html("提醒"+"("+remindCount+")");
					$("#merchantRemind").css("color","red");
				}
			});
			
		});
		function sendMessages(title){
			$.get("${pageContext.request.contextPath}/merchant/account/sendMessages/"+title+"/",showContentInfo);
		}
	</script>
</body>
</html>
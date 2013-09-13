<%@ page language="java" 
    pageEncoding="utf-8"%>
<div class="accordion" id="accordion2">
					<div class="accordion-group">
						<div class="accordion-heading" style="background-color: #EEE;">
							<a class="accordion-toggle" data-toggle="collapse"
								data-parent="#accordion2" href="#collapseOne"> 顾客管理 </a>
						</div>
						<div id="collapseOne" class="accordion-body collapse">
							<!-- <div class="accordion-inner"> -->
							<ul id="merchant-menu"
								class="nav nav-list nav-tabs  nav-stacked  main-menu">
								<li><a href="${pageContext.request.contextPath}/merchant/customer/list/" id="customerList">顾客管理</a></li>
								<!-- <li><a href="javascript:void();" id="customerAdd">顾客新增</a></li> -->
								<li><a href="#" id="customerGroup">顾客组管理</a></li>
								<!--<li><a href="javascript:void();" id="newCustomerList">新顾客列表</a></li> -->
								<li><a href="#" id="callInList">来电记录</a></li>
								<li><a href="#" id="callInStatistics">来电顾客统计</a></li>
								<!-- <li><a href="javascript:void();" id="importCustomer">导入顾客信息</a></li> -->
								<!-- <li><a href="javascript:void();" id="importLog">导入日志</a></li> -->
							</ul>
							<!-- </div> -->
						</div>
					</div>


					<div class="accordion-group">
						<div class="accordion-heading" style="background-color: #EEE;">
							<a class="accordion-toggle" data-toggle="collapse"
								data-parent="#accordion2" href="#collapseTwo"> 短信管理 </a>
						</div>
						<div id="collapseTwo" class="accordion-body collapse">
							<!-- <div class="accordion-inner"> -->
							<ul id="message-menu"
								class="nav nav-list nav-tabs  nav-stacked  main-menu">
								<li><a href="#" id="messageList">短信列表</a></li>
								<li><a href="#" id="messageAdd">短信群发</a></li>
								<!--  <li><a href="javascript:void();" id="messageSentLog">发送记录</a></li>-->
								<li><a href="#" id="messageLog">预存记录</a></li>
								<li><a href="#" id="messageAutoSend">挂机短信</a></li>
								<li><a href="#" id="messageTemplate">短信模板</a></li>
							</ul>
							<!-- </div> -->
						</div>
					</div>
					<div style="display: none;">
						<div class="accordion-group">
							<div class="accordion-heading" style="background-color: #EEE;">
								<a class="accordion-toggle" data-toggle="collapse"
									data-parent="#accordion2" href="#collapseThree"> 商户销售管理 </a>
							</div>
							<div id="collapseThree" class="accordion-body collapse">
								<!-- <div class="accordion-inner"> -->
								<ul id="message-menu"
									class="nav nav-list nav-tabs  nav-stacked  main-menu">
									<li><a href="#" id="salerList">销售列表</a></li>
									<li><a href="#" id="salerAdd">销售新增</a></li>
								</ul>
								<!-- </div> -->
							</div>
						</div>
					</div>
					<div style="display: none;">
						<div class="accordion-group">
							<div class="accordion-heading" style="background-color: #EEE;">
								<a class="accordion-toggle" data-toggle="collapse"
									data-parent="#accordion2" href="#collapseFour"> 备忘录 </a>
							</div>
							<div id="collapseFour" class="accordion-body collapse">
								<!-- <div class="accordion-inner"> -->
								<ul id="message-menu"
									class="nav nav-list nav-tabs  nav-stacked  main-menu">
									<li><a href="#" id="noteList">备忘录列表</a></li>
									<li><a href="#" id="noteAdd">新增</a></li>
									<li><a href="#" id="noteHistory">历史记录</a></li>
								</ul>
								<!-- </div> -->
							</div>
						</div>
					</div>


					<div class="accordion-group">
						<div class="accordion-heading" style="background-color: #EEE;">
							<a class="accordion-toggle" data-toggle="collapse"
								data-parent="#accordion2" href="#collapseFive"> 商户中心 </a>
						</div>
						<div id="collapseFive" class="accordion-body collapse">
							<!-- <div class="accordion-inner"> -->
							<ul id="message-menu"
								class="nav nav-list nav-tabs  nav-stacked  main-menu">
								<li><a href="#" id="passwordChange">密码修改</a></li>
								<li><a href="#" id="merchantSwitch">商户开关</a></li>
								<li><a href="#" id="merchantRemind">提醒</a></li>
								<li><a href="#" id="merchantRemindSetting">提醒设置</a></li>
								<li><a href="#" id="merchantResourceSetting">资源设置</a></li>
								<li><a href="#" id="info">商户信息</a></li>
								<li><a href="#" id="electricMenu">电子菜单</a></li>
							</ul>
							<!-- </div> -->
						</div>
					</div>


				</div>
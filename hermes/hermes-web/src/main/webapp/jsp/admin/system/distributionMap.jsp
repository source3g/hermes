<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>旺财宝位置分布图</title>
<%@include file="../../include/footer.jsp"%>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.2"></script>
<script type="text/javascript"
	src="http://api.map.baidu.com/library/MapWrapper/1.2/src/MapWrapper.min.js"></script>
</head>
<body>
	<div>
		<h3>旺财宝位置分布图</h3>
	</div>
	<div id="divMap"
		style="width: 1000px; height: 600px; border: solid 1px gray"></div>

	<script type="text/javascript">
		$(document).ready(function() {
			var myMap = new BMap.Map("divMap");
			myMap.centerAndZoom(new BMap.Point(106.208254, 37.993561), 5);
			myMap.enableScrollWheelZoom(); //启用滚轮放大缩小
			myMap.addControl(new BMap.NavigationControl()); //添加默认缩放平移控件
			myMap.addControl(new BMap.NavigationControl({
				anchor : BMAP_ANCHOR_TOP_RIGHT,
				type : BMAP_NAVIGATION_CONTROL_SMALL
			})); //右上角，仅包含平移和缩放按钮
			myMap.addControl(new BMap.NavigationControl({
				anchor : BMAP_ANCHOR_BOTTOM_LEFT,
				type : BMAP_NAVIGATION_CONTROL_PAN
			})); //左下角，仅包含平移按钮
			myMap.addControl(new BMap.NavigationControl({
				anchor : BMAP_ANCHOR_BOTTOM_RIGHT,
				type : BMAP_NAVIGATION_CONTROL_ZOOM
			})); //右下角，仅包含缩放按钮
			var mapWforGPS = new BMapLib.MapWrapper(myMap, BMapLib.COORD_TYPE_GPS);

			$.get("${pageContext.request.contextPath}/device/distributionInfo", function(data) {
				for ( var i = 0; i < data.length; i++) {
					var sn = data[i].device.sn;
					var salerName = '无';
					var branchCompanyName = '无';
					if (data[i].salerName) {
						salerName = data[i].salerName;
					}
					if (data[i].branchCompanyName) {
						branchCompanyName = data[i].branchCompanyName;
					}
					var gpsMkr = new BMap.Marker(new BMap.Point(/*GPS坐标*/data[i].device.gpsPoint.x, data[i].device.gpsPoint.y));//116.397428, 39.75923 + 0.01
					gpsMkr.addEventListener("click", function() {
						//var strXY = this.getPosition().lng.toFixed(3) + ", " + this.getPosition().lat.toFixed(3);
						var infoWin = new BMap.InfoWindow("编号:" + sn + " " + "销售:" + salerName + "分公司:" + branchCompanyName);
						this.openInfoWindow(infoWin);
					});
					mapWforGPS.addOverlay(gpsMkr);
				}

			});

		});
	</script>
	<script type="text/javascript">
		
	</script>
</body>
</html>
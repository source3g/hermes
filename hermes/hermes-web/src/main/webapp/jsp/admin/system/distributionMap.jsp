<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>旺财宝位置分布图</title>
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

		//可以转化gps坐标
		var mapWforGPS = new BMapLib.MapWrapper(myMap, BMapLib.COORD_TYPE_GPS);

		//可以转化google坐标
		var mapWforGoogle = new BMapLib.MapWrapper(myMap, BMapLib.COORD_TYPE_GOOGLE);

		for ( var i = 0; i < 30; i++) {

			//添加gps坐标mkr
			var gpsMkr = new BMap.Marker(new BMap.Point(/*GPS坐标*/116.397428, 39.75923 + i * 0.01));

			(function(i) {
				gpsMkr.addEventListener("click", function() {
					var strXY = this.getPosition().lng.toFixed(3) + ", " + this.getPosition().lat.toFixed(3);
					var infoWin = new BMap.InfoWindow("GPS-" + i + ": " + strXY);
					this.openInfoWindow(infoWin);
				});
			})(i);

			mapWforGPS.addOverlay(gpsMkr);

			//添加google坐标mkr
			var googleMkr = new BMap.Marker(new BMap.Point(/*Google坐标*/116.250 + i * 0.01, 39.910));

			(function(i) {
				googleMkr.addEventListener("click", function() {
					var strXY = this.getPosition().lng.toFixed(3) + ", " + this.getPosition().lat.toFixed(3);
					var infoWin = new BMap.InfoWindow("Google-" + i + ": " + strXY);
					this.openInfoWindow(infoWin);
				});
			})(i);

			mapWforGoogle.addOverlay(googleMkr);

		}
	</script>
</body>
</html>
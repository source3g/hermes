var chart;
var options = {
	chart : {
		renderTo : 'chartContainer',
		type : 'line',
		marginRight : 130,
		marginBottom : 50
	},
	title : {
		text : '顾客来电月度统计',
		x : -20
	// center
	},
	credits : {
		enabled : false
	// 不显示LOGO
	},
	subtitle : {
		text : '顾客来电月度统计',
		x : -20
	},
	xAxis : {
		tickmarkPlacement : 'on',
		minorTickInterval : 'auto',// 设置是否出现纵向小标尺
		labels : {
			rotation : -30,
			align : 'right'
		},
		categories : [ '12-05', '12-06', '12-07', '12-08', '12-09', '12-10', '12-11', '12-12', '12-13', '12-14', '12-15', '12-16', '12-17', '12-18', '12-19', '12-20', '12-21',
				'12-22', '12-23', '12-24', '12-25', '12-26', '12-27', '12-28', '12-29', '12-30' ]
	},
	yAxis : {
		allowDecimals : false,
		title : {
			text : '来电次数'
		},
		plotLines : [ {
			value : 0,
			width : 1,
			color : '#808080'
		} ]
	},
	tooltip : {
		formatter : function() {
			return '<b>' + this.series.name + '</b><br/>' + this.x + ': ' + this.y + '次';
		}
	},
	legend : {
		layout : 'vertical',
		align : 'right',
		verticalAlign : 'top',
		x : -10,
		y : 100,
		borderWidth : 0
	},
	series : [ {
		name : '全部顾客来电',
		data : [ 7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6, 20, 20, 21, 25, 26, 27, 28, 29, 30, 31, 32, 33, 20, 35 ]
	}, {
		name : '新顾客来电',
		data : [ -0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5, 20, 20, 21, 25, 26, 27, 28, 29, 30, 31, 32, 33, 20, 35 ]
	}, {
		name : '老顾客来电',
		data : [ -0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0, 20, 20, 21, 25, 26, 27, 28, 29, 30, 31, 32, 33, 20, 35 ]
	} ]
};

var pieChart;
var pieChartOptions = {
	chart : {
		renderTo : 'dayChartContainer',
		plotBackgroundColor : null,
		plotBorderWidth : null,
		plotShadow : false
	},
	title : {
		text : '今日来电次数统计'
	},
	tooltip : {
		pointFormat : '{series.name}: <b>{point.y}个</b>',
		percentageDecimals : 1
	},
	plotOptions : {
		pie : {
			allowPointSelect : true,
			cursor : 'pointer',
			dataLabels : {
				enabled : true,
				color : '#000000',
				connectorColor : '#000000',
				formatter : function() {
					return '<b>' + this.point.name + '</b>: ' + Math.round(this.percentage) + ' %';
				}
			}
		}
	},
	credits: {
        enabled: false
    },
	series : [ {
		type : 'pie',
		name : '来电次数',
		data : [{name:'老顾客',color:'#3DA9FF',y:65}
		, {
			name : '新顾客',
			y : 35,
			sliced : true,
			selected : true
		} ]
	} ]
};

$(document).ready(function() {
	chart = new Highcharts.Chart(options);
});

function drawPieChart(data) {
	pieChart && pieChart.destroy();
	pieChart = null;
	var newCount=data.newCount;
	var oldCount=data.oldCount;
	var all=newCount+oldCount;
	var data=[{name:'新顾客',color:'#3DA9FF',y:newCount}
	, {
		name : '老顾客',
		y : oldCount,
		sliced : true,
		selected : true
	} ];
	//var data=[["新顾客",1],["老顾客",1]];
	pieChartOptions.series[0].data=data;
	pieChart = new Highcharts.Chart(pieChartOptions);
}

function reDraw(data) {
	chart && chart.destroy();
	chart = null;
	var allList = data.allList;
	var newList = data.newList;
	var oldList = data.oldList;

	var allData = new Array();
	var allX = new Array();
	var newData = new Array();
	var newX = new Array();
	var oldData = new Array();
	var oldX = new Array();
	for ( var i = 0; i < allList.length; i++) {
		allData[i] = parseInt(allList[i].value.count);
		allX[i] = allList[i].id;
	}
	for ( var j = 0; j < newList.length; j++) {
		newData[j] = parseInt(newList[j].value.count);
		newX[j] = newList[j].id;
	}
	for ( var k = 0; k < oldList.length; k++) {
		oldData[k] = parseInt(oldList[k].value.count);
		oldX[k] = oldList[k].id;
	}

	options.series[0].data = (allData);
	options.series[1].data = (newData);
	options.series[2].data = (oldData);
	options.xAxis.categories = allX;
	chart = new Highcharts.Chart(options);
}
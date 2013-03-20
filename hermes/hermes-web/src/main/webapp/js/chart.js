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
		data : [ 0, 0, 0, 0, 0,0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 ]
	}, {
		name : '新顾客来电',
		data : [ 0, 0, 0, 0, 0,0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 ]
	}, {
		name : '老顾客来电',
		data : [  0, 0, 0, 0, 0,0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
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
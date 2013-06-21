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
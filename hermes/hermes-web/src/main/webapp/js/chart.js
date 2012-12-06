	var chart;
$(function() {
	$(document).ready(function() {
		chart = new Highcharts.Chart({
			chart : {
				renderTo : 'chartContainer',
				type : 'line',
				marginRight : 130,
				marginBottom : 25
			},
			title : {
				text : '旺财宝月度统计',
				x : -20
			// center
			},
			credits : {
				enabled : false
			// 不显示LOGO
			},
			subtitle : {
				text : '旺财宝月度统计',
				x : -20
			},
			xAxis : {
				minorTickInterval: 'auto',// 设置是否出现纵向小标尺
				labels: {rotation: -25,	align: 'right'},
				categories : [ '12-05', '12-06', '12-07', '12-08', '12-09', '12-10', '12-11', '12-12', '12-13', '12-14', '12-15', '12-16','12-17','12-18','12-19',
				               '12-20','12-21','12-22','12-23','12-24','12-25','12-26','12-27','12-28','12-29','12-30'
				               ]
			},
			yAxis : {
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
				data : [ 7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6,20,20,21,25,26,27,28,29,30,31,32,33,20,35 ]
			}, {
				name : '新顾客来电',
				data : [ -0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5,20,20,21,25,26,27,28,29,30,31,32,33,20,35 ]
			}, {
				name : '老顾客来电',
				data : [ -0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0,20,20,21,25,26,27,28,29,30,31,32,33,20,35 ]
			} ]
		});
	});

});


function reDraw(data){
	var n=[-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5,20,20,21,25,26,27,28,29,30,31,32,33,20,35];
	chart.series[0].setData(n);
	chart.series[1].setData(n);
	chart.series[2].setData(n);
}
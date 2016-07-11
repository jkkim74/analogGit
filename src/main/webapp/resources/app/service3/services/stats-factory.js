'use strict';

function NoNvd3PluginError(message) {
	this.prototype.name = 'NoNvd3PluginError';
	this.message = (message || 'The Nvd3 plugin for d3 was not found');
}

NoNvd3PluginError.prototype = new Error();

angular.module('statsFactory', [])
	.factory('statsSvc', function($http, $q) {
		if (!nv) {
			throw new NoNvd3PluginError();
		}

		// Public API
		return {// 도넛 차트(빌드 및 배포 결과 확률) 그리기.
			salesLineChart : function(data) {
				var key = "매출 추이";
				var sum = 0;
				var values = data.map(function(d, i) {
					//sum += d.value;
					return {x: d.key, y: d.value};
				});
                var color = ["#77c8a9", "#77c8a9" ];
				nv.addGraph(function() {  
					 var chart = nv.models.lineChart()
					 .margin({top: 30, right: 60, bottom: 75, left: 95})
                     .useInteractiveGuideline(true)
                     .showLegend(true)
					 .x(function(d) { return d.x; })
					 .y(function(d) { return d.y; })
					 //.color(d3.scale.category10().range())
                     .color(color)
					 .tooltipContent(function(key, x, y, e, graph) {
						 var tooltipContent = '<h3>' + key + '</h3>' + '<p><center><b>'+ y +'</b> at ' + x + '</center></p>';
			             return tooltipContent;
					 })
					 //.transitionDuration(300)
					 .clipVoronoi(false);
					  
					 chart.xAxis
					   .axisLabel('년월')
					   .rotateLabels(-20)
					   .tickFormat(function(d) {
						   return d3.time.format("%Y-%m")(new Date(d));
					   });
					 chart.yAxis
					   .axisLabel('매출 금액')
					   .tickFormat(d3.format('d'));
					
					d3.select('#sales_line > svg')
						.datum([{values: values, key: key}])
						.transition().duration(500)
						.call(chart);

					nv.utils.windowResize(chart.update);
					chart.dispatch.on('stateChange', function(e) { nv.log('New State:', JSON.stringify(e)); });
					return chart;
				});
			},
            getSumAmtByPaydate : function(startDate, endDate) {
                var uri = '/sales/sumAmt/payDate?startDate=' + startDate + '&endDate=' + endDate;
                return $http.get(uri).then(function(response) {
                    if (typeof response.data === 'object') {
                        return response.data;
                    } else {
                        // invalid response
                        return $q.reject(response.data);
                    }
                }, function(response) {
                    // something went wrong
                    return $q.reject(response.data);
                });
			}
		};
	}
);
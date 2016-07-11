var directives = angular.module('app.commonDirectives', []);
directives.config(
    function ($compileProvider) {
        directives.lazy = {
            directive: $compileProvider.directive
        };
    }
);

/**
 * directives > repeatDone
 * ng-repeat이 완료되었을 때 호출되는 함수를 정의할 수 있다.
 */
directives.directive('repeatDone', function ($timeout) {
    return function (scope, element, attrs) {
        if (scope.$last) { // all are rendered
            $timeout(function () {
                scope.$eval(attrs.repeatDone);
            });
        }
    };
});

/**
 * directives > jqGrid
 */
directives.directive('jqGrid', function () {
    return {
        restrict: 'E',
        scope: {
            config: '='
        },
        link: function (scope, element) {
            var table, pager, jqGridId;

            // config attribute 값의 변경을 확인하여 반영한다.
            scope.$watch('config', function (config) {
                jqGridId = config.id;
                if (!jqGridId) {
                    alert('cannot find jqGrid config ID!!');
                    return;
                }
                // 매장개별 초기 조건안들어갈 경우 빈화면 표시를 위해서 추가함.
                if (angular.isUndefined(config.colModel))
                    return;

                angular.element(element).children().remove();
                table = angular.element('<table id=\"' + jqGridId + '\"></table>');
                angular.element(element).append(table);

                if (config.pager) {
                    angular.element(config.pager).remove();
                    pager = angular.element('<div id=\"' + config.pager.substring(1) + '\" ></div>');
                    angular.element(element).append(pager);

                    $(table).jqGrid(config).navGrid(config.pager,
                        {add: false, edit: false, del: false, search: false, refresh: false});
                } else {
                    $(table).jqGrid(config);
                }
            });
            scope.$on('$destroy', function() {
                angular.element(element).children().remove();
            });
        }
    };
});


/**
 * directives > fixedHeader
 */
directives.directive('fixedHeader', function () {
    return {
        restrict: 'E',
        templateUrl: "page/templates/fixedTableHeader.tpl.html?_=" + DateHelper.timestamp(),
        scope: false,
        link: function (scope, element, attrs) {
            scope.id = attrs.id;
        }
    };
});

/**
 * directives > dynaTree
 */
directives.directive('dynaTree', function () {
    return {
        restrict: 'E',
        scope: {
            config: '='
        },
        link: function (scope, element) {
            var div, dynaTreeId;

            // config attribute 값의 변경을 확인하여 반영한다.
            scope.$watch('config', function (config) {
                dynaTreeId = config.id;
                if (!dynaTreeId) {
                    alert('cannot find dynaTreeId config ID!!');
                    return;
                }
                if (!config.height) {
                    config.height = 305;
                }
                if (element.children() != null)
                    element.children().remove();

                div = angular.element('<div id=\"' + dynaTreeId + '\" style=\"height:' + config.height + 'px\" class=\"overflow_wrap\"></div>');
                element.append(div);

                $(div).dynatree(config);
            });
            scope.$on('$destroy', function() {
                angular.element(element).children().remove();
            });
        }
    };
});

/**
 * directives > number format
 */
directives.directive('numberFormat', function ($filter, $browser) {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function ($scope, $element, $attrs, $ngModel) {
            var toNumberFormat = function (text) {
                text += '';
                var value = text.replace(/,/g, '');
                return $filter('number')(value, false);
            };

            $ngModel.$formatters.push(toNumberFormat);
            var listener = function () {
                var value = $element.val().replace(/,/g, '');
                $element.val($filter('number')(value, false));
            };

            // This runs when we update the text field
            $ngModel.$parsers.push(function (viewValue) {
                return viewValue.replace(/,/g, '');
            });

            // This runs when the model gets updated on the scope directly and keeps our view in sync
            $ngModel.$render = function () {
                $element.val($filter('number')($ngModel.$viewValue, false));
            };

            $element.bind('change', listener);
            $element.bind('keydown', function (event) {
                var key = event.keyCode;
                // If the keys include the CTRL, SHIFT, ALT, or META keys, or the arrow keys, do nothing.
                // This lets us support copy and paste too
                if (key == 91 || (15 < key && key < 19) || (37 <= key && key <= 40))
                    return;
                $browser.defer(listener); // Have to do this or changes don't get picked up properly
            });

            $element.bind('paste cut', function () {
                $browser.defer(listener);
            });

            $scope.$on('$destroy', function() {
                $element.off(); // deregister all event handlers
                $element.unbind();
            });
        }
    };
});

/**
 * directives > resize
 *  windows size 변경시 자동 리사이즈
 */
directives.directive('resize', function ($window, $timeout) {
    var MIN_STANDARD_WIDTH = 768;
    var MIN_STANDARD_WIDTH_PADDING = 35;
    var FOR_LOOK_GOOD_WIDTH = 322;
    var HIGHCHART_DEFAULT_HEIGHT = 236;
    var SIDE_BAR_DEFAULT_WIDTH = 250;
    var MSTR_DEFAULT_HEIGHT = 600;

    function resizePivotTableWidth(pivotTableId, curWidth){
        var sideBarWidth = $('#sidebar').hasClass('slide_hide') ? SIDE_BAR_DEFAULT_WIDTH : 0;
        var pivotTableWidth = $('#' + pivotTableId + ' .pvtTable').width();
        var newWidth = curWidth + sideBarWidth - FOR_LOOK_GOOD_WIDTH;

        if (pivotTableWidth < newWidth) {
            pivotTableWidth = newWidth;
            $('#'+pivotTableId+' .pvtRendererArea .pvtTable').width(pivotTableWidth+'px');
            $('#'+pivotTableId+'_h .fixedTableHeader').width(pivotTableWidth+'px');
        }
    }

    function resizeOlapPivotTableWidth(olapPivotTableId, curWidth){
        var sideBarWidth = $('#sidebar').hasClass('slide_hide') ? SIDE_BAR_DEFAULT_WIDTH : 0;
        var olapPivotTableWidth = $('#' + olapPivotTableId + ' .multi-header-pivot').width();
        var newWidth = curWidth + sideBarWidth - FOR_LOOK_GOOD_WIDTH;

        if (olapPivotTableWidth < newWidth) {
            olapPivotTableWidth = newWidth;
            $('#' + olapPivotTableId + ' .multi-header-pivot').width(olapPivotTableWidth + 'px');
            $('#' + olapPivotTableId + 'FixedHeader').width(olapPivotTableWidth + 'px');
        }
    }

    function resizeHighChartWidth(pivotTableId, curWidth){
        var highChartId = '#' + pivotTableId + 'Container';
        var highChart = $(highChartId).highcharts();
        if(!highChart){
            return;
        }

        var sideBarWidth = $('#sidebar').hasClass('slide_hide') ? SIDE_BAR_DEFAULT_WIDTH : 0;
        var newWidth = curWidth + sideBarWidth - MIN_STANDARD_WIDTH_PADDING;
        if (curWidth >= MIN_STANDARD_WIDTH) {
            newWidth = curWidth + sideBarWidth - FOR_LOOK_GOOD_WIDTH;
        }
        //If drag the window, full chart view is off.
        if ($(highChartId).hasClass('highChartModal')) {
            $(highChartId).removeClass('highChartModal');
        }
        highChart.setSize(newWidth, 250, true);
        highChart.reflow();
    }

    function resizeFunnelWidth(funnelId, curWidth) {
        if (!angular.element("#" + funnelId)) {
            return;
        }

        var winWidth = curWidth /*angular.element(window).width()*/;
        var sidebarWidth = angular.element("#sidebar").hasClass("slide_hide") ? 0 : angular.element("#sidebar").width();
        var newWidth = winWidth - sidebarWidth - 85;
        var minChildWidth = 200;
        var maxChildCount = Math.max.apply(null, angular.element("#" + funnelId + " .floor").map(function() {
            return angular.element(this).find(".entry, .step, .exit").length;
        }).get());

        if (maxChildCount > 0) {
            var newChildWidth = Math.floor(newWidth / maxChildCount) - 10;
            angular.element("#" + funnelId + " .funnel").find(".entry, .step, .exit").css("width", (newChildWidth < minChildWidth ? minChildWidth : newChildWidth));
            angular.element("#" + funnelId + " .funnel").find(".entry .detail .key, .exit .detail .key").css("width", (newChildWidth < minChildWidth ? minChildWidth : newChildWidth) - 52);
            angular.element("#" + funnelId + " .funnel").find(".entry .detail .value, .exit .detail .value").css("width", 50);

            var funnelWidth = maxChildCount * (angular.element("#" + funnelId + " .entry").width() + 10);
            if (newWidth > funnelWidth) {
                funnelWidth = newWidth;
            }
            angular.element("#" + funnelId + " .funnel").css("width", funnelWidth);
        }
    }

    function resizeMstr(mstrId, curWidth, curHeight) {
        if (!angular.element("#" + mstrId)) {
            return;
        }
        var sidebarWidth = angular.element("#sidebar").hasClass("slide_hide") ? 0 : angular.element("#sidebar").width();
        var newWidth = curWidth - sidebarWidth - 85 + 40;
        var searchBoxHeight = 93;

        angular.forEach(angular.element("mstr-search-box > div"), function(child) {
            child = angular.element(child);
            searchBoxHeight += child.height();
        });
        var newHeight = angular.element(".content-wrap").height() - searchBoxHeight;
        if (newHeight < MSTR_DEFAULT_HEIGHT)
            newHeight = MSTR_DEFAULT_HEIGHT;
        angular.forEach(angular.element("mstr-report > iframe"), function(child) {
            angular.element(child).attr({
                'width': newWidth - 50,
                'height': newHeight - 15
            }).css({
                'margin-left': '30px'
            });
        });

        return {
            'width': newWidth,
            'height': newHeight + 'px',
            'background-color': '#fff',
            'margin-left': '-25px',
            'margin-bottom': '15px'
        };
    }

    return {
        link: function (scope) {
            var w = angular.element($window);
            scope.getWindowDimensions = function () {
                return {
                    'h': w.height(),
                    'w': w.width()
                };
            };

            scope.$watch(scope.getWindowDimensions, function (newValue) {
                scope.windowHeight = newValue.h;
                scope.windowWidth = newValue.w;
                scope.dataAreaHeight = newValue.h - 400;

                //resize pivot table's width
                if (scope.pivotTableId) {
                    resizePivotTableWidth(scope.pivotTableId, newValue.w);
                }

                //resize OLAP-DYNAMIC-PIVOT table's width
                if (scope.olapPivotTableId) {
                    resizeOlapPivotTableWidth(scope.olapPivotTableId, newValue.w);
                }

                //resize highChart's width
                if(scope.highChartConfig){
                    resizeHighChartWidth(scope.highChartConfig.highChartId, newValue.w);
                }

                //resize funnel's width
                if(scope.funnelId) {
                    resizeFunnelWidth(scope.funnelId, newValue.w);
                }

                //resize MicroStrategy
                if(scope.mstrId) {
                    resizeMstr(scope.mstrId, newValue.w, newValue.h);
                }

                // jqGrid 라사이즈
                function reGrid(widthSize, heightSize) {
                    var SHOW_SCROLL_AREA = 10;  //pc에 따라 jqGrid scroll이 사라짐. 17이 full-size. 보이는 영역 조절.
                    var tableId = $('#' + scope.tableId);
                    if (tableId) {
                        var gridWidth = widthSize - 350 - SHOW_SCROLL_AREA;
                        if (newValue.w < 760) {
                            gridWidth += 320;
                        }
                        //todo if side-bar hide
                        if (angular.element('#sidebar').hasClass('slide_hide')) {
                            gridWidth += 250;
                        }

                        if (scope.highChartConfig) {
                            heightSize = heightSize - HIGHCHART_DEFAULT_HEIGHT;
                        }
                        tableId.setGridWidth(gridWidth + 28);   //add 28 for to fix grid table's width like highChart one.
                        tableId.setGridHeight(heightSize - 470);
                    }
                }

                scope.style = function (diff) {
                    var result = (newValue.h - 190);
                    reGrid(newValue.w, (newValue.h - diff));
                    return {
                        'height': result + 'px'
                    };
                };
                scope.styleNoTab = function (diff) {
                    reGrid(newValue.w, (newValue.h - diff));
                    return {
                        //'height': (newValue.h - (155 + diff)) + 'px'
                        'height': (newValue.h - 155) + 'px'
                    };
                };
                scope.dataAreaStyle = function (diff) {
                    if (angular.isUndefined(diff)) {
                        diff = 0;
                    }

                    if (diff === 'summary') {
                        return {
                            'height': (newValue.h - 435) + 'px'
                        };
                    }

                    //resize MicroStrategy
                    if (diff === 'mstr') {
                        return resizeMstr(scope.mstrId, newValue.w, newValue.h);
                    }

                    //for highChart
                    if (scope.highChartConfig) {
                        diff = diff + HIGHCHART_DEFAULT_HEIGHT;
                    }

                    return {
                        'height': (newValue.h - (400 + diff)) + 'px'
                    };
                };

                scope.dataAreaEasyOlapStyle = function () {
                    var containerHeight, searchBoxHeight, nonSubTitleHeight, dataAreaMarginTopHeight;
                    // 상단 검색, 하단 데이터 영역을 포함한 영역의 height를 구한다.
                    containerHeight = parseInt($('.report-search-box:visible').parent().height());
                    // 상단 검색 영역의 height를 구한다.
                    searchBoxHeight = parseInt($('.report-search-box:visible').height());
                    // 상단 검색 영역과 하단 데이터 영역을 구분하는 구분자의 height + margin-top 를 구한다.
                    nonSubTitleHeight = parseInt($('.non-sub-title:visible').height()) + parseInt($('.non-sub-title:visible').css('margin-top'));
                    // 하단 데이터 영역의
                    dataAreaMarginTopHeight = parseInt($('.data-area:visible').css('margin-top'));
                    return {
                        'height': containerHeight - searchBoxHeight - nonSubTitleHeight - dataAreaMarginTopHeight + 'px'
                    };
                };

                scope.dataAreaStyleNoTab = function (diff) {
                    return {
                        'height': (newValue.h - (250 + diff)) + 'px'
                    };
                };
                var niceScroll = angular.element('.nicescroll-rails');
                var leftNavigation = angular.element('.leftside-navigation');
                var bookmarkArea = angular.element('.bookmark-area');
                //css변경에 따른 height 조절, 가로 768이상일 경우만, carousel width 100% 지정.
                angular.element('.rn-carousel-container').css('width', '100%');
                if (newValue.w >= 768) {
                    angular.element('.container-wrap').css('height', (newValue.h - 105));
                    angular.element('#sidebar.nav-collapse').removeClass('hide-left-bar');  //20141113
                    var mainContainer = angular.element('.main-container');
                    if (mainContainer) {
                        mainContainer.css('height', (newValue.h - 105));   //main
                    }
                    $timeout(function () {
                        if (leftNavigation.length && bookmarkArea.length) {
                            leftNavigation.css('height', (newValue.h - 143));// 143 -> 93 DSS즐겨찾기부분 공백제거.
                            niceScroll.eq(0).css('bottom', '50px');
                        }
                    }, 100);
                } else {
                    $timeout(function () {
                        if (leftNavigation.length && bookmarkArea.length) {
                            leftNavigation.css('height', (newValue.h));
                            niceScroll.eq(0).css('bottom', '0px');
                        }
                    }, 100);
                }
            }, true);

            w.unbind('resize').bind('resize', function () {
                scope.$safeApply();
            });

            //iPad 가로세로 방향 event처리
            w.unbind('orientationchange').bind('orientationchange', function () {
                angular.element('.container-wrap').css('height', (angular.element($window).height() - 105));
                var mainContainer = angular.element('.main-container');
                if (mainContainer.length) {
                    mainContainer.css('height', (angular.element($window).height() - 105));
                }
            });

            scope.getSideBarState = function(){
                var isSideBarHide= $('#sidebar').hasClass('slide_hide');
                return isSideBarHide;
            };

            scope.$watch(scope.getSideBarState, function(newValue){
                var curWidth = angular.element($window).width();
                //resize pivot table's width
                if (scope.pivotTableId) {
                    resizePivotTableWidth(scope.pivotTableId, curWidth );
                }

                //resize OLAP-DYNAMIC-PIVOT table's width
                if (scope.olapPivotTableId) {
                    resizeOlapPivotTableWidth(scope.olapPivotTableId, curWidth);
                }

                //resize highChart's width
                if(scope.highChartConfig){
                    resizeHighChartWidth(scope.highChartConfig.highChartId, curWidth);
                }

                //resize funnel's width
                if (scope.funnelId) {
                    resizeFunnelWidth(scope.funnelId, curWidth);
                }

                //resize MicroStrategy
                if(scope.mstrId) {
                    resizeMstr(scope.mstrId, newValue.w, newValue.h);
                }
            });
        }
    };
});

directives.directive('onFinishRender', function ($timeout) {
    return {
        restrict: 'A',
        link: function (scope) {
            if (scope.$last === true) {
                $timeout(function () {
                    scope.$emit('ngRepeatFinished');
                });
            }
        }
    };
});

/**
 * directives > highChart
 */
directives.directive('highChart', function ($log) {
    //function getStartDate(rawData){
    //    var startDate = _.first(_.sortBy(_.keys(_.groupBy(rawData,'stdDt'))));
    //    return {
    //        yyyy: startDate.substr(0, 4) * 1,
    //        mm: startDate.substr(5, 2) * 1,
    //        dd: startDate.substr(8, 2) * 1
    //    };
    //}
    //
    //function setXAxisFormat(dateType) {
    //    return {
    //        type: 'datetime',
    //        labels: {
    //            format: dateType == 'month'?'{value:%Y-%m}':'{value:%Y-%m-%d}',
    //            rotation: -30,
    //            align: 'right'
    //        }
    //    };
    //}
    //
    //function setXAxisFormatInterval(dateType, startDate) {
    //    //set the default : 'day'
    //    var intervalConf = {
    //        pointStart: Date.UTC(startDate.yyyy, startDate.mm - 1, startDate.dd),
    //        pointInterval: 24 * 36e5
    //    };
    //    switch(dateType) {
    //        case 'week':
    //            intervalConf.pointInterval = 7 * 24 * 36e5;
    //            break;
    //        case 'month':
    //            intervalConf.pointStart = Date.UTC(startDate.yyyy, startDate.mm - 1, 1);
    //            intervalConf.pointInterval = 31 * 24 * 36e5;
    //            break;
    //    }
    //    return intervalConf;
    //}

    function setTooltipFormat(){
        return {
            //backgroundColor: '#F0F0F0',
            //pointFormat: '<b>{point.y:,.0f}</b><br/>',
            //pointFormat: '{series.name}: <b>{point.y:,.0f}</b><br/>',
            //xDateFormat: dateType == 'month' ? '%Y-%m' : '%Y-%m-%d',  //'%Y-%m-%d',
            shared: true
        };
    }

    function extractSeriesFromPivot(extraOption, rawData){
        return _.map(
            _.groupBy(rawData, function (item) {
                var customOrder = (extraOption.customOrder === undefined) ? 0 : extraOption.customOrder;
                return item.measure.substring(customOrder);   //substring to remove XX numbering index. :)
            }),
            function (value, key) {
                var sortedData = _.map(
                    _.sortBy(
                        _.map(value, function (value) {
                            return {date: value.stdDt, value: value.measureValue};
                        }), 'date'),
                    function (value) {
                        return value.value;
                    });
                return {name: key, data: sortedData};
            }
        );
    }

    function extractSeriesFromGrid(extraOption, rawData){
        var gridColsId = extraOption.gridColsInfo.colId;
        var gridColsName = extraOption.gridColsInfo.colName;

        return _.map(gridColsId, function (element, index) {
            var colsValList = _.map(rawData, function(value){
                return _.values(_.pick(value, element))[0];
            });
            //stdDt data set need not to draw highChart. It's just date list. so return falsy value like undefined.
            if(element == 'stdDt'){
                return;
            }
            return {name: gridColsName[index], data: colsValList};
        });
    }

    function setXAxisCategoryFromPivot(extraOption, rawData) {
        var category = _.map(
            _.values(
                _.groupBy(rawData, function (item) {
                    var customOrder = (extraOption.customOrder === undefined) ? 0 : extraOption.customOrder;
                    return item.measure.substring(customOrder);   //substring to remove XX numbering index. :)
                })
            )[0], function (value) {
                return value.stdDt;
            });

        return {
            categories: category
        };
    }

    function setXAxisCategoryFromGrid(rawData) {
        var category = _.map(rawData, function(value){
            return _.values(_.pick(value, 'stdDt'))[0];
        });
        return {
            categories: category
        };
    }

    function makeChartDataSet(highChartId, extraOption, rawData) {
        var seriesData,
            xAxisCategory,
            tableType = (extraOption.tableType === undefined) ? 'pivot' : extraOption.tableType;

        switch(tableType) {
            case 'pivot':
                seriesData = extractSeriesFromPivot(extraOption, rawData);
                xAxisCategory = setXAxisCategoryFromPivot(extraOption, rawData);
                break;
            case 'grid':
                seriesData = _.compact(extractSeriesFromGrid(extraOption, rawData));    //use compact for remove falsy value(e.g. stdDt)
                xAxisCategory = setXAxisCategoryFromGrid(rawData);
                break;
            default :
                $log.error('Check the highChartConfig\'s tableType option.(pivot or grid)');
                return;
        }
        return {
            tooltip: setTooltipFormat(extraOption.dateType),
            chart: {
                //renderTo : '',
                type: 'spline',
                marginRight: (extraOption.legendArea === undefined) ? 200 : extraOption.legendArea,
                events: {
                    load: function (event) {
                        //console.log('called chart event~~');
                    },
                    redraw: function () {
                        //var label = this.renderer.label('The chart was just redrawn', 100, 120)
                        //    .attr({
                        //        fill: Highcharts.getOptions().colors[0],
                        //        padding: 10,
                        //        r: 5,
                        //        zIndex: 8
                        //    })
                        //    .css({
                        //        color: '#FFFFFF'
                        //    })
                        //    .add();
                        //setTimeout(function () {
                        //    label.fadeOut();
                        //}, 1000);
                    }
                }
            },
            credits: {
                enabled: false,
                href:'https://voyager.skplanet.com',
                text : 'https://voyager.skplanet.com'
            },
            legend: {
                align: 'right',
                verticalAlign: 'top',
                layout: 'vertical'
            },
            title: {
                text: null
            },
            //xAxis: setXAxisFormat(extraOption.dateType),
            xAxis: xAxisCategory,

            series: seriesData,
            plotOptions: {
                //series: setXAxisFormatInterval(extraOption.dateType, pointStartDate)
            },
            exporting: {
                buttons: {
                    contextButton: {
                        menuItems: [{
                            text: '이미지 저장',
                            onclick: function () {
                                var highChartContainer = $('#' + highChartId + ' .highcharts-container');
                                var exportImageWidth = highChartContainer.width();
                                var exportImageHeight = highChartContainer.height();
                                //$log.debug('export Image width / height : ' + exportImageWidth + ' / ' + exportImageHeight);

                                this.exportChart({
                                        sourceWidth: exportImageWidth,
                                        sourceHeight: exportImageHeight,
                                        filename : 'ChartImage_' + highChartId
                                    });
                            },
                            separator: false
                        }, {
                            text: '차트바꿔보기(라인<->막대)',
                            onclick: function () {
                                var tmpType = this.userOptions.chart.type == 'column' ? 'spline' : 'column';
                                this.userOptions.chart.type == 'column' ? this.userOptions.chart.type = 'spline' : this.userOptions.chart.type = 'column';
                                $(this.series).each(function () {
                                    this.update({
                                        type: tmpType
                                    }, true);
                                });
                            }
                        },{
                            text: '전체화면',
                            onclick: function () {
                                if ($(this.renderTo).hasClass('highChartModal')) {
                                    this.setSize($(window).width() - 322, 250, true);
                                    $(this.renderTo).toggleClass('highChartModal');
                                } else {
                                    this.setSize($(window).width(), $(window).height(), true);
                                    $(this.renderTo).toggleClass('highChartModal');
                                }
                            }
                        },{
                            text: '라벨보기',
                            onclick: function () {
                                var isLabel = true;
                                $(this.renderTo).hasClass('showLabel') ? isLabel = false : null;
                                $(this.renderTo).toggleClass('showLabel');
                                $(this.series).each(function () {
                                    this.update({
                                        dataLabels: {
                                            enabled: isLabel
                                        }
                                    }, true);
                                });
                            }
                        }]
                    }
                }
            }
        };
    }

    return {
        restrict: 'E',
        scope: {
            config: '='
        },
        link: function (scope, element) {
            var chartArea, highChart;

            scope.$watch('config', function (config) {
                if (angular.isUndefined(config)) {
                    $log.debug('\n** highChart config is not loaded!!!');
                    return;
                }

                var highChartId = config.highChartId + 'Container';
                var chartData = makeChartDataSet(highChartId, config.extraOption, config.rawData);

                //set default chart height, if you need to-do refactoring.:)
                $('#'+highChartId).height('250px');

                //draw container
                chartArea = angular.element(element);
                chartArea.children().remove();
                highChart = angular.element('<div class=\"chart-area\" id=\"' + highChartId + '\"></div>');
                chartArea.append(highChart);

                //draw highchart
                $(highChart).highcharts(
                    chartData,
                    function () { //callback
                        $log.debug('- callback message : complete draw chart!!!');
                    });
            });
            scope.$on('$destroy', function() {
                angular.element(element).children().remove();
            });
        }
    };
});

/**
 * directives > funnelChart
 */
directives.directive('funnelChart', function () {
    return{
        restrict: 'E',
        templateUrl: "page/templates/funnels.tpl.html?_=" + DateHelper.timestamp(),
        scope: false,
        link: function (scope, element) {
            //scope.funnelId = attrs.funnelId;
            scope.$on('$destroy', function() {
                angular.element(element).children().remove();
            });
        }
    };
});

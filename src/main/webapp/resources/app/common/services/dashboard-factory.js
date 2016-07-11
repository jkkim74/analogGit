var factories = angular.module('app.commonFactory');

/**
 *  factories > dashboard Service
 */
(factories.lazy || factories).factory('bmDashboardSvc', function ($http, menuSvc, ngDialog,userSvc,urlHandleSvc) {
    return {
        /**
         * url을 정보를 이용하여 menuItem을 생성한다..
         * @returns {{service: *, category: *, menu: *, resourceUrl: string}}
         */
        urlToMenuItem: function () {
            var menuUrl = urlHandleSvc.getMenuUrl();
            return this.getDashboardByMenuCode(menuUrl);
        },

         /**
         * 새로운 menuItem 을 생성한다.
         * @param service
         * @param category
         * @param menu
         * @returns {{service: *, category: *, menu: *, resourceUrl: string}}
         */
        newMenuItem: function (service, category, menu) {
            var resourceUri = service.code + '/' + category.code;
            if (menu) {
                resourceUri += '/' + menu.code;
            }
            var templateUrl = 'page/' + resourceUri + '.html';
            return {
                service: service,
                category: category,
                menu: menu,
                templateUrl: templateUrl
            };
        },

        /**
         * menuCode에 대한 BM 대시보드를 조회한다.
         * @param menuUrl
         * @returns {{service: *, category: *, menu: {}}}
         */
        getDashboardByMenuCode: function (menuUrl) {
            var serviceItem;
            var categoryItem;
            var menuItem;
            serviceItem = menuSvc.getDashboardService();
            categoryItem = _.find(serviceItem.categories, function(obj) {
                return (menuUrl.categoryCode == obj.code);
            });
            if (!categoryItem) {
                return null;
            }
            menuItem = _.find(categoryItem.menus, function(obj) {
                return (menuUrl.menuCode == obj.code);
            });
            if (!menuItem) {
                return null;
            }
            return this.newMenuItem(serviceItem, categoryItem, menuItem);
        },

        /**
         * BM별 대시보드 시작/종료일 기본값
         * @param dateType (30, 90, 180)
         * @param diff 조회날짜 세팅. (diff만큼 이전날짜로 계산)  (voyop-338, cookatrice)
         * @returns {{startDate: *, endDate: *}}
         */
        getDashboardDefaultCal: function (dateType, diff) {
            //기준일이 오늘기준 diff만큼 이전으로 따라서 diffdate도 dateType에 (diffValue-1)을 더한만큼 변경해야 함
            var diffValue = (diff=== undefined) ? 1 : diff;
            var endDate = DateHelper.getPreviousDayDate(new Date(), diffValue);
            var diffDate = DateHelper.getDailyDiffMs(new Date(), (dateType + (diffValue-1)));
            var startDate = new Date(diffDate);
            return {
                'startDate': moment(startDate).format('YYYYMMDD'),
                'endDate': moment(endDate).format('YYYYMMDD')
            };
        },

        /**
         * 오늘날짜 데이터 값 구하기
         * @param rawData
         * @param searchDate
         * @returns {*}
         */
        searchCurrentData: function (rawData, searchDate) {
            for (var i = 0, length = rawData.length; i < length; i++) {
                if (rawData[i].dlyStrdDt == searchDate) {
                    return (rawData[i].dlyRsltVal).numberFormat();
                }
            }
            return "-";
        },

        /**
         * chart용 데이터 셋 (날짜, 값) 만들기
         * @param rawData
         * @returns {Array}
         */
        makeChartData: function (rawData) {
           return _.map(rawData, function (value) {
                return ([value.dlyStrdDt, value.dlyRsltVal]);
            });
        },

        /**
         * sparkline LINE chart 용 데이터 구분
         * @param rawData
         * @returns {Array}
         */
        separateChartDrawValue: function (rawData) {
            return _.map(rawData, function (value) {
                return value[1];
            });
        },

        /**
         * trendline chart 데이터 셋 만들기
         * @param chartData
         * @param searchPeriod
         * @returns {Array}
         */
        makeTrendlineChartData: function (chartData) {
            var chartDataLength = chartData.length;
            var dv = chartDataLength * (chartDataLength + 1) * (chartDataLength - 1) / 12;
            var mean_d = (chartDataLength + 1) / 2;
            var mean_x = 0;
            var dvd = 0;

            for (var i = 0; i < chartDataLength; i++) {
                dvd = dvd + (((-1) * mean_d) + (i + 1)) * chartData[i];
                mean_x = mean_x + chartData[i] / chartDataLength;
            }

            var slp = dvd / dv;

            if ($.isNumeric(slp)) {
                this.chartTrandlineslp = slp.numberFormat(0);   //소숫점 설정
            } else {
                this.chartTrandlineslp = '-';
            }

            var itrcpt = mean_x - mean_d * slp;

            var reg = [];

            //소숫점 처리...
            var max_of_array = Math.max.apply(Math, chartData);
            var min_of_array = Math.min.apply(Math, chartData);
            var diff_min_max = max_of_array - min_of_array;

            for (i = 0; i < chartDataLength; i++) {
                //소숫점 처리...
                if (diff_min_max > 30) {
                    reg.push(Math.round(itrcpt + (i + 1) * slp)); //소숫점 없음
                } else {
                    reg.push(Math.round((itrcpt + (i + 1) * slp) * 100) / 100); //소숫점2째자리
                }
            }
            return reg;
        },

        /**
         * EWMA 수치데이터 만들기
         * @param data
         */
        makeEWMAChartData: function (rawData) {
            var side = "right";
            var calcResult = 0;
            var barColor = "bar";
            var overRef = "N";
            var icon = "ico_mid.png";
            var decline = {width: '0%'};
            var grow = {width: '0%'};

            if (rawData.length > 0 && rawData[0] != null) {
                var tmp = rawData[0];
                var refValue = 64;
                var CL = tmp.ewmaAvg;
                var value = tmp.ewmaValue;
                var LCL = tmp.lclValue;
                var UCL = tmp.uclValue;

                if (value > CL) {
                    side = "right";
                    calcResult = ((value - CL) * refValue) / (UCL - CL);
                    if (calcResult > refValue) {
                        barColor = "bar bar-grow";
                        overRef = "Y";
                        icon = "ico_grow.png";
                    }
                } else { //value < CL
                    side = "left";
                    calcResult = ((CL - value) * refValue) / (CL - LCL);
                    if (calcResult > refValue) {
                        barColor = "bar bar-decline";
                        overRef = "Y";
                        icon = "ico_decline.png";
                    }
                }

                //100이상일 경우 100으로 세팅
                if (calcResult > 100) {
                    calcResult = 100;
                }

                if (side == 'left') {
                    //decline
                    decline = {width: calcResult + '%'};
                } else {
                    //grow
                    grow = {width: calcResult + '%'};
                }

            }
            return {side: side, val: calcResult, barColor: barColor, overRef: overRef, icon: icon, decline: decline, grow: grow};
        },

        /**
         * 도움말 dialog
         * @param type
         *
         * TYPE : sparkline | ewma
         * dashboard_help_TYPE.png
         */
        openInfoDialog: function (type) {
            ngDialog.open({
                template: '<img src="resources/images/dashboard_help_'+type+'.png" alt="도움말입니다. 참고하세요." />',
                plain: true
            });
        }
    };
});

/**
 *  factories > ocDashboard Service
 */
(factories.lazy || factories).factory('ocDashboardSvc', function () {
    return {
        /**
         * OC 대시보드 시작/종료일 기본값
         */
        getDashboardDefaultCal: function (dateType) {
            var today = new Date();
            var diff = today.getDay() == 0 ? 7 : today.getDay();

            return {
                'startDate':DateHelper.getPreviousDate(diff+372,'YYYYMMDD'),
                'endDate': DateHelper.getPreviousDate(diff,'YYYYMMDD')
            };
        },
        /**
         * 해당 주 데이터 값 구하기
         * @param rawData
         * @param curWeek
         * @returns {string}
         */
        searchCurrentDataWeek: function (rawData, curWeek) {
            var returnValue = '-';

            _.each(rawData, function (element) {
                if (element.dlyStrdDt == curWeek) {
                    returnValue = (element.dlyRsltVal).numberFormat();
                }
            });

            return returnValue;
        },
        /**
         * binding date picker
         */
        bindingDatePicker: function (dateType) {
            var tmpEndDate = DateHelper.getPreviousDate(1, 'YYYY.MM.DD');
            if (dateType == 'week') {
                tmpEndDate = DateHelper.getPastSundayDate();
            }

            $('.input-append.date').datepicker({
                format: 'yyyy.mm.dd',
                autoclose: true,
                weekStart: 1,
                endDate: tmpEndDate
            });
        },
        /**
         * 누적 건수의 데이터를 이전날짜와 비교하여 일별로 변경하는 값 구하기
         * @param dataSet   전체 데이터
         * @param baseItem  기준되는 데이터
         * @returns {number} 오늘 값에 어제 값을 뺀 데이터
         */
        getConvertDailyData: function( dataSet, baseItem ) {
            var yesterday = moment(baseItem.dlyStrdDt, 'YYYYMMDD').subtract(1, 'days').format('YYYYMMDD');
            var items = _.select( dataSet, function(value) {
                return value.dlyStrdDt >= yesterday && value.dlyStrdDt <= baseItem.dlyStrdDt;
            });
            if ( items.length == 2 ) {
                var retValue = (items[0].dlyStrdDt == baseItem.dlyStrdDt) ?
                    baseItem.dlyRsltVal - items[1].dlyRsltVal : baseItem.dlyRsltVal - items[0].dlyRsltVal;
                if ( retValue > 0 ) {
                    return retValue;
                }
            }
            return 0;
        },
        /**
         * 해당 날짜의 이동평균 7일 데이터 값 구하기
         * @param dataSet   전체 데이터
         * @param baseItem  기준되는 데이터
         * @returns {number} 7일 평균 값
         */
        getAverage7DayData: function( dataSet, baseItem ) {
            var bef7DateStr = moment(baseItem.dlyStrdDt, 'YYYYMMDD').subtract(7, 'days').format('YYYYMMDD');

            var bef7dayItems = _.select( dataSet, function(value) {
                return value.dlyStrdDt > bef7DateStr && value.dlyStrdDt <= baseItem.dlyStrdDt;
            });
            // 7일간 데이터 중 데이터가 없더라도 무시 될 수 있게 적용(데이터 보정)
            var items = _.map( bef7dayItems, function( value ) {
                return value.dlyRsltVal;
            });
            return parseInt((_.reduce(items, function(sum, num){ return sum + num; }, 0)) / (items.length || 1));
        },
        /**
         * 해당 날짜에서 이동평균 30일 데이터 값 구하기
         * @param dataSet
         * @param baseDateItem
         * @returns {number} 30일 평균 값
         */
        getAverage30DayData: function( dataSet, baseDateItem ) {
            var diffDateStr = baseDateItem.dlyStrdDt;
            var prevMonthDateStr = DateHelper.getPreviousMonthFromDate( baseDateItem.dlyStrdDt, 1, 'YYYYMMDD' );

            var prevMonthItems = _.select( dataSet, function(value) {
                return value.dlyStrdDt > prevMonthDateStr && value.dlyStrdDt <= diffDateStr;
            });

            // 30일간 데이터 중 데이터가 없더라도 무시 될 수 있게 적용(데이터 보정)
            var items = _.map( prevMonthItems, function( value ) {
                return value.dlyRsltVal;
            });
            var avg = parseInt((_.reduce(items, function(sum, num){ return sum + num; }, 0)) / (items.length || 1));
            //console.log( diffDateStr + ' : ' + _.first(prevMonthItems).dlyStrdDt + '~' + _.last(prevMonthItems).dlyStrdDt + ' : LEN ' + prevMonthItems.length + ' : CUR ' + baseDateItem.dlyRsltVal + ' : AVG ' + avg );
            return avg > 0 ? avg : 0;
        }
   };
});


/**
 *  factories > Dashboard Report Service
 */
(factories.lazy || factories).factory('dashboardReportSvc', function ($http) {
    /**
     * 날짜 컬럼 레이블 반환.
     * @param textLabel
     * @param dateObject
     * @param dateType
     * @returns {string}
     */
    function getDateColumnLabel(textLabel, dateObject, dateType) {
        var dateLabel;

        if (dateType == 'week') {
            var weekRange = DateHelper.getWeekendRange(dateObject);
            dateLabel = [weekRange.mondayYmd, '~', weekRange.sundayYmd].join('');
        } else if (dateType == 'month') {
            dateLabel = DateHelper.dateObjectToYyyymmdd(dateObject, '.').substring(0, 7);
        } else {
            dateLabel = DateHelper.dateObjectToYymmdd(dateObject);
        }

        return [textLabel, '<br /><span class="text-normal">(', dateLabel, ')</span>'].join('');
    }

    /**
     * 추이 리포트 레이블 반환
     * @param textLabel
     * @param startDate
     * @param endDate
     * @param dateType
     * @returns {string}
     */
    //function getTrendPeriodColumnLabel(textLabel, startDate, endDate, dateType) {
    //    var startDateLabel, endDateLabel;
    //
    //    if (dateType == 'week') {
    //        startDateLabel = DateHelper.getWeekendRange(startDate).mondayYmd; // 시작일자의 월요일
    //        endDateLabel = DateHelper.getWeekendRange(endDate).sundayYmd; // 기준일의 일요일
    //    } else if (dateType == 'month') {
    //        startDateLabel = DateHelper.dateObjectToYyyymmdd(startDate, '.').substring(0, 7);
    //        endDateLabel = DateHelper.dateObjectToYyyymmdd(endDate, '.').substring(0, 7);
    //    } else {
    //        startDateLabel = DateHelper.dateObjectToYymmdd(startDate);
    //        endDateLabel = DateHelper.dateObjectToYymmdd(endDate);
    //    }
    //
    //    return [textLabel, '<br /><span class="text-normal">(', startDateLabel, '~', endDateLabel, ')</span>'].join('');
    //}

    return {
        /**
         * 대시보드 리포트 기본 날짜 조회
         * @param dateType (day: 어제, week: 지난주 일요일, month: 지난달 마지막날짜)
         * @returns {*}
         */
        defaultDateStr: function (dateType) {
            var now = new Date();

            if (dateType == 'week') {
                var today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
                var lastSundayDate = new Date(today.setDate(today.getDate() - today.getDay()));
                return lastSundayDate.ymd();
            } else if (dateType == 'month') {
                return new Date(now.setDate(0)).ymd();
            } else {
                return DateHelper.getPreviousDayDate(now, 1).ymd();
            }
        },

        /**
         * 대시보드 리포트 이전 milliseconds를 구한다.
         * @param dateType (day : 8일, week : 14주, month : 13개월)
         * @param date 기준일
         * @returns {*}
         */
        getPreviousDiffMs: function (dateType, date) {
            if (dateType == 'week') {
                return DateHelper.getWeeklyDiffMs(date, 14); // 14주
            } else if (dateType == 'month') {
                return DateHelper.getMonthlyDiffMs(date, 13); // 13개월간
            } else {
                return DateHelper.getDailyDiffMs(date, 8);// 8일간
            }
        },

        /**
         * 대시보드 리포트 목록 조회
         * @param dateType
         * @param condition
         * @returns {*}
         */
        getDashboardReportResult: function (dateType, condition) {
            var url;
            if (dateType == 'week') {
                url = '/dashboard/report/weekly';
            } else if (dateType == 'month') {
                url = '/dashboard/report/monthly';
            } else {
                url = '/dashboard/report/daily';
            }
            return $http.post(url, condition);
        },

        /**
         * 날짜 칼럼 레이블
         * @param dateType
         * @param date (yyyymmdd)
         */
        getTableColumnNames: function (dateType, date) {
            var basicDate = DateHelper.stringToDateObject(date);  // 기준일

            var columnNames;
            if (dateType == 'week') {
                columnNames = [
                    'BM',
                    '지표',
                    getDateColumnLabel('기준주', basicDate, dateType),
                    getDateColumnLabel('1주전', new Date(DateHelper.getWeeklyDiffMs(basicDate, 1)), dateType),
                    getDateColumnLabel('1년전', new Date(DateHelper.getMonthlyDiffMs(basicDate, 12)), dateType)
                ];
            } else if (dateType == 'month') {
                columnNames = [
                    'BM',
                    '지표',
                    getDateColumnLabel('기준월', basicDate, dateType),
                    getDateColumnLabel('1달전', new Date(DateHelper.getMonthlyDiffMs(basicDate, 1)), dateType),
                    getDateColumnLabel('1년전', new Date(DateHelper.getMonthlyDiffMs(basicDate, 12)), dateType)
                ];
            } else {
                columnNames = [
                    'BM',
                    '지표',
                    getDateColumnLabel('기준일', basicDate, dateType),
                    getDateColumnLabel('1일전', new Date(DateHelper.getDailyDiffMs(basicDate, 1)), dateType),
                    getDateColumnLabel('1주전', new Date(DateHelper.getWeeklyDiffMs(basicDate, 1)), dateType),
                    getDateColumnLabel('1달전', new Date(DateHelper.getMonthlyDiffMs(basicDate, 1)), dateType)
                ];
            }

            return columnNames;
        },

        /**
         * 날짜 칼럼 데이터
         * @param dateType
         */
        getTableColumnModels: function (dateType) {
            var columnModels;
            if (dateType == 'week') {
                columnModels = [
                    {name: 'svcNm', index: 'svcNm', sortable: false},
                    {name: 'measure', index: 'measure', sortable: false},
                    {name: 'basicMeasureValue', index: 'basicMeasureValue', align: "right", sortable: false},
                    {name: 'oneWeekAgoMeasureValue', index: 'oneWeekAgoMeasureValue', align: "right", sortable: false},
                    {name: 'oneYearAgoMeasureValue', index: 'oneYearAgoMeasureValue', align: "right", sortable: false}
                ];
            } else if (dateType == 'month') {
                columnModels = [
                    {name: 'svcNm', index: 'svcNm', sortable: false},
                    {name: 'measure', index: 'measure', sortable: false},
                    {name: 'basicMeasureValue', index: 'basicMeasureValue', align: "right", sortable: false},
                    {
                        name: 'oneMonthAgoMeasureValue',
                        index: 'oneMonthAgoMeasureValue',
                        align: "right",
                        sortable: false
                    },
                    {name: 'oneYearAgoMeasureValue', index: 'oneYearAgoMeasureValue', align: "right", sortable: false}
                ];
            } else {
                columnModels = [
                    {name: 'svcNm', index: 'svcNm', sortable: false},
                    {name: 'measure', index: 'measure', sortable: false},
                    {name: 'basicMeasureValue', index: 'basicMeasureValue', align: "right", sortable: false},
                    {name: 'oneDayAgoMeasureValue', index: 'oneDayAgoMeasureValue', align: "right", sortable: false},
                    {name: 'oneWeekAgoMeasureValue', index: 'oneWeekAgoMeasureValue', align: "right", sortable: false},
                    {name: 'oneMonthAgoMeasureValue', index: 'oneMonthAgoMeasureValue', align: "right", sortable: false}
                ];
            }

            return columnModels;
        },

        /**
         * 기준일 지표 레이블을 표현한다.
         */
        basicMeasureLabel: function (basicMeasureValue) {
            if (basicMeasureValue == null) {
                return '-';
            }
            return basicMeasureValue.numberFormat();
        },

        /**
         * 지표 레이블을 표현한다.
         */
        measureLabel: function (basicMeasureValue, measureValue) {
            if (measureValue == null) {
                return '-';
            }

            if (basicMeasureValue == null || basicMeasureValue == 0 || measureValue == 0) {
                return measureValue.numberFormat();
            }

            var buttonClass, percentageText;
            var percentage = CalcHelper.percentageTwoNumbers(measureValue, basicMeasureValue);

            if (percentage > 0) {
                buttonClass = 'label-primary';
                percentageText = '+' + percentage;
            } else if (percentage < 0) {
                buttonClass = 'label-warning';
                percentageText = percentage;
            } else {
                buttonClass = 'label-default';
                percentageText = '=' + percentage;
            }

            return measureValue.numberFormat() + '<br /><span class="label ' + buttonClass + '">' + percentageText + '%</span>';
        }
    };
});

/**
 *  factories > Dashboard Data Widget Service
 */
(factories.lazy || factories).factory('dashboardDataWidgetSvc', function () {

    function dummyData() {
        var sin = [],
            cos = [];

        for (var i = 0; i < 100; i++) {
            sin.push({x: i, y: Math.sin(i / 10)});
            cos.push({x: i, y: .5 * Math.cos(i / 10)});
        }

        return [
            {
                values: sin,
                key: 'Sine Wave',
                color: '#ff7f0e'
            },
            {
                values: cos,
                key: 'Cosine Wave',
                color: '#2ca02c'
            }
        ];
    }

    return {
        setDataWidget: function (widgetId) {
            nv.addGraph(function () {
                var chart = nv.models.lineChart()
                    .useInteractiveGuideline(true);
                chart.xAxis
                    .axisLabel('Time (ms)' + widgetId)
                    .tickFormat(d3.format(',r'));

                chart.yAxis
                    .axisLabel('Voltage (v)')
                    .tickFormat(d3.format('.02f'));

                d3.select('#' + widgetId + ' svg')
                    .datum(dummyData())
                    .transition().duration(500)
                    .call(chart);

                nv.utils.windowResize(chart.update);

                return chart;
            });

        }
    };

});

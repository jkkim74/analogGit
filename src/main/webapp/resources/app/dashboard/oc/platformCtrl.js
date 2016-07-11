function platformCtrl($scope, apiSvc, bmDashboardSvc, ocDashboardSvc,$timeout) {

    $scope.init = function () {
        $scope.searchDateType = 30;
        $scope.searchStartDate = bmDashboardSvc.getDashboardDefaultCal($scope.searchDateType).startDate;
        $scope.searchEndDate = bmDashboardSvc.getDashboardDefaultCal($scope.searchDateType).endDate;
        $scope.formatedStartDate = DateHelper.stringToYmdStr2($scope.searchStartDate);
        $scope.formatedEndDate = DateHelper.stringToYmdStr2($scope.searchEndDate);

        loadSparklineChartData();
    };

    function getConvertDaysData(dataSet, funcAvgMath, startDate) {
        var convertDataSet = [];
        _.map(dataSet, function (value) {
            var item = $.extend(true, {}, value);
            if ( startDate == null || item.dlyStrdDt >= startDate) {
                item.dlyRsltVal = funcAvgMath(dataSet, item);
                convertDataSet.push(item);
            }
        });
        return convertDataSet;
    }

    function updateElementFromStartDate(element, startDate, endDate) {
        element.dataSet = _.select( element.dataSet, function(value) {
            return value.dlyStrdDt >= startDate;
        });

        var lastyearStartDate = moment(startDate, 'YYYYMMDD').subtract(1, 'years').format('YYYYMMDD');
        var lastyearEndDate = moment(endDate, 'YYYYMMDD').subtract(1, 'years').format('YYYYMMDD');

        element.oldDataSet = _.select( element.oldDataSet, function(value) {
            return value.dlyStrdDt >= lastyearStartDate && value.dlyStrdDt <= lastyearEndDate;
        });
        element.past7DayDataSet = _.select( element.past7DayDataSet, function(value) {
            return value.dlyStrdDt >= startDate;
        });
    }

    function loadSparklineChartData(){
        $scope.dashboardList = null;
        $scope.dashboardListMsg = '조회중입니다...';

        $scope.searchStartDateMinus31 = moment($scope.searchStartDate, 'YYYYMMDD').subtract(31, 'days').format('YYYYMMDD');

        var httpConfig = {
            url: '/dashboard/category',
            params: {
                category: 'oc',
                dateType: 'day',
                startDate: $scope.searchStartDateMinus31,
                endDate: $scope.searchEndDate
            }
        };

        var LIST_DAILYBOARDS = [ 'id_29_L111_M001_S001', 'id_29_L112_M001_S001', 'id_10_L003_M001_S001' ];
        var LIST_DAILYBOARDS_TXT = '일별 ';

        var LIST_AVG7DAYSBOARDS = [ 'id_1_L017_M001_S001', 'id_7_L017_M001_S001', 'id_29_L017_M001_S001', 'id_10_L017_M001_S001', 'id_29_L111_M001_S001', 'id_29_L112_M001_S001', 'id_31_L017_M001_S001', 'id_10_L003_M001_S001', 'id_30_L017_M001_S001' ];
        var LIST_AVG30DAYSBOARDS = [ 'id_1_L102_M001_S001', 'id_1_L104_M001_S001', 'id_39_L072_M001_S001', 'id_39_L104_M001_S001', 'id_39_L118_M001_S001' ];
        var LIST_BLOCK_LASTYEAR_BOARDS = [ '#id_29_L112_M001_S001', '#id_29_L116_M001_S001' ];

        apiSvc.voyagerHttpSvc(httpConfig).then(function (result) {
            var receiveData = result.data;
            if (receiveData && receiveData.length !== 0) {
                $scope.dashboardList = receiveData;
                //console.log($scope.dashboardList);
                $timeout(function () {
                    _.each($scope.dashboardList, function (element, index, list) {
                        var extraDataSet = {};

                        if ($.inArray(element.id, LIST_DAILYBOARDS) > -1) {
                            element.backDataSet = element.dataSet;
                            element.dataSet = getConvertDaysData(element.dataSet, ocDashboardSvc.getConvertDailyData, $scope.searchStartDate);
                            element.keyIndex = LIST_DAILYBOARDS_TXT + element.keyIndex;
                            element.oldDataSet = getConvertDaysData(element.oldDataSet, ocDashboardSvc.getConvertDailyData, null);
                        }
                        if ($.inArray(element.id, LIST_AVG7DAYSBOARDS) > -1) {
                            extraDataSet.average7DayDataSet = getConvertDaysData(element.dataSet, ocDashboardSvc.getAverage7DayData, $scope.searchStartDate);
                            element.average7DayDataSet = true;
                        }
                        if ($.inArray(element.id, LIST_AVG30DAYSBOARDS) > -1) {
                            extraDataSet.average30DayDataSet = getConvertDaysData(element.dataSet, ocDashboardSvc.getAverage30DayData, $scope.searchStartDate);
                            element.average30DayDataSet = true;
                        }
                        updateElementFromStartDate(element, $scope.searchStartDate, $scope.searchEndDate);
                        if ($.inArray(element.id, LIST_BLOCK_LASTYEAR_BOARDS) == -1) {
                            extraDataSet.oldDataSet = element.oldDataSet;
                        } else {
                            element.oldDataSet = null;
                        }

                        $scope.drawOCCustomChart('#' + element.id, element.dataSet, true, $scope.searchDateType, extraDataSet);
                        element.currentData = bmDashboardSvc.searchCurrentData(element.dataSet, $scope.searchEndDate);
                        element.trandlineSlp = $scope.curChartTrandlineslp;
                    });
                });
            } else {
                $scope.dashboardListMsg = '조회결과가 없습니다.';
            }
        });
    }

    $scope.search = function (result) {
        $scope.searchStartDate = result.searchStartDate;
        $scope.searchEndDate = result.searchEndDate;
        $scope.searchDateType = result.searchDateType;
        $scope.formatedStartDate = DateHelper.stringToYmdStr2($scope.searchStartDate);
        $scope.formatedEndDate = DateHelper.stringToYmdStr2($scope.searchEndDate);

        loadSparklineChartData();
    };

    $scope.openInfoDialog = function (type) {
        bmDashboardSvc.openInfoDialog(type);
    };
}
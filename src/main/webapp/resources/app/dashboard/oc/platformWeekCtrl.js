function platformWeekCtrl($scope, apiSvc, bmDashboardSvc, ocDashboardSvc, $timeout) {

    $scope.init = function () {
        $scope.searchDateType = 'week';
        var defaultSearchDate = ocDashboardSvc.getDashboardDefaultCal($scope.searchDateType);
        $scope.searchStartDate = defaultSearchDate.startDate;
        $scope.searchEndDate = defaultSearchDate.endDate;

        makeDayToWeek();

        $scope.searchConfig = {
            dateType : $scope.searchDateType,
            startDate : $scope.searchStartDate,
            endDate : $scope.searchEndDate
        };
    };

    function makeDayToWeek() {
        var httpConfig = {
            url: '/dashboard/dayToWeek',
            params: {
                curDate: $scope.searchEndDate
            }
        };
        apiSvc.voyagerHttpSvc(httpConfig).then(function (result) {
            $scope.tmpCurrentDate = result.data;
            loadSparklineChartData();
        });
    }

    function loadSparklineChartData() {
        $scope.dashboardList = null;
        $scope.dashboardListMsg = '조회중입니다...';

        var httpConfig = {
            url: '/dashboard/category',
            params: {
                category: 'oc',
                dateType: 'week',
                startDate: $scope.searchStartDate,
                endDate: $scope.searchEndDate
            }
        };

        var LIST_DAILYBOARDS = [ 'id_29_L115_M001_S001', 'id_29_L116_M001_S001' ];

        apiSvc.voyagerHttpSvc(httpConfig).then(function (result) {
            var receiveData = result.data;
            if (receiveData && receiveData.length != 0) {
                $scope.dashboardList = receiveData;
                for ( var i in LIST_DAILYBOARDS ) {
                    for ( var j in $scope.dashboardList ) {
                        if ( LIST_DAILYBOARDS[i] == $scope.dashboardList[j].id ) {
                            $scope.dashboardList.splice(j, 1);
                            break;
                        }
                    }
                }
                $timeout(function () {
                    _.each($scope.dashboardList, function (element, index, list) {
                        var extraDataSet = {
                            oldDataSet: element.oldDataSet
                        };
                        $scope.drawOCCustomChart('#' + element.id, element.dataSet, true, $scope.searchDateType, extraDataSet);
                        element.currentData = ocDashboardSvc.searchCurrentDataWeek(element.dataSet, $scope.tmpCurrentDate);
                        element.trandlineSlp = $scope.curChartTrandlineslp;
                        element.dataCount = element.dataSet.length;
                        if (element.dataCount > 0) {
                            var tmpList = _.sortBy(element.dataSet, 'dlyStrdDt');
                            element.formatedStartDate = DateHelper.stringToYmdStr3((_.first(tmpList)).dlyStrdDt);
                            element.formatedEndDate = DateHelper.stringToYmdStr3((_.last(tmpList)).dlyStrdDt);
                        }
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

        makeDayToWeek();
    };

    $scope.openInfoDialog = function (type) {
        bmDashboardSvc.openInfoDialog(type);
    };
}
function tcloudCtrl($scope, apiSvc, bmDashboardSvc, $timeout, MAX_EWMA_SIZE) {

    $scope.init = function () {

        $scope.searchDateType = 30;
        $scope.searchStartDate = bmDashboardSvc.getDashboardDefaultCal($scope.searchDateType).startDate;
        $scope.searchEndDate = bmDashboardSvc.getDashboardDefaultCal($scope.searchDateType).endDate;
        $scope.formatedStartDate = DateHelper.stringToYmdStr2($scope.searchStartDate);
        $scope.formatedEndDate = DateHelper.stringToYmdStr2($scope.searchEndDate);

        loadDataAndDrawChart();
    };

    function loadDataAndDrawChart(){
        $scope.dashboardList = null;
        $scope.sparklineChartList = null;
        $scope.ewmaChartList = null;
        $scope.dashboardListMsg = '조회중입니다...';

        var httpConfig = {
            url: '/dashboard/category',
            params: {
                category: 'bm',
                dateType: 'day',
                svcId: 7,
                startDate: $scope.searchStartDate,
                endDate: $scope.searchEndDate
            }
        };

       apiSvc.voyagerHttpSvc(httpConfig).then(function (result) {
            var receiveData = result.data;
            if (receiveData && receiveData.length != 0) {
                $scope.dashboardList = receiveData;
                $scope.sparklineChartList = [];
                $scope.ewmaChartList = [];
                var tmpEwmaList = [];
                _.each($scope.dashboardList, function (element, index, list) {
                    if (element.chartType == 'sparkline') {
                        $scope.sparklineChartList.push(element);
                    } else if (element.chartType == 'ewma') {
                        //console.log(element);
                        element.ewmaStatus = (element.ewmaSet.length > 0) ? bmDashboardSvc.makeEWMAChartData(element.ewmaSet) : bmDashboardSvc.makeEWMAChartData([]);
                        tmpEwmaList.push(element);
                        if (tmpEwmaList.length == MAX_EWMA_SIZE) {
                            $scope.ewmaChartList.push(tmpEwmaList);
                            tmpEwmaList= [];
                        }
                    }
                });
                if(tmpEwmaList.length > 0) {
                    $scope.ewmaChartList.push(tmpEwmaList);// remain ewma-chart
                }

                $timeout(function () {
                    _.each($scope.sparklineChartList, function (element, index, list) {
                            $scope.drawChart('#' + element.id, element.dataSet, true, $scope.searchDateType);
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

       loadDataAndDrawChart();
    };

    $scope.openInfoDialog = function (type) {
        bmDashboardSvc.openInfoDialog(type);
    };
}

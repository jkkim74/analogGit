function bmDashboardCtrl($scope, apiSvc, bmDashboardSvc, $timeout, MAX_EWMA_SIZE) {

    $scope.init = function () {
        $scope.searchDateType = 30;
        var searchDate  = bmDashboardSvc.getDashboardDefaultCal($scope.searchDateType,
            (($scope.menuContext.menu.code == 'ocb' || $scope.menuContext.menu.code == 'gifticon') ? 2 : 1));
        $scope.searchStartDate = searchDate.startDate;
        $scope.searchEndDate = searchDate.endDate;
        $scope.formatedStartDate = DateHelper.stringToYmdStr2($scope.searchStartDate);
        $scope.formatedEndDate = DateHelper.stringToYmdStr2($scope.searchEndDate);

        loadDataAndDrawChart();

        $scope.$on('bmDashboardMenuClick', function (event) {
            $scope.searchDateType = 30;
            var curDate  = bmDashboardSvc.getDashboardDefaultCal($scope.searchDateType,
                (($scope.menuContext.menu.code == 'ocb' || $scope.menuContext.menu.code == 'gifticon') ? 2 : 1));
            $scope.searchStartDate = curDate.startDate;
            $scope.searchEndDate = curDate.endDate;
            $scope.formatedStartDate = DateHelper.stringToYmdStr2($scope.searchStartDate);
            $scope.formatedEndDate = DateHelper.stringToYmdStr2($scope.searchEndDate);
            //console.log('tempMenuCode : ' + tempMenuCode + '.....diffDateValue : ' + diffDateValue + '.....searchDateType : ' + $scope.searchDateType);
            //console.log(curDate);
            loadDataAndDrawChart();
        });
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
                svcId: $scope.menuContext.bossSvcId,
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
                $scope.dashboardListMsg = '조회 결과가 없습니다.';
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

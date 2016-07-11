function oneidJoinPcCtrl1($scope, DATE_TYPE_DAY, reportSvc, $timeout) {
    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal();
        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchConfig = {
            startDate: defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain
        };

        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;

        drawFunnel();
    };

    /**
     * drawPivot
     */
    function drawFunnel() {
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };
        var url = '/oneid/register/chart?inoutFlg=2&dateType=' + $scope.searchDateType + '&startDate=' + $scope.searchStartDate + '&endDate=' + $scope.searchEndDate;
        reportSvc.getReportApi(url).then(function (result) {
            if (result && result.S1) {
                $scope.$safeApply(function () {
                    $scope.funnelId = 'oneidFunnel1';
                    $scope.funnelDatas = result;
                    $timeout(function() {
                        DomHelper.resizeFunnelWidth($scope.funnelId);
                    },100);
                    $scope.pageState.loading = false;
                    $scope.pageState.emptyData = false;
                });
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    };


    /**
     * 검색 조회 callback 함수
     *
     * @params result
     */
    $scope.search = function (result) {
        if (result.searchDateType != "day") {
            swal('다음 릴리즈때 사용하실 수 있습니다.\n조금 기다려 주세요.^^');
            return;
        }

        $scope.searchDateType = result.searchDateType;
        $scope.searchStartDate = result.searchStartDate;
        $scope.searchEndDate = result.searchEndDate;

        drawFunnel();
    };
}

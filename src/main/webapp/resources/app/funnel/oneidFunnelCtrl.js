function oneidFunnelCtrl($scope, DATE_TYPE_DAY, reportSvc, $timeout) {
    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal();
        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchConfig = {
            startDate: defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain
        };
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;
        $scope.searchProcCode = 'p1';
        $scope.searchPocCode = 'p';
        $scope.searchSstCode = '90300';
        $scope.funnelId = 'oneidFunnel';

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

        var url = '/oneid/funnel/chart?svcId=23&dateType=' + $scope.searchDateType + '&startDate=' + $scope.searchStartDate
            + '&endDate=' + $scope.searchEndDate + '&procCd=' + $scope.searchProcCode + '&poc=' + $scope.searchPocCode;
        if ($scope.searchProcCode === 'p1') {
            url += '&reqSstCd=' + $scope.searchSstCode;
        }

        reportSvc.getReportApi(url).then(function (result) {
            if (result && result.s1) {
                $scope.$safeApply(function () {
                    $scope.funnelDatas = result;
                    $timeout(function() {
                        DomHelper.resizeFunnelWidth($scope.funnelId);
                    },100);
                    $scope.pageState.loading = false;
                    $scope.pageState.emptyData = false;
                });
            } else {
                $scope.funnelDatas = {};
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    }


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
        $scope.searchProcCode = result.searchProcCode;
        $scope.searchPocCode = result.searchPocCode;
        $scope.searchSstCode = result.searchSstCode;

        drawFunnel();
    };
}

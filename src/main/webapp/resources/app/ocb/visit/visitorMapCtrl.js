function visitorMapCtrl($scope, $http, DATE_TYPE_DAY, reportSvc) {
    $scope.chartId = '#visitorMap';

    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal();
        $scope.searchConfig = {
            startDate : defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain
        };
        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;
        drawMapChart();
    };

    function drawMapChart() {
        var url = '/ocb/visit/tracking/chart?dateType=' + $scope.searchDateType +
            '&startDate=' + $scope.searchStartDate + '&endDate=' + $scope.searchEndDate;
        reportSvc.getReportApi(url).then(function(result) {
            convertData(result.rows);
            drawChart();
        });
    }

    /**
     * drawFlow Chart
     * @param  target selection
     * @param  tree  데이터 배열
     */
    function drawChart() {
    }

    /**
     * tree구조로 변경하기 위한 sid 기준으로 배열 정렬
     * @param data json 데이터
     */
    function convertData(data) {
        $scope.sankeyData = [];
        var tmpArr = [], i = 0, oldSid = 0;
        angular.forEach(data, function (obj) {
            var currentSid = obj.sid;
            if (oldSid != currentSid) {
                $scope.sankeyData.push(tmpArr);
                 i = 0;
                tmpArr = [];
            }
            tmpArr.push(obj);
            oldSid = currentSid;
        });
    }

    /**
     * 검색 조회 callback 함수
     *
     * @params result
     */
    $scope.search = function(result) {
        $scope.searchDateType = result.searchDateType;
        $scope.searchStartDate = result.searchStartDate;
        $scope.searchEndDate = result.searchEndDate;
        $scope.searchMeasureCode = result.searchCode;

        if (d3.select($scope.chartId).selectAll('svg'))
            d3.select($scope.chartId).selectAll('svg').remove();
        drawMapChart();
    };

}

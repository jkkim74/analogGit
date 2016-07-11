function d3MultiTrendLine($scope, $http, API_BASE_URL, reportSvc) {

    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal();
        $scope.searchDateType = $scope.searchBoxDateType;
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;

        $scope.drawVisitorMultiChart();
    }

    $scope.drawVisitorMultiChart = function () {
        var url = '/ocb/visit/outline/chart?dateType=' + $scope.searchDateType + '&startDate=' + $scope.searchStartDate + '&endDate=' + $scope.searchEndDate;
        url += '01' + $scope.searchPocCode;

        $http.get(url).success(function (result) {
            if (result.length != 0) {
                var values = result.map(function (d) {
                    return {x: d.stdDt, y: d.uv};
                });
                var values2 = result.map(function (d) {
                    return {x: d.stdDt, y: d.lv};
                });
                var values3 = result.map(function (d) {
                    return {x: d.stdDt, y: d.pv};
                });
                var values4 = result.map(function (d) {
                    return {x: d.stdDt, y: d.rv};
                });
                var values5 = result.map(function (d) {
                    return {x: d.stdDt, y: d.vstCnt};
                });
                var values6 = result.map(function (d) {
                    return {x: d.stdDt, y: d.timeSptFVst};
                });
                var values7 = result.map(function (d) {
                    return {x: d.stdDt, y: d.buncRate};
                });

                var trendline = [{"x":"2014-06-09","y":500000},{"x":"2014-06-12","y":600000},{"x":"2014-06-15","y":750000}];

                //console.log("values : "+values7);
                //console.log("trendline : "+trendline);

                $scope.currentDate = [
                    {"key": "UV", "area": false, "values": values},
                    {"key": "LV", "area": false, "values": values2}
                    ,{"key": "trendline", "area": true, "values": trendline,"color":"black"}
//                    {"key": "PV", "area": true, "values": values3},
//                    {"key": "RV", "area": false, "values": values4},
//                    {"key": "방문자수", "area": false, "values": values5},
//                    {"key": "체류시간", "area": false, "values": values6},
//                    {"key": "Bounce rate", "area": false, "values": values7}
                ];
            } else {
                $scope.currentDate = [];
            }
        });


        // d3 포멧 파싱 함수
        var parseDateDay = d3.time.format("%Y-%m-%d").parse;
        var perseDateMonth = d3.time.format("%Y-%m").parse;

        //y축 입력값 지정 및 포멧팅
        $scope.yFunction = function () {
            return function (d) {
                return d.y;
            }
        };

        // x 축 입력값 지정 및 포멧팅
        $scope.xFunction = function () {
            return function (d) {
                if ($scope.searchDateType == "month") {
                    return perseDateMonth(d.x);
                } else {  //day, week
                    return parseDateDay(d.x);
                }
            }
        };

        // x축 디스플레이 값 포멧 지정
        $scope.xAxisTickFormatFunction = function () {
            return function (d) {
                if ($scope.searchDateType == "month") {
                    return d3.time.format('%Y-%m')(new Date(d));
                } else {  //day, week
                    return d3.time.format('%Y-%m-%d')(new Date(d));

                }
            }
        };
        // y축 디스플레이 값 포멧 지정
        $scope.yAxisTickFormatFunction = function () {
            return function (d) {
                return d3.format(',')(d);
            }
        };


        var colorArray = ['#ffa500', '#c80032', '#0000ff', '#6464ff'];
        $scope.colorFunction = function () {
            return function (d, i) {
                return colorArray[i];
            }
        }
    };

    /**
     * 검색 조회 callback 함수
     *
     * @params result
     */
    $scope.search = function(result) {
        $scope.searchDateType = result.searchDateType;
        $scope.searchStartDate = result.searchStartDate;
        $scope.searchEndDate = result.searchEndDate;
e
        $scope.drawVisitorMultiChart();

    };
}

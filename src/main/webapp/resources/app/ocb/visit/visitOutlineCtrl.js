/**
 * Created by cookatrice on 2014. 5. 9..
 */

function visitOutlineCtrl($scope, $http, DATE_TYPE_DAY, reportSvc) {
    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal();
        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;
        $scope.searchPocCode = '01';

        $scope.drawVisitorGrid();
    };

    $scope.drawVisitorGrid = function () {
        $scope.pageState = {
            loading: true,
            emptyData: true
        };

//        var url = API_BASE_URL + '/ocb/visit/outline/grid?dateType=' + $scope.searchDateType + '&startDate='
        var url = '/ocb/visit/outline/grid?dateType=' + $scope.searchDateType + '&startDate='
            + $scope.searchStartDate + '&endDate=' + $scope.searchEndDate + '&pocCode=' +
            $scope.searchPocCode + '&sidx=stdDt&sord=asc';

        var heightVal = "";
        $scope.tableId = 'visitorListTableForGrid';
        reportSvc.getReportApi(url).then(function(result) {
            var dataLength = result.rows.length;
            if (dataLength != 0) {
                if (dataLength > 20)
                    heightVal = "400";
                else
                    heightVal = "100%";
                gridDate(result.rows, heightVal);
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    };

    function gridDate(datas, heightVal) {
        //grid
        $scope.visitorListForGridConfig = {
            data : datas,
            datatype: "local",
            height: "100%",
            shrinkToFit: true, //with scroll
            rowNum: 50000,
            colNames: ['기준일자', 'UV', 'LV(Login Visitor)', 'RV(Returning Visitor)', '방문횟수', '체류시간', 'PV', 'Bounce Rate'],
            colModel: [
                {name: 'stdDt', index: 'stdDt', editable: false, align: "center", sortable: true},
                {name: 'uv', index: 'uv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'lv', index: 'lv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'rv', index: 'rv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'vstCnt', index: 'vstCnt', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'timeSptFVst', index: 'timeSptFVst', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'pv', index: 'pv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'buncRate', index: 'buncRate', editable: false, formatter: 'integer', align: "center", sortable: false}
            ],
            gridview: true,
            sortname: 'stdDt',
            sortorder: 'asc',
            multiselect: false,
            autowidth: true,
            hidegrid: false,
            loadonce: true,
            scroll: true,
            jsonReader: {
                root: "rows"
            },
            gridComplete: function () {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = false;
            }
        };
        var $table = angular.element('<table id="' + $scope.tableId + '" />');
        angular.element("#visitorListForGrid").html($table);
        angular.element("#" + $scope.tableId).jqGrid("GridUnload");
        angular.element("#" + $scope.tableId).jqGrid($scope.visitorListForGridConfig);
    }

    $scope.$on('$destroy', function() {
        $('#visitorListForGrid').empty();
    });

    $scope.drawVisitorMultiChart = function () {
        var url = '/ocb/visit/outline/chart?dateType=' + $scope.searchDateType + '&startDate=' + $scope.searchStartDate + '&endDate=' + $scope.searchEndDate;
        url += '&pocCode=' + $scope.searchPocCode;

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

                $scope.currentDate = [
                    {"key": "UV", "area": false, "values": values},
                    {"key": "LV", "area": false, "values": values2},
                    {"key": "PV", "area": true, "values": values3},
                    {"key": "RV", "area": false, "values": values4},
                    {"key": "방문자수", "area": false, "values": values5},
                    {"key": "체류시간", "area": false, "values": values6},
                    {"key": "Bounce rate", "area": false, "values": values7}
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
            };
        };

        // x 축 입력값 지정 및 포멧팅
        $scope.xFunction = function () {
            return function (d) {
                if ($scope.searchDateType == "month") {
                    return perseDateMonth(d.x);
                } else {  //day, week
                    return parseDateDay(d.x);
                }
            };
        };

        // x축 디스플레이 값 포멧 지정
        $scope.xAxisTickFormatFunction = function () {
            return function (d) {
                if ($scope.searchDateType == "month") {
                    return d3.time.format('%Y-%m')(new Date(d));
                } else {  //day, week
                    return d3.time.format('%Y-%m-%d')(new Date(d));

                }
            };
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
            };
        };
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
        $scope.searchPocCode = result.searchCode;

        $scope.drawVisitorGrid();
        //$scope.drawVisitorGrid2();
        //$scope.drawVisitorMultiChart();

    };

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        downloadUrl : '/ocb/visit/downloadExcelForOutline',
        downloadType : 'POI',
        pivotFlag : 'N',
        tableId : 'visitorListPerDayForGrid',
        xlsName : 'visit_outline.xls'
    };

}

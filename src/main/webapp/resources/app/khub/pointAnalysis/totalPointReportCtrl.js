/**
 * Created by cookatrice
 */

function totalPointReportCtrl($scope, DATE_TYPE_DAY, reportSvc, apiSvc) {

    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal();
        $scope.searchConfig = {
            startDate: defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain
        };
        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;

        drawPivot();
    };

    function setPivotTableConfig(rawData) {
        $('#' + $scope.pivotTableId).pivotUI(
            rawData,
            {
                rows: ["rptIndNm", "rptIndDetlNm", "measure"],
                cols: ['stndrdDt'],
                vals: ['measureValue'],
                aggregatorName: 'intSum',
                rendererName: 'table',
                onRefresh: function () {
                    var config = {
                        id: $scope.pivotTableId,
                        simpleColMerge: [{row: 1, start: 1, end: 2}, {row: 2, start: 1, end: 2}, {
                            row: 3,
                            start: 1,
                            end: 2
                        }],
                        customOrder: 2,    //substring custom order. default = 0
                        modelData: [
                            {name: '구분', width: 150},
                            {name: '상세', width: 150},
                            {name: '항목', width: 100}
                        ]
                    };
                    $scope.headerView = true;
                    DomHelper.reDrawPivotTableHeader(config);
                    $scope.tableHeaderList = DomHelper.getDateList(config.id);

                    $scope.$safeApply(function () {
                        $scope.pageState.loading = false;
                        $scope.pageState.emptyData = false;
                    });
                }
            },
            true
        );
    }

    $scope.$on('$destroy', function() {
        //$('#' + $scope.pivotTableId).children().unbind();
        //$('#' + $scope.pivotTableId).children().off();
        $('#' + $scope.pivotTableId).empty();
    });

    /**
     * drawPivot
     */
    function drawPivot() {
        /** resize pivotTable and chart width **/
        $scope.pivotTableId = 'totalPointReportPivot';

        $scope.headerView = false;
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };

        var httpConfig = {
            url: '/ocb/khub/pointAnalysis/totalPointReport/pivot',
            params: {
                dateType: $scope.searchDateType,
                startDate: $scope.searchStartDate,
                endDate: $scope.searchEndDate
            }
        };

        apiSvc.voyagerHttpSvc(httpConfig).then(function (result) {
            var receiveData = result.data;
            if (receiveData && receiveData.rows.length != 0) {
                $scope.$safeApply(function () {
                    setPivotTableConfig(receiveData.rows);
                });
            } else {
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
        $scope.searchDateType = result.searchDateType;
        $scope.searchStartDate = result.searchStartDate;
        $scope.searchEndDate = result.searchEndDate;

        drawPivot();
    };

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        downloadUrl: '/download/report/excel',
        downloadType: 'POI',
        pivotFlag: 'Y',
        tableId: 'totalPointReportPivot',
        titleName: '전체포인트 보고서',
        xlsName: 'total-point-report.xls'
    };
}

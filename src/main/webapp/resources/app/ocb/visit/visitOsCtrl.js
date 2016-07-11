/**
 * Created by cookatrice on 2014. 5. 14..
 */

function visitOsCtrl($scope, DATE_TYPE_DAY, reportSvc, apiSvc) {

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
                rows: ["osNm", "osVer", "measure"],
                cols: ['stdDt'],
                vals: ['measureValue'],
                aggregatorName: 'intSum',
                onRefresh: function () {
                    var config = {
                        id: $scope.pivotTableId,
                        customOrder: 1,
                        modelData: [
                            {name: 'OS명', width: 100},
                            {name: 'OS버전', width: 100},
                            {name: 'measure', width: 100}
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
        $scope.pivotTableId = 'visitOsPivot';

        $scope.headerView = false;
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };

        var httpConfig = {
            url: '/ocb/visit/os/grid',
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
        tableId: 'visitOsPivot',
        titleName: '방문자(OS)',
        xlsName: 'visit-os.xls'
    };

}

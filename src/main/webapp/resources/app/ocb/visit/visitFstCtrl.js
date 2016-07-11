/**
 * OCB > 방문 > 월 최초방문자(고객타입별)
 *
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 * @param $scope
 * @param reportSvc
 * @param apiSvc
 * @param DATE_TYPE_DAY
 */
function visitFstCtrl($scope, reportSvc, apiSvc, DATE_TYPE_DAY) {
    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal();
        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchConfig = {
            startDate: defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain
        };
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;

        drawPivot();
    };

    function setPivotTableConfig(rawData) {
        // apply white-space for textContent
        rawData = rawData.map(function(d) {
            d.measure = d.measure.replace(/&nbsp;/g,'\u00a0');
            return d;
        });
        $('#' + $scope.pivotTableId).pivotUI(
            rawData,
            {
                rows: ["measure"],
                cols: ['strdDt'],
                vals: ["measureValue"],
                aggregatorName: 'intSum',
                onRefresh: function () {
                    var config = {
                        id: $scope.pivotTableId,
                        customOrder: 2,
                        modelData: [
                            {name: 'measure', width: 280}
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
        $scope.pivotTableId = 'visitFstPivot';
        $scope.headerView = false;
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };
        var httpConfig = {
            url: '/ocb/customer/visitFst/pivot',
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
    $scope.search = function(result) {
        $scope.searchDateType = result.searchDateType;
        $scope.searchStartDate = result.searchStartDate;
        $scope.searchEndDate = result.searchEndDate;

        drawPivot();
    };

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, titleName: string, xlsName: string}}
     */
    $scope.excelConfig = {
        downloadUrl: '/download/report/excel',
        downloadType: 'POI',
        pivotFlag: 'Y',
        tableId: 'visitFstPivot',
        titleName: '월 최초방문자(고객타입별)',
        xlsName: 'OCB_방문_월최초방문자_고객타입별.xlsx'
    };
}

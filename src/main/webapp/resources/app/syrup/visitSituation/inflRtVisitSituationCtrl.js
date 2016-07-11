/**
 * Created by ophelisis on 2015. 7. 28..
 */

function inflRtVisitSituationCtrl($scope, DATE_TYPE_DAY, reportSvc, apiSvc) {
    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal();
        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchConfig = {
            startDate : defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain
        };

        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;

        drawPivot();
    };

    function setPivotTableConfig(rawData) {
        $('#' + $scope.pivotTableId).pivotUI(
            rawData,
            {
                rows: ["category","pageId"],
                cols: ["strdDt"],
                vals: ["measureValue"],
                aggregatorName: "intSum",
                onRefresh: function () {
                    var config = {
                        id: $scope.pivotTableId,
                        customOrder: 0,
                        modelData: [
                            {name: "category", width: 100},
                            {name: "pageId", width: 300}
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

    /**
     * drawPivot
     */
    function drawPivot() {
        /** resize pivotTable and chart width **/
        $scope.pivotTableId = 'inflRtVisitSituationPivot';

        $scope.headerView = false;
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };

        var httpConfig = {
            url: '/syrup/visitSituation/inflRt/pivot',
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
        if (result.searchDateType != "day") {
            alert('다음 릴리즈때 사용하실 수 있습니다. 조금 기다려 주세요.^^');
            return;
        }

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
        downloadUrl : '/download/report/excel',
        downloadType : 'POI',
        pivotFlag : 'Y',
        tableId : 'inflRtVisitSituationPivot',
        titleName : 'Syrup DAU 기준 유입경로(Page_id 기준)',
        xlsName : 'Syrup_방문현황_DAU_유입경로별.xls'
    };
}

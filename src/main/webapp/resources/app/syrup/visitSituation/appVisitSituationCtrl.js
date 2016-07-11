/**
 * Created by cookatrice on 15. 1. 8..
 */

function appVisitSituationCtrl($scope, DATE_TYPE_DAY, reportSvc, olapSvc, apiSvc) {
    function olapSearchEleInit() {
        $scope.searchConfig = {
            ctrlId: 'appVisitSituationCtrl',
            tableId : $scope.excelConfig.tableId,
            startDate: $scope.searchStartDate,
            endDate: $scope.searchEndDate,
            dayPeriod: $scope.datePeriod.day,
            weekPeriod: $scope.datePeriod.week,
            monthPeriod: $scope.datePeriod.month
        };
        $scope.measure = {
            'code': 'measure', 'name': 'measure', 'selected': true, 'order': 6, 'axis': 'y', 'draggable': true,
            'belongs': [
                {'code': 'totUv', 'name': 'UV', 'selected': true},
                {'code': 'totPv', 'name': 'PV', 'selected': true},
                {'code': 'mbrUv', 'name': '회원UV', 'selected': true},
                {'code': 'mbrPv', 'name': '회원PV', 'selected': true},
                {'code': 'wletExecUv', 'name': '지갑실행UV', 'selected': true},
                {'code': 'wletExecPv', 'name': '지갑실행PV', 'selected': true},
                {'code': 'mbrshTwnUv', 'name': '멤버십타운UV', 'selected': true},
                {'code': 'mbrshTwnPv', 'name': '멤버십타운PV', 'selected': true},
                {'code': 'wdgExecUv', 'name': '위젯실행UV', 'selected': true},
                {'code': 'wdgExecPv', 'name': '위젯실행PV', 'selected': true}
            ]
        };
        $scope.dimensions = [
            {'code': 'standardDate', 'name': '기준일자', 'selected': true, 'order': 0, 'axis': 'x', 'draggable': false},
            {'code': 'appVersion', 'name': '앱버전', 'selected': false, 'order': 1, 'axis': 'y', 'draggable': true},
            {'code': 'sex', 'name': '성별', 'selected': false, 'order': 2, 'axis': 'y', 'draggable': true},
            {'code': 'ageRange', 'name': '연령대', 'selected': false, 'order': 3, 'axis': 'y', 'draggable': true},
            {'code': 'os', 'name': 'OS', 'selected': false, 'order': 4, 'axis': 'y', 'draggable': true},
            {'code': 'telecom', 'name': '통신사', 'selected': false, 'order': 5, 'axis': 'y', 'draggable': true}
        ];
        $scope.draggable = olapSvc.prepareDraggable($scope.dimensions, $scope.measure);
    }

    $scope.init = function () {
        $scope.datePeriod = {day: 7,week: 4,month: 6};
        $scope.searchDateType = DATE_TYPE_DAY;
        var defaultCal = reportSvc.defaultCustomCal($scope.searchDateType, $scope.datePeriod.day, $scope.datePeriod.week, $scope.datePeriod.month);
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;
        $scope.searchPocCode = '01';

        olapSearchEleInit();
        drawPivot();
    };

    function setPivotTableConfig(rawData) {
        $scope.$safeApply(function () {
            $scope.pageState.loading = false;
            $scope.pageState.emptyData = false;
        });

        olapSvc.drawPivotTable($scope.searchConfig, rawData, $scope.measure, $scope.dimensions);
    }

    $scope.$on('$destroy', function() {
        //$('#' + $scope.searchConfig.tableId).children().unbind();
        //$('#' + $scope.searchConfig.tableId).children().off();
        $('#' + $scope.searchConfig.tableId).empty();
    });

    function drawPivot() {
        /** resize olapPivotTable and chart width **/
        $scope.olapPivotTableId = $scope.searchConfig.tableId;
        // 기 생성된 dynamic header를 제거
        DomHelper.removeDynamicHeaderForPivot($scope.searchConfig.tableId);

        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };

        var dimensionCodes =  olapSvc.getSelectedDimensionsCode($scope.dimensions, $scope.measure).join('|');

        var httpConfig = {
            url: '/syrup/visitSituation/app',
            params: {
                dateType: $scope.searchDateType,
                startDate: $scope.searchStartDate,
                endDate: $scope.searchEndDate,
                dimensions: dimensionCodes
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
//        $scope.searchPocCode = result.searchCode;

        drawPivot();
    };

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        downloadUrl: '/download/report/dynamicHeaderPivotExcel',
        downloadType: 'POI',
        pivotFlag: 'Y',
        tableId: 'appVisitSituationPivot',
        titleName: 'App 방문현황 리포트',
        xlsName: 'app_visit_situation_report.xls'
    };

}

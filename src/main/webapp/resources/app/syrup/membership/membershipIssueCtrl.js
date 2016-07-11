/**
 * Created by cookatrice on 15. 1. 9..
 */
function membershipIssueCtrl($scope, DATE_TYPE_DAY, reportSvc, olapSvc, apiSvc) {
    function olapSearchEleInit() {
        $scope.searchConfig = {
            ctrlId: 'membershipIssueCtrl',
            tableId : $scope.excelConfig.tableId,
            startDate: $scope.searchStartDate,
            endDate: $scope.searchEndDate,
            dayPeriod: $scope.datePeriod.day,
            weekPeriod: $scope.datePeriod.week,
            monthPeriod: $scope.datePeriod.month
        };
        $scope.measure = {
            'code': 'measure', 'name': 'measure', 'selected': true, 'order': 1, 'axis': 'y', 'draggable': true,
            'belongs': [
                {'code': 'totIssueCnt', 'name': '당일발급건수', 'selected': true},
                {'code': 'totIssueUserCnt', 'name': '당일발급자수', 'selected': true},
                {'code': 'dayIssueCnt', 'name': '총발급건수', 'selected': true},
                {'code': 'dayIssueUserCnt', 'name': '총발급자수', 'selected': true}
            ]
        };
        $scope.dimensions = [
            {'code': 'standardDate', 'name': '기준일자', 'selected': true, 'order': 0, 'axis': 'x', 'draggable': false},
            {'code': 'membership', 'name': '멤버십유형', 'selected': true, 'order': 2, 'axis': 'y', 'draggable': true},
            {'code': 'os', 'name': 'OS', 'selected': false, 'order': 4, 'axis': 'y', 'draggable': true},
            {'code': 'sex', 'name': '성별', 'selected': false, 'order': 5, 'axis': 'y', 'draggable': true},
            {'code': 'ageRange', 'name': '연령대', 'selected': false, 'order': 6, 'axis': 'y', 'draggable': true},
            {'code': 'telecom', 'name': '통신사', 'selected': false, 'order': 7, 'axis': 'y', 'draggable': true}
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
            url: '/syrup/membership/membershipIssue',
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
        tableId: 'membershipIssuePivot',
        titleName: '멤버십발급 리포트',
        xlsName: 'membership_issue_report.xls'
    };

}

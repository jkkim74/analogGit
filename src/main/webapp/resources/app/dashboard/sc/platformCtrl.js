function platformCtrl($scope, $timeout, DATE_TYPE_DAY, REPORT_DATE_TYPES, dashboardReportSvc) {
    $scope.init = function() {
        $scope.tableId = "";
        $scope.svcId = $scope.menuContext.serviceCode;
        $scope.basicDateParamString = DateHelper.dateToParamString(dashboardReportSvc.defaultDateStr(DATE_TYPE_DAY));
        $scope.dateType = DATE_TYPE_DAY;
        $scope.menuContext.datePeriodTypes = REPORT_DATE_TYPES;

        callData();
    };

    /**
     * 지표 결과 호출
     */
    function callData() {
        $scope.pageState = {
            loading: true,
            emptyData: false
        };

        $scope.measures = [
            {
                "idxClCd": "M001",
                "idxClGrpCd": "L017",
                "idxCttCd": "S001",
                "svcId": "7"
            }
            ,{
                "idxClCd": "M001",
                "idxClGrpCd": "L102",
                "idxCttCd": "S001",
                "svcId": "1"
            }
            ,{
                "idxClCd": "M001",
                "idxClGrpCd": "L104",
                "idxCttCd": "S001",
                "svcId": "1"
            }
        ];

        var condition = {
            searchDate: ($scope.dateType == 'month') ? $scope.basicDateParamString.substring(0, 6) : $scope.basicDateParamString
            , whereConditions: $scope.measures
        };

        // 지표 목록을 조회한다.
        dashboardReportSvc.getDashboardReportResult($scope.dateType, condition).then(function(result){
            var dataLength = result.data.length;
            if (dataLength != 0) {
                $timeout(function(){
                    drawGrid(result.data);
                });
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    }

    function drawGrid(rows) {
        var colNames = dashboardReportSvc.getTableColumnNames($scope.dateType, $scope.basicDateParamString);
        var colModels = dashboardReportSvc.getTableColumnModels($scope.dateType);

        $scope.tableId = "jqGridTable_" + $scope.svcId;
        var $table = angular.element('<table id="' + $scope.tableId + '" />');

        angular.element(".jqGridTableContainer").html($table);
        angular.element("#" + $scope.tableId).jqGrid("GridUnload");
        angular.element("#" + $scope.tableId).jqGrid({
            datatype: "local",
            height: '100%',
            colNames: colNames,
            colModel: colModels,
            sortname: "id",
            sortorder: 'asc',
            multiselect: false,
            sortable: false,
            loadonce: true,
            autowidth: true,
            gridComplete: function() {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = false;
            }
        });

        var size = rows.length;
        for (var i = 0; i < size; i++) {
            var row = rows[i];
            row.oneDayAgoMeasureValue = dashboardReportSvc.measureLabel(row.basicMeasureValue, row.oneDayAgoMeasureValue);
            row.oneWeekAgoMeasureValue = dashboardReportSvc.measureLabel(row.basicMeasureValue, row.oneWeekAgoMeasureValue);
            row.oneMonthAgoMeasureValue = dashboardReportSvc.measureLabel(row.basicMeasureValue, row.oneMonthAgoMeasureValue);
            row.oneYearAgoMeasureValue = dashboardReportSvc.measureLabel(row.basicMeasureValue, row.oneYearAgoMeasureValue);
            row.basicMeasureValue = dashboardReportSvc.basicMeasureLabel(row.basicMeasureValue);

            angular.element('#' + $scope.tableId).jqGrid('addRowData', i + 1, row);
        }

        reformTable($scope.tableId);
    }

    /**
     * for reform.css
     *
     * @param tableId
     */
    function reformTable(tableId) {
        var $selector = '.jq-board03 .ui-jqgrid';
        var headerAndLeftColumns = [
            '#' + tableId + '_measure',
            'tr.ui-widget-content td[aria-describedby="' + tableId + '_measure"]'
        ];
        var rightColumns = [
            '#jqgh_' + tableId + '_basicMeasureValue',
            '#jqgh_' + tableId + '_oneDayAgoMeasureValue',
            '#jqgh_' + tableId + '_oneWeekAgoMeasureValue',
            '#jqgh_' + tableId + '_oneMonthAgoMeasureValue',
            '#jqgh_' + tableId + '_oneYearAgoMeasureValue'
        ];

        angular.forEach(headerAndLeftColumns, function (obj) {
            angular.element($selector).find(obj).css({'padding-left': '20px', 'text-align': 'left'});
        });

        angular.forEach(rightColumns, function (obj) {
            angular.element($selector).find(obj).css('text-align', 'right');
        });
    }

    /**
     * 검색 callback
     * @param result
     */
    $scope.search = function(result) {
        $scope.basicDateParamString = result.searchEndDate;
        $scope.dateType = result.searchDateType;

        callData();
    };

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        downloadUrl: '/download/dashboard/excel',
        downloadType: 'POI',
        pivotFlag: 'N',
        tableId: 'platformForGrid',
        xlsName: 'dashboard_platform_report.xls'
    };
}

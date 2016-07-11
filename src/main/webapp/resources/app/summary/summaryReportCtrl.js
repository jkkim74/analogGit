function summaryReportCtrl($scope, summaryReportSvc, $timeout, DATE_TYPE_DAY) {
    var chartOption = {
        height: '18px',
        width: '80px',
        lineColor: '#7b7b7b',
        lineWidth: '1',
        fillColor: '#e1e1e1',
        minSpotColor: false,
        maxSpotColor: false,
        spotColor: false,
        spotRadius: 2,
        highlightLineColor: '#e8e8e8',
        highlightSpotColor: '#5c5c5c'
    };
    $scope.tableId = '';
    $scope.svcId = $scope.menuContext.service.oprSvcId;
    $scope.basicDate = summaryReportSvc.defaultDateStr(DATE_TYPE_DAY);
    $scope.pageState = {
        loading: true,
        emptyData: false
    };

    /**
     * 요약리포트 초기화 callback
     * summaryReportSearchBox directive에 의해 호출된다.
     * @param result
     */
    $scope.initCallback = function(result) {
        $scope.basicDateParamString = DateHelper.dateToParamString($scope.basicDate);
        $scope.measures = result.measures;
        $scope.dateType = DATE_TYPE_DAY;
        callMeasureResult();
    };

    /**
     * 지표 결과 호출 
     */
    function callMeasureResult() {
        var condition = {
            searchDate: ($scope.dateType == 'month') ? $scope.basicDateParamString.substring(0, 6) : $scope.basicDateParamString,
            whereConditions: $scope.measures
        };

        // 지표 목록을 조회한다.
        summaryReportSvc.getSummaryDailyResult($scope.dateType, condition).then(function(result){
            var size = result.data.length;
            if(size == 0) {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            } else {
                $timeout(function(){
                    loadData(result.data);
                });
            }
        });
    }

    function loadData(rows) {
        var colNames = summaryReportSvc.getTableColumnNames($scope.dateType, $scope.basicDateParamString);
        var colModels = summaryReportSvc.getTableColumnModels($scope.dateType);

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
            var rowId = 'summaryReportRow_' + $scope.svcId + '_' + i;
            row.chartHtml = "<div id='" + rowId + "'>&nbsp;</div>";
            row.oneDayAgoMeasureValue = summaryReportSvc.measureLabel(row.basicMeasureValue, row.oneDayAgoMeasureValue);
            row.oneWeekAgoMeasureValue = summaryReportSvc.measureLabel(row.basicMeasureValue, row.oneWeekAgoMeasureValue);
            row.oneMonthAgoMeasureValue = summaryReportSvc.measureLabel(row.basicMeasureValue, row.oneMonthAgoMeasureValue);
            row.oneYearAgoMeasureValue = summaryReportSvc.measureLabel(row.basicMeasureValue, row.oneYearAgoMeasureValue);
            row.basicMeasureValue = summaryReportSvc.basicMeasureLabel(row.basicMeasureValue);

            angular.element('#' + $scope.tableId).jqGrid('addRowData', i + 1, row);
            angular.element('#' + rowId).sparkline(row.periodMeasureValues, chartOption);
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
            '#' + tableId + '_chartHtml',
            '#' + tableId + '_measure',
            'tr.ui-widget-content td[aria-describedby="' + tableId + '_chartHtml"]',
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
        $scope.measures = result.searchMeasures;
        $scope.dateType = result.searchDateType;

        callMeasureResult();
    };
}


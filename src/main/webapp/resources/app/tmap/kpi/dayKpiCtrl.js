function dayKpiCtrl($scope, DATE_TYPE_DAY, reportSvc) {
    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal(DATE_TYPE_DAY);
        $scope.searchConfig = {
            endDate: defaultCal.endDateStrPlain
        };
        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchEndDate = defaultCal.endDateStrPlain;
        $scope.searchKpiCode = '04';
        $scope.drawDayKpi();
    };

    $scope.drawDayKpi = function () {
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };

        // chart data
        var chartUrl = "/tmap/kpi/day/chart?endDate=" + $scope.searchEndDate + "&kpiCode=" + $scope.searchKpiCode;
        // grid data
        var gridUrl = "/tmap/kpi/day/grid?endDate=" + $scope.searchEndDate + "&kpiCode=" + $scope.searchKpiCode;

        $scope.tableId = 'dayKpiTableForGrid';
        reportSvc.getReportApi(gridUrl).then(function(result) {
            var dataLength = result.rows.length;
            if (dataLength != 0) {
                gridDayKpi(result.rows);
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    };

    function gridDayKpi(datas) {
        $scope.dayKpiForGridConfig = {
            data : datas,
            datatype: "local",
            height: "100%",
            shrinkToFit: false,
            rowNum: 50000,
            colNames: ['기준일자', '요일', '명절', '실적', '달성율(%)', '일증가량', '목표치', '목표치대비실적', '목표치대비실적비율'
                     , '예측치', '전월동일수치', '전월동일수치대비실적', '전월동일대비', '일UV'],
            colModel: [
                {name: 'strdDt', index: 'stdDt', editable: false, align: "center", sortable: false},
                {name: 'cdNm', index: 'cdNm', editable: false, align: "center", sortable: false},
                {name: 'hdayNm', index: 'hdayNm', editable: false, align: "center", sortable: false},
                {name: 'rsltUv', index: 'rsltUv', editable: false, align: "right", formatter: 'integer', search: false, sortable: false},
                {name: 'uvAchivRt', index: 'uvAchivRt', editable: false, align: "right", search: false, sortable: false},
                {name: 'dayIncre', index: 'dayIncre', editable: false, align: "right", formatter: 'integer', search: false, sortable: false},
                {name: 'trgtUv', index: 'trgtUv', editable: false, align: "right", formatter: 'integer', search: false, sortable: false},
                {name: 'difTrgtRslt', index: 'difTrgtRslt', editable: false, align: "right", formatter: 'integer', search: false, sortable: false},
                {name: 'difTrgtRsltRt', index: 'difTrgtRsltRt', editable: false, align: "right", search: false, sortable: false},
                {name: 'estiUv', index: 'estiUv', editable: false, align: "right", formatter: 'integer', search: false, sortable: false},
                {name: 'uv', index: 'uv', editable: false, align: "right", formatter: 'integer', search: false, sortable: false},
                {name: 'difUv', index: 'difUv', editable: false, align: "right", formatter: 'integer', search: false, sortable: false},
                {name: 'difUvRt', index: 'difUvRt', editable: false, align: "right", search: false, sortable: false},
                {name: 'dayUv', index: 'dayUv', editable: false, align: "right", search: false, formatter: 'integer', sortable: false}
            ],
            gridview: true,
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
        angular.element("#dayKpiForGrid").html($table);
        angular.element("#" + $scope.tableId).jqGrid("GridUnload");
        angular.element("#" + $scope.tableId).jqGrid($scope.dayKpiForGridConfig);
    };

    $scope.$on('$destroy', function() {
        //$('#dayKpiForGrid').children().unbind();
        //$('#dayKpiForGrid').children().off();
        $('#dayKpiForGrid').empty();
    });

    /**
     * 검색 조회 callback 함수
     *
     * @params result
     */
    $scope.search = function (result) {
        $scope.searchDateType = result.searchDateType;
        $scope.searchEndDate = result.searchEndDate;
        $scope.searchKpiCode = result.searchCode;

        $scope.drawDayKpi();
    };

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        downloadUrl: '/tmap/kpi/downloadExcelForDayKpi',
        downloadType: 'POI',
        pivotFlag: 'N',
        tableId: 'gview_dayKpiForGrid',
        xlsName: 'tmap_daily_kpi.xls'
    };
}

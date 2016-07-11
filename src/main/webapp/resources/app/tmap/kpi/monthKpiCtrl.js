/**
 * Created by cookatrice on 2015. 6. 11..
 */

function monthKpiCtrl($scope, DATE_TYPE_MONTH, reportSvc) {
    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal(DATE_TYPE_MONTH);
        $scope.searchConfig = {
            startDate: defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain
        };
        $scope.searchDateType = DATE_TYPE_MONTH;
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;
        $scope.drawGrid();
    };

    $scope.drawGrid = function () {
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };

        var url = "/tmap/kpi/mon/grid?startDate=" + $scope.searchStartDate + "&endDate=" + $scope.searchEndDate;

        $scope.tableId = 'monthKpiTableGrid';
        reportSvc.getReportApi(url).then(function (result) {
            var dataLength = result.rows.length;
            if (dataLength != 0) {
                gridMonKpi(result.rows);
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    };

    function gridMonKpi(datas) {
        var prevCell1 = {cellId: undefined, value: undefined};
        var prevPrevCell1 = "";

        //grid
        $scope.monthKpiGridConfig = {
            data: datas,
            datatype: "local",
            height: "100%",
            shrinkToFit: false,
            rowNum: 50000,
            colNames: ['기준일자', 'KPI'
                , 'Active UV', 'T map', 'T map 대중교통'
                , 'Active UV', 'T map', 'T map 대중교통'],
            colModel: [
                {
                    name: 'strdYm', index: 'strdYm', editable: false, align: "center", sortable: false,
                    cellattr: function (rowId, val, rawObject, cm, rdata) {
                        var result;
                        prevPrevCell1 = prevCell1.value;
                        if (prevPrevCell1 == val) {
                            result = ' style="display: none" rowspanid="' + prevCell1.cellId + '"';
                        }
                        else {
                            var cellId = this.id + '_row_' + rowId + '_' + cm.name;
                            result = ' rowspan="1" id="' + cellId + '"';
                            prevCell1 = {cellId: cellId, value: val};
                        }

                        return result;
                    }
                },
                { name: 'kpi', index: 'kpi', editable: false, align: "center", sortable: false },
                { name: 'uvActiveUv', index: 'uvActiveUv', editable: false, formatter: 'integer', align: "center", sortable: false },
                { name: 'uvTmap', index: 'uvTmap', editable: false, formatter: 'integer', align: "center", sortable: false },
                { name: 'uvTmapPublic', index: 'uvTmapPublic', editable: false, formatter: 'integer', align: "center", sortable: false },
                { name: 'avgActiveUv', index: 'avgActiveUv', editable: false, formatter: 'integer', align: "center", sortable: false },
                { name: 'avgTmap', index: 'avgTmap', editable: false, formatter: 'integer', align: "center", sortable: false },
                { name: 'avgTmapPublic', index: 'avgTmapPublic', editable: false, formatter: 'integer', align: "center", sortable: false }
           ],
            gridComplete: function () {
                var grid = $(this);
                $('td[rowspan="1"]', grid).each(function () {
                    var spans = $('td[rowspanid="' + this.id + '"]', grid).length + 1;
                    if (spans > 1) {
                        $(this).attr('rowspan', spans);
                    }
                });
                prevCell1 = {cellId: undefined, value: undefined};
                prevPrevCell1 = "";
                $(this).find('td:hidden').remove();
                $(this).jqGrid('destroyGroupHeader');
                $(this).jqGrid('setGroupHeaders', {
                    useColSpanStyle: true,
                    groupHeaders: [
                        {startColumnName: 'uvActiveUv', numberOfColumns: 3, titleText: 'UV'},
                        {startColumnName: 'avgActiveUv', numberOfColumns: 3, titleText: '인당월평균사용일수'}
                    ]
                });
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = false;
            },
            gridview: true,
            multiselect: false,
            autowidth: true,
            hidegrid: false,
            loadonce: true,
            scroll: true,
            jsonReader: {
                root: "rows"
            }
        };
        var $table = angular.element('<table id="' + $scope.tableId + '" />');
        angular.element("#monthKpiGrid").html($table);
        angular.element("#" + $scope.tableId).jqGrid("GridUnload");
        angular.element("#" + $scope.tableId).jqGrid($scope.monthKpiGridConfig);
    }

    $scope.$on('$destroy', function() {
        //$('#monthKpiGrid').children().unbind();
        //$('#monthKpiGrid').children().off();
        $('#monthKpiGrid').empty();
    });

    /**
     * 검색 조회 callback 함수
     *
     * @params result
     */
    $scope.search = function (result) {
        $scope.searchDateType = result.searchDateType;
        $scope.searchEndDate = result.searchEndDate;

        $scope.drawGrid();
    };

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        //downloadUrl: '/ocb/customer/sosBle/downloadExcel',
        downloadUrl: '/tmap/kpi/mon/downloadExcel',
        downloadType: 'POI',
        pivotFlag: 'N',
        tableId: 'monthKpiGrid',
        titleName: '월별 KPI 관리',
        xlsName: 'month-kpi-management.xls'
    };
}

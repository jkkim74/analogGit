/**
 * Created by cookatrice
 */

function sosBleSyrupCtrl($scope, DATE_TYPE_DAY, reportSvc) {
    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal();
        $scope.searchConfig = {
            startDate: defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain
        };
        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;
        $scope.drawGrid();
    };

    $scope.drawGrid = function () {
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };

        var url = "/ocb/customer/sosBle/grid?startDate=" + $scope.searchStartDate
            + "&endDate=" + $scope.searchEndDate + '&sidx=stdDt&sord=asc';

        $scope.tableId = 'sosBleSyrupTableForGrid';
        reportSvc.getReportApi(url).then(function(result) {
            var dataLength = result.rows.length;
            if (dataLength != 0) {
                gridSosBleSyrup(result.rows);
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    };

    function gridSosBleSyrup(datas) {
        var prevCell1 = { cellId: undefined, value: undefined };
        var prevPrevCell1 = "";

        //grid
        $scope.sosBleSyrupForGridConfig = {
            data: datas,
            datatype: "local",
            height: "100%",
            shrinkToFit: false,
            rowNum: 50000,
            colNames: ['기준일자', '구분'
                , 'BLE 모수', 'BT on', 'BLE 진단', 'Smart Beacon'
                , 'BLE 전단', 'BLE 체크인', 'BT on', 'Smart Beacon', '매장전단', '상권전단'
                , '(S)BLE ∩ (O)BLE 전단+체크인', '(S)BLE ∩ (O)BLE 전단', '(S)BLE ∩ (O)BLE 체크인', '(S)BT on ∩ (O) BT on' ],
            colModel: [
                {name: 'stdDt', index: 'stdDt', editable: false, align: "center", sortable: false,
                    cellattr: function (rowId, val, rawObject, cm) {
                        var result;
                        prevPrevCell1 = prevCell1.value;
                        if (prevPrevCell1 == val) {
                            result = ' style="display: none" rowspanid="' + prevCell1.cellId + '"';
                        }
                        else {
                            var cellId = this.id + '_row_' + rowId + '_' + cm.name;
                            result = ' rowspan="1" id="' + cellId + '"';
                            prevCell1 = { cellId: cellId, value: val };
                        }

                        return result;
                    }
                },
                {name: 'comCdNm', index: 'comCdNm', editable: false, align: "center", sortable: false},
                {name: 'syrupBleCnt', index: 'syrupBleCnt', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'syrupBtOnCnt', index: 'syrupBtOnCnt', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'syrupBleFlyrCnt', index: 'syrupBleFlyrCnt', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'syrupSmtBcnCnt', index: 'syrupSmtBcnCnt', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'ocbBleFlyrCnt', index: 'ocbBleFlyrCnt', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'ocbBleChkinCnt', index: 'ocbBleChkinCnt', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'ocbBtOnCnt', index: 'ocbBtOnCnt', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'ocbSmtBcnCnt', index: 'ocbSmtBcnCnt', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'ocbStorFlyrCnt', index: 'ocbStorFlyrCnt', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'ocbTraraFlyrCnt', index: 'ocbTraraFlyrCnt', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'bleFlyrChkinCnt', index: 'bleFlyrChkinCnt', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'bleFlyrCnt', index: 'bleFlyrCnt', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'bleChkinCnt', index: 'bleChkinCnt', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'bleBtonCnt', index: 'bleBtonCnt', editable: false, formatter: 'integer', align: "center", sortable: false}
            ],
            gridComplete: function () {
                var grid = $(this);
                $('td[rowspan="1"]', grid).each(function () {
                    var spans = $('td[rowspanid="' + this.id + '"]', grid).length + 1;
                    if (spans > 1) {
                        $(this).attr('rowspan', spans);
                    }
                });
                prevCell1 = { cellId: undefined, value: undefined };
                prevPrevCell1 = "";
                $(this).find('td:hidden').remove();
                $(this).jqGrid('destroyGroupHeader');
                $(this).jqGrid('setGroupHeaders', {
                    useColSpanStyle: true,
                    groupHeaders: [
                        {startColumnName: 'syrupBleCnt', numberOfColumns: 4, titleText: 'Syrup 모수'},
                        {startColumnName: 'ocbBleFlyrCnt', numberOfColumns: 6, titleText: 'OCB 모수'},
                        {startColumnName: 'bleFlyrChkinCnt', numberOfColumns: 4, titleText: 'Syrup ∩ OCB 모수'}
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
        angular.element("#sosBleSyrupForGrid").html($table);
        angular.element("#" + $scope.tableId).jqGrid("GridUnload");
        angular.element("#" + $scope.tableId).jqGrid($scope.sosBleSyrupForGridConfig);
    }

    $scope.$on('$destroy', function() {
        $('#sosBleSyrupForGrid').empty();
    });

    /**
     * 검색 조회 callback 함수
     *
     * @params result
     */
    $scope.search = function (result) {
        $scope.searchDateType = result.searchDateType;
        $scope.searchStartDate = result.searchStartDate;
        $scope.searchEndDate = result.searchEndDate;

        $scope.drawGrid();
    };

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        downloadUrl: '/ocb/customer/sosBle/downloadExcel',
        downloadType: 'POI',
        pivotFlag: 'N',
        tableId: 'sosBleForGrid',
        titleName: 'SOS BLE 고객모수',
        xlsName: 'sos_ble.xls'
    };
}

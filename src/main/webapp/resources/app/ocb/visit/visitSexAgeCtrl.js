function visitSexAgeCtrl($scope, API_BASE_URL, DATE_TYPE_DAY, reportSvc) {

    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal();
        $scope.searchConfig = {
            startDate: defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain
        };
        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;
        $scope.drawVisitSexAge();
    };

    $scope.drawVisitSexAge = function () {
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };
//        var url = API_BASE_URL + '/ocb/visit/sexage/grid?dateType=' + $scope.searchDateType + '&startDate='
        var url = '/ocb/visit/sexage/grid?dateType=' + $scope.searchDateType + '&startDate='
            + $scope.searchStartDate + '&endDate=' + $scope.searchEndDate + '&sidx=stdDt&sord=asc';

        $scope.tableId = 'visitSexAgeTableForGrid';
        reportSvc.getReportApi(url).then(function(result) {
            var dataLength = result.rows.length;
            if (dataLength != 0) {
                gridVisitSexAge(result.rows);
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    };

    function gridVisitSexAge(datas) {
        var prevCell1 = { cellId: undefined, value: undefined };
        var prevPrevCell1 = "";
        var prevCell2 = { cellId: undefined, value: undefined };

        $scope.visitSexAgeForGridConfig = {
            data : datas,
            datatype: "local",
            height: "100%",
            shrinkToFit: true,
            rowNum: 50000,
            colNames: ['기준일자', '성별', '연령대', 'UV', 'LV(Login Visitor)', 'RV(Returning Visitor)', '방문횟수', '체류시간', 'PV', 'Bounce Rate'],
            colModel: [
                {name: 'stdDt', index: 'stdDt', editable: false, align: "center", frozen: true, sortable: true,
                    cellattr: function (rowId, val, rawObject, cm) {
                        var result;
                        prevPrevCell1 = prevCell1.value;
                        if (prevPrevCell1 == val) {
                            result = ' style="display: none" rowspanid="' + prevCell1.cellId + '"';
                        } else {
                            var cellId = this.id + '_row_' + rowId + '_' + cm.name;
                            result = ' rowspan="1" id="' + cellId + '"';
                            prevCell1 = { cellId: cellId, value: val };
                        }
                        return result;
                    }
                },
                {name: 'sexIndNm', index: 'sexIndNm', editable: false, align: "center", frozen: true, sortable: false,
                    cellattr: function (rowId, val, rawObject, cm) {
                        var result;
                        if (prevPrevCell1 == rawObject.stdDt && prevCell2.value == val) {
                            result = ' style="display: none" rowspanid="' + prevCell2.cellId + '"';
                        } else {
                            var cellId = this.id + '_row_' + rowId + '_' + cm.name;
                            result = ' rowspan="1" id="' + cellId + '"';
                            prevCell2 = { cellId: cellId, value: val };
                        }
                        return result;
                    }
                },
                {name: 'ageLgrpNm', index: 'ageLgrpNm', editable: false, align: "center", frozen: true, sortable: false},
                {name: 'uv', index: 'uv', editable: false, formatter: 'integer', align: "right", search: false, sortable: false},
                {name: 'lv', index: 'lv', editable: false, formatter: 'integer', align: "right", search: false, sortable: false},
                {name: 'rv', index: 'rv', editable: false, formatter: 'integer', align: "right", search: false, sortable: false},
                {name: 'vstCnt', index: 'vstCnt', editable: false, formatter: 'integer', align: "right", search: false, sortable: false},
                {name: 'timeSptFVst', index: 'uv', editable: false, formatter: 'integer', align: "right", search: false, sortable: false},
                {name: 'pv', index: 'pv', editable: false, formatter: 'integer', align: "right", search: false, sortable: false},
                {name: 'buncRate', index: 'buncRate', editable: false, formatter: 'integer', align: "right", search: false, sortable: false}
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
            // rowspan 갯수 산정해서 병합 처리.
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
                prevCell2 = { cellId: undefined, value: undefined };
                grid.find('td:hidden').remove();
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = false;
            }
        };

        var $table = angular.element('<table id="' + $scope.tableId + '" />');
        angular.element("#visitSexAgeForGrid").html($table);
        angular.element("#" + $scope.tableId).jqGrid("GridUnload");
        angular.element("#" + $scope.tableId).jqGrid($scope.visitSexAgeForGridConfig);
    }

    $scope.$on('$destroy', function() {
        $('#visitSexAgeForGrid').empty();
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
        $scope.drawVisitSexAge();
    };

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        downloadUrl: '/ocb/visit/downloadExcelForSexAge',
        downloadType: 'POI',
        pivotFlag: 'N',
        tableId: 'gview_visitSexAgeForGrid',
        xlsName: 'visit-sex_age.xls'
    };
}

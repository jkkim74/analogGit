/**
 * Created by lko on 2014-09-30.
 */
function statisticsCtrl($scope, adminSvc, adminReportSvc, DATE_TYPE_DAY) {
    $scope.init = function () {
        $scope.dateFlag = true;
        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.adminDateTypes = [{key: 'day', label: '일간'}];
        var defaultCal = adminReportSvc.defaultCal($scope.searchDateType);
        $scope.selectedCalStart = defaultCal.startDateStrPlain;
        $scope.selectedCalEnd = defaultCal.endDateStrPlain;
        $scope.searchStartDate = $scope.selectedCalStart;
        $scope.searchEndDate = $scope.selectedCalEnd;
        callStatisticsResult();
    };

    function callStatisticsResult () {
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };
        var url = '/statistics/dayVisitor?startDate=' + $scope.selectedCalStart
            + '&endDate=' + $scope.selectedCalEnd;

        $scope.tableId = 'statisticsTableForGrid';
        adminSvc.getAdminApi(url).then(function(result) {
            var dataLength = result.rows.length;
            if (dataLength != 0) {
                gridStatistics(result.rows);
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    }

    function gridStatistics(datas) {
        var prevCell1 = { cellId: undefined, value: undefined };
        var prevPrevCell1 = "";
        var prevPrevCell2 = "";
        var prevCell2 = { cellId: undefined, value: undefined };
        var prevCell3 = { cellId: undefined, value: undefined };

        $scope.statisticsForGridConfig = {
            data : datas,
            datatype: "local",
            height: "600px",
            shrinkToFit: true,
            rowNum: 50000,
            colNames: ['일자', '서비스', '카테고리', 'menu', '방문횟수'],
            colModel: [
                {name: 'visitDate', index: 'visitDate', editable: false, align: "center", sortable: true,
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
                {name: 'serviceCode', index: 'serviceCode', editable: false, align: "center", sortable: false,
                    cellattr: function (rowId, val, rawObject, cm) {
                        var result;
                        prevPrevCell2 = prevCell2.value;
                        if (prevPrevCell1 == rawObject.visitDate && prevCell2.value == val) {
                            result = ' style="display: none" rowspanid="' + prevCell2.cellId + '"';
                        }
                        else {
                            var cellId = this.id + '_row_' + rowId + '_' + cm.name;
                            result = ' rowspan="1" id="' + cellId + '"';
                            prevCell2 = { cellId: cellId, value: val };
                        }

                        return result;
                    }
                },
                {name: 'categoryNm', index: 'categoryNm', editable: false, align: "center", sortable: false,
                    cellattr: function (rowId, val, rawObject, cm) {
                        var result;
                        if (prevPrevCell2 == rawObject.serviceCode && prevCell3.value == val) {
                            result = ' style="display: none" rowspanid="' + prevCell3.cellId + '"';
                        }
                        else {
                            var cellId = this.id + '_row_' + rowId + '_' + cm.name;
                            result = ' rowspan="1" id="' + cellId + '"';
                            prevCell3 = { cellId: cellId, value: val };
                        }

                        return result;
                    }
                },
                {name: 'menuNm', index: 'menuNm', editable: false, align: "center", sortable: false
                },
                {name: 'visitCount', index: 'visitCount', editable: false, align: "center", sortable: false, formatter:'currency', formatoptions:{decimalSeparator:"", thousandsSeparator: ",", decimalPlaces: 0} }
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
                prevCell3 = { cellId: undefined, value: undefined };
                grid.find('td:hidden').remove();
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = false;
            }
        };
        var $table = angular.element('<table id="' + $scope.tableId + '" />');
        angular.element("#statisticsForGrid").html($table);
        angular.element("#" + $scope.tableId).jqGrid("GridUnload");
        angular.element("#" + $scope.tableId).jqGrid($scope.statisticsForGridConfig);
    }

    $scope.$on('$destroy', function() {
        //$('#statisticsForGrid').children().unbind();
        //$('#statisticsForGrid').children().off();
        $('#statisticsForGrid').empty();
    });

    /**
     * 검색 callback
     * @param result
     */
    $scope.search = function(result) {
        $scope.searchDateType = result.searchDateType;
        $scope.selectedCalStart = result.selectedCalStart;
        $scope.selectedCalEnd = result.selectedCalEnd;
        $scope.searchStartDate = $scope.selectedCalStart;
        $scope.searchEndDate = $scope.selectedCalEnd;
        callStatisticsResult();
    };

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        downloadUrl : '/statistics/downloadExcelForDayVisitor',
        downloadType : 'POI',
        pivotFlag : 'N',
        tableId : 'statisticsForGrid',
        xlsName : 'day-Visitor.xls'
    };
}

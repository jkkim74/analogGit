function pushTickerStatCtrl($scope, DATE_TYPE_DAY, reportSvc) {
    $scope.init = function () {
        var defaultCal = reportSvc.defaultCustomCal('day', 7, 4, 6);
        $scope.searchConfig = {
            startDate: defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain,
            dayPeriod: 7,
            weekPeriod: 4,
            monthPeriod: 6
        };

        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;
        $scope.drawPushTickerStat();
    };

    $scope.drawPushTickerStat = function () {
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };
        var url = '/syrup/etc/pushTickerStatDtl/grid?dateType=' + $scope.searchDateType + '&startDate='
            + $scope.searchStartDate + '&endDate=' + $scope.searchEndDate + '&sidx=stdDt&sord=asc';

        $scope.tableId = 'pushTickerStatTableForGrid';

        reportSvc.getReportApi(url).then(function(result) {
            var dataLength = result.rows.length;
            if (dataLength != 0) {
                gridRcmdStat(result.rows);
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    };
    function gridRcmdStat(datas) {
        var prevCell1 = { cellId: undefined, value: undefined };
        var prevPrevCell1 = "";
        var prevPrevCell2 = "";
        var prevCell2 = { cellId: undefined, value: undefined };
        var dateLabel = "기준일자";
        if ($scope.searchDateType == "week") {
            dateLabel = "표시년주차";
        } else if ($scope.searchDateType == "month") {
            dateLabel = "기준년월";
        }
        $scope.pushTickerStatForGridConfig = {
            data : datas,
            datatype: "local",
            height: "100%",
            shrinkToFit: true,
            rowNum: 50000,
            colNames: ["유형", dateLabel, "푸시메시지", "대상건수", "발송완료건수", "발송성공률", "UV"
            ],
            colModel: [
                {name: 'cdNm', index: 'cdNm', width: 40, editable: false, align: "center", frozen: true, sortable: true,
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
                {name: 'dispDt', index: 'dispDt', editable: false, align: "center", frozen: true, sortable: false,
                    cellattr: function (rowId, val, rawObject, cm) {
                        var result;
                        prevPrevCell2 = prevCell2.value;
                        if (prevPrevCell1 == rawObject.cdNm && prevCell2.value == val) {
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
                {name: 'pushMsg', index: 'pushMsg', editable: false, align: "center", frozen: true, sortable: false},
                {name: 'tgtCnt', index: 'tgtCnt', editable: false, align: "right", frozen: true, sortable: false, formatter:'currency', formatoptions:{decimalSeparator:"", thousandsSeparator: ",", decimalPlaces: 0}},
                {name: 'sendCnt', index: 'sendCnt', editable: false, align: "right", frozen: true, sortable: false, formatter:'currency', formatoptions:{decimalSeparator:"", thousandsSeparator: ",", decimalPlaces: 0}},
                {name: 'sendSucCnt', index: 'sendSucCnt', editable: false, align: "right", frozen: true, sortable: false, formatter:'integer'},
                {name: 'uv', index: 'uv', editable: false, align: "right", frozen: true, sortable: false, formatter:'currency', formatoptions:{decimalSeparator:"", thousandsSeparator: ",", decimalPlaces: 0}}
            ],
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
                prevPrevCell2 = "";
                prevCell2 = { cellId: undefined, value: undefined };
                grid.find('td:hidden').remove();
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = false;
            }
        };

        var $table = angular.element('<table id="' + $scope.tableId + '" />');
        angular.element("#pushTickerStatForGrid").html($table);
        angular.element("#" + $scope.tableId).jqGrid("GridUnload");
        angular.element("#" + $scope.tableId).jqGrid($scope.pushTickerStatForGridConfig);
    }

    $scope.$on('$destroy', function() {
        $('#pushTickerStatForGrid').empty();
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
        $scope.drawPushTickerStat();
    };

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        downloadUrl : '/syrup/etc/downloadExcelForPushTickerStat',
        downloadType : 'POI',
        pivotFlag : 'N',
        tableId: 'pushTickerStatForGrid',
        xlsName: 'push-ticker-stat.xls'
    };
}


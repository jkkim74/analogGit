function couponLocStatCtrl($scope, DATE_TYPE_DAY, reportSvc) {
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
        $scope.drawCouponLocStat();
    };

    $scope.drawCouponLocStat = function () {
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };
        var url = '/syrup/coupon/cponlocstat/grid?dateType=' + $scope.searchDateType + '&startDate='
            + $scope.searchStartDate + '&endDate=' + $scope.searchEndDate + '&sidx=stdDt&sord=asc';

        $scope.tableId = 'couponLocStatTableForGrid';
        reportSvc.getReportApi(url).then(function(result) {
            var dataLength = result.rows.length;
            if (dataLength != 0) {
                gridCouponLocStat(result.rows);
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    };

    function gridCouponLocStat(datas) {
        var prevCell1 = { cellId: undefined, value: undefined };
        var prevPrevCell1 = "";
        var prevPrevCell2 = "";
        var prevPrevCell3 = "";
        var prevCell2 = { cellId: undefined, value: undefined };
        var prevCell3 = { cellId: undefined, value: undefined };
        var prevCell4 = { cellId: undefined, value: undefined };
        $scope.couponLocStatForGridConfig = {
            data : datas,
            datatype: "local",
            height: "100%",
            shrinkToFit: true,
            rowNum: 50000,
            colNames: ['기간', '구분', '위치', '브랜드명', '쿠폰명(배너명)', '클릭수'
                , '클릭고객수'
            ],
            colModel: [
                {name: 'dispDt', index: 'dispDt', width: 90, editable: false, sortable: true, align: "center",
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
                {name: 'cdNm3', index: 'cdNm3', editable: false, sortable: false, align: "center",
                    cellattr: function (rowId, val, rawObject, cm) {
                        var result;
                        prevPrevCell2 = prevCell2.value;
                        if (prevPrevCell1 == rawObject.dispDt && prevCell2.value == val) {
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
                {name: 'tgtOrder', index: 'tgtOrder', width: 40, editable: false, sortable: false, align: "center",
                    cellattr: function (rowId, val, rawObject, cm) {
                        var result;
                        prevPrevCell3 = prevCell3.value;
                        if (prevPrevCell2 == rawObject.cdNm3 && prevCell3.value == val) {
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
                {name: 'brandNm', index: 'brandNm', editable: false, sortable: false, align: "center",
                    cellattr: function (rowId, val, rawObject, cm) {
                        var result;
                        if (prevPrevCell3 == rawObject.tgtOrder && prevCell4.value == val) {
                            result = ' style="display: none" rowspanid="' + prevCell4.cellId + '"';
                        }
                        else {
                            var cellId = this.id + '_row_' + rowId + '_' + cm.name;
                            result = ' rowspan="1" id="' + cellId + '"';
                            prevCell4 = { cellId: cellId, value: val };
                        }

                        return result;
                    }
                },
                {name: 'cpNm', index: 'cpNm', editable: false, sortable: false, align: "center"},
                {name: 'pageCnt', index: 'pageCnt', editable: false, sortable: false, align: "right", formatter:'currency', formatoptions:{decimalSeparator:"", thousandsSeparator: ",", decimalPlaces: 0}},
                {name: 'pageUserCnt', index: 'pageUserCnt', editable: false, sortable: false, align: "right", formatter:'currency', formatoptions:{decimalSeparator:"", thousandsSeparator: ",", decimalPlaces: 0}}
            ],
            gridview:true,
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
                prevPrevCell3 = "";
                prevCell2 = { cellId: undefined, value: undefined };
                prevCell3 = { cellId: undefined, value: undefined };
                prevCell4 = { cellId: undefined, value: undefined };
                grid.find('td:hidden').remove();
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = false;
            }
        };
        var $table = angular.element('<table id="' + $scope.tableId + '" />');
        angular.element("#couponLocStatForGrid").html($table);
        angular.element("#" + $scope.tableId).jqGrid("GridUnload");
        angular.element("#" + $scope.tableId).jqGrid($scope.couponLocStatForGridConfig);
    }

    $scope.$on('$destroy', function() {
        $('#couponLocStatForGrid').empty();
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
        $scope.drawCouponLocStat();
    };

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        downloadUrl : '/syrup/coupon/downloadExcelForCouponLocStat',
        downloadType : 'POI',
        pivotFlag : 'N',
        tableId: 'couponLocStatForGrid',
        xlsName: 'coupon-loc-stat.xls'
    };
}

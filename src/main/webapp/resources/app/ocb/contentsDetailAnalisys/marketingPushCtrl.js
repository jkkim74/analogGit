function marketingPushCtrl($scope, DATE_TYPE_DAY, reportSvc) {
    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal();
        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchConfig = {
            startDate: defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain
        };
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;
        callResult();
    };

    function callResult() {
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };
        var url = '/ocb/contentsDetailAnalisys/marketingPush/get?dateType=' + $scope.searchDateType
            + '&startDate=' + $scope.searchStartDate + '&endDate=' + $scope.searchEndDate + '&sidx=stdDt&sord=asc';

        $scope.tableId = 'marketingPushTableForGrid';
        reportSvc.getReportApi(url).then(function(result) {
            var dataLength = result.rows.length;
            if (dataLength != 0) {
                gridMarketingPush(result.rows);
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    }

    function gridMarketingPush(datas) {
        var prevCell1 = { cellId: undefined, value: undefined };
        var prevCell2 = { cellId: undefined, value: undefined };
        var prevPrevCell1 = "";
        var prevPrevCell2 = "";

        $scope.marketingPushForGridConfig = {
            data: datas,
            datatype: "local",
            height: "100%",
            shrinkToFit: true,
            rowNum: 50000,
            colNames: ['발송일', '발송시작시간', '타이틀', '발송내용', 'Push Type', 'Display Type', 'EventID', 'Big Picture 여부', '발송건수', '도달건수', 'Push 클릭건수', '알림함 클릭건수', '바로적립 클릭건수', '클릭건수 합계', '도달율', '반응율'],
            colModel: [
                {name: 'stdDt', index: 'stdDt', editable: false, align: "center", sortable: true, width: "260",
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
                {name: 'sndTime', index: 'sndTime', editable: false, align: "center", sortable: true, width: "200",
                    cellattr: function (rowId, val, rawObject, cm) {
                        var result;
                        prevPrevCell2 = prevCell2.value;
                        if (prevPrevCell2 == val) {
                            result = ' style="display: none" rowspanid="' + prevCell2.cellId + '"';
                        }
                        else {
                            var cellId = this.id + '_row_' + rowId + '_' + cm.name;
                            result = ' rowspan="1" id="' + cellId + '"';
                            prevCell2 = { cellId: cellId, value: val };
                        }
                        if (val.indexOf("_SUMMARY") != -1) {
                            result += ' cell1Id="' + prevCell1.cellId + '" class="summaryForRow"';
                        }

                        return result;
                    }
                },
                {name: 'title', index: 'title', editable: false, align: "left", sortable: false, width: "500" },
                {name: 'msg', index: 'msg', editable: false, align: "left", sortable: false, width: "500" },
                {name: 'pushType', index: 'pushType', editable: false, align: "left", sortable: false },
                {name: 'displayType', index: 'displayType', editable: false, align: "left", sortable: false },
                {name: 'eventId', index: 'eventId', editable: false, align: "left", sortable: false },
                {name: 'bigPicture', index: 'bigPicture', editable: false, align: "left", sortable: false },
                {name: 'custCnt', index: 'custCnt', editable: false, align: "right", sortable: false, formatter: 'currency', formatoptions: {decimalSeparator: "", thousandsSeparator: ",", decimalPlaces: 0} },
                {name: 'pushRcvCustCnt', index: 'pushRcvCustCnt', editable: false, align: "right", sortable: false, formatter: 'currency', formatoptions: {decimalSeparator: "", thousandsSeparator: ",", decimalPlaces: 0} },
                {name: 'pushClkCustCnt', index: 'pushClkCustCnt', editable: false, align: "right", sortable: false, formatter: 'currency', formatoptions: {decimalSeparator: "", thousandsSeparator: ",", decimalPlaces: 0} },
                {name: 'notiClkCustCnt', index: 'notiClkCustCnt', editable: false, align: "right", sortable: false, formatter: 'currency', formatoptions: {decimalSeparator: "", thousandsSeparator: ",", decimalPlaces: 0} },
                {name: 'dirtClkCnt', index: 'dirtClkCnt', editable: false, align: "right", sortable: false, formatter: 'currency', formatoptions: {decimalSeparator: "", thousandsSeparator: ",", decimalPlaces: 0} },
                {name: 'clkSum', index: 'clkSum', editable: false, align: "right", sortable: false, formatter: 'currency', formatoptions: {decimalSeparator: "", thousandsSeparator: ",", decimalPlaces: 0} },
                {name: 'rcvPer', index: 'rcvPer', editable: false, align: "right", sortable: false, formatter: 'currency', formatoptions: {decimalSeparator: "", thousandsSeparator: ",", decimalPlaces: 0} },
                {name: 'ratPer', index: 'ratPer', editable: false, align: "right", sortable: false, formatter: 'currency', formatoptions: {decimalSeparator: "", thousandsSeparator: ",", decimalPlaces: 0} }
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
                    // subtraction summaryForRow class length
                    spans -= $('td[rowspanid="' + this.id + '"]', grid).parent().has(".summaryForRow").length;

                    if (spans > 1) {
                        $(this).attr('rowspan', spans);
                    }
                });
                // setting for summary row
                $('td.summaryForRow', grid).each(function () {
                    var cell = $(this).parent().find('td[rowspanid="' + $(this).attr('cell1Id') + '"]', grid);
                    cell.show();
                    cell.attr('colspan','8').text("일별 집계");
                    cell.parent().addClass("summaryRow");
                    cell.parent().children().eq(7).remove();
                    cell.parent().children().eq(6).remove();
                    cell.parent().children().eq(5).remove();
                    cell.parent().children().eq(4).remove();
                    cell.parent().children().eq(3).remove();
                    cell.parent().children().eq(2).remove();
                    cell.parent().children().eq(1).remove();
                });
                prevCell1 = { cellId: undefined, value: undefined };
                prevCell2 = { cellId: undefined, value: undefined };
                prevPrevCell1 = "";
                prevPrevCell2 = "";
                grid.find('td:hidden').remove();
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = false;
            }
        };
        var $table = angular.element('<table id="' + $scope.tableId + '" />');
        angular.element("#marketingPushForGrid").html($table);
        angular.element("#" + $scope.tableId).jqGrid("GridUnload");
        angular.element("#" + $scope.tableId).jqGrid($scope.marketingPushForGridConfig);
    }

    $scope.$on('$destroy', function() {
        $('#marketingPushForGrid').empty();
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
        callResult();
    };
    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        downloadUrl: '/ocb/contentsDetailAnalisys/downloadExcelForMarketingPush',
        downloadType: 'POI',
        pivotFlag: 'N',
        tableId: 'marketingPushForGrid',
        xlsName: 'OCB_컨텐츠상세분석_마케팅PUSH.xls'
    };
}

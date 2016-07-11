/**
 * Created by lko on 2014-10-14.
 */

function pushSendCtrl($scope, DATE_TYPE_DAY, reportSvc) {

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

        var url = '/ocb/proactiveNoti/mktPushSndRslt/get?dateType=' + $scope.searchDateType
            + '&startDate=' + $scope.searchStartDate + '&endDate=' + $scope.searchEndDate + '&sidx=stdDt&sord=asc';

        $scope.tableId = 'mktPushSndRsltTableForGrid';
        reportSvc.getReportApi(url).then(function(result) {
            var dataLength = result.rows.length;
            if (dataLength != 0) {
                gridMktPushSndRslt(result.rows);
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    }

    function gridMktPushSndRslt(datas) {
        var prevCell1 = { cellId: undefined, value: undefined };
        var prevCell2 = { cellId: undefined, value: undefined };
        var prevCell3 = { cellId: undefined, value: undefined };
        var prevCell4 = { cellId: undefined, value: undefined };
        var prevCell5 = { cellId: undefined, value: undefined };
        var prevPrevCell1 = "";
        var prevPrevCell2 = "";
        var prevPrevCell3 = "";
        var prevPrevCell4 = "";
        var prevPrevCell5 = "";

        $scope.mktPushSndRsltForGridConfig = {
            data: datas,
            datatype: "local",
            height: "100%",
            shrinkToFit: true,
            rowNum: 50000,
            colNames: ['발송일', '발송타입', '발송내용', 'OCB App.버전', '발송시도건수', 'App.도달건수', '클릭건수', 'App.도달율', '고객반응율'],
            colModel: [
                {name: 'stdDt', index: 'stdDt', editable: false, align: "center", sortable: true,
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
                {name: 'pushTyp', index: 'pushTyp', editable: false, align: "center", sortable: false,
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

                        return result;
                    }
                },
                {name: 'sndMsg', index: 'sndMsg', editable: false, align: "left", sortable: false,
                    cellattr: function (rowId, val, rawObject, cm) {
                        var result;
                        prevPrevCell3 = rawObject.stdDt + prevCell3.value;
                        if (prevPrevCell3 == rawObject.stdDt + val) {
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
                {name: 'appVer', index: 'appVer', editable: false, align: "center", sortable: false, formatter: function (cellvalue) {
                    return DomHelper.subCell(cellvalue, 1, "ZTOTAL");
                }},
                {name: 'sndCustCnt', index: 'sndCustCnt', editable: false, align: "right", sortable: false, formatter: 'currency', formatoptions: {decimalSeparator: "", thousandsSeparator: ",", decimalPlaces: 0} },
                {name: 'rcvCustCnt', index: 'rcvCustCnt', editable: false, align: "right", sortable: false, formatter: 'currency', formatoptions: {decimalSeparator: "", thousandsSeparator: ",", decimalPlaces: 0} },
                {name: 'lchCustCnt', index: 'lchCustCnt', editable: false, align: "right", sortable: false, formatter: 'currency', formatoptions: {decimalSeparator: "", thousandsSeparator: ",", decimalPlaces: 0} },
                {name: 'rcvRate', index: 'rcvRate', editable: false, align: "right", sortable: false, formatter: 'currency', formatoptions: {decimalSeparator: "", thousandsSeparator: ",", decimalPlaces: 2},
                    cellattr: function (rowId, val, rawObject, cm) {
                        var result;
                        if (prevPrevCell3 == rawObject.stdDt + rawObject.sndMsg && prevCell4.value == val) {
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
                {name: 'lchRate', index: 'lchRate', editable: false, align: "right", sortable: false, formatter: 'currency', formatoptions: {decimalSeparator: "", thousandsSeparator: ",", decimalPlaces: 2},
                    cellattr: function (rowId, val, rawObject, cm) {
                        var result;
                        if (prevPrevCell3 == rawObject.stdDt + rawObject.sndMsg && prevCell5.value == val) {
                            result = ' style="display: none" rowspanid="' + prevCell5.cellId + '"';
                        }
                        else {
                            var cellId = this.id + '_row_' + rowId + '_' + cm.name;
                            result = ' rowspan="1" id="' + cellId + '"';
                            prevCell5 = { cellId: cellId, value: val };
                        }

                        return result;
                    }
                }
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
                prevCell2 = { cellId: undefined, value: undefined };
                prevCell3 = { cellId: undefined, value: undefined };
                prevCell4 = { cellId: undefined, value: undefined };
                prevCell5 = { cellId: undefined, value: undefined };
                prevPrevCell1 = "";
                prevPrevCell2 = "";
                prevPrevCell3 = "";
                prevPrevCell4 = "";
                prevPrevCell5 = "";
                grid.find('td:hidden').remove();
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = false;
            }
        };
        var $table = angular.element('<table id="' + $scope.tableId + '" />');
        angular.element("#mktPushSndRsltForGrid").html($table);
        angular.element("#" + $scope.tableId).jqGrid("GridUnload");
        angular.element("#" + $scope.tableId).jqGrid($scope.mktPushSndRsltForGridConfig);
    }

    $scope.$on('$destroy', function() {
        $('#mktPushSndRsltForGrid').empty();
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
        downloadUrl: '/ocb/proactiveNoti/downloadExcelForMktPushSndRslt',
        downloadType: 'POI',
        pivotFlag: 'N',
        tableId: 'mktPushSndRsltForGrid',
        xlsName: 'mkt-push-snd_rlt.xls'
    };
}

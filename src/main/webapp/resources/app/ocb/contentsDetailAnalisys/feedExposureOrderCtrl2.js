/**
 * Created by cookatrice on 2014. 6. 11..
 */

function feedExposureOrderCtrl($scope, $http, API_BASE_URL, reportSvc) {

    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal();
        $scope.searchDateType = $scope.searchBoxDateType;
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;

        $scope.drawGrid();
    };

    /**
     * drawGrid
     */
    $scope.drawGrid = function () {
        var url = '/ocb/contentsDetailAnalisys/feedExposureOrder/grid?dateType=' + $scope.searchDateType + '&startDate=' + $scope.searchStartDate + '&endDate=' + $scope.searchEndDate;

        var oldFeedId = { id: undefined, val: undefined };
        var oldDispOrder = { id: undefined, val: undefined };
        var newYnFlag = false;
        var tmpFeedNmId = "";
        var tmpFeedStartDateId = "";
        var tmpFeedEndDateId = "";
        var tmpIsYnId = "";
        var tmpAllianceYnId = "";


        $scope.feedsExposureOrderGrid = {
            url: url,
            id: 'feedsExposureOrderGrid',
            type: "GET",
            datatype: "json",
            height: "500",
            scroll: true,
//            shrinkToFit: true,
//            useColSpanStyle: true,
//            hiddengrid: true,
//            loadui: "block",
            rowNum: 50000,
            gridview:true,
            //rowList: [10, 20, 30],
            colNames: ['피드ID', '피드명', '시작일자', '종료일자', 'IS인벤토리', '제휴사타겟팅', '노출순서', '기준일자', '클릿횟수', '클릭UV', '클릭LV'],
            colModel: [
                {name: 'feedId', index: 'feedId', editable: false, width: 20, align: "right", search: false, sortable: false,
                    cellattr: function (rowId, val, rawObject, cm, rdata) {
                        var result;
                        if (oldFeedId.val == val) {
                            result = ' style="display: none" rowspanid="' + oldFeedId.id + '"';
                            newYnFlag = false;
                        }
                        else {
                            var tmpId = this.id + '_row_' + rowId + '_' + cm.name;
                            result = ' rowspan="1" id="' + tmpId + '"';
                            oldFeedId = { id: tmpId, val: val };
                            newYnFlag = true;
                        }
                        return result;
                    }
                },
                {name: 'feedNm', index: 'feedNm', editable: false, width: 20, align: "right", search: false, sortable: false,
                    cellattr: function (rowId, val, rawObject, cm, rdata) {
                        var result;
                        if (newYnFlag) {
                            var tmpId = this.id + '_row_' + rowId + '_' + cm.name;
                            tmpFeedNmId = tmpId;
                            result = ' rowspan="1" id="' + tmpId + '"';
                        }
                        else {
                            result = ' style="display: none" rowspanid="' + tmpFeedNmId + '"';
                        }
                        return result;
                    }
                },
                {name: 'feedStartDate', index: 'feedStartDate', editable: false, width: 20, align: "right", search: false, sortable: false,
                    cellattr: function (rowId, val, rawObject, cm, rdata) {
                        var result;
                        if (newYnFlag) {
                            var tmpId = this.id + '_row_' + rowId + '_' + cm.name;
                            tmpFeedStartDateId = tmpId;
                            result = ' rowspan="1" id="' + tmpId + '"';
                        }
                        else {
                            result = ' style="display: none" rowspanid="' + tmpFeedStartDateId + '"';
                        }
                        return result;
                    }
                },
                {name: 'feedEndDate', index: 'feedEndDate', editable: false, width: 20, align: "right", search: false, sortable: false,
                    cellattr: function (rowId, val, rawObject, cm, rdata) {
                        var result;
                        if (newYnFlag) {
                            var tmpId = this.id + '_row_' + rowId + '_' + cm.name;
                            tmpFeedEndDateId = tmpId;
                            result = ' rowspan="1" id="' + tmpId + '"';
                        }
                        else {
                            result = ' style="display: none" rowspanid="' + tmpFeedEndDateId + '"';
                        }
                        return result;
                    }
                },
                {name: 'isYn', index: 'isYn', editable: false, width: 20, align: "right", search: false, sortable: false,
                    cellattr: function (rowId, val, rawObject, cm, rdata) {
                        var result;
                        if (newYnFlag) {
                            var tmpId = this.id + '_row_' + rowId + '_' + cm.name;
                            tmpIsYnId = tmpId;
                            result = ' rowspan="1" id="' + tmpId + '"';
                        }
                        else {
                            result = ' style="display: none" rowspanid="' + tmpIsYnId + '"';
                        }
                        return result;
                    }
                },
                {name: 'allianceYn', index: 'allianceYn', editable: false, width: 20, align: "right", search: false, sortable: false,
                    cellattr: function (rowId, val, rawObject, cm, rdata) {
                        var result;
                        if (newYnFlag) {
                            var tmpId = this.id + '_row_' + rowId + '_' + cm.name;
                            tmpAllianceYnId = tmpId;
                            result = ' rowspan="1" id="' + tmpId + '"';
                        }
                        else {
                            result = ' style="display: none" rowspanid="' + tmpAllianceYnId + '"';
                        }
                        return result;
                    }
                },
                {name: 'dispOrder', index: 'dispOrder', editable: false, width: 20, align: "right", search: false, sortable: false,
                    cellattr: function (rowId, val, rawObject, cm, rdata) {
                        var result;
                        if (oldFeedId.val == rawObject.feedId && oldDispOrder.val == val) {
                            result = ' style="display: none" rowspanid="' + oldDispOrder.id + '"';
                        }
                        else {
                            var tmpId = this.id + '_row_' + rowId + '_' + cm.name;
                            result = ' rowspan="1" id="' + tmpId + '"';
                            oldDispOrder = { id: tmpId, val: val };
                        }
                        return result;
                    }
                },
                {name: 'stdDt', index: 'stdDt', editable: false, width: 20, align: "center", frozen: true, sortable: true},
                {name: 'clickCnt', index: 'clickCnt', editable: false, width: 20, formatter: 'integer', align: "right", search: false, sortable: false},
                {name: 'clickCntOnUv', index: 'clickCntOnUv', editable: false, width: 20, formatter: 'integer', align: "right", search: false, sortable: false},
                {name: 'clickCntOnLv', index: 'clickCntOnLv', editable: false, width: 20, formatter: 'integer', align: "right", search: false, sortable: false}
            ],
            sortname: 'stdDt',
            sortorder: 'asc',
            multiselect: false,
            autowidth: true,
            //width: "950",
//            excel: true,
            jsonReader: {
                root: "rows",
                page: "page",
                total: "total",
                records: "records",
                repeatitems: false,
                cell: "cell"
            },
            // rowspan 갯수 산정해서 병합 처리.
            gridComplete: function () {
                var grid = this;
                $('td[rowspan="1"]', grid).each(function () {
                    var spans = $('td[rowspanid="' + this.id + '"]', grid).length + 1;

                    if (spans > 1) {
                        $(this).attr('rowspan', spans);
                    }
                });
                oldFeedId = { id: undefined, val: undefined };
                oldDispOrder = { id: undefined, val: undefined };
                newYnFlag = false;
                tmpFeedNmId = "";
                tmpFeedStartDateId = "";
                tmpFeedEndDateId = "";
                tmpIsYnId = "";
                tmpAllianceYnId = "";

                $(this).find('td:hidden').remove();

//                var tm = $("#feedsExposureOrderGrid").jqGrid('getGridParam','totaltime');
//                console.log("time check:" + tm);
            },
            loadComplete: function () {
                //console.log($(this).getGridParam('records'));
                if ($(this).getGridParam('records') === 0) {
                    $(this).append('<tr><td colspan="10" class="text-center"><h3>no Data</h3></td></tr>');
                }
            }
        };
    };


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
        'tableId': 'feedsExposureOrderGrid',
        'xlsName': 'visit-feed.xls'
    };

}

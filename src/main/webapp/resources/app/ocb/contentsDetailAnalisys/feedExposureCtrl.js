/**
 * Created by cookatrice on 2014. 5. 15..
 */

function feedExposureCtrl($scope, API_BASE_URL, DATE_TYPE_DAY, reportSvc) {
    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal();
        $scope.searchConfig = {
            startDate: defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain
        };
        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;

        /**
         * addType : select
         * label : 조회기준
         * codeUrl : /commonCode/FeedTypeEnum
         *
         * commoncode.java
         * TYPE1("전체(5.1.x 미만포함)"), TYPE2("5.1.x 이상");
         *
         * menu_search_option
         * 37	select	조회기준	day	/commonCode/FeedTypeEnum	20140825141311	period
         */
        $scope.feedType = 'TYPE1';
        $scope.drawGrid();
    };

    /**
     * drawGrid
     */
    $scope.drawGrid = function () {
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };
        var url = '/ocb/contentsDetailAnalisys/feedExposure/grid?dateType=' + $scope.searchDateType
            + '&startDate=' + $scope.searchStartDate + '&endDate=' + $scope.searchEndDate + '&sidx=stdDt&sord=asc' +
            '&feedType=' + $scope.feedType;

        $scope.tableId = 'feedExposureTableForGrid';
        reportSvc.getReportApi(url).then(function(result) {
            var dataLength = result.rows.length;
            if (dataLength != 0) {
                gridFeedsExposure(result.rows);
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    };

    function gridFeedsExposure(datas) {
        var oldFeedId = { id: undefined, val: undefined };
        var newYnFlag = false;
        var tmpFeedNmId = "";
        var tmpFeedStartDateId = "";
        var tmpFeedEndDateId = "";
        var tmpIsYnId = "";
        var tmpAllianceYnId = "";

        $scope.feedsExposureForGridConfig = {
            data: datas,
            datatype: "local",
            height: "100%",
            shrinkToFit: true,
            rowNum: 50000,
            colNames: ['피드ID', '피드명', '시작일자', '종료일자', 'IS인벤토리', '제휴사타겟팅', '기준일자', '노출횟수', '노출UV', '노출LV', '클릭횟수', '클릭UV', '클릭LV', '구매완료 횟수', '선물 횟수', '다운로드 횟수', '전화하기 횟수'],
            colModel: [
                {name: 'feedId', index: 'feedId', editable: false, align: "center", search: false, sortable: false,
                    cellattr: function (rowId, val, rawObject, cm) {
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
                {name: 'feedNm', index: 'feedNm', editable: false, align: "center", search: false, sortable: false,
                    cellattr: function (rowId, val, rawObject, cm) {
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
                {name: 'feedStartDate', index: 'feedStartDate', editable: false, align: "center", search: false, sortable: false,
                    cellattr: function (rowId, val, rawObject, cm) {
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
                {name: 'feedEndDate', index: 'feedEndDate', editable: false, align: "center", search: false, sortable: false,
                    cellattr: function (rowId, val, rawObject, cm) {
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
                {name: 'isYn', index: 'isYn', editable: false, align: "center", search: false, sortable: false,
                    cellattr: function (rowId, val, rawObject, cm) {
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
                {name: 'allianceYn', index: 'allianceYn', editable: false, align: "center", search: false, sortable: false,
                    cellattr: function (rowId, val, rawObject, cm) {
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
                {name: 'stdDt', index: 'stdDt', editable: false, align: "center", search: false, sortable: false},
                {name: 'expsrCnt', index: 'expsrCnt', editable: false, formatter: 'integer', align: "right", search: false, sortable: false},
                {name: 'expsrCntOnUv', index: 'expsrCntOnUv', editable: false, formatter: 'integer', align: "right", search: false, sortable: false},
                {name: 'expsrCntOnLv', index: 'expsrCntOnLv', editable: false, formatter: 'integer', align: "right", search: false, sortable: false},
                {name: 'clickCnt', index: 'clickCnt', editable: false, formatter: 'integer', align: "right", search: false, sortable: false},
                {name: 'clickCntOnUv', index: 'clickCntOnUv', editable: false, formatter: 'integer', align: "right", search: false, sortable: false},
                {name: 'clickCntOnLv', index: 'clickCntOnLv', editable: false, formatter: 'integer', align: "right", search: false, sortable: false},
                {name: 'prchsCnt', index: 'prchsCnt', editable: false, formatter: 'integer', align: "right", search: false, sortable: false},
                {name: 'presntCnt', index: 'presntCnt', editable: false, formatter: 'integer', align: "right", search: false, sortable: false},
                {name: 'dnldCnt', index: 'dnldCnt', editable: false, formatter: 'integer', align: "right", search: false, sortable: false},
                {name: 'callCnt', index: 'callCnt', editable: false, formatter: 'integer', align: "right", search: false, sortable: false, hidden: true}
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
                var grid = this;
                $('td[rowspan="1"]', grid).each(function () {
                    var spans = $('td[rowspanid="' + this.id + '"]', grid).length + 1;

                    if (spans > 1) {
                        $(this).attr('rowspan', spans);
                    }
                });
                oldFeedId = { id: undefined, val: undefined };
                newYnFlag = false;
                tmpFeedNmId = "";
                tmpFeedStartDateId = "";
                tmpFeedEndDateId = "";
                tmpIsYnId = "";
                tmpAllianceYnId = "";
                $(this).find('td:hidden').remove();
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = false;
            }
        };
        var $table = angular.element('<table id="' + $scope.tableId + '" />');
        angular.element("#feedsExposureForGrid").html($table);
        angular.element("#" + $scope.tableId).jqGrid("GridUnload");
        angular.element("#" + $scope.tableId).jqGrid($scope.feedsExposureForGridConfig);
    }

    $scope.$on('$destroy', function() {
        $('#feedsExposureForGrid').empty();
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
        $scope.feedType = result.searchCode;
        $scope.drawGrid();
    };

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        downloadUrl: '/ocb/contentsDetailAnalisys/downloadExcelForFeedExposure',
        downloadType: 'POI',
        pivotFlag: 'N',
        tableId: 'feedsExposureGrid',
        xlsName: 'visit-feed.xls'
    };

}

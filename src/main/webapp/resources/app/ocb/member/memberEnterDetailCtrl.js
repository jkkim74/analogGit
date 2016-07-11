/**
 * Created by cookatrice on 2014. 5. 8..
 */

function memberEnterDetailCtrl($scope, API_BASE_URL, DATE_TYPE_DAY, reportSvc) {

    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal();
        $scope.searchConfig = {
            startDate : defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain
        };

        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;

        $scope.drawMemberEnterDetailGrid();
    };

    $scope.drawMemberEnterDetailGrid = function () {
        var url = '/ocb/member/enter/detail/grid?dateType=' + $scope.searchDateType + '&startDate=' + $scope.searchStartDate + '&endDate=' + $scope.searchEndDate;
        var prevCell1 = { cellId: undefined, value: undefined };
        var prevPrevCell1 = "";
        var prevCell2 = { cellId: undefined, value: undefined };


        $scope.memberEnterDetail = {
            url: url,
            type: "GET",
            datatype: "json",
            height: "100%",
            id: 'memberEnterDetailGrid',
            rowNum: 50000,
            //rowList: [10, 20, 30],
            colNames: ['기준일자', '성별', '연령대', '신규가입자수', '누적가입자수'],
            colModel: [
                {name: 'stdDt', index: 'stdDt', editable: false, width: 90, align: "center", sortable: true,
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
                    }},
                {name: 'sexIndNm', index: 'sexIndNm', editable: false, width: 90, formatter: '', align: "center", sortable: false,
                    cellattr: function (rowId, val, rawObject, cm) {
                    var result;
                    if (prevPrevCell1 == rawObject.stdDt && prevCell2.value == val) {
                        result = ' style="display: none" rowspanid="' + prevCell2.cellId + '"';
                    }
                    else {
                        var cellId = this.id + '_row_' + rowId + '_' + cm.name;
                        result = ' rowspan="1" id="' + cellId + '"';
                        prevCell2 = { cellId: cellId, value: val };
                    }
                    return result;
                }},
                {name: 'ageLgrpNm', index: 'ageLgrpNm', editable: false, width: 90, formatter: '', align: "center", sortable: false},
                {name: 'newEntrCnt', index: 'newEntrCnt', editable: false, width: 90, formatter: 'integer', align: "right", sortable: false},
                {name: 'acmEntrCnt', index: 'acmEntrCnt', editable: false, width: 90, formatter: 'integer', align: "right", sortable: false}
            ],
            gridview: true,
            sortname: 'stdDt',
            sortorder: 'asc',
            multiselect: false,
            autowidth: true,
            hidegrid: false,
            scroll: true,
            loadonce: true,
            jsonReader: {
                root: "rows",
                page: "page",
                total: "total",
                records: "records",
                repeatitems: false,
                cell: "cell"
            },
            gridComplete: function () {
                var grid = this;
                $('td[rowspan="1"]', grid).each(function () {
                    var spans = $('td[rowspanid="' + this.id + '"]', grid).length + 1;

                    if (spans > 1) {
                        $(this).attr('rowspan', spans);
                    }
                });
                prevCell1 = { cellId: undefined, value: undefined };
                prevPrevCell1 = "";
                prevCell2 = { cellId: undefined, value: undefined };

                $(this).find('td:hidden').remove();
                if ($(this).getGridParam('records') === 0) {
                    $(this).append('<tr><td colspan="9" class="text-center"><br/><br/><h3>no Data</h3><br/><br/></td></tr>');
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
        $scope.drawMemberEnterDetailGrid();
    };

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        downloadUrl : '/ocb/member/downloadExcelForDetail',
        downloadType : 'POI',
        pivotFlag : 'N',
        tableId : 'gview_memberEnterDetailGrid',
        xlsName : 'customer-enter_sex_age.xls'
    };

}

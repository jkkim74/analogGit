/**
 * Created by cookatrice on 2014. 5. 21..
 */

function searchResultClickAlliCtrl($scope, DATE_TYPE_DAY, reportSvc) {
    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal();
        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchConfig = {
            startDate : defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain
        };

        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;

        $scope.drawGrid();
    };

    $scope.drawGrid = function(){
        var prevCell1 = { cellId: undefined, value: undefined };
        var url = '/ocb/searchAnalysis/searchResultClickAlli/grid?dateType='+
            $scope.searchDateType+'&startDate='+$scope.searchStartDate+'&endDate='+$scope.searchEndDate;

        $scope.searchResultClickAlliGrid = {
            url: url,
            id: 'searchResultClickAlli_Grid',
            datatype: "json",
            height: '100%',
            rowNum: 50000,
            rowList: [10, 20, 30],
            colNames: ['기준일','순위','제휴사ID','제휴사명', '클릭횟수'],
            colModel: [
                {name: 'stdDt', index: 'stdDt', editable: false, width: 90, align: "center", sortable: true,
                    cellattr: function (rowId, val, rawObject, cm) {
                        var result;
                        //prevPrevCell1 = prevCell1.value;
                        if (prevCell1.value == val) {
                            result = ' style="display: none" rowspanid="' + prevCell1.cellId + '"';
                        }
                        else {
                            var cellId = this.id + '_row_' + rowId + '_' + cm.name;
                            result = ' rowspan="1" id="' + cellId + '"';
                            prevCell1 = { cellId: cellId, value: val };
                        }

                        return result;
                    }},
                {name: 'rank', index: 'rank', editable: false, width: 90, formatter: 'integer', align: "center", sortable: false},
                {name: 'alliId', index: 'alliId', editable: false, width: 90, formatter: '', align: "center", sortable: false},
                {name: 'alliNm', index: 'alliNm', editable: false, width: 90, formatter: '', align: "center", sortable: false},
                {name: 'clickCnt', index: 'clickCnt', editable: false, width: 90, formatter: 'integer', align: "center", sortable: false}
            ],
            gridview: true,
            sortname: 'rank',
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
                $(this).find('td:hidden').remove();
                if ($(this).getGridParam('records') === 0) {
                    $(this).append('<tr><td colspan="5" class="text-center"><br/><br/><h3>no Data</h3><br/><br/></td></tr>');
                }
            }
        };
    };

    /**
     * 검색 조회 callback 함수
     *
     * @params result
     */
    $scope.search = function(result) {
        $scope.searchDateType = result.searchDateType;
        $scope.searchStartDate = result.searchStartDate;
        $scope.searchEndDate  = result.searchEndDate;

        $scope.drawGrid();
//        drawPivot();
    };

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        'tableId': 'gview_searchResultClickAlli_Grid',
        'xlsName': 'search-result-click-Alli.xls'
    };


}

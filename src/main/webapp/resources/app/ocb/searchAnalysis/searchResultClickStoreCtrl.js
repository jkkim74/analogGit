/**
 * Created by cookatrice on 2014. 5. 19..
 */

function searchResultClickStoreCtrl($scope, API_BASE_URL, DATE_TYPE_DAY, reportSvc) {

    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal();
        $scope.searchConfig = {
            startDate : defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain
        };
        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;

        $scope.drawGrid();
    };

    $scope.drawGrid = function(){
        var prevCell1 = { cellId: undefined, value: undefined };

        var url = '/ocb/searchAnalysis/searchResultClickStore/grid?dateType='+
            $scope.searchDateType+'&startDate='+$scope.searchStartDate+'&endDate='+$scope.searchEndDate;

         $scope.searchResultClickStoreGrid = {
            url: url,
            id: 'searchResultClickStore_Grid',
            datatype: "json",
            height: '100%',
            rowNum: -1,
            colNames: ['기준일','순위','매장ID','매장명', '클릭횟수'],
            colModel: [
                {name: 'stdDt', index: 'stdDt', editable: false, width: 90, align: "center", sortable: true,
                    cellattr: function (rowId, val, rawObject, cm) {
                        var result;
                        if (prevCell1.value == val) {
                            result = ' style="display: none" rowspanid="' + prevCell1.cellId + '"';
                        } else {
                            var cellId = this.id + '_row_' + rowId + '_' + cm.name;
                            result = ' rowspan="1" id="' + cellId + '"';
                            prevCell1 = { cellId: cellId, value: val };
                        }
                        return result;
                    }},
                {name: 'rank', index: 'rank', editable: false, width: 90, formatter: 'integer', align: "center", sortable: false},
                {name: 'storeId', index: 'storeId', editable: false, width: 90, formatter: '', align: "center", sortable: false},
                {name: 'storeNm', index: 'storeNm', editable: false, width: 90, formatter: '', align: "center", sortable: false},
                {name: 'clickCnt', index: 'clickCnt', editable: false, width: 90, formatter: 'integer', align: "center", sortable: false}
            ],
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
    };


    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        'tableId': 'gview_searchResultClickStore_Grid',
        'xlsName': 'search-result-click-store.xls'
    };

}

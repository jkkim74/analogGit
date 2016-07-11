function searchResultClickBnftCtrl($scope, $http, API_BASE_URL, DATE_TYPE_DAY, reportSvc) {

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
        var url = '/ocb/searchAnalysis/searchResultClickBnft/grid?dateType='+
            $scope.searchDateType+'&startDate='+$scope.searchStartDate+'&endDate='+$scope.searchEndDate;

        var prevPrevCell1 = "";
        var prevCell1 = { cellId: undefined, value: undefined };
        var prevCell2 = { cellId: undefined, value: undefined };
        $scope.searchResultClickBnftGrid = {
            url: url,
            id: 'searchResultClickBnft_Grid',
            datatype: "json",
            height: '100%',
            rowNum: 50000,
            colNames: ['기준일', '순위','혜택카테고리','혜택ID', '혜택명', '클릭횟수'],
            colModel: [
                {name: 'stdDt', index: 'stdDt', editable: false, width: 90, align: "center", sortable: true,
                    cellattr: function (rowId, val, rawObject, cm) {
                        var result;
                        prevPrevCell1 = prevCell1.value;
                        if (prevPrevCell1 == val) {
                            result = ' style="display: none" rowspanid="' + prevCell1.cellId + '"';
                        } else {
                            var cellId = this.id + '_row_' + rowId + '_' + cm.name;
                            result = ' rowspan="1" id="' + cellId + '"';
                            prevCell1 = { cellId: cellId, value: val };
                        }
                        return result;
                    }
                },
                {name: 'rowNumber', index: 'rowNumber', editable: false, width: 90, formatter: 'integer', align: "center", sortable: false},
                {name: 'bnftTyp', index: 'bnftTyp', editable: false, width: 90, formatter: '', align: "center", sortable: false,
                    cellattr: function (rowId, val, rawObject, cm) {
                        var result;
                        if (prevPrevCell1 == rawObject.stdDt && prevCell2.value == val) {
                            result = ' style="display: none" rowspanid="' + prevCell2.cellId + '"';
                        } else {
                            var cellId = this.id + '_row_' + rowId + '_' + cm.name;
                            result = ' rowspan="1" id="' + cellId + '"';
                            prevCell2 = { cellId: cellId, value: val };
                        }
                        return result;
                    }
                },
                {name: 'bnftId', index: 'bnftId', editable: false, width: 90, formatter: '', align: "center", sortable: false},
                {name: 'bnftName', index: 'bnftName', editable: false, width: 90, formatter: '', align: "center", sortable: false},
                {name: 'clickCnt', index: 'clickCnt', editable: false, width: 90, formatter: 'integer', align: "center", sortable: false}
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
            loadComplete: function() {
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
                if($(this).getGridParam('records') === 0) {
                    $(this).append('<tr><td colspan="7" class="text-center"><br/><br/><h3>no Data</h3><br/><br/></td></tr>');
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
        'tableId': 'gview_searchResultClickBnft_Grid',
        'xlsName': 'search-result_bnft.xls'
    };


}

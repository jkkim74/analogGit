/**
 * Created by cookatrice on 2014. 5. 17..
 */

function storeSingleCtrl($scope, $http, API_BASE_URL, reportSvc) {

    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal($scope.searchBoxDateType);
        $scope.searchDateType = $scope.searchBoxDateType;
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;

        $scope.drawGrid();
    };

    $scope.drawGrid = function () {
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };

        var url = '/ocb/contentsDetailAnalisys/storeSingle/grid/pagination?dateType=' + $scope.searchDateType + '&startDate=' + $scope.searchStartDate + '&endDate=' + $scope.searchEndDate;

        $scope.storeSingleGrid = {
            url: url,
            id: 'storeSingle_Grid',
            datatype: "json",
            height: '300',
            rowNum: 5000,
            rowList: [1000,3000,5000,7000, 10000],
            colNames: ['매장ID', '매장명', '기준일', '체크인건수', '체크인유저수', '전화걸기건수', '전화걸기유저수', '사진등록완료건수', '사진등록완료유저수', '리뷰등록완료건수', '리뷰등록완료유저수', '공유버튼클릭건수', '공유버튼클릭유저수', '혜택제공건수', '보유단골개수', '스타보유여부'],
            colModel: [
                {name: 'storeId', index: 'storeId', editable: false, width: 120, formatter: '', align: "center", sortable: false},
                {name: 'storeNm', index: 'storeNm', editable: false, width: 120, formatter: '', align: "center", sortable: false},
                {name: 'stdDt', index: 'stdDt', editable: false, width: 150, align: "center", sortable: true},
                {name: 'chkinCnt', index: 'chkinCnt', editable: false, width: 150, formatter: 'integer', align: "center", sortable: false},
                {name: 'chkinUserCnt', index: 'chkinUserCnt', editable: false, width: 150, formatter: 'integer', align: "center", sortable: false},
                {name: 'callCnt', index: 'callCnt', editable: false, width: 150, formatter: 'integer', align: "center", sortable: false},
                {name: 'callUserCnt', index: 'callUserCnt', editable: false, width: 150, formatter: 'integer', align: "center", sortable: false},
                {name: 'photoRegCnt', index: 'photoRegCnt', editable: false, width: 150, formatter: 'integer', align: "center", sortable: false},
                {name: 'photoRegUserCnt', index: 'photoRegUserCnt', editable: false, width: 150, formatter: 'integer', align: "center", sortable: false},
                {name: 'rviewRegCnt', index: 'rviewRegCnt', editable: false, width: 150, formatter: 'integer', align: "center", sortable: false},
                {name: 'rviewRegUserCnt', index: 'rviewRegUserCnt', editable: false, width: 150, formatter: 'integer', align: "center", sortable: false},
                {name: 'shrClickCnt', index: 'shrClickCnt', editable: false, width: 150, formatter: 'integer', align: "center", sortable: false, hidden: true},
                {name: 'shrClickUserCnt', index: 'shrClickUserCnt', editable: false, width: 150, formatter: 'integer', align: "center", sortable: false, hidden: true},
                {name: 'bnftOfferCnt', index: 'bnftOfferCnt', editable: false, width: 150, formatter: 'integer', align: "center", sortable: false},
                {name: 'ptrnCnt', index: 'ptrnCnt', editable: false, width: 150, formatter: 'integer', align: "center", sortable: false},
                {name: 'starYn', index: 'starYn', editable: false, width: 150, formatter: '', align: "center", sortable: false, hidden: true}
            ],
            pager: '#storeSingle_Grid_Pager',
            viewrecords: true,
            add: true,
            edit: true,
            sortname: 'stdDt',
            sortorder: 'asc',
            multiselect: false,
            hidegrid: false,
            //autowidth: true,
            width: "950",
            shrinkToFit: false,
//            scroll: true, //pagination에 없어야함
            jsonReader: {
                root: "rows",
                page: "page",
                total: "total",
                records: "records",
                repeatitems: false,
                cell: "cell"
            },
            loadComplete: function () {
                if ($(this).getGridParam('records') === 0) {
                    $(this).append('<tr><td colspan="7" class="text-center"><h3><br/>7월 1일부터 확인 가능</h3></td></tr>');
                }

                $scope.$safeApply(function() {
                    $scope.pageState.loading = false;
                    $scope.pageState.emptyData = false;
                });
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
        'tableId': 'gview_storeSingle_Grid',
        'xlsName': 'visit-store-single-grid.xls'
    };
}

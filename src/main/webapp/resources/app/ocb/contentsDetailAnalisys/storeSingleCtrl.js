/**
 * Created by cookatrice on 2014. 5. 17..
 */

function storeSingleCtrl($scope, $http, API_BASE_URL, DATE_TYPE_MONTH, reportSvc) {
    $scope.storeSingleGrid = {
        id: 'storeSingleGrid'
    };

    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal($scope.searchBoxDateType);
        $scope.searchConfig = {
            startDate: defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain
        };
        $scope.searchDateType = DATE_TYPE_MONTH;
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;
        $scope.searchString = null;

        $scope.pageState = {
            'loading': false,
            'emptyData': false
        };
    };

    $scope.drawGrid = function () {
        var url = '/ocb/contentsDetailAnalisys/storeSingle/grid?dateType=' + $scope.searchDateType
            + '&startDate=' + $scope.searchStartDate + '&endDate=' + $scope.searchEndDate + '&sidx=stdDt&sord=asc';
        if ($scope.searchString != null) {
            url += '&searchString=' + $scope.searchString;
            $scope.pageState = {
                'loading': true,
                'emptyData': true
            };
        } else {
            alert("매장ID는 필수 입력 사항입니다. 매장ID를 확인하세요.");
            return;
        }

        $scope.tableId = 'storeSingleTableForGrid';
        reportSvc.getReportApi(url).then(function(result) {
            var dataLength = result.rows.length;
            if (dataLength != 0) {
                gridStoreSingle(result.rows);
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    };

    function gridStoreSingle(datas) {
        $scope.storeSingleForGridConfig = {
            data: datas,
            datatype: "local",
            height: "100%",
            shrinkToFit: true,
            rowNum: 50000,
            colNames: ['매장ID', '매장명', '기준일', '체크인건수', '체크인유저수', '전화걸기건수', '전화걸기유저수', '사진등록완료건수',
                '사진등록완료유저수', '리뷰등록완료건수', '리뷰등록완료유저수', '공유버튼클릭건수', '공유버튼클릭유저수', '혜택제공건수', '보유단골개수', '스타보유여부'],
            colModel: [
                {name: 'storeId', index: 'storeId', editable: false, formatter: '', align: "center", sortable: false},
                {name: 'storeNm', index: 'storeNm', editable: false, formatter: '', align: "center", sortable: false},
                {name: 'stdDt', index: 'stdDt', editable: false, align: "center", sortable: true},
                {name: 'chkinCnt', index: 'chkinCnt', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'chkinUserCnt', index: 'chkinUserCnt', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'callCnt', index: 'callCnt', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'callUserCnt', index: 'callUserCnt', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'photoRegCnt', index: 'photoRegCnt', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'photoRegUserCnt', index: 'photoRegUserCnt', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'rviewRegCnt', index: 'rviewRegCnt', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'rviewRegUserCnt', index: 'rviewRegUserCnt', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'shrClickCnt', index: 'shrClickCnt', editable: false, formatter: 'integer', align: "center", sortable: false, hidden: true},
                {name: 'shrClickUserCnt', index: 'shrClickUserCnt', editable: false, formatter: 'integer', align: "center", sortable: false, hidden: true},
                {name: 'bnftOfferCnt', index: 'bnftOfferCnt', editable: false, formatter: 'integer', align: "center", sortable: false, hidden: true},
                {name: 'ptrnCnt', index: 'ptrnCnt', editable: false, formatter: 'integer', align: "center", sortable: false, hidden: true},
                {name: 'starYn', index: 'starYn', editable: false, formatter: '', align: "center", sortable: false, hidden: true}
            ],
            gridview: true,
            sortname: 'stdDt',
            sortorder: 'asc',
            multiselect: false,
            autowidth: true,
            hidegrid: false,
            loadonce: true,
            scroll: true,
            jsonReader: {
                root: "rows"
            },
            gridComplete: function () {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = false;
            }
        };
        var $table = angular.element('<table id="' + $scope.tableId + '" />');
        angular.element("#storeSingleForGrid").html($table);
        angular.element("#" + $scope.tableId).jqGrid("GridUnload");
        angular.element("#" + $scope.tableId).jqGrid($scope.storeSingleForGridConfig);
    }

    $scope.$on('$destroy', function() {
        $('#storeSingleForGrid').empty();
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
        $scope.searchString = result.searchString;

        $scope.drawGrid();
    };


    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        downloadUrl: '/ocb/contentsDetailAnalisys/downloadExcelForStoreSingle',
        downloadType: 'POI',
        pivotFlag: 'N',
        tableId: 'gview_storeSingle_Grid',
        xlsName: 'visit-store-single-grid.xls'
    };
}

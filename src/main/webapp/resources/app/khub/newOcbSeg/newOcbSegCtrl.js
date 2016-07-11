/**
 * Created by cookatrice
 */

function newOcbSegCtrl($scope, $http, API_BASE_URL, DATE_TYPE_MONTH, reportSvc) {
    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal(DATE_TYPE_MONTH);
        $scope.searchConfig = {
            endDate: defaultCal.endDateStrPlain
        };
        $scope.searchDateType = DATE_TYPE_MONTH;
        $scope.searchEndDate = defaultCal.endDateStrPlain;

        $scope.drawGrid();
    };

    $scope.drawGrid = function () {
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };

        var url = "/ocb/khub/newOcbSeg/newOcbSeg/grid?dateType=" + $scope.searchDateType
            + "&endDate=" + $scope.searchEndDate;

        $scope.tableId = 'newOcbSegTableForGrid';
        reportSvc.getReportApi(url).then(function(result) {
            var dataLength = result.rows.length;
            if (dataLength != 0) {
                gridNewOcbSeg(result.rows);
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    };

    function gridNewOcbSeg(datas) {
        //grid
        $scope.newOcbSegForGridConfig = {
            data: datas,
            datatype: "local",
            height: "100%",
            shrinkToFit: true,
            rowNum: 50000,
            colNames: ['행레이블', '전체고객수', '전체TR', '전체P/F', '적립고객수', '적립TR', '적립포인트', '사용고객수', '사용TR', '사용포인트'],
            colModel: [
                {name: 'rptIndNm', index: 'rptIndNm', editable: false, formatter: '', align: "left", sortable: false},
                {name: 'totCustCnt', index: 'totCustCnt', editable: false, formatter: 'integer', align: "right", sortable: false},
                {name: 'totTrCnt', index: 'totTrCnt', editable: false, formatter: 'integer', align: "right", sortable: false},
                {name: 'totPfSum', index: 'totPfSum', editable: false, formatter: 'integer', align: "right", sortable: false},
                {name: 'rsrvCustCnt', index: 'rsrvCustCnt', editable: false, formatter: 'integer', align: "right", sortable: false},
                {name: 'rsrvTrCnt', index: 'rsrvTrCnt', editable: false, formatter: 'integer', align: "right", sortable: false},
                {name: 'rsrvPntSum', index: 'rsrvPntSum', editable: false, formatter: 'integer', align: "right", sortable: false},
                {name: 'useCustCnt', index: 'useCustCnt', editable: false, formatter: 'integer', align: "right", sortable: false},
                {name: 'useTrCnt', index: 'useTrCnt', editable: false, formatter: 'integer', align: "right", sortable: false},
                {name: 'usePntSum', index: 'usePntSum', editable: false, formatter: 'integer', align: "right", sortable: false}
            ],
            gridComplete: function () {
                $(this).jqGrid('destroyGroupHeader');
                $(this).jqGrid('setGroupHeaders', {
                    useColSpanStyle: true,
                    groupHeaders: [
                        {startColumnName: 'totCustCnt', numberOfColumns: 3, titleText: '전체TR기준'},
                        {startColumnName: 'rsrvCustCnt', numberOfColumns: 3, titleText: '적립TR기준'},
                        {startColumnName: 'useCustCnt', numberOfColumns: 3, titleText: '사용TR기준'}
                    ]
                });
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = false;
            },
            gridview: true,
            multiselect: false,
            autowidth: true,
            hidegrid: false,
            loadonce: true,
            scroll: true,
            jsonReader: {
                root: "rows"
            }
        };
        var $table = angular.element('<table id="' + $scope.tableId + '" />');
        angular.element("#newOcbSegForGrid").html($table);
        angular.element("#" + $scope.tableId).jqGrid("GridUnload");
        angular.element("#" + $scope.tableId).jqGrid($scope.newOcbSegForGridConfig);
    }

    $scope.$on('$destroy', function() {
        //$('#newOcbSegForGrid').children().unbind();
        //$('#newOcbSegForGrid').children().off();
        $('#newOcbSegForGrid').empty();
    });

    /**
     * 검색 조회 callback 함수
     *
     * @params result
     */
    $scope.search = function (result) {
        $scope.searchDateType = result.searchDateType;
        $scope.searchEndDate = result.searchEndDate;

        $scope.drawGrid();
    };

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        downloadUrl: '/ocb/khub/newOcbSeg/downloadExcel',
        downloadType: 'POI',
        pivotFlag: 'N',
        tableId: 'newOcbSegForGrid',
        titleName: 'New OCB Seg',
        xlsName: 'new-ocb-seg.xls'
    };
}

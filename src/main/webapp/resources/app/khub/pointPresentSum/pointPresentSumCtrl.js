function pointPresentSumCtrl($scope, $http, API_BASE_URL, DATE_TYPE_DAY, reportSvc) {
    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal();
        $scope.searchConfig = {
            startDate: defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain
        };
        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;
        $scope.drawGrid();
    };

    $scope.drawGrid = function () {
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };

        var url = "/ocb/khub/pointPresentSum/pointPresentSum/grid?dateType=" + $scope.searchDateType
            + "&startDate=" + $scope.searchStartDate + "&endDate=" + $scope.searchEndDate + '&sidx=stndrdDt&sord=asc';

        $scope.tableId = 'pointPresentSumTableForGrid';
        reportSvc.getReportApi(url).then(function(result) {
            var dataLength = result.rows.length;
            if (dataLength != 0) {
                gridPointPresentSum(result.rows);
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    };

    function gridPointPresentSum(datas) {
        //grid
        $scope.pointPresentSumForGridConfig = {
            data: datas,
            datatype: "local",
            height: "100%",
            shrinkToFit: false,
            rowNum: 50000,
            colNames: ['기준일자'
                , '누적고객수(Unique)', '고객수', '선물TR건수', '선물Point', '선물누적포인트'
                , '누적고객수(Unique)', '고객수', '선물받은TR건수', '선물받은Point'
                , '합산하기가입신청고객수', '합산하기유실적고객수', '합산하기유실적누적고객수'
                , '합산하기사용누적고객수', '합산하기사용고객수', '합산하기로사용한TR건수', '합산하기로사용한Point', '누적포인트'
                , '합산하기사용누적고객수', '합산하기사용고객수', '합산하기로사용한TR건수', '합산하기로사용한Point', '누적포인트'],
            colModel: [
                {name: 'stndrdDt', index: 'stndrdDt', editable: false, width: 120, align: "center", sortable: true},
                {name: 'presntSndAcmCustCnt', index: 'presntSndAcmCustCnt', editable: false, width: 120, formatter: 'integer', align: "center", sortable: false},
                {name: 'presntSndCustCnt', index: 'presntSndCustCnt', editable: false, width: 120, formatter: 'integer', align: "center", sortable: false},
                {name: 'presntSndTrCnt', index: 'presntSndTrCnt', editable: false, width: 120, formatter: 'integer', align: "center", sortable: false},
                {name: 'presntSndPntSum', index: 'presntSndPntSum', editable: false, width: 120, formatter: 'integer', align: "center", sortable: false},
                {name: 'presntSndAcmPntSum', index: 'presntSndAcmPntSum', editable: false, width: 120, formatter: 'integer', align: "center", sortable: false},
                {name: 'presntRcvAcmCustCnt', index: 'presntRcvAcmCustCnt', editable: false, width: 120, formatter: 'integer', align: "center", sortable: false},
                {name: 'presntRcvCustCnt', index: 'presntRcvCustCnt', editable: false, width: 120, formatter: 'integer', align: "center", sortable: false},
                {name: 'presntRcvTrCnt', index: 'presntRcvTrCnt', editable: false, width: 120, formatter: 'integer', align: "center", sortable: false},
                {name: 'presntRcvPntSum', index: 'presntRcvPntSum', editable: false, width: 120, formatter: 'integer', align: "center", sortable: false},
                {name: 'entrRqstCustCnt', index: 'entrRqstCustCnt', editable: false, width: 120, formatter: 'integer', align: "center", sortable: false},
                {name: 'yachvCustCnt', index: 'yachvCustCnt', editable: false, width: 120, formatter: 'integer', align: "center", sortable: false},
                {name: 'yachvAcmCustCnt', index: 'yachvAcmCustCnt', editable: false, width: 120, formatter: 'integer', align: "center", sortable: false},
                {name: 'ownPntUseAcmCustCnt', index: 'ownPntUseAcmCustCnt', editable: false, width: 120, formatter: 'integer', align: "center", sortable: false},
                {name: 'ownPntUseCustCnt', index: 'ownPntUseCustCnt', editable: false, width: 120, formatter: 'integer', align: "center", sortable: false},
                {name: 'ownPntUseTrCnt', index: 'ownPntUseTrCnt', editable: false, width: 120, formatter: 'integer', align: "center", sortable: false},
                {name: 'ownPntUsePntSum', index: 'ownPntUsePntSum', editable: false, width: 120, formatter: 'integer', align: "center", sortable: false},
                {name: 'ownPntUseAcmPntSum', index: 'ownPntUseAcmPntSum', editable: false, width: 120, formatter: 'integer', align: "center", sortable: false},
                {name: 'othPntUseAcmCustCnt', index: 'othPntUseAcmCustCnt', editable: false, width: 120, formatter: 'integer', align: "center", sortable: false},
                {name: 'othPntUseCustCnt', index: 'othPntUseCustCnt', editable: false, width: 120, formatter: 'integer', align: "center", sortable: false},
                {name: 'othPntUseTrCnt', index: 'othPntUseTrCnt', editable: false, width: 120, formatter: 'integer', align: "center", sortable: false},
                {name: 'othPntUsePntSum', index: 'othPntUsePntSum', editable: false, width: 120, formatter: 'integer', align: "center", sortable: false},
                {name: 'othPntUseAcmPntSum', index: 'othPntUseAcmPntSum', editable: false, width: 120, formatter: 'integer', align: "center", sortable: false}
            ],
            gridComplete: function () {
                $(this).jqGrid('destroyGroupHeader');
                $(this).jqGrid('setGroupHeaders', {
                    useColSpanStyle: true,
                    groupHeaders: [
                        {startColumnName: 'presntSndAcmCustCnt', numberOfColumns: 5, titleText: ''},
                        {startColumnName: 'presntRcvAcmCustCnt', numberOfColumns: 4, titleText: ''},
                        {startColumnName: 'entrRqstCustCnt', numberOfColumns: 13, titleText: '합산하기(같이쓰기)'}
                    ]
                });
                $(this).jqGrid('setGroupHeaders', {
                    useColSpanStyle: true,
                    groupHeaders: [
                        {startColumnName: 'presntSndAcmCustCnt', numberOfColumns: 5, titleText: '선물하기 선물한 고객기준'},
                        {startColumnName: 'presntRcvAcmCustCnt', numberOfColumns: 4, titleText: '선물하기 선물받은 고객기준'},
                        {startColumnName: 'entrRqstCustCnt', numberOfColumns: 3, titleText: '전체 고객수'},
                        {startColumnName: 'ownPntUseAcmCustCnt', numberOfColumns: 5, titleText: '본인 point 사용실적'},
                        {startColumnName: 'othPntUseAcmCustCnt', numberOfColumns: 5, titleText: '타인 point 사용실적'}
                    ]
                });
                $("#pointPresentSumForGrid .ui-jqgrid-htable").find('tr:nth-child(2)').find('th:nth-child(1)').attr('rowspan', 3);

                $("#pointPresentSumForGrid .ui-jqgrid-htable").find('tr:nth-child(3)').find('th:nth-child(1)').remove();
                $("#pointPresentSumForGrid .ui-jqgrid-htable").find('tr:nth-child(2)').find('th:nth-child(2)').attr('rowspan', 2);
                $("#pointPresentSumForGrid .ui-jqgrid-htable").find('tr:nth-child(2)').find('th:nth-child(2)').text('선물하기 선물한 고객기준');

                $("#pointPresentSumForGrid .ui-jqgrid-htable").find('tr:nth-child(3)').find('th:nth-child(1)').remove();
                $("#pointPresentSumForGrid .ui-jqgrid-htable").find('tr:nth-child(2)').find('th:nth-child(3)').attr('rowspan', 2);
                $("#pointPresentSumForGrid .ui-jqgrid-htable").find('tr:nth-child(2)').find('th:nth-child(3)').text('선물하기 선물받은 고객기준');
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = false;
            },
            gridview: true,
            sortname: 'stndrdDt',
            sortorder: 'asc',
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
        angular.element("#pointPresentSumForGrid").html($table);
        angular.element("#" + $scope.tableId).jqGrid("GridUnload");
        angular.element("#" + $scope.tableId).jqGrid($scope.pointPresentSumForGridConfig);
    }

    $scope.$on('$destroy', function() {
        //$('#pointPresentSumForGrid').children().unbind();
        //$('#pointPresentSumForGrid').children().off();
        $('#pointPresentSumForGrid').empty();
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

        $scope.drawGrid();
    };

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        downloadUrl: '/ocb/khub/pointPresentSum/downloadExcel',
        downloadType: 'POI',
        pivotFlag: 'N',
        tableId: 'pointPresentSumForGrid',
        titleName: '포인트 선물_합산하기',
        xlsName: 'point-present-sum.xls'
    };
}

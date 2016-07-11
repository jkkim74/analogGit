function rcmdStatCtrl($scope, DATE_TYPE_DAY, reportSvc) {
    $scope.init = function () {
        var defaultCal = reportSvc.defaultCustomCal('day', 7, 4, 6);
        $scope.searchConfig = {
            startDate: defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain,
            dayPeriod: 7,
            weekPeriod: 4,
            monthPeriod: 6
        };

        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;

        $scope.drawRcmdStat();
    };

    $scope.drawRcmdStat = function () {
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };
        var url = '/syrup/membership/rcmdStat/grid?dateType=' + $scope.searchDateType + '&startDate='
            + $scope.searchStartDate + '&endDate=' + $scope.searchEndDate + '&sidx=stdDt&sord=asc';

        $scope.tableId = 'rcmdStatTableForGrid';
        reportSvc.getReportApi(url).then(function(result) {
            var dataLength = result.rows.length;
            if (dataLength != 0) {
                gridRcmdStat(result.rows);
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    };

    function gridRcmdStat(datas) {
        $scope.rcmdStatForGridConfig = {
            data : datas,
            datatype: "local",
            height: "100%",
            shrinkToFit: true,
            rowNum: 50000,
            colNames: ['일자'
                , '카드명'
                , '기간내 노출 건수'
                , '추천카드발급건수'
            ],
            colModel: [
                {name: 'strdDt', index: 'strdDt', editable: false, align: "center", frozen: true, sortable: true},
                {name: 'recNm', index: 'recNm', editable: false, align: "center", frozen: true, sortable: false},
                {name: 'tgtCnt', index: 'tgtCnt', editable: false, align: "right", frozen: true, sortable: false, formatter:'currency', formatoptions:{decimalSeparator:"", thousandsSeparator: ",", decimalPlaces: 0}},
                {name: 'issueCnt', index: 'issueCnt', editable: false, align: "right", frozen: true, sortable: false, formatter:'currency', formatoptions:{decimalSeparator:"", thousandsSeparator: ",", decimalPlaces: 0}}
            ],
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
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = false;
            }
        };

        var $table = angular.element('<table id="' + $scope.tableId + '" />');
        angular.element("#rcmdStatForGrid").html($table);
        angular.element("#" + $scope.tableId).jqGrid("GridUnload");
        angular.element("#" + $scope.tableId).jqGrid($scope.rcmdStatForGridConfig);
    }

    $scope.$on('$destroy', function() {
        $('#rcmdStatForGrid').empty();
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
        $scope.drawRcmdStat();
    };

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        downloadUrl : '/syrup/membership/downloadExcelForRcmdStat',
        downloadType : 'POI',
        pivotFlag : 'N',
        tableId: 'rcmdStatForGrid',
        xlsName: 'rcmd-stat.xls'
    };
}

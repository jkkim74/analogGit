function issuePaVouCtrl($scope, DATE_TYPE_DAY, reportSvc) {
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
        $scope.drawIssuePaVou();
    };

    $scope.drawIssuePaVou = function () {
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };
        var url = '/syrup/membership/cardissuePaVou/grid?dateType=' + $scope.searchDateType + '&startDate='
            + $scope.searchStartDate + '&endDate=' + $scope.searchEndDate + '&sidx=stdDt&sord=asc';

        $scope.tableId = 'issuePaVouTableForGrid';

        reportSvc.getReportApi(url).then(function(result) {
            var dataLength = result.rows.length;
            if (dataLength != 0) {
                gridIssuePaVou(result.rows);
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    };

    function gridIssuePaVou(datas) {
        $scope.issuePaVouForGridConfig = {
            data : datas,
            datatype: "local",
            height: "100%",
            shrinkToFit: true,
            rowNum: 50000,
            colNames: ['일자', '카드명', '총발급건수', '기간내발급건수', '카드실행건수', '카드실행자수', '포인트조회실행건수'
                , '포인트조회실행자수', '발급페이지조회건수', '발급페이지조회자수'
            ],
            colModel: [
                {name: 'openDt', index: 'openDt', width: 90, editable: false, align: "center", frozen: true, sortable: true},
                {name: 'cardName', index: 'cardName', editable: false, align: "center", frozen: true, sortable: false},
                {name: 'totIssueCnt', index: 'totIssueCnt', editable: false, align: "right", frozen: true, sortable: false, formatter:'currency', formatoptions:{decimalSeparator:"", thousandsSeparator: ",", decimalPlaces: 0}},
                {name: 'dayIssueCnt', index: 'dayIssueCnt', editable: false, align: "right", frozen: true, sortable: false, formatter:'currency', formatoptions:{decimalSeparator:"", thousandsSeparator: ",", decimalPlaces: 0}},
                {name: 'cardExecCnt', index: 'cardExecCnt', editable: false, align: "right", frozen: true, sortable: false, formatter:'currency', formatoptions:{decimalSeparator:"", thousandsSeparator: ",", decimalPlaces: 0}},
                {name: 'cardExecUserCnt', index: 'cardExecUserCnt', editable: false, align: "right", frozen: true, sortable: false, formatter:'currency', formatoptions:{decimalSeparator:"", thousandsSeparator: ",", decimalPlaces: 0}},
                {name: 'pntQryExecCnt', index: 'pntQryExecCnt', editable: false, align: "right", frozen: true, sortable: false, formatter:'currency', formatoptions:{decimalSeparator:"", thousandsSeparator: ",", decimalPlaces: 0}},
                {name: 'pntQryExecUserCnt', index: 'pntQryExecUserCnt', editable: false, align: "right", frozen: true, sortable: false, formatter:'currency', formatoptions:{decimalSeparator:"", thousandsSeparator: ",", decimalPlaces: 0}},
                {name: 'pageQryCnt', index: 'pageQryCnt', editable: false, align: "right", frozen: true, sortable: false, formatter:'currency', formatoptions:{decimalSeparator:"", thousandsSeparator: ",", decimalPlaces: 0}},
                {name: 'pageQryUserCnt', index: 'pageQryUserCnt', editable: false, align: "right", frozen: true, sortable: false, formatter:'currency', formatoptions:{decimalSeparator:"", thousandsSeparator: ",", decimalPlaces: 0}}
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
        angular.element("#issuePaVouForGrid").html($table);
        angular.element("#" + $scope.tableId).jqGrid("GridUnload");
        angular.element("#" + $scope.tableId).jqGrid($scope.issuePaVouForGridConfig);
    }

    $scope.$on('$destroy', function() {
        $('#issuePaVouForGrid').empty();
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
        $scope.drawIssuePaVou();
    };

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        downloadUrl : '/syrup/membership/downloadExcelForIssuePaVou',
        downloadType : 'POI',
        pivotFlag : 'N',
        tableId: 'issuePaVouForGrid',
        xlsName: 'membership-issue-pa-vou.xls'
    };
}

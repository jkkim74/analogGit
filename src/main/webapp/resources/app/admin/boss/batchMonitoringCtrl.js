function batchMonitoringCtrl($scope, adminSvc) {
    $scope.init = function () {
        $scope.dateFlag = true;
        $scope.searchDate = DateHelper.getCurrentDateByPattern('YYYYMMDD');
        callBatchMonitoringResult();
    };

    // 배치 처리 결과 데이터 조회.
    function callBatchMonitoringResult() {
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };

        var url = '/admin/boss/batchMonitoring?basicDate=' + $scope.searchDate;
        $scope.tableId = 'batchMonitoringTableForGrid';
        adminSvc.getAdminApi(url).then(function(result) {
            var dataLength = result.rows.length;
            if (dataLength != 0) {
                gridBatchMonitoring(result.rows);
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    }

    // 배치 처리 결과 jqGrid로 표시.
    function gridBatchMonitoring(datas) {
        $scope.batchMonitoringForGridConfig = {
            data : datas,
            datatype: "local",
            height: "100%",
            shrinkToFit: true,
            rowNum: 10000,
            id: 'batchMonitoringForGrid',
            colNames: ['서비스명', '주기구분', '수신예상시간', '수신상태', '처리상태', '오류상세', '수신시각', '수신항목', '실수신항목', '데이터건수', '전송파일명'],
            colModel: [
                {name: 'svcNm', index: 'svcNm', editable: false, width: 120, align: "center", sortable: true},
                {name: 'lnkgCyclCd', index: 'lnkgCyclCd', editable: false, width: 60, align: "center", sortable: false},
                {name: 'rcvExptTm', index: 'rcvExptTm', editable: false, width: 60, align: "center", sortable: false},
                {name: 'rcvYn', index: 'rcvYn', editable: false, width: 60, align: "center", sortable: false},
                {name: 'status', index: 'status', editable: false, width: 60, align: "center", sortable: false},
                {name: 'errDtl', index: 'errDtl', editable: false, width: 120, align: "center", sortable: false},
                {name: 'auditDtm', index: 'auditDtm', editable: false, width: 120, align: "center", sortable: false},
                {name: 'exptCnt', index: 'exptCnt', editable: false, width: 60, align: "center", sortable: false},
                {name: 'rcvItmCnt', index: 'rcvItmCnt', editable: false, width: 60, align: "center", sortable: false},
                {name: 'dataCnt', index: 'dataCnt', editable: false, width: 60, align: "center", sortable: false},
                {name: 'trmsFlNm', index: 'trmsFlNm', editable: false, width: 190, align: "center", sortable: false}
            ],
            gridview: true,
            multiselect: false,
            autowidth: true,
            hidegrid: false,
            loadonce: true,
            scroll: true,
            jsonReader: {
                root: "rows"
            },
            gridComplete: function() {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = false;
            }
        };
        var $table = angular.element('<table id="' + $scope.tableId + '" />');
        angular.element("#batchMonitoringForGrid").html($table);
        angular.element("#" + $scope.tableId).jqGrid("GridUnload");
        angular.element("#" + $scope.tableId).jqGrid($scope.batchMonitoringForGridConfig);
    }

    $scope.$on('$destroy', function() {
        //$('#batchMonitoringForGrid').children().unbind();
        //$('#batchMonitoringForGrid').children().off();
        $('#batchMonitoringForGrid').empty();
    });

    /**
     * 검색 callback
     * @param result
     */
    $scope.search = function(result) {
        $scope.searchDate = result.searchDate;
        callBatchMonitoringResult();
    };
}

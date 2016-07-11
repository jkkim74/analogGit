function batchInfoCtrl($scope, adminSvc) {
    $scope.init = function () {
        $scope.dateFlag = false;
        callBatchInfoResult();
    };

    // 배치 정보 조회.
    function callBatchInfoResult() {
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };
        var url = '/admin/boss/batchInfo';
        $scope.tableId = 'batchInfoTableForGrid';
        adminSvc.getAdminApi(url).then(function(result) {
            var dataLength = result.rows.length;
            if (dataLength != 0) {
                gridBatchInfo(result.rows);
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    }

    // 배치 정보를 바탕으로 jqGrid로 그리드에 표시.
    function gridBatchInfo(datas) {
        $scope.batchInfoForGridConfig = {
            data : datas,
            datatype: "local",
            height: "100%",
            shrinkToFit: true,
            rowNum: 10000,
            colNames: ['서비스명', '일', '주', '월', '일수신개수', '표시일', '일수신시간', '주수신개수', '주수신요일', '주수신시간', '월수신개수', '월수신일자', '월수신시간'],
            colModel: [
                {name: 'svcNm', index: 'svcNm', editable: false, width: 120, align: "center", sortable: true},
                {name: 'dayObjYn', index: 'dayObjYn', editable: false, width: 40, align: "center", sortable: false},
                {name: 'wkObjYn', index: 'wkObjYn', editable: false, width: 40, align: "center", sortable: false},
                {name: 'mthObjYn', index: 'mthObjYn', editable: false, width: 40, align: "center", sortable: false},
                {name: 'dayRcvItmCnt', index: 'dayRcvItmCnt', editable: false, width: 80, align: "center", sortable: false},
                {name: 'dispStrdCd', index: 'dispStrdCd', editable: false, width: 60, align: "center", sortable: false},
                {name: 'dayRcvExptTm', index: 'dayRcvExptTm', editable: false, width: 120, align: "center", sortable: false},
                {name: 'wkRcvExptCnt', index: 'wkRcvExptCnt', editable: false, width: 80, align: "center", sortable: false},
                {name: 'wkRcvExptDowCdNm', index: 'wkRcvExptDowCdNm', editable: false, width: 80, align: "center", sortable: false},
                {name: 'wkRcvExptTm', index: 'wkRcvExptTm', editable: false, width: 120, align: "center", sortable: false},
                {name: 'mthRcvItmCnt', index: 'mthRcvItmCnt', editable: false, width: 80, align: "center", sortable: false},
                {name: 'mthRcvExptDay', index: 'mthRcvExptDay', editable: false, width: 80, align: "center", sortable: false},
                {name: 'mthRcvExptTm', index: 'mthRcvExptTm', editable: false, width: 120, align: "center", sortable: false}
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
            gridComplete: function () {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = false;
            }
        };
        var $table = angular.element('<table id="' + $scope.tableId + '" />');
        angular.element("#batchInfoForGrid").html($table);
        angular.element("#" + $scope.tableId).jqGrid("GridUnload");
        angular.element("#" + $scope.tableId).jqGrid($scope.batchInfoForGridConfig);
    }

    $scope.$on('$destroy', function() {
        // unbind, off 이벤트 클리어하고 데이터 및 노드를 삭제함.
        $('#batchInfoForGrid').empty();
    });

    /**
     * 검색 callback
     * @param result
     */
    $scope.search = function() {
        callBatchInfoResult();
    };
}

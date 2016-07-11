function menuInfoCtrl($scope, adminSvc) {
    $scope.init = function () {
        $scope.dateFlag = false;
        callMenuInfoResult();
    };

    function callMenuInfoResult() {
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };

        $scope.tableId = 'menuInfoTableForGrid';
        adminSvc.getAdminApi('/menu/gets').then(function(result) {
            if (result && result.length != 0) {
                gridMenuInfo(result);
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    }

    function gridMenuInfo(datas) {
        $scope.menuInfoForGridConfig = {
            data : datas,
            datatype: "local",
            height: "100%",
            shrinkToFit: true,
            rowNum: 10000,
            colNames: ['ID', '서비스', '레벨', '경로명(서비스>카테고리>메뉴)', '메뉴명', '노출여부', '사용여부'],
            colModel: [
                {name: 'id', index: 'id', editable: false, width: 30, align: "center", sortable: true},
                {name: 'rootName', index: 'rootName', editable: false, width: 80, align: "left", sortable: false},
                {name: 'menuLevel', index: 'menuLevel', editable: false, width: 70, align: "left", sortable: false},
                {name: 'pathName', index: 'pathName', editable: false, width: 200, align: "left", sortable: false},
                {name: 'name', index: 'name', editable: false, align: "left", sortable: false},
                {name: 'visibleYn', index: 'visibleYn', editable: false, width: 30, align: "center", sortable: false},
                {name: 'deleteYn', index: 'deleteYn', editable: false, width: 30, align: "center", sortable: false}
            ],
            gridview: true,
            multiselect: true,
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
        var table = angular.element('<table id="' + $scope.tableId + '" />');
        angular.element("#menuInfoForGrid").html(table);
        angular.element("#" + $scope.tableId).jqGrid("GridUnload");
        angular.element("#" + $scope.tableId).jqGrid($scope.menuInfoForGridConfig);
    }
}

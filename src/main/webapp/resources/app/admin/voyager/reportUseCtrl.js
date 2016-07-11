/**
 * Created by cookatrice.
 */
function reportUseCtrl($scope, adminSvc, DATE_TYPE_DAY,reportSvc) {
    $scope.init = function () {
        $scope.dateFlag = true;
        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.adminDateTypes = [{key: 'day', label: '일간'}];
        var defaultCal = reportSvc.defaultCustomCal($scope.searchDateType, 7,1,1);
        $scope.selectedCalStart = defaultCal.startDateStrPlain;
        $scope.selectedCalEnd = defaultCal.endDateStrPlain;
        $scope.searchStartDate = $scope.selectedCalStart;
        $scope.searchEndDate = $scope.selectedCalEnd;
        callStatisticsResult();
    };

    function callStatisticsResult () {
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };
        var url = '/log/reportUse?startDate=' + $scope.selectedCalStart+ '&endDate=' + $scope.selectedCalEnd;

        $scope.tableId = 'reportUseTableForGrid';
        adminSvc.getAdminApi(url).then(function(result) {
            var dataLength = result.length;
            if (dataLength != 0) {
                gridStatistics(result);
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    }

    function gridStatistics(datas) {
        $scope.statisticsForGridConfig = {
            data : datas,
            datatype: "local",
            height: "600px",
            shrinkToFit: true,
            rowNum: 50000,
            colNames: ['구분', 'count'],
            colModel: [
                {name: 'kind', index: 'kind', editable: false, align: "center", sortable: false},
                {name: 'cnt', index: 'cnt', editable: false, formatter: 'integer', align: "center", sortable: false}
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
        angular.element("#reportUseForGrid").html($table);
        angular.element("#" + $scope.tableId).jqGrid("GridUnload");
        angular.element("#" + $scope.tableId).jqGrid($scope.statisticsForGridConfig);
    }

    $scope.$on('$destroy', function() {
        $('#reportUseForGrid').empty();
    });

    /**
     * 검색 callback
     * @param result
     */
    $scope.search = function(result) {
        $scope.searchDateType = result.searchDateType;
        $scope.selectedCalStart = result.selectedCalStart;
        $scope.selectedCalEnd = result.selectedCalEnd;
        $scope.searchStartDate = $scope.selectedCalStart;
        $scope.searchEndDate = $scope.selectedCalEnd;
        callStatisticsResult();
    };
}

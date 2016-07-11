/**
 * cookatrice
 */

function visitExceptPushOutlineCtrl($scope, reportSvc, DATE_TYPE_DAY) {
    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal();
        $scope.searchConfig = {
            startDate: defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain
        };
        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;
        $scope.drawVisitorGrid();

    };

    $scope.drawVisitorGrid = function () {
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };

        var url = '/ocb/visitExceptPush/outline/grid?dateType=' + $scope.searchDateType + '&startDate='
            + $scope.searchStartDate + '&endDate=' + $scope.searchEndDate + '&sidx=stdDt&sord=asc';

        var heightVal = "";
        $scope.tableId = 'visitExceptPushListTableForGrid';
        reportSvc.getReportApi(url).then(function(result) {
            var dataLength = result.rows.length;
            if (dataLength != 0) {
                if (dataLength > 20)
                    heightVal = "400";
                else
                    heightVal = "100%";
                gridVisitExceptPush(result.rows, heightVal);
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    };

    function gridVisitExceptPush(datas, heightVal) {
        //grid
        $scope.visitExceptPushListForGridConfig = {
            data: datas,
            datatype: "local",
            height: "100%",
            shrinkToFit: true, //with scroll
            rowNum: 50000,
            colNames: ['기준일자', 'UV', 'LV(Login Visitor)', 'RV(Returning Visitor)', '방문횟수', '체류시간', 'PV', 'Bounce Rate'],
            colModel: [
                {name: 'stdDt', index: 'stdDt', editable: false, align: "center", sortable: true},
                {name: 'uv', index: 'uv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'lv', index: 'lv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'rv', index: 'rv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'vstCnt', index: 'vstCnt', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'timeSptFVst', index: 'timeSptFVst', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'pv', index: 'pv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'buncRate', index: 'buncRate', editable: false, formatter: 'integer', align: "center", sortable: false}
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
        angular.element("#visitExceptPushListForGrid").html($table);
        angular.element("#" + $scope.tableId).jqGrid("GridUnload");
        angular.element("#" + $scope.tableId).jqGrid($scope.visitExceptPushListForGridConfig);
    }

    $scope.$on('$destroy', function() {
        $('#visitExceptPushListForGrid').empty();
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

        $scope.drawVisitorGrid();
    };

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        downloadUrl: '/ocb/visitExceptPush/downloadExcelForOutline',
        downloadType: 'POI',
        pivotFlag: 'N',
        tableId: 'visitExceptPushListForGrid',
        xlsName: 'visit_except_push_outline.xls'
    };

}

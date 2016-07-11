/**
 * Created by cookatrice on 2014. 5. 9..
 */

function visitOutlineCtrl($scope, $http, API_BASE_URL, DATE_TYPE_DAY, reportSvc, apiSvc) {

    $scope.$on('$locationChangeSuccess', function () {
        $scope.drawVisitorGrid();
    });

    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal();
        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;
        $scope.searchPocCode = '01';

        $scope.drawVisitorGrid();
    };

    $scope.drawVisitorGrid = function () {

        $scope.tableId = 'visitorListTableForGrid';
        /** highChart destroy for reset pivot **/
        DomHelper.destroyHighChart($scope.tableId);

        $scope.pageState = {
            loading: true,
            emptyData: true
        };

        var httpConfig = {
            url: '/ocb/visit/outline/grid',
            params: {
                dateType: $scope.searchDateType,
                startDate: $scope.searchStartDate,
                endDate: $scope.searchEndDate,
                pocCode: $scope.searchPocCode,
                sidx: 'stdDt',
                sord: 'asc'
            }
        };

        apiSvc.voyagerHttpSvc(httpConfig).then(function (result) {
            var receiveData = result.data;
            if (receiveData && receiveData.rows.length != 0) {

                var heightVal = receiveData.rows.length > 20 ? '400' : '100%';
                $scope.$safeApply(function () {
                    gridDate(receiveData.rows, heightVal);
                    setHighChartConfig(receiveData.rows);
                });
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    };

    function gridDate(datas, heightVal) {
        //grid
        $scope.visitorListForGridConfig = {
            data : datas,
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
        angular.element("#visitorListForGrid").html($table);
        angular.element("#" + $scope.tableId).jqGrid("GridUnload");
        angular.element("#" + $scope.tableId).jqGrid($scope.visitorListForGridConfig);
    }

    function setHighChartConfig(rawData) {
        /** draw highChart. just set highchart config data.:) **/
        $scope.highChartConfig = {
            highChartId: $scope.tableId,
            rawData: rawData,
            extraOption: {
                tableType : 'grid', //default : 'pivot'
                gridColsInfo: ChartHelper.getGridInfo($scope.visitorListForGridConfig), //if tableType is 'grid', this option is essential point.
                legendArea : 180,
                dateType: $scope.searchDateType
            }
        };
    }

    /**
     * 검색 조회 callback 함수
     *
     * @params result
     */
    $scope.search = function(result) {
        $scope.searchDateType = result.searchDateType;
        $scope.searchStartDate = result.searchStartDate;
        $scope.searchEndDate = result.searchEndDate;
        $scope.searchPocCode = result.searchCode;

        $scope.drawVisitorGrid();
    };

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        downloadUrl : '/ocb/visit/downloadExcelForOutline',
        downloadType : 'POI',
        pivotFlag : 'N',
        tableId : 'visitorListPerDayForGrid',
        xlsName : 'visit_outline.xls'
    };
}
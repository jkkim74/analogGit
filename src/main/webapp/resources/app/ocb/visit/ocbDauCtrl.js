/**
 * Created by ophelisis on 2015. 2. 26..
 */

function ocbDauCtrl($scope, DATE_TYPE_DAY, reportSvc, apiSvc) {
    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal();
        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchConfig = {
            startDate : defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain
        };

        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;

        drawPivot();
    };
    function setPivotTableConfig(rawData) {
        // apply white-space for textContent
        rawData = rawData.map(function(d) {
            d.measure = d.measure.replace(/&nbsp;/g,'\u00a0');
            return d;
        });
        $('#' + $scope.pivotTableId).pivotUI(
            rawData,
            {
                rows: ["measure"],
                cols: ['strdDt'],
                vals: ["measureValue"],
                aggregatorName: 'intSum',
                onRefresh: function () {
                    var config = {
                        id: $scope.pivotTableId,
                        customOrder: 2,
                        modelData: [
                            {name: 'measure', width: 200}
                        ]
                    };
                    $scope.headerView = true;
                    DomHelper.reDrawPivotTableHeader(config);
                    $scope.tableHeaderList = DomHelper.getDateList(config.id);

                    $scope.$safeApply(function () {
                        $scope.pageState.loading = false;
                        $scope.pageState.emptyData = false;
                    });
                }
            },
            true
        );
    }

    $scope.$on('$destroy', function() {
        $('#' + $scope.pivotTableId).empty();
    });

    /**
     * drawPivot
     */
    function drawPivot() {
        /** resize pivotTable and chart width **/
        $scope.pivotTableId = 'ocbDauPivot';

        $scope.headerView = false;
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };

        var httpConfig = {
            url: '/ocb/customer/ocbDau/pivot',
            params: {
                dateType: $scope.searchDateType,
                startDate: $scope.searchStartDate,
                endDate: $scope.searchEndDate
            }
        };

        apiSvc.voyagerHttpSvc(httpConfig).then(function (result) {
            var receiveData = result.data;
            if (receiveData && receiveData.rows.length != 0) {
                $scope.$safeApply(function () {
                    setPivotTableConfig(receiveData.rows);
                });
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    }

    /**
     * 검색 조회 callback 함수
     *
     * @params result
     */
    $scope.search = function(result) {
        if (result.searchDateType != "day") {
            swal('다음 릴리즈때 사용하실 수 있습니다.\n조금 기다려 주세요.^^');
            return;
        }

        $scope.searchDateType = result.searchDateType;
        $scope.searchStartDate = result.searchStartDate;
        $scope.searchEndDate = result.searchEndDate;

        drawPivot();
    };


	/**
	 * 엑셀 다운로드 설정
	 * @type {{tableId: string, xlsName: string}}
	 */
	$scope.excelConfig = {
        downloadUrl : '/download/report/excel',
        downloadType : 'POI',
        pivotFlag : 'Y',
        tableId : 'ocbDauPivot',
        titleName : 'OCB DAU(KPI 기준)',
        xlsName : 'ocb-dau.xls'
	};
}

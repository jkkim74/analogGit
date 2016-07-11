/**
 * Created by cookatrice on 2014. 5. 15..
 */

function feedExposureOrderCtrl($scope, DATE_TYPE_DAY, reportSvc, apiSvc) {

    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal();
        $scope.searchConfig = {
            startDate : defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain
        };
        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;
        drawPivot();
    };
    function setPivotTableConfig(rawData) {
        $('#' + $scope.pivotTableId).pivotUI(
            rawData,
            {
                rows: ["feedId", "feedNm", "feedStartDate", "feedEndDate", "isYn", "allianceYn", "dispOrder", 'stdDt'],
                cols: ["measure"],
                vals: ["measureValue"],
                aggregatorName: 'intSum',
                onRefresh: function () {
                    var config = {
                        id: $scope.pivotTableId,
                        modelData: [
                            {name: '피드ID', width: 60},
                            {name: '피드명', width: 160},
                            {name: '시작일자', width: 100},
                            {name: '종료일자', width: 100},
                            {name: 'IS인벤토리', width: 30},
                            {name: '제휴사타켓팅', width: 30},
                            {name: '노출순서', width: 60},
                            {name: '기준일자', width: 80}
                        ],
                        extendWidth: 500
                    };
                    $scope.headerView = true;
                    DomHelper.reDrawPivotTableHeader(config);
                    $scope.tableHeaderList = DomHelper.customOrder_h(DomHelper.getDateList(config.id), 1); //like customOrder. substring header pre-word.

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
        //$('#' + $scope.pivotTableId).children().unbind();
        //$('#' + $scope.pivotTableId).children().off();
        $('#' + $scope.pivotTableId).empty();
    });

    /**
     * drawPivot
     */
    function drawPivot() {
        /** resize pivotTable and chart width **/
        $scope.pivotTableId = 'feedsExposureOrderPivot';

        $scope.headerView = false;
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };

        var httpConfig = {
            url: '/ocb/contentsDetailAnalisys/feedExposureOrder/pivot',
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
        downloadUrl : '/ocb/contentsDetailAnalisys/downloadExcelForFeedExposureOrder',
        downloadType : 'POI',
        pivotFlag : 'N',
        tableId : 'feedsExposureOrderPivot',
        xlsName : 'visit-feed_order.xls'
	};

}

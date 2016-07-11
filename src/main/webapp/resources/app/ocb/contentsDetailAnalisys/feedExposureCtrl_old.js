/**
 * Created by cookatrice on 2014. 5. 15..
 */

function feedExposureCtrl($scope, $http, API_BASE_URL, reportSvc) {
    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal();
        $scope.searchDateType = $scope.searchBoxDateType;
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;

        drawPivot();
    };

    /**
     * drawPivot
     */
    function drawPivot() {
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };

        var url = '/ocb/contentsDetailAnalisys/feedExposure/pivot?dateType=' + $scope.searchDateType + '&startDate=' + $scope.searchStartDate + '&endDate=' + $scope.searchEndDate;

        $http.get(url).success(function (result) {
            if (result.length != 0 && result.rows.length != 0) {
                var pivotData = result.rows;
                $("#feedsExposurePivot").pivotUI(
                    pivotData,
                    {
                        rows: ["feedId", "feedNm", "feedStartDate", "feedEndDate", "isYn", "allianceYn", 'stdDt'],
                        cols: ["measure"],
                        vals: ["measureValue"],
                        aggregatorName: 'intSum',
                        onRefresh : function() {
                            $("#feedsExposurePivot .pvtAxisLabel").each(function (index, domElement) {
                                if (index == 1)
                                    $(domElement).text('피드ID');
                                else if (index == 2)
                                    $(domElement).text('피드명').css('width', '20%');
                                else if (index == 3)
                                    $(domElement).text('시작일자');
                                else if (index == 4)
                                    $(domElement).text('종료일자');
                                else if (index == 5)
                                    $(domElement).text('IS인벤토리');
                                else if (index == 6)
                                    $(domElement).text('제휴사타겟팅');
                                else if (index == 7)
                                    $(domElement).text('기준일자');

                            });
                            $("#feedsExposurePivot .pvtRendererArea table.pvtTable").pivotBoard();
                            $("#feedsExposurePivot .pvtTotal, .pvtTotalLabel, .pvtGrandTotal").remove();
                            $("#feedsExposurePivot .pvtTable tr.head-group").find('th.pvtColLabel').each(function (index, domElement) {
                                $(domElement).text($(domElement).text().substring(2));
                            });

                            $scope.$apply(function(){
                                $scope.pageState.loading = false;
                                $scope.pageState.emptyData = false;
                            });
                        }
                    },
                    true
                );
                $("#feedsExposurePivot > table").find("tr").each(function (index, domElement) {
                    if (index == 0 || index == 1)
                        $(domElement).css("display", "none");
                });
                $(".pvtAxisContainer.pvtRows").hide();
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        }).error(function () {
            alert('error!');
        });
    };


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
	    'tableId': 'feedsExposurePivot',
	    'xlsName': 'visit-feed.xls'
	};

}

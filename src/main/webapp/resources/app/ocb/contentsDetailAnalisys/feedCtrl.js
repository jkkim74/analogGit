/**
 * Created by cookatrice on 2014. 5. 15..
 */

function feedCtrl($scope, $http, API_BASE_URL, reportSvc) {

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
        //$('#feedsPivotLoading').show();
        //$('#feedsPivot').hide();
        $scope.feedsPivotLoading = true;
        $scope.feedsPivotNoData = false;
        $scope.feedsPivotData = false;
        var url = '/ocb/contentsDetailAnalisys/feed/pivot?dateType=' + $scope.searchDateType + '&startDate=' + $scope.searchStartDate + '&endDate=' + $scope.searchEndDate;

        $http.get(url).success(function (result) {
            if (result.length != 0 && result.rows.length != 0) {
                var pivotData = result.rows;
                $("#feedsPivot").pivotUI(
                    pivotData,
                    {
                        rows: ["feedId", "feedNm", "feedStartDate", "feedEndDate", "isYn", "allianceYn", "dispOrder", 'stdDt'],
                        cols: ["measure"],
                        vals: ["measureValue"],
                        aggregatorName: 'intSum',
                        onRefresh : function() {
                            $("#feedsPivot .pvtAxisLabel").each(function (index, domElement) {
                                if (index == 1)
                                    $(domElement).text('피드ID');
                                else if (index == 2)
                                    $(domElement).text('피드명');
                                else if (index == 3)
                                    $(domElement).text('시작일자');
                                else if (index == 4)
                                    $(domElement).text('종료일자');
                                else if (index == 5)
                                    $(domElement).text('IS인벤토리');
                                else if (index == 6)
                                    $(domElement).text('제휴사타겟팅');
                                else if (index == 7)
                                    $(domElement).text('노출순서');
                                else if (index == 8)
                                    $(domElement).text('기준일자');
                            });
                            $("#feedsPivot .pvtRendererArea table.pvtTable").pivotBoard();
                            $("#feedsPivot .pvtTotal, .pvtTotalLabel, .pvtGrandTotal").remove();
                            $scope.feedsPivotLoading = false;
                            $scope.feedsPivotNoData = false;
                            $scope.feedsPivotData = true;
                            $scope.$safeApply();
                            //if(!$scope.$$phase)
                            //    $scope.$apply();
                            //$('#feedsPivotLoading').hide();
                            //$('#feedsPivot').show();
                        }
                    },
                    true
                );
                $("#feedsPivot > table").find("tr").each(function (index, domElement) {
                    if (index == 0 || index == 1)
                        $(domElement).css("display", "none");
                });
                $(".pvtAxisContainer.pvtRows").hide();
            } else {
                $scope.feedsPivotLoading = false;
                $scope.feedsPivotNoData = true;
                $scope.feedsPivotData = false;
                if(!$scope.$$phase)
                    $scope.$apply();
            }
        }).error(function () {
            alert('error!');
        });
    }

    $scope.$on('$destroy', function() {
        $("#feedsPivot").empty();
    });

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
        'tableId': 'feedsPivot',
        'xlsName': 'visit-feed.xls'
	};
}

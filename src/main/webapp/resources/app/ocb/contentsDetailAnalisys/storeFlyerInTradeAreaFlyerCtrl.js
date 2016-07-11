/**
 * Created by cookatrice
 */

function storeFlyerInTradeAreaFlyerCtrl($scope,reportSvc, $http, API_BASE_URL, DATE_TYPE_DAY) {

    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal();
        $scope.searchConfig = {
            startDate : defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain
        };
        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;
        $scope.searchMeasureCode = 'UV';

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

        var url = '/ocb/contentsDetailAnalisys/storeFlyerInTradeAreaFlyer/pivot?dateType='+
            $scope.searchDateType+'&startDate='+$scope.searchStartDate+'&endDate='+$scope.searchEndDate;
        url += '&measureCode=' + $scope.searchMeasureCode;

        $http.get(url).success(function (result) {
            if (result.length != 0 && result.rows.length != 0) {
                var pivotData = result.rows;
                $("#storeFlyerInTradeAreaFlyerPivot").pivotUI(
                    pivotData,
                    {
                        rows: ["pushGrpId","grpId","sndStartDt","sndEndDt","flyrId","storeNm"],
                        cols: ['stdDt'],
                        vals: ["measureValue"],
                        aggregatorName: 'intSum',
                        onRefresh: function () {
                            $("#storeFlyerInTradeAreaFlyerPivot .pvtAxisLabel").each(function (index, domElement) {
                                if (index == 0)
                                    $(domElement).text('기준일자');
                                else if (index == 1)
                                    $(domElement).text('권역(Grouping ID)');
                                else if (index == 2)
                                    $(domElement).text('권역');
                                else if (index == 3)
                                    $(domElement).text('발송시작일');
                                else if (index == 4)
                                    $(domElement).text('발송종료일');
                                else if (index == 5)
                                    $(domElement).text('매장전단ID');
                                else if (index == 6)
                                    $(domElement).text('가맹점명');
                            });
                            $("#storeFlyerInTradeAreaFlyerPivot .pvtRendererArea table.pvtTable").pivotBoard();
                            $("#storeFlyerInTradeAreaFlyerPivot .pvtTotal, .pvtTotalLabel, .pvtGrandTotal").remove();

                            $("#storeFlyerInTradeAreaFlyerPivot .pvtTable tr th.pvtRowLabel").each(function (i, obj) {
                                $(obj).css('text-align', 'left');
                            });

                            if($("#storeFlyerInTradeAreaFlyerPivot .pvtTable").width() < 954){
                                $("#storeFlyerInTradeAreaFlyerPivot .pvtTable").width(952);
                            }

                            $scope.$safeApply(function() {
                                $scope.pageState.loading = false;
                                $scope.pageState.emptyData = false;
                            });
                        }
                    },
                    true
                );
                $("#storeFlyerInTradeAreaFlyerPivot > table").find("tr").each(function (index, domElement) {
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
    }

    $scope.$on('$destroy', function() {
        $('#storeFlyerInTradeAreaFlyerPivot').empty();
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
        $scope.searchMeasureCode = result.searchCode;

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
        tableId : 'storeFlyerInTradeAreaFlyerPivot',
        titleName : '상권전단내 매장전단',
        xlsName : 'store-in-trade-area.xls'
    };
}

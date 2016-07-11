/**
 * Created by cookatrice
 */

function mbilFlyrPageInquiryCtrl($scope, reportSvc, $http, API_BASE_URL, DATE_TYPE_DAY) {

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

        var url = '/ocb/contentsDetailAnalisys/mobileFlyerPageInquiry/pivot?dateType='+
            $scope.searchDateType+'&startDate='+$scope.searchStartDate+'&endDate='+$scope.searchEndDate;
        url += '&measureCode=' + $scope.searchMeasureCode;

        $http.get(url).success(function (result) {
            if (result.length != 0 && result.rows.length != 0) {
                var pivotData = result.rows;
                $("#mbilFlyrPageInquiryPivot").pivotUI(
                    pivotData,
                    {
                        rows: ["pageId","id","title"],
                        cols: ['stdDt'],
                        vals: ["measureValue"],
                        aggregatorName: 'intSum',
                        onRefresh: function () {
                            $("#mbilFlyrPageInquiryPivot .pvtAxisLabel").each(function (index, domElement) {
                                if (index == 0)
                                    $(domElement).text('기준일자');
                                else if (index == 1)
                                    $(domElement).text('페이지ID');
                                else if (index == 2)
                                    $(domElement).text('ID');
                                else if (index == 3)
                                    $(domElement).text('타이틀');
                            });
                            $("#mbilFlyrPageInquiryPivot .pvtRendererArea table.pvtTable").pivotBoard();
                            $("#mbilFlyrPageInquiryPivot .pvtTotal, .pvtTotalLabel, .pvtGrandTotal").remove();

                            $("#mbilFlyrPageInquiryPivot .pvtTable tr th.pvtRowLabel").each(function (i, obj) {
                                $(obj).css('text-align', 'left');
                            });

                            if($("#mbilFlyrPageInquiryPivot .pvtTable").width() < 954){
                                $("#mbilFlyrPageInquiryPivot .pvtTable").width(952);
                            }

                            $scope.$safeApply(function() {
                                $scope.pageState.loading = false;
                                $scope.pageState.emptyData = false;
                            });
                        }
                    },
                    true
                );
                $("#mbilFlyrPageInquiryPivot > table").find("tr").each(function (index, domElement) {
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
        //$('#mbilFlyrPageInquiryPivot').children().unbind();
        //$('#mbilFlyrPageInquiryPivot').children().off();
        $('#mbilFlyrPageInquiryPivot').empty();
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
        tableId : 'mbilFlyrPageInquiryPivot',
        titleName : '모바일전단 페이지조회',
        xlsName : 'mobile-flyer-page-inquiry.xls'
    };
}

/**
 * Created by cookatrice
 */

function newOcbSegCtrl($scope, $http, API_BASE_URL, DATE_TYPE_MONTH, reportSvc) {

    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal(DATE_TYPE_MONTH);
        $scope.searchConfig = {
            startDate: defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain
        };
        $scope.searchDateType = DATE_TYPE_MONTH;
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;

        drawPivot();
    };

    function drawPivot() {
        var url = "/ocb/khub/newOcbSeg/newOcbSeg/grid?dateType=" + $scope.searchDateType
            + "&startDate=" + $scope.searchStartDate
            + "&endDate=" + $scope.searchEndDate;

        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };

        $http.get(url).success(function (result) {

//            console.log(result);

            if (result.length != 0 && result.rows.length != 0) {
                var pivotData = result.rows;
                $("#newOcbSegPivot").pivotUI(
                    pivotData,
                    {
                        rows: ["rptIndNm"],
                        cols: ["measure"],
                        vals: ["measureValue"],
                        aggregatorName: 'intSum',
                        rendererName: 'table',
                        onRefresh: function () {
                            $("#newOcbSegPivot .pvtAxisLabel").each(function (index, domElement) {
                                if (index == 0)
                                    $(domElement).text('기준일자');
                                if (index == 1)
                                    $(domElement).text('measure');

                            });
                            $("#newOcbSegPivot .pvtRendererArea table.pvtTable").pivotBoard();
                            $("#newOcbSegPivot .pvtTotal, .pvtTotalLabel, .pvtGrandTotal").remove();

                            $("#newOcbSegPivot .pvtTable tr .pvtColLabel").each(function (i, obj) {
                                $(obj).text($(obj).text().substring(2));
                            });

                            $("#newOcbSegPivot .pvtTable tr th.pvtRowLabel").each(function (i, obj) {
                                $(obj).css('text-align', 'left');
                            });

                            $("#newOcbSegPivot .pvtTable tr:first").before("<tr class='head-group'><th class='pvtColLabel'></th><th class='pvtColLabel'></th><th class='pvtColLabel' colspan='3'>전체TR기준</th><th class='pvtColLabel' colspan='3'>적립TR기준</th><th class='pvtColLabel' colspan='3'>사용TR기준</th></tr>");


                            if ($("#newOcbSegPivot .pvtTable").width() < 954) {
                                $("#newOcbSegPivot .pvtTable").width(952);
                            }
                            $scope.$safeApply(function() {
                                $scope.pageState.loading = false;
                                $scope.pageState.emptyData = false;
                            });
                        }
                    },
                    true
                );

                $("#newOcbSegPivot > table").find("tr").each(function (index, domElement) {
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
    $scope.search = function (result) {
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
        downloadUrl: '/download/report/excel',
        downloadType: 'POI',
        pivotFlag: 'Y',
        tableId: 'newOcbSegPivot',
        titleName: 'New OCB Seg',
        xlsName: 'new-ocb-seg.xls'
    };
}

function pointCtrl($scope, $http, API_BASE_URL, DATE_TYPE_DAY, reportSvc) {

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

    /**
     * drawPivot
     */
    function drawPivot() {
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };

        var url = "/ocb/contentsDetailAnalisys/point/pivot?dateType=" + $scope.searchDateType + "&startDate=" + $scope.searchStartDate + "&endDate=" + $scope.searchEndDate;
        $http.get(url).success(function (result) {
            if (result.length != 0 && result.rows.length != 0) {
                var pivotData = result.rows;
                $("#pointPivot").pivotUI(
                    pivotData,
                    {
                        rows: ["measure"],
                        cols: ['stdDt'],
                        vals: ['measureValue'],
                        aggregatorName: 'intSum',
                        onRefresh: function () {
                            $("#pointPivot .pvtAxisLabel").each(function (index, domElement) {
                                if (index == 0)
                                    $(domElement).text('기준일자');
                            });
                            $("#pointPivot .pvtRendererArea table.pvtTable").pivotBoard();
                            $("#pointPivot .pvtTotal, .pvtTotalLabel, .pvtGrandTotal").remove();
                            $('#pointPivot th.pvtRowLabel').each(function (i, obj) {
                                    $(obj).text($(obj).text().substring(1))
                                        .css('text-align', 'left');
                            });

                            if ($("#pointPivot .pvtTable").width() < 954) {
                                $("#pointPivot .pvtTable").width(952);
                            }

                            $scope.$safeApply(function() {
                                $scope.pageState.loading = false;
                                $scope.pageState.emptyData = false;
                            });
                        }
                    },
                    true
                );
                $("#pointPivot > table").find("tr").each(function (index, domElement) {
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
        //$('#pointPivot').children().unbind();
        //$('#pointPivot').children().off();
        $("#pointPivot").empty();
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
        drawPivot();
    };

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        'tableId': 'pointPivot',
        'xlsName': 'contents-point.xls'
    };
}

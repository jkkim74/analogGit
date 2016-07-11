/**
 * Created by cookatrice
 */

function discoverCtrl($scope, reportSvc, $http, API_BASE_URL, DATE_TYPE_DAY) {

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

        var url = '/ocb/contentsDetailAnalisys/discover/pivot?dateType='+$scope.searchDateType+'&startDate='+
            $scope.searchStartDate+'&endDate='+$scope.searchEndDate;

        $http.get(url).success(function (result) {
            if (result.length != 0 && result.rows.length != 0) {
                var pivotData = result.rows;
                $("#discoverPivot").pivotUI(
                    pivotData,
                    {
                        rows: ["mainCategory", "subCategory", "measure"],
                        cols: ['stdDt'],
                        vals: ["measureValue"],
                        aggregatorName: 'intSum',
                        onRefresh: function () {

                            $("#discoverPivot .pvtAxisLabel").each(function (index, domElement) {
                                if (index == 0)
                                    $(domElement).text('기준일자');
                                else if (index == 1)
                                    $(domElement).text('항목');
                                else if (index == 2)
                                    $(domElement).text('구분');
                            });
                            $("#discoverPivot .pvtRendererArea table.pvtTable").pivotBoard();
                            $("#discoverPivot .pvtTotal, .pvtTotalLabel, .pvtGrandTotal").remove();

                            $("#discoverPivot .pvtTable tr th.pvtRowLabel").each(function (i, obj) {
                                $(obj).text($(obj).text().substring(2))
                                    .css('text-align', 'left');
                            });

                            if($("#discoverPivot .pvtTable").width() < 954){
                                $("#discoverPivot .pvtTable").width(952);
                            }

                            $scope.$safeApply(function() {
                                $scope.pageState.loading = false;
                                $scope.pageState.emptyData = false;
                            });
                        }
                    },
                    true
                );
                $("#discoverPivot > table").find("tr").each(function (index, domElement) {
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

    $scope.$on('$destroy', function() {
        //$('#discoverPivot').children().unbind();
        //$('#discoverPivot').children().off();
        $("#discoverPivot").children().remove();
    });

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        downloadUrl : '/download/report/excel',
        downloadType : 'POI',
        pivotFlag : 'Y',
        tableId : 'discoverPivot',
        titleName : 'discover',
        xlsName : 'discover.xls'
    };
}

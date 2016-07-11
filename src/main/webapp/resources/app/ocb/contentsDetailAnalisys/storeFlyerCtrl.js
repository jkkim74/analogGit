function storeFlyerCtrl($scope,reportSvc, $http, API_BASE_URL, DATE_TYPE_DAY) {

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

        var url = '/ocb/contentsDetailAnalisys/storeFlyer/pivot?dateType='+$scope.searchDateType+'&startDate='+$scope.searchStartDate+'&endDate='+$scope.searchEndDate;
        url += '&measureCode=' + $scope.searchMeasureCode;

        $http.get(url).success(function (result) {
            if (result.length != 0 && result.rows.length != 0) {
                var pivotData = result.rows;
                $("#storeFlyerPivot").pivotUI(
                    pivotData,
                    {
                        rows: ["pushGrpId","grpId","sndStartDt","sndEndDt","measure"],
                        cols: ['stdDt'],
                        vals: ["measureValue"],
                        aggregatorName: 'intSum',
                        onRefresh: function () {
                            $("#storeFlyerPivot .pvtAxisLabel").each(function (index, domElement) {
                                if (index == 0) $(domElement).text('기준일자');
                                else if (index == 1) $(domElement).text('권역 Grouping ID');
                                else if (index == 2) $(domElement).text('권역');
                                else if (index == 3) $(domElement).text('발송시작일');
                                else if (index == 4) $(domElement).text('발송종료일');
                            });

                            $("#storeFlyerPivot .pvtRendererArea table.pvtTable").pivotBoard();
                            $("#storeFlyerPivot .pvtTotal, .pvtTotalLabel, .pvtGrandTotal").remove();

                            $("#storeFlyerPivot .pvtTable tr th.pvtRowLabel").each(function (i, obj) {
                                if($(obj).text().indexOf("RCV") > 0) $(obj).text('수신');
                                if($(obj).text().indexOf("CLICK") > 0) $(obj).text('클릭');
                                $(obj).css({'text-align':'left','padding-left': '20px'});
                            });

                            if($("#storeFlyerPivot .pvtTable").width() < 954){
                                $("#storeFlyerPivot .pvtTable").width(952);
                            }

                            $scope.$safeApply(function() {
                                $scope.pageState.loading = false;
                                $scope.pageState.emptyData = false;
                            });
                        }
                    },
                    true
                );
                $("#storeFlyerPivot > table").find("tr").each(function (index, domElement) {
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
        //$('#storeFlyerPivot').children().unbind();
        //$('#storeFlyerPivot').children().off();
        $('#storeFlyerPivot').empty();
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
        tableId : 'storeFlyerPivot',
        titleName : '상권전단',
        xlsName : 'store-flyer.xls'
    };
}

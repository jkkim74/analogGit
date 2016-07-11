/**
 * Created by mimul
 */

function mbilFlyrCopnCtrl($scope, reportSvc, DATE_TYPE_DAY, apiSvc) {

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

    function setPivotTableConfig(rawData) {
        $('#' + $scope.pivotTableId).pivotUI(
            rawData,
            {
                rows: ["flyrId", "flyrTitle", "storeNm", "sndStartDt", "copnId", "copnNm"],
                cols: ['stdDt'],
                vals: ["measureValue"],
                aggregatorName: 'intSum',
                onRefresh: function () {
                    var config = {
                        id: $scope.pivotTableId,
                        modelData: [
                            {name: '전단ID', width: 100},
                            {name: '전단제목', width: 200},
                            {name: '가맹점명', width: 100},
                            {name: '발송요청일', width: 100},
                            {name: '쿠폰ID', width: 100},
                            {name: '쿠폰명', width: 200}
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
        //$('#' + $scope.pivotTableId).children().unbind();
        //$('#' + $scope.pivotTableId).children().off();
        $('#' + $scope.pivotTableId).empty();
    });

    /**
     * drawPivot
     */
    function drawPivot() {
        /** resize pivotTable and chart width **/
        $scope.pivotTableId = 'mbilFlyrCopnPivot';
        $scope.headerView = false;
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };

        var httpConfig = {
            url: '/ocb/contentsDetailAnalisys/mobileCoupon/pivot',
            params: {
                dateType: $scope.searchDateType,
                startDate: $scope.searchStartDate,
                endDate: $scope.searchEndDate,
                measureCode: $scope.searchMeasureCode
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
        tableId : 'mbilFlyrCopnPivot',
        titleName : '모바일전단 쿠폰다운',
        xlsName : 'mobile-frontlayer-coupon.xls'
    };
}

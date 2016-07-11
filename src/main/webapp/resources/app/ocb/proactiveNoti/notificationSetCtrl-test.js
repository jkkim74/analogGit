/**
 * Created by cookatrice on 2014. 5. 21..
 */

function notificationSetCtrl($scope, $http, API_BASE_URL, DATE_TYPE_DAY, reportSvc) {

    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal();
        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchConfig = {
            startDate : defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain
        };

        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;

        drawPivot();
    };

    /**
     * drawPivot
     */
    function drawPivot() {
        $scope.headerView = false;
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };

//        var url = API_BASE_URL + '/ocb/proactiveNoti/notificationSet/pivot?dateType=' + $scope.searchDateType + '&startDate=' + $scope.searchStartDate + '&endDate=' + $scope.searchEndDate;
        var url = '/ocb/proactiveNoti/notificationSet/pivot?dateType=' + $scope.searchDateType + '&startDate=' + $scope.searchStartDate + '&endDate=' + $scope.searchEndDate;

        $http.get(url).success(function (result) {
            if (result.length != 0 && result.rows.length != 0) {
                var pivotData = result.rows;
                $("#notificationSetPivot").pivotUI(
                    pivotData,
                    {
                        rows: ["measure"],
                        cols: ['stdDt'],
                        vals: ["measureValue"],
                        aggregatorName: 'intSum',
                        onRefresh: function() {
//                            $("#notificationSetPivot .pvtRendererArea table.pvtTable").pivotBoard();
//                            $("#notificationSetPivot .pvtTotal, .pvtTotalLabel, .pvtGrandTotal").remove();
//                            $('#notificationSetPivot th.pvtRowLabel').each(function (i, obj) {
//                                $(obj).text($(obj).text().substring(2))
//                                    .css('text-align', 'left');
//                            });
//
//                            /** Fix the pivot table header - start ***************************************************/
//
//                            $scope.headerView = true;
//                            $scope.tableHeaderList = [];
//                            $("#notificationSetPivot .pvtTable tr.head-group th.pvtColLabel").each(function (i, obj) {
//                                $scope.tableHeaderList.push($(obj).text());
//                            });
////                            console.log($scope.tableHeaderList);
////                            console.log('pvtTable table width : ' + $('#notificationSetPivot .pvtTable').width());
//
//                            $('.data-area').on('scroll', function () {
//                                $('#notificationSetPivot_h .fixedTableHeader').css('top', $(this).scrollTop());
//                            });
//
//                            var pivotTableWidth = $('#notificationSetPivot .pvtTable').width();
//                            if(pivotTableWidth<500){ pivotTableWidth = 500; }
//
//                            //pivot table header control
//                            $('#notificationSetPivot_h .fixedTableHeader').css('width',pivotTableWidth);        //header table width
//                            $('#notificationSetPivot_h .fixedTableHeader tr th:first-child').css('width',200);  //first category width
//
//                            //
//                            $('#notificationSetPivot .pvtTable').css('width',pivotTableWidth);        //header table width
//                            $('#notificationSetPivot .pvtTable tr:nth-child(1) th:nth-child(1)').remove();
//                            $('#notificationSetPivot .pvtTable tr:nth-child(2)').remove();
//                            $('#notificationSetPivot .pvtTable tr th:nth-child(1)').each(function (i, obj) {$(obj).attr('colspan', 1)});
//                            $('#notificationSetPivot .pvtTable tr:nth-child(1) th').each(function (i, obj) {$(obj).attr('rowspan', 1)});
//
//                            $('#notificationSetPivot .pvtTable tr:first-child th:first-child').css('width',200);  //first category width
//                            $('#notificationSetPivot .pvtTable tr:first-child th:first-child').text('measure');   //first category name


                            var config = {
                                id: "notificationSetPivot",
                                customOrder : 2,    //substring custom order. default = 0.
                                modelData: [
                                    {name: 'measure', width: 150}
                                ]
                            };
                            $scope.headerView = true;
                            DomHelper.reDrawPivotTableHeader(config);
                            $scope.tableHeaderList = DomHelper.getDateList(config.id);


                            /** Fix the pivot table header - end ***************************************************/

                            $scope.$apply(function(){
                                $scope.pageState.loading = false;
                                $scope.pageState.emptyData = false;
                            });                        }
                    },
                    true
                );
//                $("#notificationSetPivot > table").find("tr").each(function (index, domElement) {
//                    if (index == 0 || index == 1)
//                        $(domElement).css("display", "none");
//                });
//                $(".pvtAxisContainer.pvtRows").hide();
//                /** 헤더 없애기 **/
//                $("#notificationSetPivot .pvtTable").find('tr').each(function (index, domElement) {
//                    if (index == 0 || index == 1)
//                        $(domElement).css("display", "none");
//                });

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
        downloadUrl : '/download/report/excel',
        downloadType : 'POI',
        pivotFlag : 'Y',
        tableId : 'notificationSetPivot',
        titleName : '알림설정',
        xlsName : 'notifiication-set.xls'
	};
}

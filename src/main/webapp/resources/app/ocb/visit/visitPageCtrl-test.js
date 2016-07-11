function visitPageCtrl($scope, $http, API_BASE_URL, DATE_TYPE_DAY, reportSvc) {
    $scope.grida = null;
    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal();
        $scope.searchConfig = {
            startDate : defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain
        };
        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;
        $scope.searchMeasureCode = 'LV';
        drawPivot();
    }

    /**
     * drawPivot
     */
    function drawPivot() {
        $scope.headerView = false;
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };

//        var url = API_BASE_URL + '/ocb/visit/page/grid?dateType=' + $scope.searchDateType + '&startDate=' + $scope.searchStartDate + '&endDate=' + $scope.searchEndDate;
        var url = '/ocb/visit/page/grid?dateType=' + $scope.searchDateType + '&startDate=' + $scope.searchStartDate + '&endDate=' + $scope.searchEndDate;
        url += '&measureCode=' + $scope.searchMeasureCode;

        $http.get(url).success(function (result) {
            if (result.length != 0 && result.rows.length != 0) {
                var pivotData = result.rows;

                $("#visitorsPageGrid").pivotUI(
                    pivotData,
                    {
                        rows: ["measure", "pageId","depth1Nm","depth2Nm","depth3Nm","depth4Nm","depth5Nm"],
                        cols: ['stdDt'],
                        vals: ['measureValue'],
                        aggregatorName: 'intSum',
                        onRefresh : function() {

                            /** Fix the pivot table header - start ***************************************************/
                            var config = {
                                id: "visitorsPageGrid",
                                modelData: [
                                    {name: 'measure', width: 60},
                                    {name: '페이지명', width: 300},
                                    {name: 'depth1', width: 100},
                                    {name: 'depth2', width: 100},
                                    {name: 'depth3', width: 100},
                                    {name: 'depth4', width: 100},
                                    {name: 'depth5', width: 100}
                                ]
                            };

                            $scope.headerView = true;

//                            $scope.tableHeaderList = [];
//                            $("#visitorsPageGrid .pvtTable tr.head-group th.pvtColLabel").each(function (i, obj) {
//                                $scope.tableHeaderList.push($(obj).text());
//                            });

//                            $("#visitorsPageGrid .pvtRendererArea table.pvtTable").pivotBoard();
//                            $("#visitorsPageGrid .pvtTotal, .pvtTotalLabel, .pvtGrandTotal").remove();
//                            $('#visitorsPageGrid .pvtRowLabel').css('text-align', 'left');
                            DomHelper.reDrawPivotTableHeader(config);
                            $scope.tableHeaderList = DomHelper.getDateList(config.id);
                            //console.log($scope.tableHeaderList);

//                            $('.data-area').on('scroll', function () {
//                                $('#visitorsPageGrid_h .fixedTableHeader').css('top', $(this).scrollTop());
//                            });

//                            var pivotTableWidth = $('#visitorsPageGrid .pvtTable').width();
//                            if (pivotTableWidth < 500) { pivotTableWidth = 500; }
//
//                            $('#visitorsPageGrid_h .fixedTableHeader').css('width',pivotTableWidth);        //header table width
//                            $('#visitorsPageGrid_h .fixedTableHeader tr th:first-child').css('width',60);   //first category width
//                            $('#visitorsPageGrid_h .fixedTableHeader tr th:nth-child(2)').css('width',300); //second category width
//                            $('#visitorsPageGrid_h .fixedTableHeader tr th:nth-child(3)').css('width',100); //third category width
//                            $('#visitorsPageGrid_h .fixedTableHeader tr th:nth-child(4)').css('width',100); //4th category width
//                            $('#visitorsPageGrid_h .fixedTableHeader tr th:nth-child(5)').css('width',100); //5th category width
//                            $('#visitorsPageGrid_h .fixedTableHeader tr th:nth-child(6)').css('width',100); //6th category width
//                            $('#visitorsPageGrid_h .fixedTableHeader tr th:nth-child(7)').css('width',100); //7th category width
//
//
////                            //pivot table header control
//                            $('#visitorsPageGrid .pvtTable').css('width',pivotTableWidth);        //header table width
//                            $('#visitorsPageGrid .pvtTable tr:nth-child(1) th:nth-child(1)').remove();
//                            $('#visitorsPageGrid .pvtTable tr:nth-child(2)').remove();
//                            $('#visitorsPageGrid .pvtTable tr:nth-child(1) th').each(function (i, obj) {$(obj).attr('rowspan', 1)});
//
//                            //병합..나누기
//                            $('#visitorsPageGrid .pvtTable tr th:nth-child(7)').each(function (i, obj) {$(obj).attr('colspan', 1)});
//                            $('#visitorsPageGrid .pvtTable tr th:nth-child(6)').each(function (i, obj) {$(obj).attr('colspan', 1)});
//
//                            //컬럼추가(역순으로)
//                            $('#visitorsPageGrid .pvtTable tr:first-child th:first-child').text('depth5');   //first category name - default <th>변경
////                            $('.pvtTable tr:nth-child(1)').prepend('<th>depth5</th>');
//                            $('#visitorsPageGrid .pvtTable tr:nth-child(1)').prepend('<th>depth4</th>');
//                            $('#visitorsPageGrid .pvtTable tr:nth-child(1)').prepend('<th>depth3</th>');
//                            $('#visitorsPageGrid .pvtTable tr:nth-child(1)').prepend('<th>depth2</th>');
//                            $('#visitorsPageGrid .pvtTable tr:nth-child(1)').prepend('<th>depth1</th>');
//                            $('#visitorsPageGrid .pvtTable tr:nth-child(1)').prepend('<th>페이지명</th>');
//                            $('#visitorsPageGrid .pvtTable tr:nth-child(1)').prepend('<th>measure</th>');
//
//                            $('#visitorsPageGrid .pvtTable tr:first-child th:first-child').css('width',60);  //first category width
//                            $('#visitorsPageGrid .pvtTable tr:first-child th:nth-child(2)').css('width',300);  // 2nd category width
//                            $('#visitorsPageGrid .pvtTable tr:first-child th:nth-child(3)').css('width',100);  // 3rd category width
//                            $('#visitorsPageGrid .pvtTable tr:first-child th:nth-child(4)').css('width',100);  // 4th category width
//                            $('#visitorsPageGrid .pvtTable tr:first-child th:nth-child(5)').css('width',100);  // 5th category width
//                            $('#visitorsPageGrid .pvtTable tr:first-child th:nth-child(6)').css('width',100);  // 6th category width
//                            $('#visitorsPageGrid .pvtTable tr:first-child th:nth-child(7)').css('width',100);  // 7th category width

                            /** Fix the pivot table header - end ***************************************************/

                            $scope.$apply(function(){
                                $scope.pageState.loading = false;
                                $scope.pageState.emptyData = false;
                            });
                        }
                    },
                    true
                );

//                $("#visitorsPageGrid > table").find("tr").each(function (index, domElement) {
//                    if (index == 0 || index == 1)
//                        $(domElement).css("display", "none");
//                });
//                $(".pvtAxisContainer.pvtRows").hide();

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
    $scope.search = function(result) {
        $scope.searchDateType = result.searchDateType;
        $scope.searchStartDate = result.searchStartDate;
        $scope.searchEndDate = result.searchEndDate;
        $scope.searchMeasureCode = result.searchCode;
        drawPivot();
    };

    $scope.toExcel = function () {
        $scope.grida.toExcel();
    };

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        downloadUrl : '/download/report/excel',
        downloadType : 'POI',
        pivotFlag : 'Y',
        tableId : 'visitorsPageGrid',
        titleName : '방문페이지',
        xlsName : 'visit-page.xlsx'
    };

}

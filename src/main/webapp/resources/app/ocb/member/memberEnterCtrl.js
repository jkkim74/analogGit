/**
 * Created by cookatrice on 2014. 5. 8..
 */

function memberEnterCtrl($scope, DATE_TYPE_DAY, reportSvc) {

    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal();
        $scope.searchConfig = {
            startDate : defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain
        };

        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;
        $scope.drawMemberEnterGrid();
    };

    $scope.drawMemberEnterGrid = function(){
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };
        var memberEnterData = [];
        var heightVal = "400";
        $.ajax({
            url: '/ocb/member/enter/grid?dateType='+$scope.searchDateType+'&startDate='+$scope.searchStartDate+'&endDate='+$scope.searchEndDate+
                '&rows=-1&page=1&sidx=stdDt&sord=asc',
            type: "GET",
            dataType: "json",
            cache: false,
            async: false,
            timeout: 10000,
            success: function(data) {
                if (data.rows.length != 0) {
                    $scope.pageState.loading = false;
                    $scope.pageState.emptyData = false;
                    memberEnterData = data.rows;
                    if (data.rows.length > 20)
                        heightVal = "400";
                    else
                        heightVal = "100%";
                } else {
                    $scope.pageState.loading = false;
                    $scope.pageState.emptyData = true;
                    heightVal = "400";
                }

            },
            error: function () {
            }
        });

        $scope.memberEnter = {
            data : memberEnterData,
            id: 'memberEnterGrid',
            datatype: "local",
            //datatype: "json",
            height: '100%',
            //rowNum: 10000,
            //rowList: [10, 20, 30],
            colNames: ['기준일자', '신규가입자수', '누적가입자수'],
            colModel: [
                {name: 'stdDt', index: 'stdDt', editable: false, width: 90, align: "center", sortable: true},
                {name: 'newEntrCnt', index: 'newEntrCnt', editable: false, width: 90, formatter: 'integer', align: "center", sortable: false},
                {name: 'acmEntrCnt', index: 'acmEntrCnt', editable: false, width: 90, formatter: 'integer', align: "center", sortable: false}
            ],
            gridview: true,
            viewrecords: true,
            sortname: 'stdDt',
            sortorder: 'asc',
            multiselect: false,
            autowidth: true,
            scroll: true,
            loadonce: true,
            jsonReader: {
                root: "rows",
                page: "page",
                total: "total",
                records: "records",
                repeatitems: false,
                cell: "cell"
            }
        };
    };

    /**
     * 검색 조회 callback 함수
     *
     * @params result
     */
    $scope.search = function(result) {
        $scope.searchDateType = result.searchDateType;
        $scope.searchStartDate = result.searchStartDate;
        $scope.searchEndDate  = result.searchEndDate;
        $scope.drawMemberEnterGrid();
    };

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        downloadUrl : '/ocb/member/downloadExcelForEnter',
        downloadType : 'POI',
        pivotFlag : 'N',
        tableId : 'gview_memberEnterGrid',
        xlsName : 'customer-enter.xls'
    };

}

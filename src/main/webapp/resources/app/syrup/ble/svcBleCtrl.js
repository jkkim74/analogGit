function svcBleCtrl($scope, $http, API_BASE_URL, BLE_DATE_TYPES, reportSvc) {
    $scope.init = function () {
        var defaultCal = reportSvc.defaultCustomCal('day', 1, 1, 1);
        $scope.searchConfig = {
            startDate: defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain,
            //measureTypeName: 'Measure 종류',
            //measureTypes: [
            //    {code: '1', displayName:'PV'},
            //    {code: '2', displayName:'UV'}
            //    //{code: '3', displayName:'UU'}
            //],
            searchItemName: '항목검색',
            searchItems: [
                {code: '1', displayName:'Campaign ID'},
                //{code: '2', displayName:'제휴사'},
                {code: '3', displayName:'가맹점(MID)'},
                {code: '4', displayName:'BID'}
            ],
            serviceTypeName: '서비스종류',
            serviceTypes: [
                {code: '1', displayName:'전체'},
                {code: '2', displayName:'OCB'},
                {code: '3', displayName:'Syrup'}
            ],
            statContentName:'통계항목',
            statContents: [
                {code: '1', displayName:'전체', disabled: false},
                {code: '2', displayName:'BLE발송항목', disabled: true},
                {code: '3', displayName:'BLE수신항목', disabled: false},
                {code: '4', displayName:'BLE클릭항목', disabled: false},
                {code: '5', displayName:'BLE열람항목', disabled: false}
                //{code: '6', displayName:'BLE콘텐츠항목'}
            ]
//            newTechName: 'New-tech종류',
//            newTechs: [
//                {code: '1', displayName:'전체'},
//                {code: '2', displayName:'BLE'},
//                {code: '3', displayName:'Wi-Fi'},
//                {code: '4', displayName:'Sound'}
//            ],
//            versionTypeName: '버전종류',
//            versionTypes: [
//                {code: '1', displayName:'전체'},
//                {code: '2', displayName:'OCB APP'},
//                {code: '3', displayName:'Syrup APP'},
//                {code: '4', displayName:'IOS'},
//                {code: '5', displayName:'AOS'},
//                {code: '6', displayName:'SDK'}
//            ],
//            conditionTypeName: '조건설정',
//            conditionTypes: [
//                {code: '1', displayName:'Device Traffic 0건'}
//            ],
//            errorTypeName: '오류항목',
//            errorTypes: [
//                {code: '1', displayName:'상세 오류항목 보기'}
//            ]
        };
        $scope.searchDateType = 'sum';
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;
        $scope.searchCode = '1';
        //$scope.searchString = null;//29024063
        $scope.searchServiceType = '1';
        $scope.searchStatContent = ["1"];
        $scope.colNames = [];
        $scope.colModels = [];
        $scope.group1Headers = [];
        $scope.group2Headers = [];
        $scope.drawGrid();
    };

    $scope.drawGrid = function () {
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };
        $scope.tableId = 'svcBleForGrid';
        gridSvcBle();
    };

    function gridSvcBle() {
        getHeader();
        //grid
        $scope.svcBleForGridConfig = {
            id: 'svcBleForGrid',
            url: '/syrup/ble/service/grid',
            mtype: "POST",
            serializeGridData: function(postData) {
                var newPostData = $.extend(postData, {
                    dateType : $scope.searchDateType,
                    startDate: $scope.searchStartDate,
                    endDate: $scope.searchEndDate,
                    itemCode: $scope.searchCode,
                    searchString: $scope.searchString,
                    serviceTypeCode: $scope.searchServiceType
                });
                return $.param(newPostData);
            },
            ajaxGridOptions: { contentType: "application/x-www-form-urlencoded; charset=utf-8" },
            datatype: "json",
            height: "100%",
            shrinkToFit: false,
            rowNum: 1000,
            rowList: [1000,2000,3000],
            colNames: $scope.colNames,
            colModel: $scope.colModels,
            gridComplete: function () {
                angular.element('#' + $scope.tableId).jqGrid('destroyGroupHeader');
                angular.element('#' + $scope.tableId).jqGrid('setGroupHeaders', {
                    useColSpanStyle: true,
                    groupHeaders: $scope.group1Headers
                });
                angular.element('#' + $scope.tableId).jqGrid('setGroupHeaders', {
                    useColSpanStyle: true,
                    groupHeaders: $scope.group2Headers
                });
                var $headTag = $("#gbox_svcBleForGrid .ui-jqgrid-htable").find('tr:nth-child(2)');
                $headTag.find('th:nth-child(1)').attr('rowspan', 3).next().attr('rowspan', 3).next().attr('rowspan', 3).next().attr('rowspan', 3);
            },
            gridview: true,
            multiselect: false,
            autowidth: true,
            hidegrid: false,
            loadonce: false,
            scroll: false,
            pager: '#pager_svcBleForGrid',
            viewrecords: true,
            pagination:true,
            jsonReader: {
                root: "rows",
                page: "page",
                total: "total",
                records: "records",
                repeatitems: false,
                cell: "cell"
            },
            subGrid: true,
            subGridRowExpanded: function(subgrid_id, row_id) {
                var subgrid_table_id = subgrid_id + '_t';
                var rowData = $(this).jqGrid("getRowData", row_id);
                angular.element('#' + subgrid_id).html("<table id='" + subgrid_table_id + "' class='scroll'></table>");
                angular.element('#' + subgrid_table_id).jqGrid({
                    url: '/syrup/ble/service/subGrid?searchString=' + rowData.mid,
                    type: "GET",
                    datatype: "json",
                    colNames: ['가맹점명','MID','Campaign ID','BID'],
                    colModel: [
                        {name:"name1",index:"name1",width:130,key:true},
                        {name:"mid",index:"mid",width:100,align:"center"},
                        {name:"campaignId",index:"campaignId",width:100,align:"center"},
                        {name:"bid",index:"bid",width:100,align:"center"}
                    ],
                    height: '100%',
                    rowNum:1000,
                    sortname: 'campaignId',
                    sortorder: "asc"
                });
            }
        };
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
        $scope.searchCode = result.searchCode;
        $scope.searchString = result.searchString;
        $scope.searchServiceType = result.searchServiceType.code;
        $scope.searchStatContent = result.searchStatContent;
        $scope.colNames = [];
        $scope.colModels = [];
        $scope.group1Headers = [];
        $scope.group2Headers = [];
        // 데이터 건수가 많아서 조회기간이 5일 넘으면 조회기간 재 지정요청

        $scope.drawGrid();
    };

    function getHeader() {
        setDefaultHeader();// 초기값 셋팅.
        if ($scope.searchServiceType === '1') { //전체
            getServiceAll();
        } else if ($scope.searchServiceType === '2') {// OCB
            getServiceOcb();
        } else { // Syrup
            getServiceSyrup();
        }
    }

    function setDefaultHeader() {
        $scope.colNames = ['기준일자', '가맹점명', 'MID', 'PV', 'UV'];
        $scope.colModels = [{name: 'stdDt', index: 'stdDt', editable: false, align: "center", sortable: false},
            {name: 'name1', index: 'name1', editable: false, align: "center", sortable: false},
            {name: 'mid', index: 'mid', editable: false,  align: "center", sortable: false},
            {name: 'ocbSrpPv', index: 'ocbSrpPv', editable: false, formatter: 'integer', align: "center", sortable: false},
            {name: 'ocbSrpUv', index: 'ocbSrpUv', editable: false, formatter: 'integer', align: "center", sortable: false}];
        $scope.group1Headers = [{startColumnName: 'ocbSrpPv', numberOfColumns: 2, titleText: 'BLE Triggering 항목'}];
        $scope.group2Headers = [{startColumnName: 'ocbSrpPv', numberOfColumns: 2, titleText: 'OCB/Syrup 서버요청'}];
    }

    function getServiceAll() {//measureType=PV, ServiceType=1(전체)
        if (_.contains($scope.searchStatContent, '1')) {//전체
            $scope.colNames.push('PV', 'UV', 'PV', 'UV', 'PV', 'UV', 'PV', 'UV', 'PV', 'UV',
                'PV', 'UV', 'PV', 'UV', 'PV', 'UV', 'PV', 'UV', 'PV', 'UV',
                'PV', 'UV', 'PV', 'UV', 'PV', 'UV');
            $scope.colModels.push(
                {name: 'ocbCoinPv', index: 'ocbCoinPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'ocbCoinUv', index: 'ocbCoinUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'ocbLeafletPv', index: 'ocbLeafletPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'ocbLeafletUv', index: 'ocbLeafletUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpPushPopPv', index: 'srpPushPopPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpPushPopUv', index: 'srpPushPopUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpPushNotiPv', index: 'srpPushNotiPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpPushNotiUv', index: 'srpPushNotiUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpIconBtnPv', index: 'srpIconBtnPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpIconBtnUv', index: 'srpIconBtnUv', editable: false, formatter: 'integer', align: "center", sortable: false},

                {name: 'ocbMlfClkPv', index: 'ocbMlfClkPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'ocbMlfClkUv', index: 'ocbMlfClkUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                //{name: 'ocbChkinClkPv', index: 'ocbChkinClkPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                //{name: 'ocbChkinClkUv', index: 'ocbChkinClkUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpOkClkPv', index: 'srpOkClkPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpOkClkUv', index: 'srpOkClkUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpCnclClkPv', index: 'srpCnclClkPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpCnclClkUv', index: 'srpCnclClkUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpIconBtnClkPv', index: 'srpIconBtnClkPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpIconBtnClkUv', index: 'srpIconBtnClkUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpIconPv', index: 'srpIconPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpIconUv', index: 'srpIconUv', editable: false, formatter: 'integer', align: "center", sortable: false},

                {name: 'ocbMlfReadPv', index: 'ocbMlfReadPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'ocbMlfReadUv', index: 'ocbMlfReadUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'ocbChkinReadPv', index: 'ocbChkinReadPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'ocbChkinReadUv', index: 'ocbChkinReadUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpOkReadPv', index: 'srpOkReadPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpOkReadUv', index: 'srpOkReadUv', editable: false, formatter: 'integer', align: "center", sortable: false});
            $scope.group1Headers.push({startColumnName: 'ocbCoinPv', numberOfColumns: 10, titleText: 'BLE 수신항목'},
                {startColumnName: 'ocbMlfClkPv', numberOfColumns: 10, titleText: 'BLE 클릭항목'},
                {startColumnName: 'ocbMlfReadPv', numberOfColumns: 6, titleText: 'BLE 열람항목'});
            $scope.group2Headers.push({startColumnName: 'ocbCoinPv', numberOfColumns: 2, titleText: 'OCB 체크인'},
                {startColumnName: 'ocbLeafletPv', numberOfColumns: 2, titleText: 'OCB 전단'},
                {startColumnName: 'srpPushPopPv', numberOfColumns: 2, titleText: 'Syrup Push 팝업'},
                {startColumnName: 'srpPushNotiPv', numberOfColumns: 2, titleText: 'Syrup Push Noti'},
                {startColumnName: 'srpIconBtnPv', numberOfColumns: 2, titleText: 'Syrup 여기(버튼)'},
                {startColumnName: 'ocbMlfClkPv', numberOfColumns: 2, titleText: 'OCB 전단'},
                //{startColumnName: 'ocbChkinClkPv', numberOfColumns: 2, titleText: 'OCB 체크인'},
                {startColumnName: 'srpOkClkPv', numberOfColumns: 2, titleText: 'Syrup 확인'},
                {startColumnName: 'srpCnclClkPv', numberOfColumns: 2, titleText: 'Syrup 취소'},
                {startColumnName: 'srpIconBtnClkPv', numberOfColumns: 2, titleText: 'Noti 조회'},
                {startColumnName: 'srpIconPv', numberOfColumns: 2, titleText: 'Syrup 여기(버튼)'},
                {startColumnName: 'ocbMlfReadPv', numberOfColumns: 2, titleText: 'OCB 전단'},
                {startColumnName: 'ocbChkinReadPv', numberOfColumns: 2, titleText: 'OCB 체크인'},
                {startColumnName: 'srpOkReadPv', numberOfColumns: 2, titleText: 'Syrup 체크인/URL 확인'});
        } else {
            if (_.contains($scope.searchStatContent, '3')) {
                $scope.colNames.push('PV', 'UV', 'PV', 'UV', 'PV', 'UV', 'PV', 'UV', 'PV', 'UV');
                $scope.colModels.push({name: 'ocbCoinPv', index: 'ocbCoinPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'ocbCoinUv', index: 'ocbCoinUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'ocbLeafletPv', index: 'ocbLeafletPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'ocbLeafletUv', index: 'ocbLeafletUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpPushPopPv', index: 'srpPushPopPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpPushPopUv', index: 'srpPushPopUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpPushNotiPv', index: 'srpPushNotiPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpPushNotiUv', index: 'srpPushNotiUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpIconBtnPv', index: 'srpIconBtnPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpIconBtnUv', index: 'srpIconBtnUv', editable: false, formatter: 'integer', align: "center", sortable: false});
                $scope.group1Headers.push({startColumnName: 'ocbCoinPv', numberOfColumns: 10, titleText: 'BLE 수신항목'});
                $scope.group2Headers.push({startColumnName: 'ocbCoinPv', numberOfColumns: 2, titleText: 'OCB 체크인'},
                    {startColumnName: 'ocbLeafletPv', numberOfColumns: 2, titleText: 'OCB 전단'},
                    {startColumnName: 'srpPushPopPv', numberOfColumns: 2, titleText: 'Syrup Push 팝업'},
                    {startColumnName: 'srpPushNotiPv', numberOfColumns: 2, titleText: 'Syrup Push Noti'},
                    {startColumnName: 'srpIconBtnPv', numberOfColumns: 2, titleText: 'Syrup 여기(버튼)'});
            }
            if (_.contains($scope.searchStatContent, '4')) {
                $scope.colNames.push('PV', 'UV', 'PV', 'UV', 'PV', 'UV', 'PV', 'UV', 'PV', 'UV');
                $scope.colModels.push({name: 'ocbMlfClkPv', index: 'ocbMlfClkPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'ocbMlfClkUv', index: 'ocbMlfClkUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    //{name: 'ocbChkinClkPv', index: 'ocbChkinClkPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    //{name: 'ocbChkinClkUv', index: 'ocbChkinClkUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpOkClkPv', index: 'srpOkClkPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpOkClkUv', index: 'srpOkClkUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpCnclClkPv', index: 'srpCnclClkPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpCnclClkUv', index: 'srpCnclClkUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpIconBtnClkPv', index: 'srpIconBtnClkPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpIconBtnClkUv', index: 'srpIconBtnClkUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpIconPv', index: 'srpIconPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpIconUv', index: 'srpIconUv', editable: false, formatter: 'integer', align: "center", sortable: false});
                $scope.group1Headers.push({startColumnName: 'ocbMlfClkPv', numberOfColumns: 10, titleText: 'BLE 클릭항목'});
                $scope.group2Headers.push({startColumnName: 'ocbMlfClkPv', numberOfColumns: 2, titleText: 'OCB 전단'},
                    //{startColumnName: 'ocbChkinClkPv', numberOfColumns: 2, titleText: 'OCB 체크인'},
                    {startColumnName: 'srpOkClkPv', numberOfColumns: 2, titleText: 'Syrup 확인'},
                    {startColumnName: 'srpCnclClkPv', numberOfColumns: 2, titleText: 'Syrup 취소'},
                    {startColumnName: 'srpIconBtnClkPv', numberOfColumns: 2, titleText: 'Noti 조회'},
                    {startColumnName: 'srpIconPv', numberOfColumns: 2, titleText: 'Syrup 여기(버튼)'});
            }
            if (_.contains($scope.searchStatContent, '5')) {
                $scope.colNames.push('PV', 'UV', 'PV', 'UV', 'PV', 'UV');
                $scope.colModels.push({name: 'ocbMlfReadPv', index: 'ocbMlfReadPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'ocbMlfReadUv', index: 'ocbMlfReadUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'ocbChkinReadPv', index: 'ocbChkinReadPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'ocbChkinReadUv', index: 'ocbChkinReadUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpOkReadPv', index: 'srpOkReadPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpOkReadUv', index: 'srpOkReadUv', editable: false, formatter: 'integer', align: "center", sortable: false});
                $scope.group1Headers.push({startColumnName: 'ocbMlfReadPv', numberOfColumns: 6, titleText: 'BLE 열람항목'});
                $scope.group2Headers.push({startColumnName: 'ocbMlfReadPv', numberOfColumns: 2, titleText: 'OCB 전단'},
                {startColumnName: 'ocbChkinReadPv', numberOfColumns: 2, titleText: 'OCB 체크인'},
                {startColumnName: 'srpOkReadPv', numberOfColumns: 2, titleText: 'Syrup 체크인/URL 확인'});
            }
        }
    }

    function getServiceOcb() {
        if (_.contains($scope.searchStatContent, '1')) {//전체
            $scope.colNames.push('PV', 'UV', 'PV', 'UV',
                'PV', 'UV',
                'PV', 'UV', 'PV', 'UV');
            $scope.colModels.push({name: 'ocbCoinPv', index: 'ocbCoinPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'ocbCoinUv', index: 'ocbCoinUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'ocbLeafletPv', index: 'ocbLeafletPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'ocbLeafletUv', index: 'ocbLeafletUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'ocbMlfClkPv', index: 'ocbMlfClkPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'ocbMlfClkUv', index: 'ocbMlfClkUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                //{name: 'ocbChkinClkPv', index: 'ocbChkinClkPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                //{name: 'ocbChkinClkUv', index: 'ocbChkinClkUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'ocbMlfReadPv', index: 'ocbMlfReadPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'ocbMlfReadUv', index: 'ocbMlfReadUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'ocbChkinReadPv', index: 'ocbChkinReadPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'ocbChkinReadUv', index: 'ocbChkinReadUv', editable: false, formatter: 'integer', align: "center", sortable: false});
            $scope.group1Headers.push({startColumnName: 'ocbCoinPv', numberOfColumns: 4, titleText: 'BLE 수신항목'},
                {startColumnName: 'ocbMlfClkPv', numberOfColumns: 2, titleText: 'BLE 클릭항목'},
                {startColumnName: 'ocbMlfReadPv', numberOfColumns: 4, titleText: 'BLE 열람항목'});
            $scope.group2Headers.push({startColumnName: 'ocbCoinPv', numberOfColumns: 2, titleText: 'OCB 체크인'},
                {startColumnName: 'ocbLeafletPv', numberOfColumns: 2, titleText: 'OCB 전단'},
                {startColumnName: 'ocbMlfClkPv', numberOfColumns: 2, titleText: 'OCB 전단'},
                //{startColumnName: 'ocbChkinClkPv', numberOfColumns: 2, titleText: 'OCB 체크인'},
                {startColumnName: 'ocbMlfReadPv', numberOfColumns: 2, titleText: 'OCB 전단'},
                {startColumnName: 'ocbChkinReadPv', numberOfColumns: 2, titleText: 'OCB 체크인'});
        } else {
            if (_.contains($scope.searchStatContent, '3')) {
                $scope.colNames.push('PV', 'UV', 'PV', 'UV');
                $scope.colModels.push({name: 'ocbCoinPv', index: 'ocbCoinPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'ocbCoinUv', index: 'ocbCoinUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'ocbLeafletPv', index: 'ocbLeafletPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'ocbLeafletUv', index: 'ocbLeafletUv', editable: false, formatter: 'integer', align: "center", sortable: false});
                $scope.group1Headers.push({startColumnName: 'ocbCoinPv', numberOfColumns: 4, titleText: 'BLE 수신항목'});
                $scope.group2Headers.push({startColumnName: 'ocbCoinPv', numberOfColumns: 2, titleText: 'OCB 체크인'},
                    {startColumnName: 'ocbLeafletPv', numberOfColumns: 2, titleText: 'OCB 전단'});
            }
            if (_.contains($scope.searchStatContent, '4')) {
                $scope.colNames.push('PV', 'UV');
                $scope.colModels.push({name: 'ocbMlfClkPv', index: 'ocbMlfClkPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'ocbMlfClkUv', index: 'ocbMlfClkUv', editable: false, formatter: 'integer', align: "center", sortable: false});
                    //{name: 'ocbChkinClkPv', index: 'ocbChkinClkPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    //{name: 'ocbChkinClkUv', index: 'ocbChkinClkUv', editable: false, formatter: 'integer', align: "center", sortable: false});
                $scope.group1Headers.push({startColumnName: 'ocbMlfClkPv', numberOfColumns: 2, titleText: 'BLE 클릭항목'});
                $scope.group2Headers.push({startColumnName: 'ocbMlfClkPv', numberOfColumns: 2, titleText: 'OCB 전단'});
                    //{startColumnName: 'ocbChkinClkPv', numberOfColumns: 2, titleText: 'OCB 체크인'});
            }
            if (_.contains($scope.searchStatContent, '5')) {
                $scope.colNames.push('PV', 'UV', 'PV', 'UV');
                $scope.colModels.push({name: 'ocbMlfReadPv', index: 'ocbMlfReadPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'ocbMlfReadUv', index: 'ocbMlfReadUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'ocbChkinReadPv', index: 'ocbChkinReadPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'ocbChkinReadUv', index: 'ocbChkinReadUv', editable: false, formatter: 'integer', align: "center", sortable: false});
                $scope.group1Headers.push({startColumnName: 'ocbMlfReadPv', numberOfColumns: 4, titleText: 'BLE 열람항목'});
                $scope.group2Headers.push({startColumnName: 'ocbMlfReadPv', numberOfColumns: 2, titleText: 'OCB 전단'},
                    {startColumnName: 'ocbChkinReadPv', numberOfColumns: 2, titleText: 'OCB 체크인'});
            }
        }
    }

    function getServiceSyrup() {
        if (_.contains($scope.searchStatContent, '1')) {//전체
            $scope.colNames.push('PV', 'UV', 'PV', 'UV', 'PV', 'UV',
                'PV', 'UV', 'PV', 'UV', 'PV', 'UV', 'PV', 'UV',
                'PV', 'UV');
            $scope.colModels.push(
                {name: 'srpPushPopPv', index: 'srpPushPopPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpPushPopUv', index: 'srpPushPopUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpPushNotiPv', index: 'srpPushNotiPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpPushNotiUv', index: 'srpPushNotiUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpIconBtnPv', index: 'srpIconBtnPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpIconBtnUv', index: 'srpIconBtnUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpOkClkPv', index: 'srpOkClkPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpOkClkUv', index: 'srpOkClkUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpCnclClkPv', index: 'srpCnclClkPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpCnclClkUv', index: 'srpCnclClkUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpIconBtnClkPv', index: 'srpIconBtnClkPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpIconBtnClkUv', index: 'srpIconBtnClkUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpIconPv', index: 'srpIconPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpIconUv', index: 'srpIconUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpOkReadPv', index: 'srpOkReadPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                {name: 'srpOkReadUv', index: 'srpOkReadUv', editable: false, formatter: 'integer', align: "center", sortable: false});
            $scope.group1Headers.push({startColumnName: 'srpPushPopPv', numberOfColumns: 6, titleText: 'BLE 수신항목'},
                {startColumnName: 'srpOkClkPv', numberOfColumns: 8, titleText: 'BLE 클릭항목'},
                {startColumnName: 'srpOkReadPv', numberOfColumns: 2, titleText: 'BLE 열람항목'});
            $scope.group2Headers.push({startColumnName: 'srpPushPopPv', numberOfColumns: 2, titleText: 'Syrup Push 팝업'},
                {startColumnName: 'srpPushNotiPv', numberOfColumns: 2, titleText: 'Syrup Push Noti'},
                {startColumnName: 'srpIconBtnPv', numberOfColumns: 2, titleText: 'Syrup 여기(버튼)'},
                {startColumnName: 'srpOkClkPv', numberOfColumns: 2, titleText: 'Syrup 확인'},
                {startColumnName: 'srpCnclClkPv', numberOfColumns: 2, titleText: 'Syrup 취소'},
                {startColumnName: 'srpIconBtnClkPv', numberOfColumns: 2, titleText: 'Noti 조회'},
                {startColumnName: 'srpIconPv', numberOfColumns: 2, titleText: 'Syrup 여기(버튼)'},
                {startColumnName: 'srpOkReadPv', numberOfColumns: 2, titleText: 'Syrup 체크인/URL 확인'});
        } else {
            if (_.contains($scope.searchStatContent, '3')) {
                $scope.colNames.push('PV', 'UV', 'PV', 'UV', 'PV', 'UV');
                $scope.colModels.push({name: 'srpPushPopPv', index: 'srpPushPopPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpPushPopUv', index: 'srpPushPopUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpPushNotiPv', index: 'srpPushNotiPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpPushNotiUv', index: 'srpPushNotiUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpIconBtnPv', index: 'srpIconBtnPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpIconBtnUv', index: 'srpIconBtnUv', editable: false, formatter: 'integer', align: "center", sortable: false});
                $scope.group1Headers.push({startColumnName: 'srpPushPopPv', numberOfColumns: 6, titleText: 'BLE 수신항목'});
                $scope.group2Headers.push({startColumnName: 'srpPushPopPv', numberOfColumns: 2, titleText: 'Syrup Push 팝업'},
                    {startColumnName: 'srpPushNotiPv', numberOfColumns: 2, titleText: 'Syrup Push Noti'},
                    {startColumnName: 'srpIconBtnPv', numberOfColumns: 2, titleText: 'Syrup 여기(버튼)'});
            }
            if (_.contains($scope.searchStatContent, '4')) {
                $scope.colNames.push('PV', 'UV', 'PV', 'UV', 'PV', 'UV', 'PV', 'UV');
                $scope.colModels.push({name: 'srpOkClkPv', index: 'srpOkClkPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpOkClkUv', index: 'srpOkClkUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpCnclClkPv', index: 'srpCnclClkPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpCnclClkUv', index: 'srpCnclClkUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpIconBtnClkPv', index: 'srpIconBtnClkPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpIconBtnClkUv', index: 'srpIconBtnClkUv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpIconPv', index: 'srpIconPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpIconUv', index: 'srpIconUv', editable: false, formatter: 'integer', align: "center", sortable: false});
                $scope.group1Headers.push({startColumnName: 'srpOkClkPv', numberOfColumns: 8, titleText: 'BLE 클릭항목'});
                $scope.group2Headers.push({startColumnName: 'srpOkClkPv', numberOfColumns: 2, titleText: 'Syrup 확인'},
                    {startColumnName: 'srpCnclClkPv', numberOfColumns: 2, titleText: 'Syrup 취소'},
                    {startColumnName: 'srpIconBtnClkPv', numberOfColumns: 2, titleText: 'Noti 조회'},
                    {startColumnName: 'srpIconPv', numberOfColumns: 2, titleText: 'Syrup 여기(버튼)'});
            }
            if (_.contains($scope.searchStatContent, '5')) {
                $scope.colNames.push('PV', 'UV');
                $scope.colModels.push({name: 'srpOkReadPv', index: 'srpOkReadPv', editable: false, formatter: 'integer', align: "center", sortable: false},
                    {name: 'srpOkReadUv', index: 'srpOkReadUv', editable: false, formatter: 'integer', align: "center", sortable: false});
                $scope.group1Headers.push({startColumnName: 'srpOkReadPv', numberOfColumns: 2, titleText: 'BLE 열람항목'});
                $scope.group2Headers.push({startColumnName: 'srpOkReadPv', numberOfColumns: 2, titleText: 'Syrup 체크인/URL 확인'});
            }
        }
    }

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        downloadUrl: '/syrup/ble/service/downloadExcel',
        downloadType: 'POI',
        pivotFlag: 'N',
        tableId: 'svcBleForGrid',
        titleName: 'BLE 서비스 통계',
        xlsName: 'svc_ble.xls'
    };
}

function couponStatCtrl($scope, DATE_TYPE_DAY, reportSvc) {
    $scope.init = function () {
        var defaultCal = reportSvc.defaultCustomCal('day', 1, 4, 6);
        $scope.searchConfig = {
            startDate: defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain,
            dayPeriod: 1,
            weekPeriod: 4,
            monthPeriod: 6
        };
        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;

        drawCouponStat();
    };

    function drawCouponStat() {
        $scope.tableId = 'couponStatForGrid';
        $scope.couponStatForGridConfig = {
            id: 'couponStatForGrid',
            url: '/syrup/coupon/cponstat/grid',
            mtype: "POST",
            serializeGridData: function(postData) {
                var newPostData = $.extend(postData, {
                    dateType : $scope.searchDateType,
                    startDate: $scope.searchStartDate,
                    endDate: $scope.searchEndDate
                });
                return $.param(newPostData);
            },
            ajaxGridOptions: { contentType: "application/x-www-form-urlencoded; charset=utf-8" },
            datatype: "json",
            height: "100%",
            shrinkToFit: false,
            rowNum: 1000,
            rowList: [1000,2000,3000],
            colNames: ['쿠폰ID', '유형', '브랜드명(제휴사명)', '쿠폰명', '발급건수', '누적발급건수', '사용건수', '누적사용건수'
                , '노출건수', '누적노출건수', '발급페이지조회수', '누적발급페이지조회수', '상세발급페이지조회수', '누적상세발급페이지조회수'],
            colModel: [
                {name: 'cpProdCd', index: 'cpProdCd', editable: false, align: "center", sortable: false},
                {name: 'regPocNm', index: 'regPocNm', editable: false, align: "center", sortable: false},
                {name: 'ptNm', index: 'ptNm', editable: false, align: "center"},
                {name: 'cponNm', index: 'cponNm', editable: false, align: "center", sortable: false},
                {name: 'issueCnt', index: 'issueCnt', width: 80, editable: false, align: "right", sortable: false, formatter:'currency', formatoptions:{decimalSeparator:"", thousandsSeparator: ",", decimalPlaces: 0}},
                {name: 'accumIssueCnt', index: 'accumIssueCnt', width: 80, editable: false, align: "right", sortable: false, formatter:'currency', formatoptions:{decimalSeparator:"", thousandsSeparator: ",", decimalPlaces: 0}},
                {name: 'useCnt', index: 'useCnt', width: 80, editable: false, align: "right", sortable: false, formatter:'currency', formatoptions:{decimalSeparator:"", thousandsSeparator: ",", decimalPlaces: 0}},
                {name: 'accumUseCnt', index: 'accumUseCnt', width: 80, editable: false, align: "right", sortable: false, formatter:'currency', formatoptions:{decimalSeparator:"", thousandsSeparator: ",", decimalPlaces: 0}},
                {name: 'showCnt', index: 'showCnt', width: 80, editable: false, align: "right", sortable: false, formatter:'currency', formatoptions:{decimalSeparator:"", thousandsSeparator: ",", decimalPlaces: 0}},
                {name: 'accumShowCnt', index: 'accumShowCnt', width: 80, editable: false, align: "right", sortable: false, formatter:'currency', formatoptions:{decimalSeparator:"", thousandsSeparator: ",", decimalPlaces: 0}},
                {name: 'issuePageCnt', index: 'issuePageCnt', width: 80, editable: false, align: "right", sortable: false, formatter:'currency', formatoptions:{decimalSeparator:"", thousandsSeparator: ",", decimalPlaces: 0}},
                {name: 'accumIssuePageCnt', index: 'accumIssuePageCnt', width: 80, editable: false, align: "right", sortable: false, formatter:'currency', formatoptions:{decimalSeparator:"", thousandsSeparator: ",", decimalPlaces: 0}},
                {name: 'issuePageDtlCnt', index: 'issuePageDtlCnt', width: 80, editable: false, align: "right", sortable: false, formatter:'currency', formatoptions:{decimalSeparator:"", thousandsSeparator: ",", decimalPlaces: 0}},
                {name: 'accumIssuePageDtlCnt', index: 'accumIssuePageDtlCnt', width: 80, editable: false, align: "right", sortable: false, formatter:'currency', formatoptions:{decimalSeparator:"", thousandsSeparator: ",", decimalPlaces: 0}}
            ],
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
        drawCouponStat();
    };

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        downloadUrl : '/syrup/coupon/downloadExcelForCouponStat',
        downloadType : 'POI',
        pivotFlag : 'N',
        tableId: 'couponStatForGrid',
        xlsName: 'coupon-stat.xls'
    };
}

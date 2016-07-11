function bpmResultSumCtrl($scope, bpmResultSvc) {
    $scope.pageState = {
        'loading': true,
        'emptyData': true
    };

    /**
     * 초기화 작업. bpm-result-search-box directive가 완성된 뒤에 호출된다.
     * @param params
     */
    $scope.init = function (params) {
        $scope.excelFlag = false;
        callBpmResultSums(params);
    };

    /**
     * 엑셀 다운로드
     */
    function drtExcelDownload (params) {
        var $reportExcelForm = $('#reportExcelForm');
        $reportExcelForm.attr('action', $scope.drtexcelConfig.downloadUrl + "/" + $scope.searchDateType);
        $reportExcelForm.find('input[name="svcId"]').val(params.svcId);
        $reportExcelForm.find('input[name="dateType"]').val($scope.searchDateType);
        $reportExcelForm.find('input[name="startDate"]').val(params.startDate);
        $reportExcelForm.find('input[name="endDate"]').val(params.endDate);
        $reportExcelForm.find('input[name="idxClGrpCd"]').val(params.idxClGrpCd);
        $reportExcelForm.find('input[name="idxClCd"]').val(params.idxClCd);
        $reportExcelForm.find('input[name="startWeekNumber"]').val(params.startWeekNumber);
        $reportExcelForm.find('input[name="endWeekNumber"]').val(params.endWeekNumber);
        $reportExcelForm.find('input[name="xlsName"]').val($scope.drtexcelConfig.xlsName);
        $reportExcelForm.find('input[name="titleName"]').val($scope.drtexcelConfig.titleName);
        $reportExcelForm.find('input[name="pivotFlag"]').val($scope.drtexcelConfig.pivotFlag);
        $reportExcelForm.submit();
    }

    /**
     * 실적조회를 호출한다.
     * @param params
     */
    function callBpmResultSums(params) {
        $scope.pageState.loading = true;
        $scope.pageState.emptyData = true;
        $scope.searchDateType = params.dateType;
        $scope.excelFlag = params.excelFlag;
        if ($scope.excelFlag) {
            $scope.pageState.loading = false;
            $scope.pageState.emptyData = false;
            drtExcelDownload(params);
        } else {
            $scope.headerView = false;
            bpmResultSvc.getBpmResultSums(params).then(function (result) {
                loadData(params.dateType, result.rows);
            });
        }
    }

    /**
     * 데이터를 load 한다.
     * @param dateType
     * @param pivotData
     */
    function loadData(dateType, pivotData) {
        /** resize pivotTable and chart width **/
        $scope.pivotTableId = 'bpmResultSumGrid';

        $scope.headerView = false;
        if(pivotData.length == 0) {
            $scope.pageState.loading = false;
            $scope.pageState.emptyData = true;
            return;
        }

        $('#bpmResultSumGrid').pivotUI(
            pivotData,
            {
                rows: ['idxClCdGrpNm', 'idxClCdVal', 'idxCttCdVal'],
                cols: bpmResultSvc.getPivotCols(dateType),
                vals: bpmResultSvc.getPivotVals(dateType),
                aggregatorName: 'intSum',
                rendererName: 'table',
                onRefresh: function () {
                    reformTable();
                    customResizePivotTable();
               }
            },
            true
        );
    }

    $scope.$on('$destroy', function() {
        $('#bpmResultSumGrid').children().unbind();
        $('#bpmResultSumGrid').children().off();
        $('#bpmResultSumGrid').empty();
    });

    /**
     * table을 reform 한다.
     */
    function reformTable() {
        var config = {
            id: "bpmResultSumGrid",
            modelData: [
                {name: 'Power Index', width: 150},
                {name: 'Power Index', width: 150},
                {name: 'Power Index', width: 150}
            ],
            minCellWidth: 80
        };
        $scope.headerView = true;
        DomHelper.reDrawPivotTableHeader(config);
        $scope.tableHeaderList = DomHelper.getDateList(config.id);

        $scope.$safeApply(function () {
            $scope.pageState.loading = false;
            $scope.pageState.emptyData = false;
        });

        DomHelper.reDrawPivotTableMinCellWidth(config);
        //header 변경
        var headerWidth = function () {
            var tmp = 0;
            var dataLength = config.modelData.length;
            dataLength.times(function (i) {
                tmp += config.modelData[i].width;
            });
            return tmp;
        };
        $('#bpmResultSumGrid_h .fixedTableHeader tr:nth-child(1) th:nth-child(3)').remove();
        $('#bpmResultSumGrid_h .fixedTableHeader tr:nth-child(1) th:nth-child(2)').remove();
        $('#bpmResultSumGrid_h .fixedTableHeader tr:nth-child(1) th:nth-child(1)').attr('colspan', '3').css('width',headerWidth()).text('Power Index');

        //data부분 병합
        var $gridTable = $('#bpmResultSumGrid > table');
        // cell 가공
        $gridTable.find('th.pvtRowLabel').each(function (i, obj) {
            // 순서조절을 위한 prefix 제거
            var firstChar = $(obj).text().charAt(0);
            if (firstChar == 1 || firstChar == 2) {
                $(obj).text($(obj).text().substring(1));
            }

            // 총계 cell 이면 3depth를 제거하고, 2depth의 속성에 colspan을 준다.
            if($(obj).text() == '총계') {
                if($(obj).hasClass('num-scope'))  {
                    $(obj).hide();
                } else {
                    $(obj).attr('colspan', 2); // 2depth
                }
            }
        });
    }

    // 피봇 그리드 라사이징.
    function customResizePivotTable() {
        var pivotTableWidth = $('#' + $scope.pivotTableId + ' .pvtTable').width();
        var curWindowWidth = $(window).width() - 322;
        if (pivotTableWidth < curWindowWidth) {
            pivotTableWidth = curWindowWidth;
        }
        $('#' + $scope.pivotTableId + '_h .fixedTableHeader').css('width', pivotTableWidth);   //fix header table width
        $('#' + $scope.pivotTableId + ' .pvtTable').css('width', pivotTableWidth);             //pivot header table width
    }

    /**
     * 검색 조회 callback
     * @param result
     */
    $scope.searchCallback = function(params){
        callBpmResultSums(params);
    };

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.excelConfig = {
        downloadUrl : '/download/bpmResultSum/excel',
        tableId : 'bpmResultSumGrid',
        titleName : '경영실적Data조회',
        xlsName : 'boss-detail.xls'
    };

    /**
     * 엑셀 다운로드 설정
     * @type {{tableId: string, xlsName: string}}
     */
    $scope.drtexcelConfig = {
        downloadUrl : '/bpmResultSum/downloadExcelForbpmResultSum',
        downloadType : 'POI',
        pivotFlag : 'N',
        tableId : 'bpmResultSumGrid',
        titleName : '경영실적Data조회',
        xlsName : 'boss-detail.xls'
    };
}

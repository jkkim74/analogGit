'use strict';

angular.module('App')
    .controller('Pan0105Ctrl', ['$scope', '$q', '$http', '$timeout', '$filter', 'uiGridConstants', 'apiService', 'uploadService', function ($scope, $q, $http, $timeout, $filter, uiGridConstants, apiService, uploadService) {

        var self = this;
        $scope.title = '거래 실적 및 유실적 고객 추출';

        $scope.selectOptions = [
            { label: '회원ID', value: 'mbr_id' },
            { label: '카드번호', value: 'card_no' },
            { label: '가맹점코드', value: 'stlmt_mcnt_cd' }
        ];

        $scope.selectOptions2 = [
            { label: 'TR', value: 'tr' },
            { label: '회원ID', value: 'mbrId' }
        ];

        $scope.selectOptions3 = [
            { label: '접수일자', value: 'rcv_dt' },
            { label: '매출일자', value: 'sale_dt' }
        ];

        $scope.selectOptions4 = [
            { label: '포함', value: true },
            { label: '미포함', value: false }
        ];

        $scope.datepickerOptions = {
            showWeeks: false
        };
        $scope.altInputFormats = ['M!/d!/yyyy'];

        $scope.gridOptionsMembers = {
            enablePaginationControls: false,
            paginationPageSize: 100,
            useExternalPagination: true,
            columnDefs: [
                { field: 'rcvDt', displayName: '접수일시', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'rcvSeq', displayName: '접수번호', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'apprDttm', displayName: '승인일시', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'repApprNo', displayName: '대표 승인번호', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'apprNo', displayName: '승인번호', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'saleDttm', displayName: '매출일시', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'mbrId', displayName: '회원ID', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'cardDtlGrp', displayName: '카드코드', width: 300, cellTooltip: true, headerTooltip: true },
                { field: 'cardNo', displayName: '카드번호', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'alcmpn', displayName: '발생제휴사', width: 300, cellTooltip: true, headerTooltip: true },
                { field: 'mcnt', displayName: '발생가맹점', width: 300, cellTooltip: true, headerTooltip: true },
                { field: 'stlmtMcnt', displayName: '정산가맹점', width: 300, cellTooltip: true, headerTooltip: true },
                { field: 'pntKnd', displayName: '포인트종류', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'slip', displayName: '전표', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'saleAmt', displayName: '매출금액', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
                { field: 'pnt', displayName: '적립포인트', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
                { field: 'csMbrCmmsn', displayName: '제휴사연회비', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
                { field: 'cmmsn', displayName: '수수료', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
                { field: 'pmntWayCd', displayName: '지불수단', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'org', displayName: '기관', width: 300, cellTooltip: true, headerTooltip: true },
                { field: 'oilPrdctSgrp', displayName: '유종', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'saleQty', displayName: '주유량', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
                { field: 'cpnPrd', displayName: '쿠폰', width: 100, cellTooltip: true, headerTooltip: true }
            ],
            onRegisterApi: function (gridApi) {
                $scope.gridApi = gridApi;
                // $scope.gridApi.core.on.sortChanged($scope, function (grid, sortColumns) {
                //   $scope.loadMembers();
                // });
                gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                    $scope.loadMembers();
                });
            }
        };

        $scope.upload = function (file) {
            $scope.uploadRecords = 0;
            $scope.uploadProgressLoadingMessage = 'Uploading...';

            $scope.uploadPromise = uploadService.upload({ file: file, columnName: $scope.selectedOption2.value });
            $scope.uploadPromise.then(function () {
                $scope.uploadProgressLoadingMessage = 'Extracting...';

                return apiService.extractMemberInfo({
                    inputDataType: $scope.selectedOption.value,
                    periodType: $scope.selectedOption3.value,
                    periodFrom: $filter('date')($scope.periodFrom, 'yyyyMMdd'),
                    periodTo: $filter('date')($scope.periodTo, 'yyyyMMdd')
                });
            }).then(function () {
                $scope.extracted = true;
            });
        };

        $scope.loadMembers = function () {
            var offset = ($scope.gridApi.pagination.getPage() - 1) * $scope.gridOptionsMembers.paginationPageSize;
            var limit = $scope.gridOptionsMembers.paginationPageSize;

            $scope.membersPromise = apiService.getMembers({ offset: offset, limit: limit });
            $scope.membersPromise.then(function (data) {
                $scope.gridOptionsMembers.data = data.value;
                $scope.gridOptionsMembers.totalItems = data.totalRecords;
            });
        };

        $scope.sendPts = function (ptsUsername, ptsMasking) {
            $scope.sendPtsPromise = apiService.sendPts({ ptsUsername: ptsUsername, ptsMasking: !!ptsMasking });
            $scope.sendPtsPromise.finally(function () {

            });
        };

    }]);
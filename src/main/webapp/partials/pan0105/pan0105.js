'use strict';

angular.module('app').controller('PAN0105Ctrl', ['$scope', '$q', '$http', '$timeout', '$filter', 'uiGridConstants', 'apiSvc', 'authSvc', 'toastr', '$uibModal',
    function ($scope, $q, $http, $timeout, $filter, uiGridConstants, apiSvc, authSvc, toastr, $uibModal) {

        $scope.selectOptions = [
            { label: '회원ID', value: 'mbr_id' },
            { label: '카드번호', value: 'card_no' },
            { label: '가맹점코드', value: 'stlmt_mcnt_cd' }
        ];

        $scope.selectOptions2 = [
            { label: 'TR', value: 'tr' },
            { label: 'TR (고객성명 포함)', value: 'tr_mbrKorNm' },
            { label: '회원ID', value: 'mbrId' }
        ];

        $scope.selectOptions3 = [
            { label: '접수일자', value: 'rcv_dt' },
            { label: '매출일자', value: 'sale_dt' }
        ];
        
        $scope.selectOptions4 = [
            { label: '전체', value: 't' },
            { label: '일반', value: 'g' },
            { label: '주유', value: 'f' }
        ];        

        $scope.clear = function () {
            $scope.selectedOption = $scope.selectOptions[0];
            $scope.selectedOption2 = $scope.selectOptions2[0];
            $scope.selectedOption3 = $scope.selectOptions3[0];
            $scope.selectedOption4 = $scope.selectOptions4[0];
            $scope.uploaded = false;
            $scope.disableUpload = false;
            $scope.mbrId = null;
            $scope.periodFrom = null;
            $scope.periodTo = null;
            $scope.uploadedItems = 0;
        };

        // $scope.gridOptionsMembers = {
        //     enableRowSelection: true,
        //     enableRowHeaderSelection: false,
        //     multiSelect: false,
        //     useExternalPagination: true,
        //     columnDefs: [
        //         {
        //             field: 'no', displayName: 'No.', width: 100,
        //             cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1 + (grid.api.pagination.getPage() - 1) * grid.options.paginationPageSize}}</div>'
        //         },
        //         { field: 'rcvDt', displayName: '접수일시', width: 100, cellTooltip: true, headerTooltip: true },
        //         { field: 'rcvSeq', displayName: '접수번호', width: 100, cellTooltip: true, headerTooltip: true },
        //         { field: 'apprDttm', displayName: '승인일시', width: 200, cellTooltip: true, headerTooltip: true },
        //         { field: 'repApprNo', displayName: '대표 승인번호', width: 150, cellTooltip: true, headerTooltip: true },
        //         { field: 'apprNo', displayName: '승인번호', width: 100, cellTooltip: true, headerTooltip: true },
        //         { field: 'saleDttm', displayName: '매출일시', width: 200, cellTooltip: true, headerTooltip: true },
        //         { field: 'mbrId', displayName: '회원ID', width: 100, cellTooltip: true, headerTooltip: true },
        //         { field: 'cardDtlGrp', displayName: '카드코드', width: 300, cellTooltip: true, headerTooltip: true },
        //         { field: 'cardNo', displayName: '카드번호', width: 150, cellTooltip: true, headerTooltip: true },
        //         { field: 'alcmpn', displayName: '발생제휴사', width: 300, cellTooltip: true, headerTooltip: true },
        //         { field: 'mcnt', displayName: '발생가맹점', width: 300, cellTooltip: true, headerTooltip: true },
        //         { field: 'stlmtMcnt', displayName: '정산가맹점', width: 300, cellTooltip: true, headerTooltip: true },
        //         { field: 'pntKnd', displayName: '포인트종류', width: 200, cellTooltip: true, headerTooltip: true },
        //         { field: 'slip', displayName: '전표', width: 200, cellTooltip: true, headerTooltip: true },
        //         { field: 'saleAmt', displayName: '매출금액', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
        //         { field: 'pnt', displayName: '적립포인트', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
        //         { field: 'csMbrCmmsn', displayName: '제휴사연회비', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
        //         { field: 'cmmsn', displayName: '수수료', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
        //         { field: 'pmntWayCd', displayName: '지불수단', width: 100, cellTooltip: true, headerTooltip: true },
        //         { field: 'org', displayName: '기관', width: 300, cellTooltip: true, headerTooltip: true },
        //         { field: 'oilPrdctSgrp', displayName: '유종', width: 100, cellTooltip: true, headerTooltip: true },
        //         { field: 'saleQty', displayName: '주유량', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
        //         { field: 'cpnPrd', displayName: '쿠폰', width: 100, cellTooltip: true, headerTooltip: true },
        //         { field: 'mbrKorNm', displayName: '회원한글명', width: 100, cellTooltip: true, headerTooltip: true }
        //     ],
        //     onRegisterApi: function (gridApi) {
        //         $scope.gridApi = gridApi;
        //         gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
        //             var offset = (newPage - 1) * pageSize;
        //             $scope.loadMembers(offset, pageSize);
        //         });
        //     }
        // };

        $scope.upload = function (file) {
            if ($scope.form.$invalid) {
                toastr.warning('파일 업로드 이후 모든 조회 조건은 고정됩니다. 조건을 모두 선택하시고 파일을 업로드해주세요.');
                return;
            }

            $scope.uploadPromise = apiSvc.upload({ file: file, param: $scope.selectedOption2.value });
            $scope.uploadPromise.then(function () {
                $scope.checkUploadProgress();
                $scope.uploaded = true;
                $scope.disableUpload = true;
                $scope.mbrId = null;
            });
        };

        $scope.checkUploadProgress = function () {
            // 업로드 끝 flag 까지 재귀반복
            $timeout(function () {
                apiSvc.getUploaded({ countOnly: true }).then(function (data) {
                    $scope.uploadedItems = data.totalItems;

                    if (data.message === 'FINISHED') {
                        $scope.uploadStatusIsRunning = false;
                    } else {
                        $scope.uploadStatusIsRunning = true;
                        $scope.checkUploadProgress();
                    }
                });
            }, 1000);
        };

        $scope.sendOneMbrId = function () {
            var data = new Blob([$scope.mbrId], { type: 'text/csv' });

            $scope.upload(data);
        };

        // $scope.loadMembers = function (offset, limit) {
        //     var params = {
        //         offset: offset || 0,
        //         limit: limit || $scope.gridOptionsMembers.paginationPageSize
        //     }

        //     $scope.membersPromise = apiSvc.getMembers(params);
        //     $scope.membersPromise.then(function (data) {
        //         $scope.gridOptionsMembers.data = data.value;
        //         $scope.gridOptionsMembers.totalItems = data.totalItems;
        //     });
        // };

        $scope.sendPts = function (ptsMasking, ptsPrefix) {
            var modalInstance = $uibModal.open({
                component: 'confirmPtsModal',
                resolve: {
                    params: function () {
                        return {
                            inputDataType: $scope.selectedOption.label,
                            extractionTarget: $scope.selectedOption2.label,
                            extractionCond: $scope.selectedOption4.label,
                            periodType: $scope.selectedOption3.label,
                            periodFrom: $filter('date')($scope.periodFrom, 'yyyy.MM.dd'),
                            periodTo: $filter('date')($scope.periodTo, 'yyyy.MM.dd')
                        };
                    }
                }
            });

            modalInstance.result.then(function () {
                $scope.sendPtsPromise = apiSvc.extractMemberInfo({
                    inputDataType: $scope.selectedOption.value,
                    extractionCond: $scope.selectedOption4.value,
                    periodType: $scope.selectedOption3.value,
                    periodFrom: $filter('date')($scope.periodFrom, 'yyyyMMdd'),
                    periodTo: $filter('date')($scope.periodTo, 'yyyyMMdd'),
                    ptsMasking: ptsMasking,
                    ptsPrefix: ptsPrefix
                });

                $scope.sendPtsPromise.then(function () {
                    $scope.clear();
                    // $scope.sendPtsPromise = apiSvc.sendPts({ ptsMasking: ptsMasking });
                });
            }, function () {
                $scope.clear();
            });
        };

        $scope.isPtsDisabled = function () {
            return !$scope.uploaded;
        };

        authSvc.getUserInfo().then(function (userInfo) {
            $scope.ptsUsername = userInfo.ptsUsername;
            $scope.maskingAuth = userInfo.maskingYn;
        });

        $scope.changeColumnVisible = function () {
             if ($scope.selectedOption2.value === 'mbrId') {
            	 $scope.extr_bt = {'display': 'none'};
            	 $scope.extr_br1 = {'display': 'none'};
            	 $scope.extr_br2 = {'display': 'none'};
            	 $scope.desc_bt = {'display': 'none'};
             } else {
            	 $scope.extr_bt = {'display': 'true'};
            	 $scope.extr_br1 = {'display': 'true'};
            	 $scope.extr_br2 = {'display': 'true'};
            	 $scope.desc_bt = {'display': 'true'};
             }
        };

        $scope.openExplain = function () {
            $uibModal.open({
                templateUrl: 'explainModal.html'
            });
        };

    }
]).component('confirmPtsModal', {
    templateUrl: 'confirmPtsModal.html',
    bindings: {
        resolve: '<',
        close: '&',
        dismiss: '&'
    },
    controller: function () {
        var self = this;

        self.$onInit = function () {
            self.params = self.resolve.params;
        };

    }
});
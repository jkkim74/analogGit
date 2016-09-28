'use strict';

angular.module('App')
    .controller('Pan0104Ctrl', ['$scope', '$q', '$http', '$timeout', 'uiGridConstants', 'apiService', function ($scope, $q, $http, $timeout, uiGridConstants, apiService) {

        var self = this;

        $scope.title = '시효 만료 포인트 사전 고지';

        self.setYearMonthOption = function (options) {
            var now = new Date();
            var year = now.getFullYear();
            var month = now.getMonth() + 1;
            // move to next month
            if (month == 12) {
                year++;
                month = 1;
            } else {
                month++;
            }

            for (var i = 0; i < 7; i++ , month--) {
                if (month < 1) {
                    year--;
                    month = 12;
                }
                options.push({ label: year + '년 ' + month + '월', value: year + ('0' + month).slice(-2) });
            }
        };

        $scope.selectOptions = [{ label: '- 선택 -', value: '' }];
        self.setYearMonthOption($scope.selectOptions);

        $scope.selectOptions2 = [{ label: '전체', value: '' }];
        self.setYearMonthOption($scope.selectOptions2);

        $scope.selectOptions3 = [
            { label: 'OCB', value: 'ocbcom' },
            { label: 'EM', value: 'em' },
            { label: 'SMS', value: 'sms' },
            { label: 'TM', value: 'tm' }
        ];

        $scope.gridOptionsTarget = {
            enableColumnMenus: false,
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            minRowsToShow: 7,
            columnDefs: [
                { field: 'mbrFgNm', displayName: '회원구분', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'extnctMbrFgNm', displayName: '항목', width: 150, cellTooltip: true, headerTooltip: true },
                { field: 'ocbcom', displayName: 'OCB 고객수', cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
                { field: 'em', displayName: 'EM 고객수', cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
                { field: 'sms', displayName: 'SMS 고객수', cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
                { field: 'tm', displayName: 'TM 고객수', cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
                { field: 'totCnt', displayName: '전체 고객수', cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
                { field: 'totPnt', displayName: '전체 포인트', cellTooltip: true, headerTooltip: true, cellFilter: 'number' }
            ]
        };

        $scope.gridOptionsResult = {
            enableColumnMenus: false,
            enableSorting: false,
            useExternalPagination: true,
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            columnDefs: [
                { field: 'baseYm', displayName: '기준년월', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'unitedId', displayName: 'OCB닷컴 United ID', cellTooltip: true, headerTooltip: true },
                { field: 'mbrId', displayName: '회원ID', cellTooltip: true, headerTooltip: true },
                { field: 'mbrKorNm', displayName: '고객성명', cellTooltip: true, headerTooltip: true },
                { field: 'clphnNo', displayName: '휴대폰번호', cellTooltip: true, headerTooltip: true },
                { field: 'emailAddr', displayName: '이메일주소', cellTooltip: true, headerTooltip: true }
            ],
            onRegisterApi: function (gridApi) {
                $scope.gridApi = gridApi;
                gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                    var offset = (newPage - 1) * pageSize;
                    $scope.searchResults(offset, pageSize);
                });
            }
        };

        $scope.searchTargets = function () {
            if ($scope.selectedOption.value === '') {
                return;
            }

            var params = {
                baseYm: $scope.selectedOption.value
            };

            $scope.targetPromise = apiService.getExpirePointTargets(params);
            $scope.targetPromise.then(function (data) {
                $scope.gridOptionsTarget.data = data;
            });
        };

        $scope.searchResults = function (offset, limit) {
            var params = {
                baseYm: $scope.selectedOption2.value,
                notiTarget: $scope.selectedOption3.value,
                unitedId: $scope.unitedId,
                mbrKorNm: $scope.mbrKorNm,
                clphnNo: $scope.clphnNo,
                emailAddr: $scope.emailAddr,
                offset: offset || 0,
                limit: limit || $scope.gridOptionsResult.paginationPageSize
            };

            $scope.resultPromise = apiService.getNoticeResults(params);
            $scope.resultPromise.then(function (data) {
                $scope.gridOptionsResult.data = data.value;
                $scope.gridOptionsResult.totalItems = data.totalRecords;
            });
        };

        $scope.noticeExpirePoint = function (notiTarget) {
            var param = {
                baseYm: $scope.selectedOption.value,
                notiTarget: notiTarget
            };

            $scope.targetPromise = apiService.noticeExpirePoint(params);
            $scope.targetPromise.then(function () {

            });
        };

    }]);
'use strict';

angular.module('App').controller('Ctas0101Ctrl', ['$scope', '$log', '$q', '$http', '$timeout', '$filter', 'uiGridConstants', 'toastr', 'apiSvc', '$uibModal',
    function ($scope, $log, $q, $http, $timeout, $filter, uiGridConstants, toastr, apiSvc, $uibModal) {

        var self = this;
        $scope.title = '이메일 발송 관리';

        $scope.visibleDetail = true;
        $scope.campaign = {};

        $scope.selectOptions = [
            { label: '커머스센터 대용량 메일', value: 'MAIL' },
            { label: '앱푸시/모바일전용 회원ID 전송 (PTS)', value: 'PUSH' },
            { label: 'SMS 발송용 목록 전송 (PTS)', value: 'SMS' }
        ];

        $scope.dateOptions = {
            minDate: new Date()
        };

        $scope.gridOptionsList = {
            enableRowSelection: true,
            enableRowHeaderSelection: false,
            multiSelect: false,
            useExternalPagination: true,
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            minRowsToShow: 16,
            columnDefs: [
                { field: 'cmpgnId', displayName: '캠페인코드', cellTooltip: true, headerTooltip: true },
                { field: 'cmpgnNm', displayName: '캠페인명', cellTooltip: true, headerTooltip: true },
                { field: 'cmpgnSndChnlFgCd', displayName: '발송채널', cellTooltip: true, headerTooltip: true },
                { field: 'mergeDt', displayName: '머지일자', cellTooltip: true, headerTooltip: true },
                { field: 'sndDt', displayName: '발송일자', cellTooltip: true, headerTooltip: true },
                { field: 'updId', displayName: '요청자', cellTooltip: true, headerTooltip: true },
                { field: 'stsFgNm', displayName: '상태', cellTooltip: true, headerTooltip: true }
            ],
            onRegisterApi: function (gridApi) {
                $scope.gridApi = gridApi;
                gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                    var offset = (newPage - 1) * pageSize;
                    $scope.search(offset, pageSize);
                });
            }
        };

        $scope.search = function (offset, limit) {
            var params = {
                offset: offset || 0,
                limit: limit || $scope.gridOptionsList.paginationPageSize,
                periodFrom: $filter('date')($scope.periodFrom, 'yyyyMMdd'),
                periodTo: $filter('date')($scope.periodTo, 'yyyyMMdd')
            };

            $scope.searchPromise = apiSvc.getCampaigns(params);
            $scope.searchPromise.then(function (data) {
                $scope.gridOptionsList.data = data.value;
                $scope.gridOptionsList.totalItems = data.totalItems;
            });
        };

        $scope.clear = function () {
            $scope.campaign = {
                cmpgnSndChnlFgCd: $scope.selectOptions[0].value
            };
        };

        $scope.saveCampaign = function () {
            var campaign = angular.extend({}, $scope.campaign);
            angular.extend(campaign, {
                mergeDt: $filter('date')(campaign.mergeDt, 'yyyyMMdd')
            });

            var deferred = $q.defer();

            $scope.savePromise = apiSvc.saveCampaign(campaign);
            $scope.savePromise.then(function (data) {
                $scope.campaign.cmpgnId = data.value.cmpgnId;
                $scope.search();
                deferred.resolve();
            }, function () {
                deferred.reject();
            });

            return deferred.promise;
        };

        $scope.deleteCampaign = function () {
            var selectedRow = $scope.gridApi.selection.getSelectedRows()[0];

            apiSvc.deleteCampaign(selectedRow).then(function () {
                var index = $scope.gridOptionsList.data.indexOf(selectedRow);
                $scope.gridOptionsList.data.splice(index, 1);
            });
        };

        $scope.gridOptionsCellList = {
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            minRowsToShow: 5,
            columnDefs: [
                { field: 'cellId', displayName: '셀ID', width: 50, cellTooltip: true, headerTooltip: true },
                { field: 'cellDesc', displayName: '설명', cellTooltip: true, headerTooltip: true },
                { field: 'cellPrcntg', displayName: '백분율(%)', width: 50, cellTooltip: true, headerTooltip: true },
                { field: 'extrctCnt', displayName: '추출수', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
                { field: 'fnlExtrctCnt', displayName: '최종추출수', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
                { field: 'mergeDt', displayName: '머지일자', cellTooltip: true, headerTooltip: true },
                { field: 'sndDt', displayName: '발송일자', cellTooltip: true, headerTooltip: true },
                { field: 'stsFgNm', displayName: '셀상태', width: 100, cellTooltip: true, headerTooltip: true }
            ]
        };

        $scope.openTargeting = function () {
            $scope.saveCampaign().then(function () {
                var modalInstance = $uibModal.open({
                    component: 'ctas0103Modal',
                    size: 'lg',
                    resolve: {
                        campaign: function () { return $scope.campaign; },
                        targetingInfo: function () { return $scope.targetingInfo; }
                    }
                });

                modalInstance.result.then(function (selectedItem) {
                    $log.debug(selectedItem);
                    $scope.targetingInfo = selectedItem;

                    $scope.loadCells();
                });
            });
        };

        $scope.loadCells = function () {
            $scope.detailPromise = apiSvc.getCampaignDetail($scope.campaign);
            $scope.detailPromise.then(function (data) {
                $scope.gridOptionsCellList.data = data.value;
            });
        };

        $scope.requestTransmission = function () {
            var componentName;
            if ($scope.selectedOption.value === 'em') {
                componentName = 'ctas0104Modal';
            } else {
                componentName = 'ctas0105Modal';
            }

            $uibModal.open({
                component: componentName,
                resolve: {
                    notiType: function () { return $scope.selectedOption.value; },
                    targetingInfo: function () { return $scope.targetingInfo; }
                }
            });
        };

    }]);
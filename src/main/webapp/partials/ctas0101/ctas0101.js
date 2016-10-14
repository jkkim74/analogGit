'use strict';

angular.module('App').controller('Ctas0101Ctrl', ['$scope', '$log', '$q', '$http', '$timeout', '$filter', 'uiGridConstants', 'toastr', 'apiSvc', '$uibModal', 'uibDateParser',
    function ($scope, $log, $q, $http, $timeout, $filter, uiGridConstants, toastr, apiSvc, $uibModal, uibDateParser) {

        var self = this;
        $scope.title = '이메일 발송 관리';

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
            minRowsToShow: 8,
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
                    $scope.searchCampaign(offset, pageSize);
                });
            }
        };

        $scope.searchCampaign = function (offset, limit) {
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

        $scope.saveCampaign = function () {
            var campaign = angular.extend({}, $scope.campaign);
            angular.extend(campaign, {
                mergeDt: $filter('date')(campaign.mergeDt, 'yyyyMMdd')
            });

            var deferred = $q.defer();

            $scope.savePromise = apiSvc.saveCampaign(campaign);
            $scope.savePromise.then(function (data) {
                $scope.campaign.cmpgnId = data.value.cmpgnId;
                $scope.searchCampaign();
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

        $scope.detailCampaign = function () {
            var selectedRow = $scope.gridApi.selection.getSelectedRows()[0];

            $scope.campaign = angular.extend({}, selectedRow);
            $scope.campaign.mergeDt = uibDateParser.parse(selectedRow.mergeDt, 'yyyy-MM-dd');

            $scope.loadCellList();
            $scope.loadTargeting();
        };

        $scope.clearDetail = function () {
            $scope.campaign = {
                cmpgnSndChnlFgCd: $scope.selectOptions[0].value
            };

            $scope.gridOptionsCellList.data = [];
        };

        $scope.gridOptionsCellList = {
            enableCellEdit: false,
            enableRowSelection: true,
            enableRowHeaderSelection: false,
            multiSelect: false,
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            minRowsToShow: 8,
            columnDefs: [
                { field: 'cellId', displayName: '셀ID', cellTooltip: true, headerTooltip: true },
                { field: 'cellDesc', displayName: '설명', cellTooltip: true, headerTooltip: true, enableCellEdit: true },
                { field: 'cellPrcntg', displayName: '백분율(%)', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'extrctCnt', displayName: '추출수', cellTooltip: true, headerTooltip: true, enableCellEdit: true, cellFilter: 'number' },
                { field: 'fnlExtrctCnt', displayName: '최종추출수', cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
                { field: 'mergeDt', displayName: '머지일자', cellTooltip: true, headerTooltip: true },
                { field: 'sndDt', displayName: '발송일자', cellTooltip: true, headerTooltip: true },
                { field: 'stsFgNm', displayName: '셀상태', width: 100, cellTooltip: true, headerTooltip: true }
            ],
            onRegisterApi: function (gridApi) {
                $scope.gridApi2 = gridApi;
            }
        };

        $scope.openTargeting = function () {
            $scope.campaign.objRegFgCd = 'TRGT';

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

                    $scope.loadCellList();
                });
            });
        };

        $scope.loadTargeting = function () {

        };

        $scope.loadCellList = function () {
            $scope.cellListPromise = apiSvc.getCampaignDetail($scope.campaign);
            $scope.cellListPromise.then(function (data) {
                $scope.gridOptionsCellList.data = data.value;
            });
        };

        $scope.addCell = function () {
            var params = {
                cmpgnId: $scope.campaign.cmpgnId
            };
            $scope.cellListPromise = apiSvc.saveCampaignDetail(params);
            $scope.cellListPromise.then(function () {
                $scope.loadCellList();
            });
        };

        $scope.deleteCell = function () {
            var selectedRow = $scope.gridApi2.selection.getSelectedRows()[0];

            apiSvc.deleteCampaignDetail(selectedRow).then(function () {
                var index = $scope.gridOptionsCellList.data.indexOf(selectedRow);
                $scope.gridOptionsCellList.data.splice(index, 1);
            });
        };

        $scope.requestTransmission = function () {
            var componentName;
            if ($scope.campaign.cmpgnSndChnlFgCd === 'MAIL') {
                componentName = 'ctas0104Modal';
            } else {
                componentName = 'ctas0105Modal';
            }

            $uibModal.open({
                component: componentName,
                resolve: {
                    notiType: function () { return $scope.campaign.cmpgnSndChnlFgCd; },
                    targetingInfo: function () { return $scope.targetingInfo; }
                }
            });
        };

    }]);
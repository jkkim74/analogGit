'use strict';

angular.module('App').controller('Ctas0101Ctrl', ['$scope', '$log', '$q', '$http', '$timeout', '$filter', 'uiGridConstants', 'toastr', 'apiSvc', '$uibModal', 'uibDateParser', 'uploadSvc',
    function ($scope, $log, $q, $http, $timeout, $filter, uiGridConstants, toastr, apiSvc, $uibModal, uibDateParser, uploadSvc) {

        var self = this;
        $scope.title = '이메일 발송 관리';

        $scope.currCampaign = {};

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
            var campaign = angular.extend({}, $scope.currCampaign);
            angular.extend(campaign, {
                mergeDt: $filter('date')(campaign.mergeDt, 'yyyyMMdd')
            });

            var deferred = $q.defer();

            $scope.savePromise = apiSvc.saveCampaign(campaign);
            $scope.savePromise.then(function (data) {
                $scope.currCampaign.cmpgnId = data.value.cmpgnId;
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

            $scope.currCampaign = angular.extend({}, selectedRow);
            $scope.currCampaign.mergeDt = uibDateParser.parse(selectedRow.mergeDt, 'yyyy-MM-dd');
            $scope.currCampaign.sndDt = uibDateParser.parse(selectedRow.sndDt, 'yyyy-MM-dd');

            $scope.loadCellList();
            $scope.loadTargeting();
        };

        $scope.clearDetail = function () {
            $scope.currCampaign = {
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
                {
                    field: 'cellDesc', displayName: '설명', cellTooltip: true, headerTooltip: true, enableCellEdit: true,
                    cellTemplate: '<div class="ui-grid-cell-contents"><span title="TOOLTIP">{{ COL_FIELD CUSTOM_FILTERS }}</span> <i class="fa fa-pencil" aria-hidden="true" title="수정하려면 더블클릭"></i></div>'
                },
                { field: 'cellPrcntg', displayName: '백분율(%)', width: 100, cellTooltip: true, headerTooltip: true },
                {
                    field: 'extrctCnt', displayName: '추출수', cellTooltip: true, headerTooltip: true, enableCellEdit: true, cellFilter: 'number',
                    cellTemplate: '<div class="ui-grid-cell-contents"><span title="TOOLTIP">{{ COL_FIELD CUSTOM_FILTERS }}</span> <i class="fa fa-pencil" aria-hidden="true" title="수정하려면 더블클릭"></i></div>'
                },
                { field: 'fnlExtrctCnt', displayName: '최종추출수', cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
                { field: 'mergeDt', displayName: '머지일자', cellTooltip: true, headerTooltip: true },
                { field: 'sndDt', displayName: '발송일자', cellTooltip: true, headerTooltip: true },
                { field: 'stsFgNm', displayName: '셀상태', width: 100, cellTooltip: true, headerTooltip: true }
            ],
            onRegisterApi: function (gridApi) {
                $scope.gridApi2 = gridApi;
                $scope.gridApi2.edit.on.afterCellEdit($scope, $scope.saveCellListColumn);
            }
        };

        $scope.saveCellListColumn = function (rowEntity, colDef, newValue, oldValue) {
            if (newValue === oldValue) {
                return;
            }

            var params = {
                cellId: rowEntity.cellId,
                cmpgnId: rowEntity.cmpgnId
            };
            params[colDef.name] = newValue;

            apiSvc.saveCampaignDetail(params);
        };

        $scope.upload = function (file) {
            var params = {
                url: '/api/campaigns/targeting',
                file: file,
                columnName: 'mbrId'
            };

            params = angular.extend(params, $scope.currCampaign);
            params.mergeDt = $filter('date')(params.mergeDt, 'yyyyMMdd');

            $scope.uploadPromise = uploadSvc.upload(params);
            $scope.uploadPromise.then(function (data) {
                $scope.currCampaign = data.value;
                $scope.currCampaign.mergeDt = uibDateParser.parse($scope.currCampaign.mergeDt, 'yyyy-MM-dd');

                $scope.searchCampaign();
            });
        };

        $scope.openTargeting = function () {
            $scope.currCampaign.objRegFgCd = 'TRGT';

            $scope.saveCampaign().then(function () {
                var modalInstance = $uibModal.open({
                    component: 'ctas0103Modal',
                    size: 'lg',
                    backdrop: 'static',
                    resolve: {
                        campaign: function () { return $scope.currCampaign; }
                    }
                });

                modalInstance.result.then(function (item) {
                    $log.debug(item);
                    $scope.currCampaign = item;
                    $scope.currCampaign.mergeDt = uibDateParser.parse($scope.currCampaign.mergeDt, 'yyyy-MM-dd');
                    $scope.currCampaign.sndDt = uibDateParser.parse($scope.currCampaign.sndDt, 'yyyy-MM-dd');

                    $scope.searchCampaign();
                    $scope.loadCellList();
                });
            });
        };

        $scope.loadTargeting = function () {

        };

        $scope.loadCellList = function () {
            $scope.cellListPromise = apiSvc.getCampaignDetail($scope.currCampaign);
            $scope.cellListPromise.then(function (data) {
                $scope.gridOptionsCellList.data = data.value;
                $scope.gridApi2.selection.clearSelectedRows();
            });
        };

        $scope.addCell = function () {
            var params = {
                cmpgnId: $scope.currCampaign.cmpgnId
            };
            $scope.cellListPromise = apiSvc.saveCampaignDetail(params);
            $scope.cellListPromise.then(function () {
                $scope.loadCellList();
            });
        };

        $scope.deleteCell = function () {
            var selectedRow = $scope.gridApi2.selection.getSelectedRows()[0];

            apiSvc.deleteCampaignDetail(selectedRow).then(function () {
                $scope.loadCellList();
            });
        };

        $scope.requestTransmission = function () {
            var componentName;
            if ($scope.currCampaign.cmpgnSndChnlFgCd === 'MAIL') {
                componentName = 'ctas0104Modal';
            } else {
                componentName = 'ctas0105Modal';
            }

            $uibModal.open({
                component: componentName,
                resolve: {
                    notiType: function () { return $scope.currCampaign.cmpgnSndChnlFgCd; },
                    targetingInfo: function () { return $scope.currCampaign.targetingInfo; }
                }
            });
        };

    }]);
'use strict';

angular.module('App')
    .controller('Ctas0101Ctrl', ['$scope', '$q', '$http', '$timeout', 'uiGridConstants', 'toastr', 'apiSvc', '$uibModal', function ($scope, $q, $http, $timeout, uiGridConstants, toastr, apiSvc, $uibModal) {

        var self = this;
        $scope.title = '이메일 발송 관리';

        $scope.visibleDetail = true;

        $scope.selectOptions = [
            { label: '커머스센터 대용량 메일', value: 'em' },
            { label: '앱푸시/모바일전용 회원ID 전송 (PTS)', value: 'push' },
            { label: 'SMS 발송용 목록 전송 (PTS)', value: 'sms' }
        ];

        $scope.gridOptionsList = {
            useExternalPagination: true,
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            minRowsToShow: 16,
            columnDefs: [
                { field: 'no', displayName: 'No.', width: 50, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
                { field: 'cpmnCd', displayName: '캠페인코드', cellTooltip: true, headerTooltip: true },
                { field: 'cpmnNm', displayName: '캠페인명', cellTooltip: true, headerTooltip: true },
                { field: 'sndChnl', displayName: '발송채널', cellTooltip: true, headerTooltip: true },
                { field: 'mrgDt', displayName: '머지일자', cellTooltip: true, headerTooltip: true },
                { field: 'sndDt', displayName: '발송일자', cellTooltip: true, headerTooltip: true },
                { field: 'requester', displayName: '요청담당자', cellTooltip: true, headerTooltip: true },
                { field: 'sndSts', displayName: '상태', cellTooltip: true, headerTooltip: true }
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
                limit: limit || $scope.gridOptionsList.paginationPageSize
            };

            // $scope.searchPromise = apiSvc.getCampaigns(params);
            // $scope.searchPromise.then(function (data) {
            //     $scope.gridOptionsList.data = data.value;
            //     $scope.gridOptionsList.totalItems = data.totalRecords;
            // });
        };


        $scope.gridOptionsCellList = {
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            minRowsToShow: 5,
            columnDefs: [
                { field: 'cpmnCd', displayName: '셀ID', width: 50, cellTooltip: true, headerTooltip: true },
                { field: 'cpmnNm', displayName: '설명', cellTooltip: true, headerTooltip: true },
                { field: 'sndChnl', displayName: '백분율(%)', width: 50, cellTooltip: true, headerTooltip: true },
                { field: 'mrgDt', displayName: '추출수', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
                { field: 'sndDt', displayName: '최종추출수', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
                { field: 'requester', displayName: '머지일자', cellTooltip: true, headerTooltip: true },
                { field: 'sndSts', displayName: '발송일자', cellTooltip: true, headerTooltip: true },
                { field: 'sndSts', displayName: '셀상태', width: 100, cellTooltip: true, headerTooltip: true }
            ]
        };

        $scope.openTargeting = function () {
            var modalInstance = $uibModal.open({
                component: 'ctas0103Modal',
                size: 'lg',
                resolve: {}
            });

            modalInstance.result.then(function (selectedItem) {
                $scope.selectedTargeting = selectedItem;
            }, function () {

            });
        };

        $scope.requestTransmission = function () {
            var componentName;
            if ($scope.selectedOption.value === 'em') {
                componentName = 'ctas0104Modal';
            } else {
                componentName = 'ctas0102Modal';
            }

            $uibModal.open({
                component: componentName,
                resolve: {
                    notiType: function () { return $scope.selectedOption.value; }
                }
            });
        };

    }]);
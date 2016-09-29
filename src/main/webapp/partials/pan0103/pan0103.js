'use strict';

angular.module('App')
    .controller('Pan0103Ctrl', ['$scope', '$q', '$http', '$timeout', 'uiGridConstants', 'apiSvc', 'uploadService', function ($scope, $q, $http, $timeout, uiGridConstants, apiSvc, uploadService) {

        var self = this;
        $scope.title = '배치 적립 파일 검증';
        $scope.enablePreview = true;

        $scope.selectOptions = [
            { label: '회원ID', value: 'mbrId' },
            { label: '카드번호', value: 'cardNo' },
            { label: 'CI번호', value: 'ciNo' }
        ];

        $scope.gridOptionsPreview = {
            enableColumnMenus: false,
            enableSorting: false,
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            minRowsToShow: 9,
            columnDefs: [
                { field: 'no', displayName: 'No.', width: 100, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
                { field: 'column1', displayName: '회원ID' },
                { field: 'column2', displayName: '카드번호' },
                { field: 'column3', displayName: 'CI번호' },
                { field: 'column4', displayName: '성명' },
                { field: 'column5', displayName: '생년월일' },
                { field: 'column6', displayName: '성별' }
            ]
        };

        $scope.gridOptionsMembers = {
            enablePaginationControls: false,
            paginationPageSize: 100,
            useExternalPagination: true,
            columnDefs: [
                { field: 'mbrId', displayName: '회원ID', cellTooltip: true, headerTooltip: true },
                { field: 'cardNo', displayName: '카드번호', cellTooltip: true, headerTooltip: true },
                { field: 'ciNo', displayName: 'CI번호', cellTooltip: true, headerTooltip: true },
                { field: 'mbrKorNm', displayName: '성명', cellTooltip: true, headerTooltip: true },
                { field: 'leglBthdt', displayName: '생년월일', cellTooltip: true, headerTooltip: true },
                { field: 'leglGndrFgCd', displayName: '성별', cellTooltip: true, headerTooltip: true },
                { field: 'cardNoYn', displayName: 'OCB 카드 여부', cellTooltip: true, headerTooltip: true },
                { field: 'ciNoYn', displayName: 'CI 일치 여부', cellTooltip: true, headerTooltip: true },
                { field: 'mbrKorNmYn', displayName: '성명 일치 여부', cellTooltip: true, headerTooltip: true },
                { field: 'leglBthdtYn', displayName: '생년월일 일치 여부', cellTooltip: true, headerTooltip: true },
                { field: 'leglGndrFgYn', displayName: '성별 일치 여부', cellTooltip: true, headerTooltip: true },
                { field: 'allYn', displayName: '불일치 항목 포함 여부', cellTooltip: true, headerTooltip: true }
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

            $scope.uploadPromise = uploadService.upload({ file: file, columnName: $scope.selectedOption.value });
            $scope.uploadPromise.then(function () {
                self.checkUploadProgress();
                $scope.uploadProgressLoadingMessage = 'Loading...';

                return uploadService.getUploadedPreview();
            }, null, function (progressPercentage) {
                $scope.uploadProgressLoadingMessage = 'Uploading...' + progressPercentage + '%';
            }).then(function (data) {
                $scope.gridOptionsPreview.data = data.value;
            });
        };

        $scope.loadMembers = function () {
            var offset = ($scope.gridApi.pagination.getPage() - 1) * $scope.gridOptionsMembers.paginationPageSize;
            var limit = $scope.gridOptionsMembers.paginationPageSize;

            $scope.membersPromise = apiSvc.getMembers({ offset: offset, limit: limit });
            $scope.membersPromise.then(function (data) {
                $scope.gridOptionsMembers.data = data.value;
                $scope.gridOptionsMembers.totalItems = data.totalRecords;
            });
        };

        self.checkUploadProgress = function () {
            uploadService.getUploadProgress().finally(function (data) {
                $scope.uploadProgress = false;
            }, function (totalRecords) {
                $scope.uploadProgress = true;
                $scope.uploadedRecords = totalRecords;
            });
        };

        // 이전 업로드가 진행중이라면 표시.
        // self.checkUploadProgress();


        $scope.sendPts = function (ptsUsername, ptsMasking) {
            $scope.sendPtsPromise = apiSvc.sendPts({ ptsUsername: ptsUsername, ptsMasking: !!ptsMasking });
            $scope.sendPtsPromise.finally(function () {

            });
        };

    }]);
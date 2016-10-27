'use strict';

angular.module('App').controller('Pan0103Ctrl', ['$scope', '$q', '$http', '$timeout', 'uiGridConstants', 'apiSvc', 'uploadSvc', 'authSvc', 'FileSaver',
    function ($scope, $q, $http, $timeout, uiGridConstants, apiSvc, uploadSvc, authSvc, FileSaver) {

        var self = this;

        $scope.selectOptions = [
            { label: '회원ID', value: 'mbrId' },
            { label: '카드번호', value: 'cardNo' },
            { label: 'CI번호', value: 'ciNo' }
        ];

        $scope.gridOptionsPreview = {
            enableSorting: false,
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            minRowsToShow: 9,
            columnDefs: [
                { field: 'no', displayName: 'No.', width: 100, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
                { field: 'column1', displayName: '회원ID', cellTooltip: true, headerTooltip: true },
                { field: 'column2', displayName: '카드번호', width: 200, cellTooltip: true, headerTooltip: true },
                { field: 'column3', displayName: 'CI번호', cellTooltip: true, headerTooltip: true },
                { field: 'column4', displayName: '성명', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'column5', displayName: '생년월일', cellTooltip: true, headerTooltip: true },
                { field: 'column6', displayName: '성별', cellTooltip: true, headerTooltip: true }
            ]
        };

        $scope.gridOptionsMembers = {
            enableRowSelection: true,
            enableRowHeaderSelection: false,
            multiSelect: false,
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
                gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                    var offset = (newPage - 1) * pageSize;
                    $scope.loadMembers(offset, pageSize);
                });
            }
        };

        $scope.upload = function (file) {
            $scope.uploadPromise = uploadSvc.upload({ file: file, columnName: $scope.selectedOption.value });
            $scope.uploadPromise.then(function () {
                self.checkUploadProgress();
                return uploadSvc.getUploadedPreview();
            }).then(function (data) {
                $scope.gridOptionsPreview.data = data.value;
            });
        };

        self.checkUploadProgress = function () {
            uploadSvc.getUploadProgress().finally(function () {
                $scope.uploadStatusIsRunning = false;
            }, function (totalItems) {
                $scope.uploadStatusIsRunning = true;
                $scope.uploadedItems = totalItems;
            });
        };

        $scope.loadMembers = function (offset, limit) {
            var params = {
                offset: offset || 0,
                limit: limit || $scope.gridOptionsMembers.paginationPageSize
            }

            $scope.membersPromise = apiSvc.getMembers(params);
            $scope.membersPromise.then(function (data) {
                $scope.gridOptionsMembers.data = data.value;
                $scope.gridOptionsMembers.totalItems = data.totalItems;
            });
        };

        $scope.sendPts = function () {
            $scope.sendPtsPromise = apiSvc.sendPts({ ptsMasking: !!$scope.ptsMasking });
            $scope.sendPtsPromise.finally(function () {

            });
        };

        $scope.downloadCsvFormat = function () {
            var text = '회원ID,카드번호,CI번호,성명,생년월일,성별';
            var data = new Blob([text], { type: 'text/csv;charset=utf-8' });
            FileSaver.saveAs(data, '배치적립파일검증 업로드 양식.csv');
        };

        authSvc.getUserInfo().then(function (userInfo) {
            $scope.ptsUsername = userInfo.ptsUsername;
        });

    }]);
'use strict';

angular.module('App').controller('Pan0101Ctrl', ['$scope', '$q', '$http', '$timeout', 'uiGridConstants', 'apiSvc', 'uploadSvc', 'authSvc',
    function ($scope, $q, $http, $timeout, uiGridConstants, apiSvc, uploadSvc, authSvc) {

        var self = this;
        $scope.title = '멤버 ID 일괄 전환';

        $scope.selectOptions = [
            { label: '회원ID', value: 'mbrId' },
            { label: 'OCB닷컴 로그인ID', value: 'ocbcomLgnId' },
            { label: 'CI번호', value: 'ciNo' },
            { label: '카드번호', value: 'cardNo' },
            { label: '시럽 스마트월렛 회원ID', value: 'sywMbrId' },
            { label: '11번가 회원ID', value: 'evsMbrId' }
        ];

        $scope.gridOptionsMembers = {
            enableRowSelection: true,
            enableRowHeaderSelection: false,
            multiSelect: false,
            useExternalPagination: true,
            columnDefs: [
                { field: 'mbrId', displayName: '회원ID', cellTooltip: true, headerTooltip: true },
                { field: 'ocbcomLgnId', displayName: 'OCB닷컴 로그인ID', cellTooltip: true, headerTooltip: true },
                { field: 'ciNo', displayName: 'CI번호', cellTooltip: true, headerTooltip: true },
                { field: 'mbrKorNm', displayName: '한글성명', cellTooltip: true, headerTooltip: true },
                { field: 'cardNo', displayName: '카드번호', cellTooltip: true, headerTooltip: true },
                { field: 'sywMbrId', displayName: '시럽 스마트월렛 회원ID', cellTooltip: true, headerTooltip: true },
                { field: 'evsMbrId', displayName: '11번가 회원ID', cellTooltip: true, headerTooltip: true }
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

        authSvc.getUserInfo().then(function (userInfo) {
            $scope.ptsUsername = userInfo.ptsUsername;
        });

    }]);
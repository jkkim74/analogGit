'use strict';

angular.module('app').controller('PAN0003Ctrl', ['$scope', '$q', '$http', '$timeout', 'uiGridConstants', 'apiSvc',
    function ($scope, $q, $http, $timeout, uiGridConstants, apiSvc) {

        var self = this;

        $scope.checkMenuId = {};
        $scope.checkResults = [];

        $scope.$watchCollection('checkMenuId', function () {
            $scope.checkResults = [];
            angular.forEach($scope.checkMenuId, function (value, key) {
                if (value) {
                    $scope.checkResults.push(key);
                }
            });
        });

        $scope.gridOptionsUserList = {
            enableRowSelection: true,
            enableRowHeaderSelection: false,
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            minRowsToShow: 20,
            columnDefs: [
                { field: 'no', displayName: 'No.', width: 50, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
                { field: 'username', displayName: 'Pnet ID', cellTooltip: true, headerTooltip: true }
            ],
            onRegisterApi: function (gridApi) {
                $scope.gridApi = gridApi;
                gridApi.selection.setMultiSelect(false);
                gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                    self.initSelection(row.entity);
                });
            }
        };

        self.loadUsers = function () {
            $scope.usersPromise = apiSvc.getUsers();
            $scope.usersPromise.then(function (data) {
                $scope.gridOptionsUserList.data = data;
            });
        };
        self.loadUsers();

        self.initSelection = function (entity) {
            $scope.checkMenuId.PAN0101 = entity.menuList.indexOf('PAN0101') !== -1;
            $scope.checkMenuId.PAN0102 = entity.menuList.indexOf('PAN0102') !== -1;
            $scope.checkMenuId.PAN0103 = entity.menuList.indexOf('PAN0103') !== -1;
            $scope.checkMenuId.PAN0104 = entity.menuList.indexOf('PAN0104') !== -1;
            $scope.checkMenuId.PAN0105 = entity.menuList.indexOf('PAN0105') !== -1;
            $scope.checkMenuId.PAN0106 = entity.menuList.indexOf('PAN0106') !== -1;
            $scope.checkMenuId.PAN0107 = entity.menuList.indexOf('PAN0107') !== -1;
        };

        $scope.saveAccess = function () {
            var selectedRow = $scope.gridApi.selection.getSelectedRows()[0];
            var username = selectedRow.username;
            var menuList = $scope.checkResults.join(',');

            apiSvc.saveAccess({ username: username, menuList: menuList }).then(function () {
                selectedRow.menuList = menuList;
            });
        };

    }]);
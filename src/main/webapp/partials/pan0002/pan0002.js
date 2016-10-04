'use strict';

angular.module('App')
    .controller('Pan0002Ctrl', ['$scope', '$q', '$http', '$timeout', 'uiGridConstants', 'apiSvc', function ($scope, $q, $http, $timeout, uiGridConstants, apiSvc) {

        var self = this;

        $scope.title = '사용자 등록 관리';

        $scope.gridOptionsUserList = {
            enableColumnMenus: false,
            enableCellEdit: false,
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            showGridFooter: true,
            minRowsToShow: 20,
            columnDefs: [
                { field: 'no', displayName: 'No.', width: 50, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
                { field: 'username', displayName: 'Pnet ID', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'fullname', displayName: '이름', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'emailAddr', displayName: '이메일주소', width: 200, cellTooltip: true, headerTooltip: true },
                {
                    field: 'ptsUsername', displayName: 'PTS 계정', width: 150, cellTooltip: true, headerTooltip: true, enableCellEdit: true,
                    cellTemplate: '<div class="ui-grid-cell-contents"><span title="TOOLTIP">{{ COL_FIELD CUSTOM_FILTERS }}</span> <i class="fa fa-pencil" aria-hidden="true" title="수정하려면 더블클릭"></i></div>'
                },
                {
                    field: 'enabled', displayName: '사용여부', width: 50, cellTooltip: true, headerTooltip: true, type: 'boolean', enableCellEdit: true,
                    cellTemplate: '<div class="ui-grid-cell-contents"><input type="checkbox" ng-model="row.entity.enabled" ng-change="grid.appScope.saveColumn(row.entity, {name:\'enabled\'}, COL_FIELD)"></div>'
                },
                { field: 'beginDttm', displayName: '사용시작일시', cellTooltip: true, headerTooltip: true },
                { field: 'endDttm', displayName: '사용종료일시', cellTooltip: true, headerTooltip: true }
            ],
            onRegisterApi: function (gridApi) {
                $scope.gridApi = gridApi;
                gridApi.edit.on.afterCellEdit($scope, $scope.saveColumn);
            }
        };

        $scope.saveColumn = function (rowEntity, colDef, newValue, oldValue) {
            if (newValue === oldValue) {
                return;
            }

            if (colDef.name === 'ptsUsername') {
                apiSvc.saveUser({ username: rowEntity.username, ptsUsername: rowEntity.ptsUsername });
            } else if (colDef.name === 'enabled') {
                apiSvc.saveUser({ username: rowEntity.username, enabled: rowEntity.enabled });
            }
        };

        self.saveRow = function (rowEntity) {
            var promise = apiSvc.saveUser({ username: rowEntity.username, ptsUsername: rowEntity.ptsUsername });
            $scope.gridApi.rowEdit.setSavePromise(rowEntity, promise);
        };

        $scope.createUser = function (username) {
            apiSvc.createUser({ username: username.toUpperCase() }).then(function () {
                self.loadUsers();
            });
        };

        self.loadUsers = function () {
            $scope.usersPromise = apiSvc.getUsers();
            $scope.usersPromise.then(function (data) {
                $scope.gridOptionsUserList.data = data;
            });
        };
        self.loadUsers();

        // $scope.disableUser = function () {
        //     var selectedRows = $scope.gridApi.selection.getSelectedRows();
        //     if (selectedRows.length <= 0) {
        //         return;
        //     }

        //     selectedRows.forEach(function (row) {
        //         row.enabled = false;
        //         apiSvc.saveUser({ username: row.username, enabled: row.enabled });
        //     });
        // };

        $scope.setUserAsAdmin = function () {

        };

    }]);
'use strict';

angular.module('app').controller('PAN0002Ctrl', ['$scope', '$q', '$http', '$timeout', 'uiGridConstants', 'toastr', 'apiSvc',
    function ($scope, $q, $http, $timeout, uiGridConstants, toastr, apiSvc) {

        $scope.gridOptionsUserList = {
            enableCellEdit: false,
            multiSelect: false,
            enableRowSelection: true,
            enableRowHeaderSelection: false,
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
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
                    field: 'enabled', displayName: '사용여부', width: 100, cellTooltip: true, headerTooltip: true, type: 'boolean',
                    cellTemplate: '<div class="ui-grid-cell-contents"><input type="checkbox" ng-model="row.entity.enabled" ng-change="grid.appScope.saveColumn(row.entity, {name:\'enabled\'}, COL_FIELD)"></div>'
                },
                {
                    field: 'maskingYn', displayName: '마스킹권한', width: 100, cellTooltip: true, headerTooltip: true, type: 'boolean',
                    cellTemplate: '<div class="ui-grid-cell-contents"><input type="checkbox" ng-model="row.entity.maskingYn" ng-change="grid.appScope.saveColumn(row.entity, {name:\'maskingYn\'}, COL_FIELD)"></div>'
                },
                { field: 'beginDttm', displayName: '사용시작일시', cellTooltip: true, headerTooltip: true },
                { field: 'endDttm', displayName: '사용종료일시', cellTooltip: true, headerTooltip: true },
                {
                    field: 'isAdmin', displayName: '관리자', width: 100, cellTooltip: true, headerTooltip: true, type: 'boolean',
                    cellTemplate: '<div class="ui-grid-cell-contents"><input type="checkbox" ng-model="row.entity.isAdmin" ng-change="grid.appScope.saveColumn(row.entity, {name:\'isAdmin\'}, COL_FIELD)" ng-init="grid.appScope.initAdmin(row.entity)"></div>'
                }
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
                apiSvc.saveUser({ username: rowEntity.username, ptsUsername: rowEntity.ptsUsername || '' });
            } else if (colDef.name === 'enabled') {
                apiSvc.saveUser({ username: rowEntity.username, enabled: rowEntity.enabled }).then(function (data) {
                    rowEntity.beginDttm = data.value.beginDttm;
                    rowEntity.endDttm = data.value.endDttm;
                });
            } else if (colDef.name === 'isAdmin') {
                var allRows = $scope.gridApi.core.getVisibleRows($scope.gridApi.grid);
                var admins = allRows.filter(function (row) {
                    return row.entity.isAdmin;
                });

                if (admins.length <= 0) {
                    toastr.warning('최소 1명의 관리자는 필요합니다');
                    rowEntity.isAdmin = true;
                    return;
                }

                apiSvc.saveAdmin({ username: rowEntity.username, isAdmin: rowEntity.isAdmin });
            } else if(colDef.name === 'maskingYn'){
                apiSvc.saveMasking({ username: rowEntity.username, maskingYn: rowEntity.maskingYn});
            }
        };

        // $scope.saveRow = function (rowEntity) {
        //     var promise = apiSvc.saveUser({ username: rowEntity.username, ptsUsername: rowEntity.ptsUsername });
        //     $scope.gridApi.rowEdit.setSavePromise(rowEntity, promise);
        // };

        $scope.createUser = function () {
            $scope.formPromise = apiSvc.createUser({ username: $scope.newUsername.toUpperCase() });
            $scope.formPromise.then(function () {
                $scope.loadUsers();
            });
        };

        $scope.loadUsers = function () {
            $scope.usersPromise = apiSvc.getUsers();
            $scope.usersPromise.then(function (data) {
                $scope.gridOptionsUserList.data = data;
            });
        };
        $scope.loadUsers();

        $scope.initAdmin = function (rowEntity) {
            var roleAdmin = rowEntity.authorities.filter(function (obj) {
                return obj.authority === 'ROLE_ADMIN';
            });
            rowEntity.isAdmin = roleAdmin.length > 0;
        };

    }]);
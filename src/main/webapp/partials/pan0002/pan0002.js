'use strict';

angular.module('App')
    .controller('Pan0002Ctrl', ['$scope', '$q', '$http', '$timeout', 'uiGridConstants', 'apiSvc', function ($scope, $q, $http, $timeout, uiGridConstants, apiSvc) {

        var self = this;

        $scope.title = '사용자 등록 관리';

        $scope.gridOptionsUserList = {
            enableColumnMenus: false,
            enableCellEdit: false,
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            enableRowSelection: true,
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
                { field: 'beginDt', displayName: '사용시작일자', cellTooltip: true, headerTooltip: true },
                { field: 'endDt', displayName: '사용종료일자', cellTooltip: true, headerTooltip: true }
            ],
            onRegisterApi: function (gridApi) {
                $scope.gridApi = gridApi;
                gridApi.rowEdit.on.saveRow($scope, self.saveRow);
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

    }]);
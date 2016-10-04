'use strict';

angular.module('App')
    .controller('Pan0002Ctrl', ['$scope', '$q', '$http', '$timeout', 'uiGridConstants', 'apiSvc', function ($scope, $q, $http, $timeout, uiGridConstants, apiSvc) {

        var self = this;

        $scope.title = '사용자 등록 관리';

        $scope.gridOptionsUserList = {
            enableColumnMenus: false,
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            enableRowSelection: true,
            showGridFooter: true,
            minRowsToShow: 20,
            columnDefs: [
                { field: 'no', displayName: 'No.', width: 50, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
                { field: 'username', displayName: 'Pnet ID', width: 100 },
                { field: 'fullname', displayName: '이름', width: 100 },
                { field: 'emailAddr', displayName: '이메일주소', width: 200 },
                { field: 'ptsUsername', displayName: 'PTS 계정', width: 150 },
                { field: 'beginDt', displayName: '사용시작일자' },
                { field: 'endDt', displayName: '사용종료일자' }
            ],
            onRegisterApi: function (gridApi) {
                $scope.gridApi = gridApi;
            }
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
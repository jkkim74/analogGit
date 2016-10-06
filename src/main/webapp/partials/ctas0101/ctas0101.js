'use strict';

angular.module('App')
    .controller('Ctas0101Ctrl', ['$scope', '$q', '$http', '$timeout', 'uiGridConstants', 'toastr', 'apiSvc', function ($scope, $q, $http, $timeout, uiGridConstants, toastr, apiSvc) {

        var self = this;
        $scope.title = '이메일 발송 관리';

        $scope.gridOptionsList = {
            useExternalPagination: true,
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            columnDefs: [
                { field: 'no', displayName: 'No.', width: 100, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
                { field: 'column1', displayName: 'Uploaded Data' }
            ],
            onRegisterApi: function (gridApi) {
                $scope.gridApi = gridApi;
                gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                    $scope.loadMembers();
                });
            }
        };

    }]);
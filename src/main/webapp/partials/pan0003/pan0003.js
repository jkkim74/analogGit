'use strict';

angular.module('App')
    .controller('Pan0003Ctrl', ['$scope', '$q', '$http', '$timeout', 'uiGridConstants', 'apiService', 'uploadService', function ($scope, $q, $http, $timeout, uiGridConstants, apiService, uploadService) {

        $scope.title = '화면 권한 관리';

        $scope.gridOptionsUserList = {
            enableColumnMenus: false,
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            showGridFooter: true,
            minRowsToShow: 20,
            columnDefs: [
                { field: 'no', displayName: 'No.', width: 50, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
                { field: 'username', displayName: 'Pnet ID' },
                { field: 'fullname', displayName: '이름' },
                { field: 'emailAddr', displayName: '이메일주소' }
            ]
        };

    }]);
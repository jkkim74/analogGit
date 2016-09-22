'use strict';

angular.module('App')
    .controller('Pan0002Ctrl', ['$scope', '$q', '$http', '$timeout', 'uiGridConstants', 'apiService', 'uploadService', function ($scope, $q, $http, $timeout, uiGridConstants, apiService, uploadService) {

        $scope.title = '사용자 등록 관리';

        $scope.gridOptionsUserList = {
            enableColumnMenus: false,
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            enableRowSelection: true,
            showGridFooter: true,
            minRowsToShow: 20,
            columnDefs: [
                { field: 'no', displayName: 'No.', width: 50, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
                { field: 'username', displayName: 'Pnet ID' },
                { field: 'fullname', displayName: '이름' },
                { field: 'emailAddr', displayName: '이메일주소' },
                { field: 'isAdmin', displayName: '관리자여부', width: 100 },
                { field: 'column1', displayName: '사용시작일자' },
                { field: 'column1', displayName: '사용종료일자' }
            ]
        };

    }]);
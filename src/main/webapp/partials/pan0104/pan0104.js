'use strict';

angular.module('App')
    .controller('Pan0104Ctrl', ['$scope', '$q', '$http', '$timeout', 'uiGridConstants', function ($scope, $q, $http, $timeout, uiGridConstants) {

        $scope.title = '시효 만료 포인트 사전 고지';

        $scope.selectOptions = [];

        var now = new Date();
        var year = now.getFullYear();
        var month = now.getMonth() + 1;
        // move to next month
        if (month == 12) {
            year++;
            month = 1;
        } else {
            month++;
        }

        for (var i = 0; i < 7; i++ , month--) {
            if (month < 1) {
                year--;
                month = 12;
            }
            $scope.selectOptions.push({ label: year + '년 ' + month + '월', value: year + ('0' + month).slice(-2) });
        }

        $scope.gridOptionsTransmission = {
            enableColumnMenus: false,
            enableSorting: false,
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            columnDefs: [
                { field: 'column1', displayName: '회원구분' },
                { field: 'column1', displayName: '항목' },
                { field: 'column1', displayName: 'OCB 고객수' },
                { field: 'column1', displayName: 'EM 고객수' },
                { field: 'column1', displayName: 'SMS 고객수' },
                { field: 'column1', displayName: 'TM 고객수' },
                { field: 'column1', displayName: '전체 고객수' },
                { field: 'column1', displayName: '전체 포인트' }
            ]
        };

        $scope.gridOptionsResult = {
            enableColumnMenus: false,
            enableSorting: false,
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            minRowsToShow: 7,
            columnDefs: [
                { field: 'column1', displayName: 'EC_USER_ID' },
                { field: 'column1', displayName: '회원ID' },
                { field: 'column1', displayName: '고객성명' },
                { field: 'column1', displayName: '휴대폰번호' },
                { field: 'column1', displayName: '이메일주소' }
            ]
        };

    }]);
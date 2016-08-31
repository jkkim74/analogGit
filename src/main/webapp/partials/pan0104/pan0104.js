'use strict';

App.controller('Pan0104Ctrl', ["$scope", "$q", "$http", "$timeout", "$stateParams", "Upload", "uiGridConstants", "toastr", function ($scope, $q, $http, $timeout, $stateParams, Upload, uiGridConstants, toastr) {

  $scope.title = "시효 만료 포인트 사전 고지";

  $scope.selectOptions = [];

  var nextMonth = moment().add(1, 'months');

  for (var year = nextMonth.year(), month = nextMonth.month() + 1, i = 0; i < 7; i++ , month--) {
    if (month < 1) {
      year--;
      month = 12;
    }
    $scope.selectOptions.push({ label: year + '년 ' + month + '월', value: year + ('0' + month).slice(-2) });
  }

  $scope.gridOptionsTransmission = {
    enableSorting: false,
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
    enableSorting: false,
    columnDefs: [
      { field: 'column1', displayName: 'EC_USER_ID' },
      { field: 'column1', displayName: '회원ID' },
      { field: 'column1', displayName: '고객성명' },
      { field: 'column1', displayName: '휴대폰번호' },
      { field: 'column1', displayName: '이메일주소' }
    ]
  };

}]);
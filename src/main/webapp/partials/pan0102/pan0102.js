'use strict';

App.controller('Pan0102Ctrl', ["$scope", "$q", "$http", "$timeout", "$stateParams", "Upload", "uiGridConstants", "toastr", function ($scope, $q, $http, $timeout, $stateParams, Upload, uiGridConstants, toastr) {

  $scope.title = '고객 정보 및 장기 거래 실적 조회';

  $scope.selectOptions = [
    { label: '회원ID', value: 'mbrId' },
    { label: '카드번호', value: 'cardNo' },
    { label: 'OCB닷컴 로그인ID', value: 'ocbcomLgnId' }
  ];

}]);
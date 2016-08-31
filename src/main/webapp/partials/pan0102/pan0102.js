'use strict';

App.controller('Pan0102Ctrl', ["$scope", "$q", "$http", "$timeout", "$stateParams", "Upload", "uiGridConstants", "toastr", function ($scope, $q, $http, $timeout, $stateParams, Upload, uiGridConstants, toastr) {

  $scope.title = '고객 정보 및 장기 거래 실적 조회';

  $scope.selectOptions = [
    { label: '회원ID', value: 'mbrId' },
    { label: '카드번호', value: 'cardNo' },
    { label: 'OCB닷컴 로그인ID', value: 'ocbcomLgnId' }
  ];

  $scope.gridOptionsTransactionHistory = {
    enableSorting: false,
    columnDefs: [
      { field: 'column1', displayName: '접수일시' },
      { field: 'column1', displayName: '접수번호' },
      { field: 'column1', displayName: '승인일시' },
      { field: 'column1', displayName: '대표승인번호' },
      { field: 'column1', displayName: '승인번호' },
      { field: 'column1', displayName: '매출일시' },
      { field: 'column1', displayName: '회원ID' },
      { field: 'column1', displayName: '카드번호' },
      { field: 'column1', displayName: '발생제휴사' },
      { field: 'column1', displayName: '발생가맹점' },
      { field: 'column1', displayName: '정산가맹점' },
      { field: 'column1', displayName: '포인트종류' },
      { field: 'column1', displayName: '전표' },
      { field: 'column1', displayName: '매출금액' },
      { field: 'column1', displayName: '적립포인트' },
      { field: 'column1', displayName: '사용포인트' },
      { field: 'column1', displayName: '제휴사연회비' },
      { field: 'column1', displayName: '수수료' },
      { field: 'column1', displayName: '지불수단' },
      { field: 'column1', displayName: '유종' },
      { field: 'column1', displayName: '주유량' },
      { field: 'column1', displayName: '쿠폰' }
    ]
  };

  $scope.gridOptionsEmailSendHistory = {
    enableSorting: false,
    columnDefs: [
      { field: 'column1', displayName: '발송일자' },
      { field: 'column1', displayName: '이메일 제목' },
      { field: 'column1', displayName: '이메일 발송 결과' }
    ]
  };

  $scope.gridOptionsAppPushHistory = {
    enableSorting: false,
    columnDefs: [
      { field: 'column1', displayName: '발송일자' },
      { field: 'column1', displayName: '푸쉬 제목' },
      { field: 'column1', displayName: '푸쉬 결과' }
    ]
  };


}]);
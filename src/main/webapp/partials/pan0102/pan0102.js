'use strict';

App.controller('Pan0102Ctrl', ["$scope", "$q", "$http", "$timeout", "apiService", function ($scope, $q, $http, $timeout, apiService) {

  $scope.title = '고객 정보 및 장기 거래 실적 조회';

  $scope.selectOptions = [
    { label: '회원ID', value: 'mbrId' },
    { label: '카드번호', value: 'cardNo' },
    { label: 'OCB닷컴 로그인ID', value: 'ocbcomLgnId' },
    { label: '메시지발송ID (검토중)', value: 'msgSndId' }
  ];

  $scope.gridOptionsTransactionHistory = {
    enableColumnMenus: false,
    columnDefs: [
      { field: 'column1', displayName: '접수일시', cellTooltip: true, headerTooltip: true },
      { field: 'column1', displayName: '접수번호', cellTooltip: true, headerTooltip: true },
      { field: 'column1', displayName: '승인일시', cellTooltip: true, headerTooltip: true },
      { field: 'column1', displayName: '대표승인번호', cellTooltip: true, headerTooltip: true },
      { field: 'column1', displayName: '승인번호', cellTooltip: true, headerTooltip: true },
      { field: 'column1', displayName: '매출일시', cellTooltip: true, headerTooltip: true },
      { field: 'column1', displayName: '회원ID', cellTooltip: true, headerTooltip: true },
      { field: 'column1', displayName: '카드번호', cellTooltip: true, headerTooltip: true },
      { field: 'column1', displayName: '발생제휴사', cellTooltip: true, headerTooltip: true },
      { field: 'column1', displayName: '발생가맹점', cellTooltip: true, headerTooltip: true },
      { field: 'column1', displayName: '정산가맹점', cellTooltip: true, headerTooltip: true },
      { field: 'column1', displayName: '포인트종류', cellTooltip: true, headerTooltip: true },
      { field: 'column1', displayName: '전표', cellTooltip: true, headerTooltip: true },
      { field: 'column1', displayName: '매출금액', cellTooltip: true, headerTooltip: true },
      { field: 'column1', displayName: '적립포인트', cellTooltip: true, headerTooltip: true },
      { field: 'column1', displayName: '사용포인트', cellTooltip: true, headerTooltip: true },
      { field: 'column1', displayName: '제휴사연회비', cellTooltip: true, headerTooltip: true },
      { field: 'column1', displayName: '수수료', cellTooltip: true, headerTooltip: true },
      { field: 'column1', displayName: '지불수단', cellTooltip: true, headerTooltip: true },
      { field: 'column1', displayName: '유종', cellTooltip: true, headerTooltip: true },
      { field: 'column1', displayName: '주유량', cellTooltip: true, headerTooltip: true },
      { field: 'column1', displayName: '쿠폰', cellTooltip: true, headerTooltip: true }
    ]
  };

  $scope.gridOptionsEmailSendHistory = {
    enableColumnMenus: false,
    columnDefs: [
      { field: 'column1', displayName: '발송일자' },
      { field: 'column1', displayName: '이메일 제목' },
      { field: 'column1', displayName: '이메일 발송 결과' }
    ]
  };

  $scope.gridOptionsAppPushHistory = {
    enableColumnMenus: false,
    columnDefs: [
      { field: 'column1', displayName: '발송일자' },
      { field: 'column1', displayName: '푸쉬 제목' },
      { field: 'column1', displayName: '푸쉬 결과' }
    ]
  };

  $scope.searchAll = function (selectedOption) {

  };

}]);
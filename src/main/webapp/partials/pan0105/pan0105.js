'use strict';

App.controller('Pan0105Ctrl', ["$scope", "$q", "$http", "$timeout", "apiService", "uploadService", function ($scope, $q, $http, $timeout, apiService, uploadService) {

  $scope.title = "거래 실적 및 유실적 고객 추출";

  $scope.selectOptions = [
    { label: '회원ID', value: 'mbrId' },
    { label: '카드번호', value: 'cardNo' },
    { label: '가맹점코드', value: 'Cd' }
  ];

  $scope.selectOptions2 = [
    { label: 'TR', value: 'tr' },
    { label: '회원ID', value: 'mbrId' }
  ];

  $scope.selectOptions3 = [
    { label: '접수일자', value: 'tr' },
    { label: '매출일자', value: 'mbrId' }
  ];

  $scope.datepickerOptions = {
    showWeeks: false
  };
  $scope.altInputFormats = ['M!/d!/yyyy'];

  $scope.gridOptionsPreview = {
    enableSorting: false,
    columnDefs: [
      { field: 'no', displayName: 'No.', width: 100, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
      { field: 'column1', displayName: 'Uploaded Data' }
    ]
  };

  $scope.gridOptions = {
    enablePaginationControls: false,
    paginationPageSize: 100,
    useExternalPagination: true,
    useExternalSorting: true,
    columnDefs: [
      { field: 'mbrId', displayName: '접수일시' },
      { field: 'cardNo', displayName: '접수번호' },
      { field: 'ciNo', displayName: '승인일시' },
      { field: 'mbrKorNm', displayName: '대표 승인번호' },
      { field: 'mbrKorNm', displayName: '승인번호' },
      { field: 'mbrKorNm', displayName: '매출일시' },
      { field: 'ocbcomLgnId', displayName: '회원ID' },
      { field: 'ocbcomLgnId', displayName: '카드코드' },
      { field: 'ocbcomLgnId', displayName: '카드번호' },
      { field: 'ocbcomLgnId', displayName: '발생제휴사' },
      { field: 'ocbcomLgnId', displayName: '발생가맹점' },
      { field: 'ocbcomLgnId', displayName: '정산가맹점' },
      { field: 'ocbcomLgnId', displayName: '포인트종류' },
      { field: 'ocbcomLgnId', displayName: '전표' },
      { field: 'ocbcomLgnId', displayName: '매출금액' },
      { field: 'ocbcomLgnId', displayName: '적립포인트' },
      { field: 'ocbcomLgnId', displayName: '제휴사연회비' },
      { field: 'ocbcomLgnId', displayName: '수수료' },
      { field: 'ocbcomLgnId', displayName: '지불수단' },
      { field: 'ocbcomLgnId', displayName: '유종' },
      { field: 'ocbcomLgnId', displayName: '주유량' },
      { field: 'ocbcomLgnId', displayName: '쿠폰' }
    ],
    onRegisterApi: function (gridApi) {
      $scope.gridApi = gridApi;
      // $scope.gridApi.core.on.sortChanged($scope, function (grid, sortColumns) {
      //   $scope.loadMerged();
      // });
      gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
        $scope.loadMerged();
      });
    }
  };

  $scope.upload = function (file) {
    uploadService.upload(file, $scope.selectedOption.value).then(function () {
      $timeout(function () {
        $scope.loadPreview();
      }, 1500);
    });
  };

  $scope.loadPreview = function () {
    apiService.getUploadedPreview().then(function (data) {
      $scope.gridOptionsPreview.data = data;
    });
  };

  $scope.loadMerged = function () {
    var offset = ($scope.gridApi.pagination.getPage() - 1) * $scope.gridOptions.paginationPageSize;
    var limit = $scope.gridOptions.paginationPageSize;

    apiService.getMembers(offset, limit).then(function (data) {
      $scope.gridOptions.data = data;
    });
  };

}]);
'use strict';

App.controller('Pan0103Ctrl', ["$scope", "$q", "$http", "$timeout", "apiService", "uploadService", function ($scope, $q, $http, $timeout, apiService, uploadService) {

  $scope.title = "배치 적립 파일 검증";

  $scope.selectOptions = [
    { label: '카드번호', value: 'cardNo' }
  ];

  $scope.gridOptionsPreview = {
    enableSorting: false,
    columnDefs: [
      { field: 'no', displayName: 'No.', width: 100, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
      { field: 'column1', displayName: '회원ID' },
      { field: 'column2', displayName: '카드번호' },
      { field: 'column3', displayName: 'CI번호' },
      { field: 'column4', displayName: '성명' },
      { field: 'column5', displayName: '생년월일' },
      { field: 'column6', displayName: '성별' }
    ]
  };

  $scope.gridOptions = {
    enablePaginationControls: false,
    paginationPageSize: 100,
    useExternalPagination: true,
    useExternalSorting: true,
    columnDefs: [
      { field: 'mbrId', displayName: '회원ID' },
      { field: 'cardNo', displayName: '카드번호' },
      { field: 'ciNo', displayName: 'CI번호' },
      { field: 'mbrKorNm', displayName: '성명' },
      { field: 'mbrKorNm', displayName: '생년월일' },
      { field: 'mbrKorNm', displayName: '성별' },
      { field: 'ocbcomLgnId', displayName: 'OCB 카드 여부' },
      { field: 'ocbcomLgnId', displayName: 'CI 일치 여부' },
      { field: 'ocbcomLgnId', displayName: '성명 일치 여부' },
      { field: 'ocbcomLgnId', displayName: '생년월일 일치 여부' },
      { field: 'ocbcomLgnId', displayName: '성별 일치 여부' },
      { field: 'ocbcomLgnId', displayName: '불일치 항목 포함 여부' }
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
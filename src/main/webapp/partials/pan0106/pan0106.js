'use strict';

App.controller('Pan0106Ctrl', ["$scope", "$q", "$http", "$timeout", "apiService", "uploadService", function ($scope, $q, $http, $timeout, apiService, uploadService) {

  $scope.title = "회원 프로파일 분석";

  $scope.selectOptions = [
    { label: '회원ID', value: 'mbrId' }
  ];

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
      { field: 'mbrId', displayName: '회원ID' },
      { field: 'ocbcomLgnId', displayName: 'OCB 회원 여부' },
      { field: 'ocbcomLgnId', displayName: '마케팅 동의 여부' },
      { field: 'ocbcomLgnId', displayName: '캠페인 블랙리스트 포함 여부' },
      { field: 'mbrKorNm', displayName: '성별' },
      { field: 'mbrKorNm', displayName: '연령' },
      { field: 'ocbcomLgnId', displayName: '거주지역' },
      { field: 'ocbcomLgnId', displayName: '직장지역' },
      { field: 'ocbcomLgnId', displayName: '기미혼 여부' }
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
    uploadService.getUploadedPreview().then(function (data) {
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
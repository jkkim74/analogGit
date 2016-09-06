'use strict';

App.controller('Pan0101Ctrl', ['$scope', '$q', '$http', '$timeout', 'uiGridConstants', 'apiService', 'uploadService', function ($scope, $q, $http, $timeout, uiGridConstants, apiService, uploadService) {

  $scope.title = '멤버 ID 일괄 전환';

  $scope.selectOptions = [
    { label: '회원ID', value: 'mbrId' },
    { label: 'OCB닷컴 로그인ID', value: 'ocbcomLgnId' },
    { label: 'CI번호', value: 'ciNo' },
    { label: '카드번호', value: 'cardNo' },
    { label: '시럽 스마트월렛 회원ID', value: 'sywMbrId' },
    { label: '11번가 회원ID', value: 'evsMbrId' }
  ];

  $scope.gridOptionsPreview = {
    enableColumnMenus: false,
    enableSorting: false,
    enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
    minRowsToShow: 7,
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
      { field: 'ocbcomLgnId', displayName: 'OCB닷컴 로그인ID' },
      { field: 'ciNo', displayName: 'CI번호' },
      { field: 'mbrKorNm', displayName: '한글성명' },
      { field: 'cardNo', displayName: '카드번호' },
      { field: 'sywMbrId', displayName: '시럽 스마트월렛 회원ID' },
      { field: 'evsMbrId', displayName: '11번가 회원ID' }
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

    apiService.getMembers({ offset: offset, limit: limit }).then(function (data) {
      $scope.gridOptions.data = data;
    });
  };

}]);
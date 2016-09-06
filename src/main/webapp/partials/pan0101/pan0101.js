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

  $scope.gridOptionsMembers = {
    enablePaginationControls: false,
    paginationPageSize: 100,
    useExternalPagination: true,
    columnDefs: [
      { field: 'mbrId', displayName: '회원ID', cellTooltip: true, headerTooltip: true },
      { field: 'ocbcomLgnId', displayName: 'OCB닷컴 로그인ID', cellTooltip: true, headerTooltip: true },
      { field: 'ciNo', displayName: 'CI번호', cellTooltip: true, headerTooltip: true },
      { field: 'mbrKorNm', displayName: '한글성명', cellTooltip: true, headerTooltip: true },
      { field: 'cardNo', displayName: '카드번호', cellTooltip: true, headerTooltip: true },
      { field: 'sywMbrId', displayName: '시럽 스마트월렛 회원ID', cellTooltip: true, headerTooltip: true },
      { field: 'evsMbrId', displayName: '11번가 회원ID', cellTooltip: true, headerTooltip: true }
    ],
    onRegisterApi: function (gridApi) {
      $scope.gridApi = gridApi;
      // $scope.gridApi.core.on.sortChanged($scope, function (grid, sortColumns) {
      //   $scope.loadMembers();
      // });
      gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
        $scope.loadMembers();
      });
    }
  };

  $scope.upload = function (file) {
    $scope.uploadPromise = uploadService.upload({ file: file, columnName: $scope.selectedOption.value });
    $scope.uploadPromise.then(function () {
      return uploadService.getUploadedPreview();
    }).then(function (data) {
      $scope.gridOptionsPreview.data = data;
    });
  };

  $scope.loadMembers = function () {
    var offset = ($scope.gridApi.pagination.getPage() - 1) * $scope.gridOptionsMembers.paginationPageSize;
    var limit = $scope.gridOptionsMembers.paginationPageSize;

    $scope.membersPromise = apiService.getMembers({ offset: offset, limit: limit });
    $scope.membersPromise.then(function (data) {
      $scope.gridOptionsMembers.data = data.value;
      $scope.gridOptionsMembers.totalItems = data.totalRecords;
    });
  };

  var checkUploadProgress = function () {

  };

  checkUploadProgress();

}]);
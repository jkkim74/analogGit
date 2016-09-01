'use strict';

App.controller('Pan0103Ctrl', ["$scope", "$q", "$http", "$timeout", "$stateParams", "Upload", "uiGridConstants", "toastr", function ($scope, $q, $http, $timeout, $stateParams, Upload, uiGridConstants, toastr) {

  $scope.title = "배치 적립 파일 검증";
  $scope.username = 'test2';

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
    if (!$scope.form.file.$valid || !file) {
      console.log("Error: invalid file");
      return;
    }

    Upload.upload({
      url: '/api/upload',
      data: { file: file, pageId: $stateParams.pageId, username: $scope.username, columnName: $scope.selectedOption.value }
    }).then(function (resp) {
      console.log('Success [' + resp.config.data.file.name + '] uploaded.');

      toastr.success(resp.config.data.file.name, '업로드 성공!');

      $timeout(function () {
        $scope.loadPreview()
      }, 2000);

    }, function (resp) {
      console.log('Error status: ' + resp.status);
    }, function (event) {
      var progressPercentage = parseInt(100.0 * event.loaded / event.total);
      console.log('progress: ' + progressPercentage + '% ' + event.config.data.file.name);
    });
  };

  $scope.loadPreview = function () {
    var canceler = $q.defer();
    $http
      .get('/api/upload', { params: { pageId: $stateParams.pageId, username: $scope.username }, timeout: canceler.promise })
      .then(function (resp) {
        $scope.gridOptionsPreview.data = resp.data;
      }, function (resp) {
        console.error(resp);
      });
  };

  $scope.loadMerged = function () {
    var canceler = $q.defer();
    $http
      .get('/api/mergedMember', {
        params: {
          pageId: $stateParams.pageId,
          username: $scope.username,
          offset: ($scope.gridApi.pagination.getPage() - 1) * $scope.gridOptions.paginationPageSize,
          limit: $scope.gridOptions.paginationPageSize
        },
        timeout: canceler.promise
      })
      .then(function (resp) {
        $scope.gridOptions.data = resp.data;
      }, function (resp) {
        console.error(resp);
      });
  };

}]);
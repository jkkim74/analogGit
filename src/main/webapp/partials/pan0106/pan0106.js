'use strict';

App.controller('Pan0106Ctrl', ["$scope", "$q", "$http", "$timeout", "$stateParams", "Upload", "uiGridConstants", "toastr", function ($scope, $q, $http, $timeout, $stateParams, Upload, uiGridConstants, toastr) {

  $scope.title = "회원 프로파일 분석";
  $scope.username = 'test2';

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
      .success(function (data) {
        $scope.gridOptionsPreview.data = data;
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
      .success(function (data) {
        $scope.gridOptions.data = data;
      });
  };

}]);
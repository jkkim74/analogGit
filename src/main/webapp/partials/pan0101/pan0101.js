'use strict';

App.controller('Pan0101Ctrl', ["$scope", "$q", "$http", "$stateParams", "Upload", function ($scope, $q, $http, $stateParams, Upload) {

  $scope.title = '멤버 ID 일괄 전환';
  $scope.username = 'test';

  $scope.selectOptions = [
    { label: '회원ID', value: 'mbrId' },
    { label: 'OCB닷컴 로그인ID', value: 'ocbcomLgnId' },
    { label: 'CI번호', value: 'ciNo' },
    { label: '카드번호', value: 'cardNo' },
    { label: '시럽 스마트월렛 회원ID', value: 'sywMbrId' },
    { label: '11번가 회원ID', value: 'evsMbrId' }
  ];

  $scope.gridOptionsPreview = {
    data: [],
    columnDefs: [
      { field: 'no', displayName: 'No.', width: 100, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
      { field: 'column1', displayName: 'Uploaded Data Preview' }
    ]
  };

  $scope.gridOptions = {
    data: [],
    columnDefs: [
      { field: 'mbrId', displayName: '회원ID' },
      { field: 'ocbcomLgnId', displayName: 'OCB닷컴 로그인ID' },
      { field: 'ciNo', displayName: 'CI번호' },
      { field: 'mbrKorNm', displayName: '한글성명' },
      { field: 'cardNo', displayName: '카드번호' },
      { field: 'sywMbrId', displayName: '시럽 스마트월렛 회원ID' },
      { field: 'evsMbrId', displayName: '11번가 회원ID' }
    ]
  };

  // upload later on form submit or something similar
  // $scope.submit = function () {
  //   if ($scope.form.file.$valid && $scope.file) {
  //     $scope.upload($scope.file);
  //   }
  // };

  // upload on file select or drop
  $scope.upload = function (file) {
    if (!$scope.form.file.$valid || !file) {
      console.log("Error: invalid file");
      return;
    }

    Upload.upload({
      url: '/api/upload',
      data: { file: file, username: $scope.username, pageId: $stateParams.pageId, dataType: $scope.selectedOption.value }
    }).then(function (resp) {
      console.log('Success ' + resp.config.data.file.name + ' uploaded. Response: ' + resp.data);

      $scope.loadPreview();

    }, function (resp) {
      console.log('Error status: ' + resp.status);
    }, function (event) {
      var progressPercentage = parseInt(100.0 * event.loaded / event.total);
      console.log('progress: ' + progressPercentage + '% ' + event.config.data.file.name);
    });
  };

  $scope.loadPreview = function () {
    var canceler = $q.defer();
    $http.get('/api/upload', { params: { username: $scope.username, pageId: $stateParams.pageId }, timeout: canceler.promise })
      .success(function (data) {
        $scope.gridOptionsPreview.data = data;
      });
  };

}]);
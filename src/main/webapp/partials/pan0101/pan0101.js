'use strict';

App.controller('Pan0101Ctrl', ["$scope", "$q", "$http", "$stateParams", "Upload", "$mdToast", function ($scope, $q, $http, $stateParams, Upload, $mdToast) {

  $scope.username = 'test';

  $scope.selectOptions = [
    { displayName: 'OCB MBR_ID', value: 'ocb_mbr_id' },
    { displayName: 'okcashbag.com 로그인 ID', value: 'ocb_login_id' },
    { displayName: 'CI', value: 'ci' },
    { displayName: 'OCB 카드번호', value: 'ocb_card_number' },
    { displayName: 'Syrup Wallet MEMBER_ID', value: 'syrup_wallet_member_id' },
    { displayName: '11번가 MEM_NO', value: 'elevenst_mem_no' }
  ];

  $scope.gridOptionsPreview = {
    data: [],
    columnDefs: [
      { field: 'no', displayName: 'No.', width: 100 },
      { field: 'column1', displayName: 'Preview' }
    ]
  };

  $scope.gridOptions = {
    data: [],
    columnDefs: [
      { field: 'firstName', displayName: 'OCB MBR_ID' },
      { field: 'lastName', displayName: 'okcashbag.com 로그인 ID' },
      { field: 'address', displayName: 'CI' },
      { field: 'address', displayName: '한글성명' },
      { field: 'address', displayName: 'OCB 카드번호' },
      { field: 'address', displayName: 'Syrup Wallet MEMBER_ID' },
      { field: 'address', displayName: '11번가 MEM_NO' }
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
      return;
    }

    Upload.upload({
      url: '/api/upload',
      data: { file: file, username: $scope.username, pageId: $stateParams.pageId, dataType: $scope.dataType }
    }).then(function (resp) {
      console.log('Success ' + resp.config.data.file.name + 'uploaded. Response: ' + resp.data);

      $mdToast.show(
        $mdToast.simple()
          .textContent(file.name + ' 업로드 됨')
          //.position('top right')
          .hideDelay(4000)
      );

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
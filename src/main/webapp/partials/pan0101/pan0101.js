'use strict';

App.controller('Pan0101Ctrl', ["$scope", "$stateParams", "Upload", function ($scope, $stateParams, Upload) {

  $scope.username = 'Jhon';

  $scope.selectOptions = [
    { displayName: 'OCB MBR_ID', value: 'ocb_mbr_id' },
    { displayName: 'okcashbag.com 로그인 ID', value: 'ocb_login_id' },
    { displayName: 'CI', value: 'ci' },
    { displayName: 'OCB 카드번호', value: 'ocb_card_number' },
    { displayName: 'Syrup Wallet MEMBER_ID', value: 'syrup_wallet_member_id' },
    { displayName: '11번가 MEM_NO', value: '11st_mem_no' }
  ];

  $scope.gridOptionsPreview = {
    data: [
      {
        "no": "1",
        "ocb_mbr_id": "Carney"
      },
      {
        "no": "2",
        "ocb_mbr_id": "Carney"
      },
      {
        "no": "3",
        "ocb_mbr_id": "Carney"
      }
    ],
    columnDefs: [
      { field: 'no', displayName: 'No.' },
      { field: 'ocb_mbr_id', displayName: 'OCB MBR_ID' }
    ]
  };

  $scope.gridOptions = {
    data: [

    ],
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
  $scope.submit = function () {
    if ($scope.form.file.$valid && $scope.file) {
      $scope.upload($scope.file);
    }
  };

  // upload on file select or drop
  $scope.upload = function (file) {
    Upload.upload({
      url: '/upload',
      data: { file: file, username: $scope.username, pageId: $stateParams.pageId, dataType: $scope.dataType }
    }).then(function (resp) {
      console.log('Success ' + resp.config.data.file.name + 'uploaded. Response: ' + resp.data);
    }, function (resp) {
      console.log('Error status: ' + resp.status);
    }, function (event) {
      var progressPercentage = parseInt(100.0 * event.loaded / event.total);
      console.log('progress: ' + progressPercentage + '% ' + event.config.data.file.name);
    });
  };

  $scope.select = function ($file) {
    $scope.selectedFile = $file;
  }

}]);
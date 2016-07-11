/**
 * 작업요청 등록 Ctrl
 */
var addNewWorkRequestCtrl = function ($scope, $http, userSvc, $modalInstance, issueTypeList, serviceTypeList) {

    $scope.issueTypeList = issueTypeList;
    $scope.serviceTypeList = serviceTypeList;

//    console.log($scope.issueTypeList);
//    console.log($scope.serviceTypeList);
//    console.log(userSvc.getUser());

    $scope.modalInit = function () {
        $scope.form = {
            issue: '',
            service: '',
            title: '',
            context: '',
            dueDate: '',
            applicant: userSvc.getUser().fullname + '(' + userSvc.getUser().username + ')',
            approval: getApprovalMan(),
            reference: ''
        };
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    /**
     * 작업요청 데이터 던지기...
     */
    $scope.sendWorkRequestForm = function() {
        var jiraApiUrl = '/jiraApi';

        $http.post(jiraApiUrl, $scope.form).success(function (data, status, headers, config) {
//            if (data.code === '0000') {
//                console.log(data);
//            }
            $modalInstance.close('save');
        });
    };

    function getApprovalMan(){
        //todo 해당 결재 or 담당자 리턴

        return '하하하(pp12345)';
    }

};

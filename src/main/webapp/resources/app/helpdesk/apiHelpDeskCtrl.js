function workRequestHelpDeskCtrl($scope, $http, $modal, userSvc) {

    function loadCode(){
        var issueTypeUrl = "/commonGroupCode/getCodes?groupCodeId=ISSUE_TYPE";
        $http.get(issueTypeUrl).success(function (data, status, headers, config) {
            $scope.issueTypeList = data;
        });

        var serviceTypeUrl = "/commonGroupCode/getCodes?groupCodeId=SERVICE_TYPE";
        $http.get(serviceTypeUrl).success(function (data, status, headers, config) {
            $scope.serviceTypeList = data;
        });
    }

    $scope.init = function () {
        loadCode();

    };


    $scope.sendData = function(){
        var modalInstance = $modal.open({
            templateUrl: 'page/templates/helpdesk.workrequest.tpl.html',
            controller: 'addNewWorkRequestCtrl',
            resolve: {
                issueTypeList: function () {
                    return $scope.issueTypeList;
                },
                serviceTypeList: function () {
                    return $scope.serviceTypeList;
                }
            },
            keyboard: false
        });
        modalInstance.result.then(
            function (result) {
                //console.log('function (result)');
                //console.log(result);
            },
            function (reason) {
                //console.log('function (reason)');
                //console.log(reason);
            }
        );

    };

}

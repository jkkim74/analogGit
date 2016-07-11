function unauthorizedCtrl($scope, $window) {
    $scope.authorityLink = 'http://jira.skplanet.com/secure/Dashboard.jspa?selectPageId=12005';

    $scope.goAuthorizedLink = function() {
        $window.open($scope.authorityLink,'_blank');
    }
}

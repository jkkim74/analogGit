function moveToJiraCtrl($scope) {
    $scope.init = function () {
        window.open('http://jira.skplanet.com/secure/Dashboard.jspa?selectPageId=12005','_blank');
    };
}
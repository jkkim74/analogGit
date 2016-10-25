'use strict';

angular.module('App').controller('HeaderCtrl', ['$scope', 'authSvc', function ($scope, authSvc) {

    $scope.isNavCollapsed = true
    $scope.isAuthenticated = authSvc.isAuthenticated();

    $scope.isAllowedPage = function (pageId) {
        return authSvc.isAllowedPage(pageId);
    }

    $scope.logout = function () {
        authSvc.logout();
    };

    authSvc.getUserInfo().then(function (userInfo) {
        $scope.name = userInfo.fullname || userInfo.username;
    });

}]);

'use strict';

angular.module('app').controller('HeaderCtrl', ['$scope', 'authSvc', function ($scope, authSvc) {

    $scope.isNavCollapsed = true
    $scope.isAuthenticated = authSvc.isAuthenticated();

    $scope.isAllowedPage = function (menuId) {
        return authSvc.isAllowedPage(menuId);
    }

    $scope.logout = function () {
        authSvc.logout();
    };

    authSvc.getUserInfo().then(function (userInfo) {
        $scope.name = userInfo.fullname || userInfo.username;
    });

}]);

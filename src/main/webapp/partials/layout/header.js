'use strict';

angular.module('app').controller('HeaderCtrl', ['$scope', 'authSvc', function ($scope, authSvc) {

    $scope.isNavCollapsed = true
    $scope.isAuthenticated = authSvc.isAuthenticated();

    $scope.isAllowedMenu = function (menuId) {
        return authSvc.isAllowedMenu(menuId);
    }

    $scope.logout = function () {
        authSvc.logout();
    };

    authSvc.getUserInfo().then(function (userInfo) {
        $scope.name = userInfo.fullname || userInfo.username;
    });

}]);

'use strict';

angular.module('App')
    .controller('HeaderCtrl', ['$scope', 'authService', function ($scope, authService) {

        $scope.isLogin = function () {
            return authService.isAuthenticated();
        };

        $scope.logout = function () {
            authService.logout();
        };

        $scope.username = authService.getUsername();

    }])
    .controller('FooterCtrl', ['$scope', function ($scope) {

        $scope.year = new Date().getFullYear();

    }]);

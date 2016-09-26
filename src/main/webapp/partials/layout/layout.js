'use strict';

angular.module('App')
    .controller('HeaderCtrl', ['$scope', 'authService', function ($scope, authService) {

        $scope.isAuthenticated = authService.isAuthenticated();

        $scope.isAllowedPage = function (pageId) {
            return authService.isAllowedPage(pageId);
        }

        $scope.logout = function () {
            authService.logout();
        };

        authService.getUsername().then(function (username) {
            $scope.username = username;
        });

    }])
    .controller('FooterCtrl', ['$scope', function ($scope) {

        $scope.year = new Date().getFullYear();

    }]);

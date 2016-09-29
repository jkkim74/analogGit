'use strict';

angular.module('App')
    .controller('HeaderCtrl', ['$scope', 'authSvc', function ($scope, authSvc) {

        $scope.isAuthenticated = authSvc.isAuthenticated();

        $scope.isAllowedPage = function (pageId) {
            return authSvc.isAllowedPage(pageId);
        }

        $scope.logout = function () {
            authSvc.logout();
        };

        authSvc.getUsername().then(function (username) {
            $scope.username = username;
        });

    }])
    .controller('FooterCtrl', ['$scope', function ($scope) {

        $scope.year = new Date().getFullYear();

    }]);

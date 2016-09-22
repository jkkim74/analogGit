'use strict';

angular.module('App')
    .controller('HeaderCtrl', ['$scope', 'authService', function ($scope, authService) {

        $scope.authService = authService;

        authService.getUsername().then(function (username) {
            $scope.username = username;
        });

    }])
    .controller('FooterCtrl', ['$scope', function ($scope) {

        $scope.year = new Date().getFullYear();

    }]);

'use strict';

angular.module('App')
    .controller('HomeCtrl', ['$scope', '$q', '$http', '$stateParams', 'authService', function ($scope, $q, $http, $stateParams, authService) {

        $scope.isLogout = function () {
            return !authService.isAuthenticated();
        };

        $scope.login = function (username, password) {
            authService.authenticate(username, password);
        };

    }]);
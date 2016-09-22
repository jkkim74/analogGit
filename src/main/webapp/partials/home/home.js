'use strict';

angular.module('App')
    .controller('HomeCtrl', ['$scope', '$q', '$http', '$state', 'authService', function ($scope, $q, $http, $state, authService) {

        $scope.isAuthenticated = authService.isAuthenticated();

        $scope.authenticate = function (username, password) {
            authService.authenticate(username, password);
        };

    }]);
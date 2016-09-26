'use strict';

angular.module('App')
    .controller('HomeCtrl', ['$scope', '$q', '$http', '$state', 'authService', function ($scope, $q, $http, $state, authService) {

        $scope.isAuthenticated = authService.isAuthenticated();

        $scope.isAllowedPage = function (pageId) {
            return authService.isAllowedPage(pageId);
        }

        $scope.authenticate = function (username, password) {
            authService.authenticate(username, password);
        };

    }]);
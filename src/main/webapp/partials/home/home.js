'use strict';

angular.module('App')
    .controller('HomeCtrl', ['$scope', '$q', '$http', '$state', 'authSvc', function ($scope, $q, $http, $state, authSvc) {

        $scope.isAuthenticated = authSvc.isAuthenticated();

        $scope.isAllowedPage = function (pageId) {
            return authSvc.isAllowedPage(pageId);
        }

        $scope.authenticate = function () {
            $scope.loginPromise = authSvc.authenticate($scope.username, $scope.password);
        };

    }]);
'use strict';

angular.module('app').controller('HomeCtrl', ['$scope', '$q', '$http', '$state', 'authSvc',
    function ($scope, $q, $http, $state, authSvc) {

        $scope.isAuthenticated = authSvc.isAuthenticated();

        $scope.isAllowedPage = function (menuId) {
            return authSvc.isAllowedPage(menuId);
        }

        $scope.authenticate = function () {
            $scope.loginPromise = authSvc.authenticate($scope.username, $scope.password);
        };

    }]);
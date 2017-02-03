'use strict';

angular.module('app').controller('HomeCtrl', ['$scope', '$q', '$http', '$state', 'authSvc',
    function ($scope, $q, $http, $state, authSvc) {

        $scope.isAuthenticated = authSvc.isAuthenticated();

        $scope.isAllowedMenu = function (menuId) {
            return authSvc.isAllowedMenu(menuId);
        };

        $scope.authenticate = function () {
            $scope.loginPromise = authSvc.authenticate($scope.username, $scope.password);
        };

    }]);
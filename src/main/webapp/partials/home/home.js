'use strict';

angular.module('app').controller('HomeCtrl', ['$scope', '$q', '$http', '$state', 'authSvc',
    function ($scope, $q, $http, $state, authSvc) {

        $scope.isAuthenticated = authSvc.isAuthenticated();

        $scope.isAllowedPage = function (pageId) {
            return authSvc.isAllowedPage(pageId);
        }

        $scope.authenticate = function () {
            $scope.loginPromise = authSvc.authenticate($scope.username, $scope.password);
        };

    }]);
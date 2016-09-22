'use strict';

angular.module('App')
    .controller('HomeCtrl', ['$scope', '$q', '$http', '$stateParams', 'authService', function ($scope, $q, $http, $stateParams, authService) {

        $scope.authService = authService;

    }]);
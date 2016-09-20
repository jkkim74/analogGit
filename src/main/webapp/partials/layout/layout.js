'use strict';

angular.module('App')
    .controller('FooterCtrl', ['$scope', function ($scope) {

        $scope.year = new Date().getFullYear();

    }]);

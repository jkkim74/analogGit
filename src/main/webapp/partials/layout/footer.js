'use strict';

angular.module('app').controller('FooterCtrl', ['$scope', function ($scope) {

    $scope.year = new Date().getFullYear();

}]);

'use strict';

angular.module('App')
    .service('authService', ["$log", "$q", "$http", "$stateParams", "toastr", function ($log, $q, $http, $stateParams, toastr) {

        this.authenticated = function () {
            return false;
        };

    }]);
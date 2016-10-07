'use strict';

angular.module('App')
    .component('ctas0103Modal', {
        templateUrl: 'partials/ctas0103/ctas0103.html',
        bindings: {
            resolve: '<',
            close: '&',
            dismiss: '&'
        },
        controller: function () {
            var self = this;

            self.$onInit = function () {

            };

            
        }
    });
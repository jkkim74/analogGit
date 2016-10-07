'use strict';

angular.module('App')
    .component('ctas0102Modal', {
        templateUrl: 'partials/ctas0102/ctas0102.html',
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
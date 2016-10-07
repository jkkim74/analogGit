'use strict';

angular.module('App')
    .component('ctas0104Modal', {
        templateUrl: 'partials/ctas0104/ctas0104.html',
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
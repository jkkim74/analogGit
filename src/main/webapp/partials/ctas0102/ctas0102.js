'use strict';

angular.module('App')
    .component('ctas0102Modal', {
        templateUrl: 'partials/ctas0102/ctas0102.html',
        bindings: {
            resolve: '<',
            close: '&',
            dismiss: '&'
        },
        controller: function (apiSvc) {
            var self = this;

            self.$onInit = function () {

            };

            self.requestTransmission = function () {

                var params = angular.extend(self.resolve.selectedTargeting, {});

                apiSvc.requestTransmission(params).then(function () {
                    self.close();
                });
            };

        }
    });
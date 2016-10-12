'use strict';

angular.module('App')
    .component('ctas0105Modal', {
        templateUrl: 'partials/ctas0105/ctas0105.html',
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
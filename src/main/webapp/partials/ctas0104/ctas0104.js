'use strict';

angular.module('App').component('ctas0104Modal', {
    templateUrl: 'partials/ctas0104/ctas0104.html',
    bindings: {
        resolve: '<',
        close: '&',
        dismiss: '&'
    },
    controller: function (apiSvc) {
        var self = this;

        self.$onInit = function () {
            self.campaignCell = self.resolve.campaignCell;
        };

        self.ok = function () {
            self.okPromise = apiSvc.requestTransmission(self.campaignCell);
            self.okPromise.then(function () {
                self.close();
            });
        };

    }
});